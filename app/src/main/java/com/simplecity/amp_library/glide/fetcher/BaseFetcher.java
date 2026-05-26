package com.simplecity.amp_library.glide.fetcher;

import android.support.annotation.CallSuper;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.simplecity.amp_library.model.ArtworkProvider;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
abstract class BaseFetcher implements DataFetcher<InputStream> {

    protected ArtworkProvider artworkProvider;

    protected InputStream stream;

    BaseFetcher(ArtworkProvider artworkProvider) {
        this.artworkProvider = artworkProvider;
    }

    protected abstract String getTag();

    protected abstract InputStream getStream() throws IOException;

    @Override
    public InputStream loadData(Priority priority) throws Exception {

        stream = getStream();
        return stream;
    }

    @Override
    @CallSuper
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
    }

    @Override
    public void cancel() {
        // Intentionally left empty.
    }

    @Override
    public String getId() {
        return artworkProvider.getArtworkKey() + "_" + getTag();
    }
}
