package com.simplecity.amp_library.ui.common; // NOSONAR

import android.Manifest; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.ServiceConnection; // NOSONAR
import android.media.AudioManager; // NOSONAR
import android.os.Build; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.IBinder; // NOSONAR
import android.support.annotation.CallSuper; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.view.KeyEvent; // NOSONAR
import android.view.Window; // NOSONAR
import android.widget.Toast; // NOSONAR
import com.afollestad.aesthetic.AestheticActivity; // NOSONAR
import com.android.billingclient.api.BillingClient; // NOSONAR
import com.android.billingclient.api.Purchase; // NOSONAR
import com.greysonparrelli.permiso.Permiso; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.billing.BillingManager; // NOSONAR
import com.simplecity.amp_library.constants.Config; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeDialog; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.MusicServiceConnectionUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import dagger.android.AndroidInjection; // NOSONAR
import dagger.android.AndroidInjector; // NOSONAR
import dagger.android.DispatchingAndroidInjector; // NOSONAR
import dagger.android.support.HasSupportFragmentInjector; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseActivity extends AestheticActivity implements //NOSONAR
        HasSupportFragmentInjector, //NOSONAR
        ServiceConnection, //NOSONAR
        BillingManager.BillingUpdatesListener { //NOSONAR

    private Boolean bindInFlight = false; //NOSONAR

    @Nullable //NOSONAR
    private MusicServiceConnectionUtils.ServiceToken token; //NOSONAR

    @Inject //NOSONAR
    DispatchingAndroidInjector<Fragment> fragmentInjector; //NOSONAR

    @Inject //NOSONAR
    BillingManager billingManager; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    AnalyticsManager analyticsManager; //NOSONAR

    @CallSuper //NOSONAR
    protected void onCreate(final Bundle savedInstanceState) { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        Permiso.getInstance().setActivity(this); //NOSONAR

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() { //NOSONAR
            @Override //NOSONAR
            public void onPermissionResult(Permiso.ResultSet resultSet) { //NOSONAR
                if (resultSet.areAllPermissionsGranted()) { //NOSONAR
                    bindService(); //NOSONAR
                } else { //NOSONAR
                    Toast.makeText(BaseActivity.this, "Permission check failed", Toast.LENGTH_LONG).show(); //NOSONAR
                    finish(); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) { //NOSONAR
                callback.onRationaleProvided(); //NOSONAR
            } // NOSONAR
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK); //NOSONAR

        setVolumeControlStream(AudioManager.STREAM_MUSIC); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onResume() { //NOSONAR
        keepScreenOn(settingsManager.keepScreenOn()); //NOSONAR
        super.onResume(); //NOSONAR

        if (token == null) { //NOSONAR
            bindService(); //NOSONAR
        } // NOSONAR

        Permiso.getInstance().setActivity(this); //NOSONAR

        if (billingManager.getBillingClientResponseCode() == BillingClient.BillingResponse.OK) { //NOSONAR
            billingManager.queryPurchases(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //NOSONAR
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); //NOSONAR
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDestroy() { //NOSONAR
        unbindService(); //NOSONAR

        billingManager.destroy(); //NOSONAR

        super.onDestroy(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public final AndroidInjector<Fragment> supportFragmentInjector() { //NOSONAR
        return fragmentInjector; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPurchasesUpdated(List<Purchase> purchases) { //NOSONAR
        for (Purchase purchase : purchases) { //NOSONAR
            if (purchase.getSku().equals(Config.SKU_PREMIUM)) { //NOSONAR
                ((ShuttleApplication) getApplicationContext()).setIsUpgraded(true); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPremiumPurchaseCompleted() { //NOSONAR
        ((ShuttleApplication) getApplicationContext()).setIsUpgraded(true); //NOSONAR
        new UpgradeDialog().show(getSupportFragmentManager()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPremiumPurchaseRestored() { //NOSONAR
        ((ShuttleApplication) getApplicationContext()).setIsUpgraded(true); //NOSONAR
        Toast.makeText(BaseActivity.this, R.string.iab_purchase_restored, Toast.LENGTH_SHORT).show(); //NOSONAR
    } // NOSONAR

    void bindService() { //NOSONAR
        if (!bindInFlight) { //NOSONAR
            bindInFlight = true; //NOSONAR
            MusicServiceConnectionUtils.bindToService( //NOSONAR
                    getLifecycle(), //NOSONAR
                    this, //NOSONAR
                    analyticsManager, //NOSONAR
                    this, serviceToken -> { //NOSONAR
                        token = serviceToken; //NOSONAR
                        this.bindInFlight = false; //NOSONAR
                    } // NOSONAR
            ); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    void unbindService() { //NOSONAR
        if (token != null) { //NOSONAR
            MusicServiceConnectionUtils.unbindFromService(token); //NOSONAR
            token = null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    @CallSuper //NOSONAR
    public void onServiceConnected(ComponentName name, IBinder service) { //NOSONAR
        sendBroadcast(new Intent(InternalIntents.SERVICE_CONNECTED)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onServiceDisconnected(ComponentName name) { //NOSONAR
        unbindService(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onKeyDown(int keyCode, KeyEvent event) { //NOSONAR
        //Fix for issue on LG devices // NOSONAR
        if (keyCode == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR
        return super.onKeyDown(keyCode, event); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onKeyUp(int keyCode, KeyEvent event) { //NOSONAR
        //Fix for issue on LG devices // NOSONAR
        if (keyCode == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) { //NOSONAR
            openOptionsMenu(); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR
        return super.onKeyUp(keyCode, event); //NOSONAR
    } // NOSONAR

    private void keepScreenOn(boolean on) { //NOSONAR
        final Window window = getWindow(); //NOSONAR
        if (on) { //NOSONAR
            window.addFlags(FLAG_KEEP_SCREEN_ON); //NOSONAR
        } else { //NOSONAR
            window.clearFlags(FLAG_KEEP_SCREEN_ON); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    protected abstract String screenName(); //NOSONAR
} // NOSONAR
