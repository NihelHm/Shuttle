package com.simplecity.amp_library.glide.utils;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.simplecity.amp_library.glide.loader.ArtworkModelLoader;
import com.simplecity.amp_library.model.ArtworkProvider;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class CustomGlideModule implements GlideModule {

    public CustomGlideModule() {
        // Intentionally left empty.
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Intentionally left empty.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(ArtworkProvider.class, InputStream.class, new ArtworkModelLoader.Factory());
    }
}
