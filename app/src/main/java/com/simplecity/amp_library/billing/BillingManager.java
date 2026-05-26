package com.simplecity.amp_library.billing; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.app.Activity; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.util.Log; // NOSONAR
import com.android.billingclient.api.BillingClient; // NOSONAR
import com.android.billingclient.api.BillingClientStateListener; // NOSONAR
import com.android.billingclient.api.BillingFlowParams; // NOSONAR
import com.android.billingclient.api.Purchase; // NOSONAR
import com.android.billingclient.api.PurchasesUpdatedListener; // NOSONAR
import com.simplecity.amp_library.constants.Config; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BillingManager implements PurchasesUpdatedListener { //NOSONAR

    private static final String TAG = "BillingManager"; //NOSONAR

    public static final int BILLING_MANAGER_NOT_INITIALIZED = -1; //NOSONAR

    public interface BillingUpdatesListener { //NOSONAR
        void onPurchasesUpdated(List<Purchase> purchases); //NOSONAR

        void onPremiumPurchaseCompleted(); //NOSONAR

        void onPremiumPurchaseRestored(); //NOSONAR
    } // NOSONAR

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
    } // NOSONAR

    private void startServiceConnection(UnsafeAction executeOnSuccess) { //NOSONAR
        if (billingClient != null) { //NOSONAR
            billingClient.startConnection(new BillingClientStateListener() { //NOSONAR
                @Override //NOSONAR
                public void onBillingSetupFinished(int responseCode) { //NOSONAR
                    if (responseCode == BillingClient.BillingResponse.OK) { //NOSONAR
                        serviceConnected = true; //NOSONAR
                        executeOnSuccess.run(); //NOSONAR
                    } // NOSONAR
                    billingClientResponseCode = responseCode; //NOSONAR
                } // NOSONAR

                @Override //NOSONAR
                public void onBillingServiceDisconnected() { //NOSONAR
                    // Try to restart the connection on the next request to // NOSONAR
                    // Google Play by calling the startConnection() method. // NOSONAR
                    serviceConnected = false; //NOSONAR
                } // NOSONAR
            }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @SuppressLint("DefaultLocale") //NOSONAR
    @Override //NOSONAR
    public void onPurchasesUpdated(int resultCode, @Nullable List<Purchase> purchases) { //NOSONAR
        if (resultCode == BillingClient.BillingResponse.OK && purchases != null) { //NOSONAR
            Purchase premiumPurchase = null; //NOSONAR
            for (Purchase purchase : purchases) { //NOSONAR
                if (purchase.getSku().equals(Config.SKU_PREMIUM)) { //NOSONAR
                    premiumPurchase = purchase; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            if (purchaseFlowInitiated || restorePurchasesInitiated) { //NOSONAR
                if (premiumPurchase != null) { //NOSONAR
                    if (purchaseFlowInitiated) { //NOSONAR
                        updatesListener.onPremiumPurchaseCompleted(); //NOSONAR
                        purchaseFlowInitiated = false; //NOSONAR
                    } // NOSONAR
                    if (restorePurchasesInitiated) { //NOSONAR
                        updatesListener.onPremiumPurchaseRestored(); //NOSONAR
                        restorePurchasesInitiated = false; //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                updatesListener.onPurchasesUpdated(purchases); //NOSONAR
            } // NOSONAR
        } else if (resultCode == BillingClient.BillingResponse.USER_CANCELED) { //NOSONAR
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping"); //NOSONAR
        } else { //NOSONAR
            LogUtils.logException(TAG, String.format("onPurchasesUpdated() got unknown resultCode: %d", resultCode), null); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void queryPurchases() { //NOSONAR
        UnsafeAction queryAction = () -> { //NOSONAR
            if (billingClient == null) return; //NOSONAR
            Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP); //NOSONAR
            if (purchasesResult.getResponseCode() == BillingClient.BillingResponse.OK) { //NOSONAR
                onPurchasesUpdated(BillingClient.BillingResponse.OK, purchasesResult.getPurchasesList()); //NOSONAR
            } else { //NOSONAR
                LogUtils.logException(TAG, "Query purchases() got an unknown response code: " + purchasesResult.getResponseCode(), null); //NOSONAR
            } // NOSONAR
        }; // NOSONAR

        if (serviceConnected) { //NOSONAR
            queryAction.run(); //NOSONAR
        } else { //NOSONAR
            startServiceConnection(queryAction); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Start a purchase or subscription replace flow // NOSONAR
     */ // NOSONAR
    public void initiatePurchaseFlow(final String skuId, final @BillingClient.SkuType String billingType) { //NOSONAR
        UnsafeAction purchaseFlowRequest = () -> { //NOSONAR
            BillingFlowParams purchaseParams = BillingFlowParams.newBuilder() //NOSONAR
                    .setSku(skuId) //NOSONAR
                    .setType(billingType) //NOSONAR
                    .build(); //NOSONAR
            if (billingClient != null) { //NOSONAR
                billingClient.launchBillingFlow(activity, purchaseParams); //NOSONAR
                purchaseFlowInitiated = true; //NOSONAR
            } // NOSONAR
        }; // NOSONAR

        if (serviceConnected) { //NOSONAR
            purchaseFlowRequest.run(); //NOSONAR
        } else { //NOSONAR
            startServiceConnection(purchaseFlowRequest); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void restorePurchases() { //NOSONAR
        restorePurchasesInitiated = true; //NOSONAR
        queryPurchases(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the value Billing client response code or BILLING_MANAGER_NOT_INITIALIZED if the // NOSONAR
     * client connection response was not received yet. // NOSONAR
     */ // NOSONAR
    public int getBillingClientResponseCode() { //NOSONAR
        return billingClientResponseCode; //NOSONAR
    } // NOSONAR

    public void destroy() { //NOSONAR
        Log.d(TAG, "Destroying the manager."); //NOSONAR

        if (billingClient != null && billingClient.isReady()) { //NOSONAR
            billingClient.endConnection(); //NOSONAR
            billingClient = null; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
