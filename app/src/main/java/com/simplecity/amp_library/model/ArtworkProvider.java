package com.simplecity.amp_library.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface ArtworkProvider { //NOSONAR

    @interface Type { //NOSONAR
        int MEDIA_STORE = 0; //NOSONAR
        int TAG = 1; //NOSONAR
        int FOLDER = 2; //NOSONAR
        int REMOTE = 3; //NOSONAR
    }

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
}
