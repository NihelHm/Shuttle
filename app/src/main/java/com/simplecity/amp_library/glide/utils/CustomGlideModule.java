package com.simplecity.amp_library.glide.utils; // NOSONAR

import android.content.Context; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.GlideBuilder; // NOSONAR
import com.bumptech.glide.module.GlideModule; // NOSONAR
import com.simplecity.amp_library.glide.loader.ArtworkModelLoader; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomGlideModule implements GlideModule { //NOSONAR

    public CustomGlideModule() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void applyOptions(Context context, GlideBuilder builder) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void registerComponents(Context context, Glide glide) { //NOSONAR
        glide.register(ArtworkProvider.class, InputStream.class, new ArtworkModelLoader.Factory()); //NOSONAR
    } // NOSONAR
} // NOSONAR
