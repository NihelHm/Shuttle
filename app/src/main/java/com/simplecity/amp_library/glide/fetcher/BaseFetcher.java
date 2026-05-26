package com.simplecity.amp_library.glide.fetcher; // NOSONAR

import android.support.annotation.CallSuper; // NOSONAR
import com.bumptech.glide.Priority; // NOSONAR
import com.bumptech.glide.load.data.DataFetcher; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import java.io.IOException; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
abstract class BaseFetcher implements DataFetcher<InputStream> { //NOSONAR

    protected ArtworkProvider artworkProvider; //NOSONAR

    protected InputStream stream; //NOSONAR

    BaseFetcher(ArtworkProvider artworkProvider) { //NOSONAR
        this.artworkProvider = artworkProvider; //NOSONAR
    } // NOSONAR

    protected abstract String getTag(); //NOSONAR

    protected abstract InputStream getStream() throws IOException; //NOSONAR

    @Override //NOSONAR
    public InputStream loadData(Priority priority) throws Exception { //NOSONAR

        stream = getStream(); //NOSONAR
        return stream; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    @CallSuper //NOSONAR
    public void cleanup() { //NOSONAR
        try { //NOSONAR
            if (stream != null) { //NOSONAR
                stream.close(); //NOSONAR
            } // NOSONAR
        } catch (IOException e) { //NOSONAR
            // Ignored // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void cancel() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return artworkProvider.getArtworkKey() + "_" + getTag(); //NOSONAR
    } // NOSONAR
} // NOSONAR
