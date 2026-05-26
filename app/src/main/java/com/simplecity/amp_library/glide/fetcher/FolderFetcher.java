package com.simplecity.amp_library.glide.fetcher;

import com.simplecity.amp_library.model.ArtworkProvider;
import com.simplecity.amp_library.utils.ArtworkUtils;
import java.io.File;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
class FolderFetcher extends BaseFetcher {

    private static final String TAG = "FolderFetcher";

    private File file;

    FolderFetcher(ArtworkProvider artworkProvider, File file) {
        super(artworkProvider);
        this.file = file;
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected InputStream getStream() {

        if (file == null) {
            return artworkProvider.getFolderArtwork();
        }

        return ArtworkUtils.getFileArtwork(file);
    }
}
