package com.simplecity.amp_library.glide.loader; // NOSONAR

import android.content.Context; // NOSONAR
import com.bumptech.glide.load.data.DataFetcher; // NOSONAR
import com.bumptech.glide.load.model.ModelLoader; // NOSONAR
import com.simplecity.amp_library.glide.fetcher.TypeFetcher; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TypeLoader implements ModelLoader<ArtworkProvider, InputStream> { //NOSONAR

    private static final String TAG = "ArtworkModelLoader"; //NOSONAR

    private Context applicationContext; //NOSONAR

    @ArtworkProvider.Type //NOSONAR
    private int type; //NOSONAR

    private File file; //NOSONAR

    public TypeLoader(Context context, @ArtworkProvider.Type int type, File file) { //NOSONAR
        applicationContext = context.getApplicationContext(); //NOSONAR
        this.type = type; //NOSONAR
        this.file = file; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public DataFetcher<InputStream> getResourceFetcher(ArtworkProvider model, int width, int height) { //NOSONAR
        return new TypeFetcher(applicationContext, model, type, file); //NOSONAR
    } // NOSONAR
} // NOSONAR
