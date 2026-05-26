package com.simplecity.amp_library.http.lastfm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
class LastFmTrack implements LastFmResult {

    @SerializedName("track")
    @SuppressWarnings("java:S1104")
    public Track track;

    public static class Track {
        @SerializedName("album")
        @SuppressWarnings("java:S1104")
        public TrackAlbum album;

        public static class TrackAlbum {

            @SerializedName("album")
            @SuppressWarnings("java:S1104")
            public TrackAlbum album;

            @SerializedName("image")
            @SuppressWarnings("java:S1104")
            public List<LastFmImage> images = new ArrayList<>();
        }
    }

    @Override
    public String getImageUrl() {
        return LastFmUtils.getBestImageUrl(track.album.images);
    }
}
