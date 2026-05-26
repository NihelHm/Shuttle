package com.simplecity.amp_library.glide.fetcher; // NOSONAR

import android.content.Context; // NOSONAR
import com.bumptech.glide.Priority; // NOSONAR
import com.bumptech.glide.load.data.DataFetcher; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import com.simplecity.amp_library.model.UserSelectedArtwork; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MultiFetcher implements DataFetcher<InputStream> { //NOSONAR

    private static final String TAG = "MultiFetcher"; //NOSONAR

    private Context applicationContext; //NOSONAR

    private DataFetcher<InputStream> dataFetcher; //NOSONAR

    private ArtworkProvider artworkProvider; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private boolean allowOfflineDownload = false; //NOSONAR

    public MultiFetcher(Context context, ArtworkProvider artworkProvider, SettingsManager settingsManager, boolean allowOfflineDownload) { //NOSONAR
        applicationContext = context; //NOSONAR
        this.artworkProvider = artworkProvider; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
        this.allowOfflineDownload = allowOfflineDownload; //NOSONAR
    } // NOSONAR

    private InputStream loadData(DataFetcher<InputStream> dataFetcher, Priority priority) { //NOSONAR
        InputStream inputStream; //NOSONAR
        try { //NOSONAR
            inputStream = dataFetcher.loadData(priority); //NOSONAR
        } catch (Exception e) { //NOSONAR
            if (dataFetcher != null) { //NOSONAR
                dataFetcher.cleanup(); //NOSONAR
            } // NOSONAR
            inputStream = null; //NOSONAR
        } // NOSONAR
        return inputStream; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream loadData(Priority priority) throws Exception { //NOSONAR

        InputStream inputStream = null; //NOSONAR

        //Custom/user selected artwork. Loads from a specific source. // NOSONAR
        UserSelectedArtwork userSelectedArtwork = ((ShuttleApplication) applicationContext).userSelectedArtwork.get(artworkProvider.getArtworkKey()); //NOSONAR
        if (userSelectedArtwork != null) { //NOSONAR
            switch (userSelectedArtwork.type) { //NOSONAR
                case ArtworkProvider.Type.MEDIA_STORE: //NOSONAR
                    dataFetcher = new MediaStoreFetcher(applicationContext, artworkProvider); //NOSONAR
                    break; //NOSONAR
                case ArtworkProvider.Type.FOLDER: //NOSONAR
                    dataFetcher = new FolderFetcher(artworkProvider, new File(userSelectedArtwork.path)); //NOSONAR
                    break; //NOSONAR
                case ArtworkProvider.Type.TAG: //NOSONAR
                    dataFetcher = new TagFetcher(artworkProvider); //NOSONAR
                    break; //NOSONAR
                case ArtworkProvider.Type.REMOTE: //NOSONAR
                    dataFetcher = new RemoteFetcher(artworkProvider); //NOSONAR
                    break; //NOSONAR
            } // NOSONAR
            inputStream = loadData(dataFetcher, priority); //NOSONAR
        } // NOSONAR

        //No user selected artwork. Check local then remote sources, according to user's preferences. // NOSONAR

        //Check the MediaStore // NOSONAR
        if (inputStream == null && !settingsManager.ignoreMediaStoreArtwork()) { //NOSONAR
            dataFetcher = new MediaStoreFetcher(applicationContext, artworkProvider); //NOSONAR
            inputStream = loadData(dataFetcher, priority); //NOSONAR
        } // NOSONAR

        if (inputStream == null) { //NOSONAR
            if (settingsManager.preferEmbeddedArtwork()) { //NOSONAR
                //Check tags // NOSONAR
                if (!settingsManager.ignoreEmbeddedArtwork()) { //NOSONAR
                    dataFetcher = new TagFetcher(artworkProvider); //NOSONAR
                    inputStream = loadData(dataFetcher, priority); //NOSONAR
                } // NOSONAR
                //Check folders // NOSONAR
                if (inputStream == null && !settingsManager.ignoreFolderArtwork()) { //NOSONAR
                    dataFetcher = new FolderFetcher(artworkProvider, null); //NOSONAR
                    inputStream = loadData(dataFetcher, priority); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                //Check folders // NOSONAR
                if (!settingsManager.ignoreFolderArtwork()) { //NOSONAR
                    dataFetcher = new FolderFetcher(artworkProvider, null); //NOSONAR
                    inputStream = loadData(dataFetcher, priority); //NOSONAR
                } // NOSONAR
                //Check tags // NOSONAR
                if (inputStream == null && !settingsManager.ignoreEmbeddedArtwork()) { //NOSONAR
                    dataFetcher = new TagFetcher(artworkProvider); //NOSONAR
                    inputStream = loadData(dataFetcher, priority); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (inputStream == null) { //NOSONAR
            if (allowOfflineDownload //NOSONAR
                    || (settingsManager.canDownloadArtworkAutomatically() //NOSONAR
                    && ShuttleUtils.isOnline(applicationContext, true))) { //NOSONAR

                //Last FM // NOSONAR
                dataFetcher = new RemoteFetcher(artworkProvider); //NOSONAR
                inputStream = loadData(dataFetcher, priority); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return inputStream; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void cleanup() { //NOSONAR
        if (dataFetcher != null) { //NOSONAR
            dataFetcher.cleanup(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void cancel() { //NOSONAR
        if (dataFetcher != null) { //NOSONAR
            dataFetcher.cancel(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private String getCustomArtworkSuffix(Context context) { //NOSONAR
        if (((ShuttleApplication) context.getApplicationContext()).userSelectedArtwork.containsKey(artworkProvider.getArtworkKey())) { //NOSONAR
            UserSelectedArtwork userSelectedArtwork = ((ShuttleApplication) context.getApplicationContext()).userSelectedArtwork.get(artworkProvider.getArtworkKey()); //NOSONAR
            return "_" + userSelectedArtwork.type + "_" + (userSelectedArtwork.path == null ? "" : userSelectedArtwork.path.hashCode()); //NOSONAR
        } // NOSONAR
        return ""; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return artworkProvider.getArtworkKey() + getCustomArtworkSuffix(applicationContext); //NOSONAR
    } // NOSONAR
} // NOSONAR
