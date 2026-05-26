package com.simplecity.amp_library.billing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.simplecity.amp_library.constants.Config;
import com.simplecity.amp_library.rx.UnsafeAction;
import com.simplecity.amp_library.utils.LogUtils;
import java.util.List;
import javax.inject.Inject;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BillingManager implements PurchasesUpdatedListener { //NOSONAR

    private static final String TAG = "BillingManager"; //NOSONAR

    public static final int BILLING_MANAGER_NOT_INITIALIZED = -1; //NOSONAR

    public interface BillingUpdatesListener { //NOSONAR
        void onPurchasesUpdated(List<Purchase> purchases); //NOSONAR

        void onPremiumPurchaseCompleted(); //NOSONAR

        void onPremiumPurchaseRestored(); //NOSONAR
    }

    private Activity activity; //NOSONAR

    private BillingUpdatesListener updatesListener; //NOSONAR

    @Nullable //NOSONAR
    private BillingClient billingClient; //NOSONAR

    boolean serviceConnected = false; //NOSONAR

    int billingClientResponseCode = BILLING_MANAGER_NOT_INITIALIZED; //NOSONAR

    private boolean purchaseFlowInitiated = false; //NOSONAR
    private boolean restorePurchasesInitiated = false; //NOSONAR

    @Inject //NOSONAR
    public BillingManager(Activity activity, BillingUpdatesListener updatesListener) { //NOSONAR

        this.activity = activity; //NOSONAR
        this.updatesListener = updatesListener; //NOSONAR

        billingClient = BillingClient.newBuilder(activity) //NOSONAR
                .setListener(this) //NOSONAR
                .build(); //NOSONAR

        startServiceConnection(this::queryPurchases); //NOSONAR
    }

    private void startServiceConnection(UnsafeAction executeOnSuccess) { //NOSONAR
        if (billingClient != null) { //NOSONAR
            billingClient.startConnection(new BillingClientStateListener() { //NOSONAR
                @Override //NOSONAR
                public void onBillingSetupFinished(int responseCode) { //NOSONAR
                    if (responseCode == BillingClient.BillingResponse.OK) { //NOSONAR
                        serviceConnected = true; //NOSONAR
                        executeOnSuccess.run(); //NOSONAR
                    }
                    billingClientResponseCode = responseCode; //NOSONAR
                }

                @Override //NOSONAR
                public void onBillingServiceDisconnected() { //NOSONAR
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    serviceConnected = false; //NOSONAR
                }
            });
        }
    }

    @SuppressLint("DefaultLocale") //NOSONAR
    @Override //NOSONAR
    public void onPurchasesUpdated(int resultCode, @Nullable List<Purchase> purchases) { //NOSONAR
        if (resultCode == BillingClient.BillingResponse.OK && purchases != null) { //NOSONAR
            Purchase premiumPurchase = null; //NOSONAR
            for (Purchase purchase : purchases) { //NOSONAR
                if (purchase.getSku().equals(Config.SKU_PREMIUM)) { //NOSONAR
                    premiumPurchase = purchase; //NOSONAR
                }
            }
            if (purchaseFlowInitiated || restorePurchasesInitiated) { //NOSONAR
                if (premiumPurchase != null) { //NOSONAR
                    if (purchaseFlowInitiated) { //NOSONAR
                        updatesListener.onPremiumPurchaseCompleted(); //NOSONAR
                        purchaseFlowInitiated = false; //NOSONAR
                    }
                    if (restorePurchasesInitiated) { //NOSONAR
                        updatesListener.onPremiumPurchaseRestored(); //NOSONAR
                        restorePurchasesInitiated = false; //NOSONAR
                    }
                }
            } else { //NOSONAR
                updatesListener.onPurchasesUpdated(purchases); //NOSONAR
            }
        } else if (resultCode == BillingClient.BillingResponse.USER_CANCELED) { //NOSONAR
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping"); //NOSONAR
        } else { //NOSONAR
            LogUtils.logException(TAG, String.format("onPurchasesUpdated() got unknown resultCode: %d", resultCode), null); //NOSONAR
        }
    }

    public void queryPurchases() { //NOSONAR
        UnsafeAction queryAction = () -> { //NOSONAR
            if (billingClient == null) return; //NOSONAR
            Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP); //NOSONAR
            if (purchasesResult.getResponseCode() == BillingClient.BillingResponse.OK) { //NOSONAR
                onPurchasesUpdated(BillingClient.BillingResponse.OK, purchasesResult.getPurchasesList()); //NOSONAR
            } else { //NOSONAR
                LogUtils.logException(TAG, "Query purchases() got an unknown response code: " + purchasesResult.getResponseCode(), null); //NOSONAR
            }
        };

        if (serviceConnected) { //NOSONAR
            queryAction.run(); //NOSONAR
        } else { //NOSONAR
            startServiceConnection(queryAction); //NOSONAR
        }
    }

    /**
     * Start a purchase or subscription replace flow
     */
    public void initiatePurchaseFlow(final String skuId, final @BillingClient.SkuType String billingType) { //NOSONAR
        UnsafeAction purchaseFlowRequest = () -> { //NOSONAR
            BillingFlowParams purchaseParams = BillingFlowParams.newBuilder() //NOSONAR
                    .setSku(skuId) //NOSONAR
                    .setType(billingType) //NOSONAR
                    .build(); //NOSONAR
            if (billingClient != null) { //NOSONAR
                billingClient.launchBillingFlow(activity, purchaseParams); //NOSONAR
                purchaseFlowInitiated = true; //NOSONAR
            }
        };

        if (serviceConnected) { //NOSONAR
            purchaseFlowRequest.run(); //NOSONAR
        } else { //NOSONAR
            startServiceConnection(purchaseFlowRequest); //NOSONAR
        }
    }

    public void restorePurchases() { //NOSONAR
        restorePurchasesInitiated = true; //NOSONAR
        queryPurchases(); //NOSONAR
    }

    /**
     * Returns the value Billing client response code or BILLING_MANAGER_NOT_INITIALIZED if the
     * client connection response was not received yet.
     */
    public int getBillingClientResponseCode() { //NOSONAR
        return billingClientResponseCode; //NOSONAR
    }

    public void destroy() { //NOSONAR
        Log.d(TAG, "Destroying the manager."); //NOSONAR

        if (billingClient != null && billingClient.isReady()) { //NOSONAR
            billingClient.endConnection(); //NOSONAR
            billingClient = null; //NOSONAR
        }
    }
}
