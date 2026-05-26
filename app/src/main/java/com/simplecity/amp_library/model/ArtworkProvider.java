package com.simplecity.amp_library.model; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface ArtworkProvider { //NOSONAR

    @interface Type { //NOSONAR
        int MEDIA_STORE = 0; //NOSONAR
        int TAG = 1; //NOSONAR
        int FOLDER = 2; //NOSONAR
        int REMOTE = 3; //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    String getArtworkKey(); //NOSONAR

    @Nullable //NOSONAR
    String getRemoteArtworkUrl(); //NOSONAR

    @Nullable //NOSONAR
    InputStream getMediaStoreArtwork(Context context); //NOSONAR

    @Nullable //NOSONAR
    InputStream getFolderArtwork(); //NOSONAR

    @Nullable //NOSONAR
    InputStream getTagArtwork(); //NOSONAR

    @Nullable //NOSONAR
    List<File> getFolderArtworkFiles(); //NOSONAR
} // NOSONAR
