package com.simplecity.amp_library.ui.settings; // NOSONAR

import android.content.Intent; // NOSONAR
import android.support.annotation.StringRes; // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog; // NOSONAR
import com.afollestad.materialdialogs.color.ColorChooserDialog; // NOSONAR
import com.simplecity.amp_library.ui.views.PurchaseView; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface SettingsView extends PurchaseView { //NOSONAR

    // Support // NOSONAR

    void showChangelog(); //NOSONAR

    void showRestorePurchasesMessage(@StringRes int messageResId); //NOSONAR

    // Display // NOSONAR

    void showTabChooserDialog(); //NOSONAR

    void showDefaultPageDialog(MaterialDialog dialog); //NOSONAR

    // Themes // NOSONAR

    void showBaseThemeDialog(MaterialDialog dialog); //NOSONAR

    void showPrimaryColorDialog(ColorChooserDialog dialog); //NOSONAR

    void showAccentColorDialog(ColorChooserDialog dialog); //NOSONAR

    // Artwork // NOSONAR

    void showDownloadArtworkDialog(MaterialDialog dialog); //NOSONAR

    void showDeleteArtworkDialog(MaterialDialog dialog); //NOSONAR

    void showArtworkPreferenceChangeDialog(MaterialDialog dialog); //NOSONAR

    // Scrobbling // NOSONAR
    void launchDownloadScrobblerIntent(Intent intent); //NOSONAR

    // Blacklist/Whitelist // NOSONAR

    void showBlacklistDialog(); //NOSONAR

    void showWhitelistDialog(); //NOSONAR
} // NOSONAR
