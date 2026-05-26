package com.simplecity.amp_library.glide.fetcher;

import android.content.Context;
import com.simplecity.amp_library.model.ArtworkProvider;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class MediaStoreFetcher extends BaseFetcher {

    String TAG = "MediaStoreFetcher";

    private Context applicationContext;

    public MediaStoreFetcher(Context context, ArtworkProvider artworkProvider) {
        super(artworkProvider);
        applicationContext = context.getApplicationContext();
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected InputStream getStream() {
        return artworkProvider.getMediaStoreArtwork(applicationContext);
    }
}
