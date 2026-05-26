package com.simplecity.amp_library.glide.fetcher;

import com.simplecity.amp_library.model.ArtworkProvider;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TagFetcher extends BaseFetcher { //NOSONAR

    String TAG = "TagFetcher"; //NOSONAR

    public TagFetcher(ArtworkProvider artworkProvider) { //NOSONAR
        super(artworkProvider); //NOSONAR
    }

    @Override //NOSONAR
    protected String getTag() { //NOSONAR
        return TAG; //NOSONAR
    }

    @Override //NOSONAR
    protected InputStream getStream() { //NOSONAR
        return artworkProvider.getTagArtwork(); //NOSONAR
    }
}
