package com.simplecity.amp_library.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.simplecity.amp_library.BuildConfig;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.ui.common.Presenter;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import javax.inject.Inject;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SupportPresenter extends Presenter<SupportView> { //NOSONAR

    private ShuttleApplication application; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    public SupportPresenter(ShuttleApplication application, SettingsManager settingsManager) { //NOSONAR
        this.application = application; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(@NonNull SupportView view) { //NOSONAR
        super.bindView(view); //NOSONAR

        setAppVersion(); //NOSONAR
    }

    private void setAppVersion() { //NOSONAR
        SupportView supportView = getView(); //NOSONAR
        if (supportView != null) { //NOSONAR
            supportView.setVersion("Shuttle Music Player " + BuildConfig.VERSION_NAME + (ShuttleUtils.isUpgraded(application, settingsManager) ? " (Upgraded)" : " (Free)")); //NOSONAR
        }
    }

    public void faqClicked() { //NOSONAR
        Intent intent = new Intent(Intent.ACTION_VIEW); //NOSONAR
        intent.setData(Uri.parse("http://www.shuttlemusicplayer.com/#faq")); //NOSONAR
        SupportView supportView = getView(); //NOSONAR
        if (supportView != null) { //NOSONAR
            supportView.showFaq(intent); //NOSONAR
        }
    }

    public void helpClicked() { //NOSONAR
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discordapp.com/channels/499448243491569673")); //NOSONAR
        SupportView supportView = getView(); //NOSONAR
        if (supportView != null) { //NOSONAR
            supportView.showHelp(intent); //NOSONAR
        }
    }

    public void rateClicked() { //NOSONAR

        settingsManager.setHasRated(); //NOSONAR

        SupportView supportView = getView(); //NOSONAR
        if (supportView != null) { //NOSONAR
            Intent intent = ShuttleUtils.getShuttleStoreIntent(application.getPackageName()); //NOSONAR
            supportView.showRate(intent); //NOSONAR
        }
    }
}
