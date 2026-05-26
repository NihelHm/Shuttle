package com.simplecity.amp_library.glide.loader;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.simplecity.amp_library.glide.fetcher.TypeFetcher;
import com.simplecity.amp_library.model.ArtworkProvider;
import java.io.File;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class TypeLoader implements ModelLoader<ArtworkProvider, InputStream> {

    private static final String TAG = "ArtworkModelLoader";

    private Context applicationContext;

    @ArtworkProvider.Type
    private int type;

    private File file;

    public TypeLoader(Context context, @ArtworkProvider.Type int type, File file) {
        applicationContext = context.getApplicationContext();
        this.type = type;
        this.file = file;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(ArtworkProvider model, int width, int height) {
        return new TypeFetcher(applicationContext, model, type, file);
    }
}
