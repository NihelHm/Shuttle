package com.simplecity.amp_library.http.lastfm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class LastFmAlbum implements LastFmResult {

    @SerializedName("album")
    @SuppressWarnings("java:S1104")
    public Album album;

    public static class Album {
        @SuppressWarnings("java:S1104")
        public String name;
        @SerializedName("image")
        @SuppressWarnings("java:S1104")
        public List<LastFmImage> images = new ArrayList<>();
        @SuppressWarnings("java:S1104")
        public Wiki wiki;
    }

    @Override
    public String getImageUrl() {
        if (album != null) {
            return LastFmUtils.getBestImageUrl(album.images);
        } else {
            return null;
        }
    }

    public static class Wiki {
        @SuppressWarnings("java:S1104")
        public String summary;
    }
}
