package com.simplecity.amp_library.http.lastfm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
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
