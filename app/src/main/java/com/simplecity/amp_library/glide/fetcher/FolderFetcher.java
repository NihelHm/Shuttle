package com.simplecity.amp_library.glide.fetcher; // NOSONAR

import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import com.simplecity.amp_library.utils.ArtworkUtils; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class FolderFetcher extends BaseFetcher { //NOSONAR

    private static final String TAG = "FolderFetcher"; //NOSONAR

    private File file; //NOSONAR

    FolderFetcher(ArtworkProvider artworkProvider, File file) { //NOSONAR
        super(artworkProvider); //NOSONAR
        this.file = file; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String getTag() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected InputStream getStream() { //NOSONAR

        if (file == null) { //NOSONAR
            return artworkProvider.getFolderArtwork(); //NOSONAR
        } // NOSONAR

        return ArtworkUtils.getFileArtwork(file); //NOSONAR
    } // NOSONAR
} // NOSONAR
