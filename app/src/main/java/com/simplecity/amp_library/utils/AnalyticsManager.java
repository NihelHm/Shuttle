package com.simplecity.amp_library.utils; // NOSONAR

import android.app.Activity; // NOSONAR
import android.content.Context; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.util.Log; // NOSONAR
import com.crashlytics.android.answers.Answers; // NOSONAR
import com.crashlytics.android.answers.CustomEvent; // NOSONAR
import com.crashlytics.android.core.CrashlyticsCore; // NOSONAR
import com.google.firebase.analytics.FirebaseAnalytics; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR
import javax.inject.Inject; // NOSONAR
import javax.inject.Singleton; // NOSONAR

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AnalyticsManager { //NOSONAR

    private static final String TAG = "AnalyticsManager"; //NOSONAR

    private Context context; //NOSONAR

    @Inject //NOSONAR
    public AnalyticsManager(Context context) { //NOSONAR
        this.context = context; //NOSONAR
    } // NOSONAR

    private boolean analyticsEnabled() { //NOSONAR
        return !BuildConfig.DEBUG; //NOSONAR
    } // NOSONAR

    public @interface UpgradeType { //NOSONAR
        String NAG = "Nag"; //NOSONAR
        String FOLDER = "Folder"; //NOSONAR
        String UPGRADE = "Upgrade"; //NOSONAR
    } // NOSONAR

    public void logChangelogViewed() { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Bundle bundle = new Bundle(); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "changelog"); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0"); //NOSONAR

        FirebaseAnalytics.getInstance(context) //NOSONAR
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle); //NOSONAR
        Answers.getInstance().logCustom(new CustomEvent("Changelog Viewed")); //NOSONAR
    } // NOSONAR

    public void logUpgrade(@UpgradeType String upgradeType) { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Bundle bundle = new Bundle(); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0"); //NOSONAR
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY, 0); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, upgradeType); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "upgrade"); //NOSONAR

        FirebaseAnalytics.getInstance(context) //NOSONAR
                .logEvent(FirebaseAnalytics.Event.PRESENT_OFFER, bundle); //NOSONAR
    } // NOSONAR

    public void logScreenName(Activity activity, String name) { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        CrashlyticsCore.getInstance().log(String.format("Screen: %s", name)); //NOSONAR
        FirebaseAnalytics.getInstance(context).setCurrentScreen(activity, name, null); //NOSONAR
    } // NOSONAR

    public void setIsUpgraded(boolean isUpgraded) { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        FirebaseAnalytics.getInstance(context).setUserProperty("Upgraded", String.valueOf(isUpgraded)); //NOSONAR
    } // NOSONAR

    public void logInitialTheme(ThemeUtils.Theme theme) { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Bundle params = new Bundle(); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(theme.id)); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, String.format("%s-%s-%s", theme.primaryColorName, theme.accentColorName, theme.isDark)); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "themes"); //NOSONAR
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params); //NOSONAR
    } // NOSONAR

    public void logRateShown() { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Bundle params = new Bundle(); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "show_rate_snackbar"); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "show_rate_snackbar"); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "rate_app"); //NOSONAR

        FirebaseAnalytics.getInstance(context) //NOSONAR
                .logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params); //NOSONAR
    } // NOSONAR

    public void logRateClicked() { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Bundle bundle = new Bundle(); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "rate_snackbar"); //NOSONAR
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0"); //NOSONAR

        FirebaseAnalytics.getInstance(context) //NOSONAR
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle); //NOSONAR
    } // NOSONAR

    public void didSnow() { //NOSONAR
        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Bundle params = new Bundle(); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "show_snow"); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "show_snow"); //NOSONAR
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "easter_eggs"); //NOSONAR

        FirebaseAnalytics.getInstance(context) //NOSONAR
                .logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params); //NOSONAR
    } // NOSONAR

    public void dropBreadcrumb(String tag, String breadCrumb) { //NOSONAR

        Log.d(tag, breadCrumb); //NOSONAR

        if (!analyticsEnabled()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        CrashlyticsCore.getInstance().log(String.format("%s | %s", tag, breadCrumb)); //NOSONAR
    } // NOSONAR
} // NOSONAR
