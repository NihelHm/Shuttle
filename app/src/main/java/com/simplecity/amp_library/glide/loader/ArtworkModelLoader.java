package com.simplecity.amp_library.glide.loader; // NOSONAR

import android.content.Context; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import com.bumptech.glide.load.data.DataFetcher; // NOSONAR
import com.bumptech.glide.load.model.GenericLoaderFactory; // NOSONAR
import com.bumptech.glide.load.model.ModelLoader; // NOSONAR
import com.bumptech.glide.load.model.ModelLoaderFactory; // NOSONAR
import com.simplecity.amp_library.glide.fetcher.MultiFetcher; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkModelLoader implements ModelLoader<ArtworkProvider, InputStream> { //NOSONAR

    private boolean allowOfflineDownload; //NOSONAR

    private Context applicationContext; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    public ArtworkModelLoader(Context context, boolean allowOfflineDownload) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
        this.allowOfflineDownload = allowOfflineDownload; //NOSONAR
        this.settingsManager = new SettingsManager(PreferenceManager.getDefaultSharedPreferences(context)); //NOSONAR
    } // NOSONAR

    private static final String TAG = "ArtworkModelLoader"; //NOSONAR

    @Override //NOSONAR
    public DataFetcher<InputStream> getResourceFetcher(ArtworkProvider model, int width, int height) { //NOSONAR
        return new MultiFetcher(applicationContext, model, settingsManager, allowOfflineDownload); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * The default factory for {@link ArtworkModelLoader}s. // NOSONAR
     */ // NOSONAR
    public static class Factory implements ModelLoaderFactory<ArtworkProvider, InputStream> { //NOSONAR

        @Override //NOSONAR
        public ModelLoader<ArtworkProvider, InputStream> build(Context context, GenericLoaderFactory factories) { //NOSONAR
            return new ArtworkModelLoader(context, false); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void teardown() { //NOSONAR
            // Do nothing. // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
