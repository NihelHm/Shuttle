package com.simplecity.amp_library.http.lastfm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class LastFmArtist implements LastFmResult {

    @SerializedName("artist")
    @SuppressWarnings("java:S1104")
    public Artist artist;

    public static class Artist {
        @SuppressWarnings("java:S1104")
        public String name;
        @SerializedName("image")
        @SuppressWarnings("java:S1104")
        public List<LastFmImage> images = new ArrayList<>();
        @SuppressWarnings("java:S1104")
        public Bio bio;
    }

    @Override
    public String getImageUrl() {
        if (artist == null || artist.images == null || artist.images.isEmpty()) {
            return null;
        }
        return LastFmUtils.getBestImageUrl(artist.images);
    }

    public static class Bio {
        @SuppressWarnings("java:S1104")
        public String summary;
    }
}
