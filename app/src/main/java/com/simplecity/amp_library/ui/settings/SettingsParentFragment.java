package com.simplecity.amp_library.ui.settings; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.StringRes; // NOSONAR
import android.support.annotation.XmlRes; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.support.v7.preference.Preference; // NOSONAR
import android.support.v7.preference.PreferenceFragmentCompat; // NOSONAR
import android.support.v7.preference.SwitchPreferenceCompat; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.Toast; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.Rx; // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog; // NOSONAR
import com.afollestad.materialdialogs.color.ColorChooserDialog; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.billing.BillingManager; // NOSONAR
import com.simplecity.amp_library.model.InclExclItem; // NOSONAR
import com.simplecity.amp_library.ui.dialog.ChangelogDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.InclExclDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.MiniPlayerLockManager; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import javax.inject.Inject; // NOSONAR
import test.com.androidnavigation.base.Controller; // NOSONAR
import test.com.androidnavigation.base.NavigationController; // NOSONAR
import test.com.androidnavigation.fragment.BaseController; // NOSONAR
import test.com.androidnavigation.fragment.BaseNavigationController; // NOSONAR
import test.com.androidnavigation.fragment.FragmentInfo; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SettingsParentFragment extends BaseNavigationController implements //NOSONAR
        DrawerLockManager.DrawerLock, //NOSONAR
        MiniPlayerLockManager.MiniPlayerLock { //NOSONAR

    public static String ARG_PREFERENCE_RESOURCE = "preference_resource"; //NOSONAR
    public static String ARG_TITLE = "title"; //NOSONAR

    @BindView(R.id.toolbar) //NOSONAR
    Toolbar toolbar; //NOSONAR

    @XmlRes //NOSONAR
    int preferenceResource; //NOSONAR

    @StringRes //NOSONAR
    int titleResId; //NOSONAR

    private Unbinder unbinder; //NOSONAR

    public static SettingsParentFragment newInstance(@XmlRes int preferenceResource, @StringRes int titleResId) { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putInt(ARG_PREFERENCE_RESOURCE, preferenceResource); //NOSONAR
        args.putInt(ARG_TITLE, titleResId); //NOSONAR
        SettingsParentFragment fragment = new SettingsParentFragment(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    } // NOSONAR

    public SettingsParentFragment() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAttach(Context context) { //NOSONAR
        super.onAttach(context); //NOSONAR

        titleResId = getArguments().getInt(ARG_TITLE); //NOSONAR
        preferenceResource = getArguments().getInt(ARG_PREFERENCE_RESOURCE); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public FragmentInfo getRootViewControllerInfo() { //NOSONAR
        return SettingsFragment.getFragmentInfo(preferenceResource); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //NOSONAR
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        toolbar.setTitle(titleResId); //NOSONAR
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed()); //NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR
        DrawerLockManager.getInstance().addDrawerLock(this); //NOSONAR
        MiniPlayerLockManager.getInstance().addMiniPlayerLock(this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        DrawerLockManager.getInstance().removeDrawerLock(this); //NOSONAR
        MiniPlayerLockManager.getInstance().removeMiniPlayerLock(this); //NOSONAR
        super.onPause(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    } // NOSONAR

    public static class SettingsFragment extends PreferenceFragmentCompat implements //NOSONAR
            Controller, //NOSONAR
            SupportView, //NOSONAR
            SettingsView, //NOSONAR
            ColorChooserDialog.ColorCallback { //NOSONAR

        @XmlRes //NOSONAR
        int preferenceResource; //NOSONAR

        @Inject //NOSONAR
        SupportPresenter supportPresenter; //NOSONAR

        @Inject //NOSONAR
        SettingsPresenter settingsPresenter; //NOSONAR

        @Inject //NOSONAR
        BillingManager billingManager; //NOSONAR

        @Inject //NOSONAR
        AnalyticsManager analyticsManager; //NOSONAR

        @Inject //NOSONAR
        SettingsManager settingsManager; //NOSONAR

        private ColorChooserDialog primaryColorDialog; //NOSONAR
        private ColorChooserDialog accentColorDialog; //NOSONAR

        private Disposable aestheticDisposable; //NOSONAR

        public static FragmentInfo getFragmentInfo(@XmlRes int preferenceResource) { //NOSONAR
            Bundle args = new Bundle(); //NOSONAR
            args.putInt(ARG_PREFERENCE_RESOURCE, preferenceResource); //NOSONAR
            return new FragmentInfo(SettingsFragment.class, args, "settingsRoot"); //NOSONAR
        } // NOSONAR

        public static SettingsFragment newInstance(@XmlRes int preferenceResource) { //NOSONAR
            Bundle args = new Bundle(); //NOSONAR
            args.putInt(ARG_PREFERENCE_RESOURCE, preferenceResource); //NOSONAR
            SettingsFragment settingsFragment = new SettingsFragment(); //NOSONAR
            settingsFragment.setArguments(args); //NOSONAR
            return settingsFragment; //NOSONAR
        } // NOSONAR

        public SettingsFragment() { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onAttach(Context context) { //NOSONAR
            super.onAttach(context); //NOSONAR

            preferenceResource = getArguments().getInt(ARG_PREFERENCE_RESOURCE); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) { //NOSONAR
            addPreferencesFromResource(preferenceResource); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onCreate(Bundle savedInstanceState) { //NOSONAR
            AndroidSupportInjection.inject(this); //NOSONAR
            super.onCreate(savedInstanceState); //NOSONAR

            // Support Preferences // NOSONAR

            Preference changelogPreference = findPreference(SettingsManager.KEY_PREF_CHANGELOG); //NOSONAR
            if (changelogPreference != null) { //NOSONAR
                changelogPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.changelogClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference faqPreference = findPreference(SettingsManager.KEY_PREF_FAQ); //NOSONAR
            if (faqPreference != null) { //NOSONAR
                faqPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    supportPresenter.faqClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference helpPreference = findPreference(SettingsManager.KEY_PREF_HELP); //NOSONAR
            if (helpPreference != null) { //NOSONAR
                helpPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    supportPresenter.helpClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference ratePreference = findPreference(SettingsManager.KEY_PREF_RATE); //NOSONAR
            if (ratePreference != null) { //NOSONAR
                ratePreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    supportPresenter.rateClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference restorePurchasesPreference = findPreference(SettingsManager.KEY_PREF_RESTORE_PURCHASES); //NOSONAR
            if (restorePurchasesPreference != null) { //NOSONAR
                if (ShuttleUtils.isAmazonBuild() || ShuttleUtils.isUpgraded((ShuttleApplication) getContext().getApplicationContext(), settingsManager)) { //NOSONAR
                    restorePurchasesPreference.setVisible(false); //NOSONAR
                } // NOSONAR
                restorePurchasesPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.restorePurchasesClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            // Display // NOSONAR

            Preference chooseTabsPreference = findPreference(SettingsManager.KEY_PREF_TAB_CHOOSER); //NOSONAR
            if (chooseTabsPreference != null) { //NOSONAR
                chooseTabsPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.chooseTabsClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference defaultPagePreference = findPreference(SettingsManager.KEY_PREF_DEFAULT_PAGE); //NOSONAR
            if (defaultPagePreference != null) { //NOSONAR
                defaultPagePreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.chooseDefaultPageClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            // Themes // NOSONAR

            Preference baseThemePreference = findPreference(SettingsManager.KEY_PREF_THEME_BASE); //NOSONAR
            if (baseThemePreference != null) { //NOSONAR
                baseThemePreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.baseThemeClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference primaryColorPreference = findPreference(SettingsManager.KEY_PREF_PRIMARY_COLOR); //NOSONAR
            if (primaryColorPreference != null) { //NOSONAR
                primaryColorPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.primaryColorClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference accentColorColorPreference = findPreference(SettingsManager.KEY_PREF_ACCENT_COLOR); //NOSONAR
            if (accentColorColorPreference != null) { //NOSONAR
                accentColorColorPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.accentColorClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat tintNavBarColorPreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_PREF_NAV_BAR); //NOSONAR
            if (tintNavBarColorPreference != null) { //NOSONAR
                tintNavBarColorPreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.tintNavBarClicked(getContext(), (Boolean) newValue); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat usePalettePreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_PREF_PALETTE); //NOSONAR
            if (usePalettePreference != null) { //NOSONAR
                usePalettePreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.usePaletteClicked(getContext(), (Boolean) newValue); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat usePaletteNowPlayingOnlyPreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_PREF_PALETTE_NOW_PLAYING_ONLY); //NOSONAR
            if (usePaletteNowPlayingOnlyPreference != null) { //NOSONAR
                usePaletteNowPlayingOnlyPreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.usePaletteNowPlayingOnlyClicked(getContext(), (Boolean) newValue); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            // Artwork // NOSONAR

            Preference downloadArtworkPreference = findPreference(SettingsManager.KEY_PREF_DOWNLOAD_ARTWORK); //NOSONAR
            if (downloadArtworkPreference != null) { //NOSONAR
                downloadArtworkPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.downloadArtworkClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference deleteArtworkPreference = findPreference(SettingsManager.KEY_PREF_DELETE_ARTWORK); //NOSONAR
            if (deleteArtworkPreference != null) { //NOSONAR
                deleteArtworkPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.deleteArtworkClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat ignoreEmbeddedArtworkPreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_IGNORE_EMBEDDED_ARTWORK); //NOSONAR
            if (ignoreEmbeddedArtworkPreference != null) { //NOSONAR
                ignoreEmbeddedArtworkPreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.changeArtworkPreferenceClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat ignoreFolderArtworkPreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_IGNORE_FOLDER_ARTWORK); //NOSONAR
            if (ignoreFolderArtworkPreference != null) { //NOSONAR
                ignoreFolderArtworkPreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.changeArtworkPreferenceClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat preferEmbeddedArtworkPreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_PREFER_EMBEDDED_ARTWORK); //NOSONAR
            if (preferEmbeddedArtworkPreference != null) { //NOSONAR
                preferEmbeddedArtworkPreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.changeArtworkPreferenceClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            SwitchPreferenceCompat ignoreMediaStoreArtworkPreference = (SwitchPreferenceCompat) findPreference(SettingsManager.KEY_IGNORE_MEDIASTORE_ART); //NOSONAR
            if (ignoreMediaStoreArtworkPreference != null) { //NOSONAR
                ignoreMediaStoreArtworkPreference.setOnPreferenceChangeListener((preference, newValue) -> { //NOSONAR
                    settingsPresenter.changeArtworkPreferenceClicked(getContext()); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            // Headset/Bluetooth // NOSONAR

            // Scrobbling // NOSONAR

            Preference downloadScrobblerPreference = findPreference(SettingsManager.KEY_PREF_DOWNLOAD_SCROBBLER); //NOSONAR
            if (downloadScrobblerPreference != null) { //NOSONAR
                if (ShuttleUtils.isAmazonBuild()) { //NOSONAR
                    // Amazon don't allow links to the Play Store // NOSONAR
                    downloadScrobblerPreference.setVisible(false); //NOSONAR
                } // NOSONAR
                downloadScrobblerPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.downloadScrobblerClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            // Whitelist/Blacklist // NOSONAR

            Preference viewBlacklistPreference = findPreference(SettingsManager.KEY_PREF_BLACKLIST); //NOSONAR
            if (viewBlacklistPreference != null) { //NOSONAR
                viewBlacklistPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.viewBlacklistClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            Preference viewWhitelistPreference = findPreference(SettingsManager.KEY_PREF_WHITELIST); //NOSONAR
            if (viewWhitelistPreference != null) { //NOSONAR
                viewWhitelistPreference.setOnPreferenceClickListener(preference -> { //NOSONAR
                    settingsPresenter.viewWhitelistClicked(); //NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR

            // Upgrade preference // NOSONAR
            Preference upgradePreference = findPreference(SettingsManager.KEY_PREF_UPGRADE); //NOSONAR
            if (upgradePreference != null) { //NOSONAR
                if (ShuttleUtils.isUpgraded((ShuttleApplication) getContext().getApplicationContext(), settingsManager)) { //NOSONAR
                    upgradePreference.setVisible(false); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onResume() { //NOSONAR
            super.onResume(); //NOSONAR

            supportPresenter.bindView(this); //NOSONAR
            settingsPresenter.bindView(this); //NOSONAR

            aestheticDisposable = Aesthetic.get(getContext()).colorAccent() //NOSONAR
                    .compose(Rx.distinctToMainThread()) //NOSONAR
                    .subscribe(this::invalidateColors); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onPause() { //NOSONAR
            supportPresenter.unbindView(this); //NOSONAR
            settingsPresenter.unbindView(this); //NOSONAR

            aestheticDisposable.dispose(); //NOSONAR

            super.onPause(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public boolean onPreferenceTreeClick(Preference preference) { //NOSONAR
            if (preference.getKey() != null) { //NOSONAR
                switch (preference.getKey()) { //NOSONAR
                    case "pref_display": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_display), "DisplaySettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_themes": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_themes), "ThemeSettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_artwork": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_artwork), "ArtworkSettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_playback": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_playback), "PlaybackSettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_headset": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_headset), "HeadsetSettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_scrobbling": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_scrobbling), "ScrobblingSettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_blacklist": //NOSONAR
                        getNavigationController().pushViewController(SettingsFragment.newInstance(R.xml.settings_blacklist), "BlacklistSettings"); //NOSONAR
                        break; //NOSONAR
                    case "pref_upgrade": //NOSONAR
                        settingsPresenter.upgradeClicked(); //NOSONAR
                        break; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        void invalidateColors(int color) { //NOSONAR
            int preferenceCount = getPreferenceScreen().getPreferenceCount(); //NOSONAR
            for (int i = 0; i < preferenceCount; i++) { //NOSONAR
                tintPreferenceIcon(getPreferenceScreen().getPreference(i), color); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        void tintPreferenceIcon(Preference preference, int color) { //NOSONAR
            if (preference != null) { //NOSONAR
                Drawable icon = preference.getIcon(); //NOSONAR
                if (icon != null) { //NOSONAR
                    icon = DrawableCompat.wrap(icon); //NOSONAR
                    DrawableCompat.setTint(icon, color); //NOSONAR
                    preference.setIcon(icon); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) { //NOSONAR
            if (dialog == primaryColorDialog) { //NOSONAR
                settingsPresenter.changePrimaryColor(getContext(), selectedColor); //NOSONAR
            } else if (dialog == accentColorDialog) { //NOSONAR
                settingsPresenter.changeAccentColor(getContext(), selectedColor); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        // Support View // NOSONAR

        @Override //NOSONAR
        public void setVersion(String version) { //NOSONAR
            final Preference versionPreference = findPreference("pref_version"); //NOSONAR
            if (versionPreference != null) { //NOSONAR
                versionPreference.setSummary(version); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showFaq(Intent intent) { //NOSONAR
            startActivity(intent); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showHelp(Intent intent) { //NOSONAR
            startActivity(intent); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showRate(Intent intent) { //NOSONAR
            startActivity(intent); //NOSONAR
        } // NOSONAR

        @NonNull //NOSONAR
        @Override //NOSONAR
        public NavigationController<Fragment> getNavigationController() { //NOSONAR
            return BaseController.findNavigationController(this); //NOSONAR
        } // NOSONAR

        // Settings View // NOSONAR

        // Support // NOSONAR

        @Override //NOSONAR
        public void showChangelog() { //NOSONAR
            ChangelogDialog.Companion.newInstance().show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showUpgradeDialog() { //NOSONAR
            new UpgradeDialog().show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showRestorePurchasesMessage(int messageResId) { //NOSONAR
            Toast.makeText(getContext(), messageResId, Toast.LENGTH_LONG).show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showTabChooserDialog() { //NOSONAR
            new TabChooserDialog().show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showDefaultPageDialog(MaterialDialog dialog) { //NOSONAR
            dialog.show(); //NOSONAR
        } // NOSONAR

        // Themes // NOSONAR

        @Override //NOSONAR
        public void showBaseThemeDialog(MaterialDialog dialog) { //NOSONAR
            dialog.show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showPrimaryColorDialog(ColorChooserDialog dialog) { //NOSONAR
            primaryColorDialog = dialog.show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showAccentColorDialog(ColorChooserDialog dialog) { //NOSONAR
            accentColorDialog = dialog.show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        // Artwork // NOSONAR

        @Override //NOSONAR
        public void showDownloadArtworkDialog(MaterialDialog dialog) { //NOSONAR
            dialog.show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showDeleteArtworkDialog(MaterialDialog dialog) { //NOSONAR
            dialog.show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showArtworkPreferenceChangeDialog(MaterialDialog dialog) { //NOSONAR
            dialog.show(); //NOSONAR
        } // NOSONAR

        // Scrobbling // NOSONAR

        @Override //NOSONAR
        public void launchDownloadScrobblerIntent(Intent intent) { //NOSONAR
            startActivity(intent); //NOSONAR
        } // NOSONAR

        // Blacklist/Whitelist // NOSONAR

        @Override //NOSONAR
        public void showBlacklistDialog() { //NOSONAR
            InclExclDialog.Companion.newInstance(InclExclItem.Type.EXCLUDE).show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showWhitelistDialog() { //NOSONAR
            InclExclDialog.Companion.newInstance(InclExclItem.Type.INCLUDE).show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
