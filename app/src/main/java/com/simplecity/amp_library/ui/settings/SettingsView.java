package com.simplecity.amp_library.ui.settings;

import android.content.Intent;
import android.support.annotation.StringRes;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.simplecity.amp_library.ui.views.PurchaseView;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface SettingsView extends PurchaseView {

    // Support

    void showChangelog();

    void showRestorePurchasesMessage(@StringRes int messageResId);

    // Display

    void showTabChooserDialog();

    void showDefaultPageDialog(MaterialDialog dialog);

    // Themes

    void showBaseThemeDialog(MaterialDialog dialog);

    void showPrimaryColorDialog(ColorChooserDialog dialog);

    void showAccentColorDialog(ColorChooserDialog dialog);

    // Artwork

    void showDownloadArtworkDialog(MaterialDialog dialog);

    void showDeleteArtworkDialog(MaterialDialog dialog);

    void showArtworkPreferenceChangeDialog(MaterialDialog dialog);

    // Scrobbling
    void launchDownloadScrobblerIntent(Intent intent);

    // Blacklist/Whitelist

    void showBlacklistDialog();

    void showWhitelistDialog();
}
