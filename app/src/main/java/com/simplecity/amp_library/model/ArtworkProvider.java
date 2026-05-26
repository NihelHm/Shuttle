package com.simplecity.amp_library.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface ArtworkProvider {

    @interface Type {
        int MEDIA_STORE = 0;
        int TAG = 1;
        int FOLDER = 2;
        int REMOTE = 3;
    }

    @NonNull
    String getArtworkKey();

    @Nullable
    String getRemoteArtworkUrl();

    @Nullable
    InputStream getMediaStoreArtwork(Context context);

    @Nullable
    InputStream getFolderArtwork();

    @Nullable
    InputStream getTagArtwork();

    @Nullable
    List<File> getFolderArtworkFiles();
}
