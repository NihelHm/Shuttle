package com.simplecity.amp_library.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.annimon.stream.IntPair;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.billing.BillingManager;
import com.simplecity.amp_library.model.CategoryItem;
import com.simplecity.amp_library.services.ArtworkDownloadService;
import com.simplecity.amp_library.ui.common.PurchasePresenter;
import com.simplecity.amp_library.utils.AnalyticsManager;
import com.simplecity.amp_library.utils.ColorPalette;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SettingsPresenter extends PurchasePresenter<SettingsView> { //NOSONAR

    private ShuttleApplication application; //NOSONAR
    private BillingManager billingManager; //NOSONAR
    private AnalyticsManager analyticsManager; //NOSONAR
    private SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    public SettingsPresenter( //NOSONAR
            ShuttleApplication application, //NOSONAR
            BillingManager billingManager, //NOSONAR
            AnalyticsManager analyticsManager, //NOSONAR
            SettingsManager settingsManager //NOSONAR
    ) {

        super(); //NOSONAR

        this.application = application; //NOSONAR
        this.billingManager = billingManager; //NOSONAR
        this.analyticsManager = analyticsManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    // Support Preferences

    void changelogClicked() { //NOSONAR
        analyticsManager.logChangelogViewed(); //NOSONAR

        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showChangelog(); //NOSONAR
        }
    }

    public void restorePurchasesClicked() { //NOSONAR
        billingManager.restorePurchases(); //NOSONAR
    }

    // Display

    public void chooseTabsClicked() { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showTabChooserDialog(); //NOSONAR
        }
    }

    public void chooseDefaultPageClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context); //NOSONAR
            List<CategoryItem> categoryItems = Stream.of(CategoryItem.getCategoryItems(sharedPreferences)) //NOSONAR
                    .filter(categoryItem -> categoryItem.isChecked) //NOSONAR
                    .toList(); //NOSONAR

            int defaultPageType = settingsManager.getDefaultPageType(); //NOSONAR
            int defaultPage = Math.min(Stream.of(categoryItems) //NOSONAR
                    .indexed() //NOSONAR
                    .filter(categoryItemIntPair -> categoryItemIntPair.getSecond().type == defaultPageType) //NOSONAR
                    .map(IntPair::getFirst) //NOSONAR
                    .findFirst() //NOSONAR
                    .orElse(1), categoryItems.size()); //NOSONAR

            settingsView.showDefaultPageDialog( //NOSONAR
                    new MaterialDialog.Builder(context) //NOSONAR
                            .title(R.string.pref_title_default_page) //NOSONAR
                            .items(Stream.of(categoryItems) //NOSONAR
                                    .map(categoryItem -> context.getString(categoryItem.getTitleResId())) //NOSONAR
                                    .toList()) //NOSONAR
                            .itemsCallbackSingleChoice(defaultPage, (dialog, itemView, which, text) -> { //NOSONAR
                                settingsManager.setDefaultPageType(categoryItems.get(which).type); //NOSONAR
                                return false; //NOSONAR
                            })
                            .build()); //NOSONAR
        }
    }

    // Themes

    public void baseThemeClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showBaseThemeDialog(new MaterialDialog.Builder(context) //NOSONAR
                    .title(R.string.pref_title_base_theme) //NOSONAR
                    .items(R.array.baseThemeArray) //NOSONAR
                    .itemsCallback((materialDialog, view, i, charSequence) -> changeBaseTheme(context, i)) //NOSONAR
                    .build()); //NOSONAR
        }
    }

    private void changeBaseTheme(Context context, int i) { //NOSONAR
        int theme = R.style.AppTheme_Light; //NOSONAR
        boolean isDark = false; //NOSONAR
        switch (i) { //NOSONAR
            case 0: //NOSONAR
                //Light
                theme = R.style.AppTheme_Light; //NOSONAR
                isDark = false; //NOSONAR
                break; //NOSONAR
            case 1: //NOSONAR
                //Dark
                theme = R.style.AppTheme; //NOSONAR
                isDark = true; //NOSONAR
                break; //NOSONAR
            case 2: //NOSONAR
                //Black
                theme = R.style.AppTheme_Black; //NOSONAR
                isDark = true; //NOSONAR
                break; //NOSONAR
        }

        Aesthetic.get(context) //NOSONAR
                .activityTheme(theme) //NOSONAR
                .isDark(isDark) //NOSONAR
                .apply(); //NOSONAR
    }

    public void primaryColorClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showPrimaryColorDialog( //NOSONAR
                    new ColorChooserDialog.Builder(context, R.string.pref_title_theme_pick_color) //NOSONAR
                            .customColors(ColorPalette.getPrimaryColors(context, settingsManager), ColorPalette.getPrimaryColorsSub(context, settingsManager)) //NOSONAR
                            .allowUserColorInput(ShuttleUtils.isUpgraded(application, settingsManager)) //NOSONAR
                            .allowUserColorInputAlpha(false) //NOSONAR
                            .dynamicButtonColor(false) //NOSONAR
                            .preselect(Aesthetic.get(context).colorPrimary().blockingFirst()) //NOSONAR
                            .build()); //NOSONAR
        }
    }

    public void changePrimaryColor(Context context, int color) { //NOSONAR
        Aesthetic.get(context) //NOSONAR
                .colorPrimary(color) //NOSONAR
                .colorStatusBarAuto() //NOSONAR
                .apply(); //NOSONAR

        settingsManager.storePrimaryColor(color); //NOSONAR
    }

    public void accentColorClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showAccentColorDialog( //NOSONAR
                    new ColorChooserDialog.Builder(context, R.string.pref_title_theme_pick_accent_color) //NOSONAR
                            .accentMode(true) //NOSONAR
                            .allowUserColorInput(true) //NOSONAR
                            .allowUserColorInputAlpha(false) //NOSONAR
                            .dynamicButtonColor(false) //NOSONAR
                            .preselect(Aesthetic.get(context).colorAccent().blockingFirst()) //NOSONAR
                            .build()); //NOSONAR
        }
    }

    public void changeAccentColor(Context context, int color) { //NOSONAR
        Aesthetic.get(context) //NOSONAR
                .colorAccent(color) //NOSONAR
                .apply(); //NOSONAR

        settingsManager.storeAccentColor(color); //NOSONAR
    }

    public void tintNavBarClicked(Context context, boolean tintNavBar) { //NOSONAR
        Aesthetic.get(context) //NOSONAR
                .colorNavigationBarAuto(tintNavBar) //NOSONAR
                .apply(); //NOSONAR
    }

    public void usePaletteClicked(Context context, boolean usePalette) { //NOSONAR
        // If we're not using palette any more, set the primary color back to default
        if (!usePalette) { //NOSONAR
            int storedPrimaryColor = settingsManager.getPrimaryColor(); //NOSONAR
            int storedAccentColor = settingsManager.getAccentColor(); //NOSONAR

            Aesthetic.get(context) //NOSONAR
                    .colorPrimary(storedPrimaryColor == -1 ? ContextCompat.getColor(context, R.color.md_blue_500) : storedPrimaryColor) //NOSONAR
                    .colorAccent(storedAccentColor == -1 ? ContextCompat.getColor(context, R.color.md_amber_300) : storedAccentColor) //NOSONAR
                    .colorStatusBarAuto() //NOSONAR
                    .colorNavigationBarAuto(settingsManager.getTintNavBar()) //NOSONAR
                    .apply(); //NOSONAR
        }
    }

    public void usePaletteNowPlayingOnlyClicked(Context context, boolean usePaletteNowPlayingOnly) { //NOSONAR
        // If we're only using palette for 'now playing', set the primary color back to default
        if (usePaletteNowPlayingOnly) { //NOSONAR
            int storedPrimaryColor = settingsManager.getPrimaryColor(); //NOSONAR
            int storedAccentColor = settingsManager.getAccentColor(); //NOSONAR

            Aesthetic.get(context) //NOSONAR
                    .colorPrimary(storedPrimaryColor == -1 ? ContextCompat.getColor(context, R.color.md_blue_500) : storedPrimaryColor) //NOSONAR
                    .colorAccent(storedAccentColor == -1 ? ContextCompat.getColor(context, R.color.md_amber_300) : storedAccentColor) //NOSONAR
                    .colorStatusBarAuto() //NOSONAR
                    .colorNavigationBarAuto(settingsManager.getTintNavBar()) //NOSONAR
                    .apply(); //NOSONAR
        }
    }

    // Artwork

    public void downloadArtworkClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showDownloadArtworkDialog( //NOSONAR
                    new MaterialDialog.Builder(context) //NOSONAR
                            .title(R.string.pref_title_download_artwork) //NOSONAR
                            .content(R.string.pref_warning_download_artwork) //NOSONAR
                            .positiveText(R.string.download) //NOSONAR
                            .onPositive((dialog, which) -> downloadArtwork(context)) //NOSONAR
                            .negativeText(R.string.cancel) //NOSONAR
                            .build()); //NOSONAR
        }
    }

    private void downloadArtwork(Context context) { //NOSONAR
        Intent intent = new Intent(context, ArtworkDownloadService.class); //NOSONAR
        context.startService(intent); //NOSONAR
    }

    public void deleteArtworkClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showDeleteArtworkDialog( //NOSONAR
                    new MaterialDialog.Builder(context) //NOSONAR
                            .title(R.string.pref_title_delete_artwork) //NOSONAR
                            .iconRes(R.drawable.ic_warning_24dp) //NOSONAR
                            .content(R.string.delete_artwork_confirmation_dialog) //NOSONAR
                            .positiveText(R.string.button_ok) //NOSONAR
                            .onPositive((materialDialog, dialogAction) -> deleteArtwork()) //NOSONAR
                            .negativeText(R.string.cancel) //NOSONAR
                            .build()); //NOSONAR
        }
    }

    private void deleteArtwork() { //NOSONAR
        //Clear Glide' mem & disk cache

        Glide.get(application).clearMemory(); //NOSONAR

        Completable.fromAction(() -> Glide.get(application).clearDiskCache()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(); //NOSONAR
    }

    public void changeArtworkPreferenceClicked(Context context) { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showArtworkPreferenceChangeDialog( //NOSONAR
                    new MaterialDialog.Builder(context) //NOSONAR
                            .title(R.string.pref_title_delete_artwork) //NOSONAR
                            .content(R.string.pref_summary_change_artwork_source) //NOSONAR
                            .positiveText(R.string.pref_button_remove_artwork) //NOSONAR
                            .onPositive((dialog1, which) -> deleteArtwork()) //NOSONAR
                            .negativeText(R.string.close) //NOSONAR
                            .show()); //NOSONAR
        }
    }

    // Headset/Bluetooth

    // Scrobbling

    public void downloadScrobblerClicked() { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.launchDownloadScrobblerIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.adam.aslfms"))); //NOSONAR
        }
    }

    public void viewBlacklistClicked() { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showBlacklistDialog(); //NOSONAR
        }
    }

    public void viewWhitelistClicked() { //NOSONAR
        SettingsView settingsView = getView(); //NOSONAR
        if (settingsView != null) { //NOSONAR
            settingsView.showWhitelistDialog(); //NOSONAR
        }
    }
}
