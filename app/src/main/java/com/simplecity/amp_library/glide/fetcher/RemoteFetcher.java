package com.simplecity.amp_library.glide.fetcher; // NOSONAR

import com.bumptech.glide.load.data.HttpUrlFetcher; // NOSONAR
import com.bumptech.glide.load.model.GlideUrl; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RemoteFetcher extends HttpUrlFetcher { //NOSONAR

    String TAG = "RemoteFetcher"; //NOSONAR

    public RemoteFetcher(ArtworkProvider artworkProvider) { //NOSONAR
        super(new GlideUrl(artworkProvider.getRemoteArtworkUrl())); //NOSONAR
    } // NOSONAR
} // NOSONAR
