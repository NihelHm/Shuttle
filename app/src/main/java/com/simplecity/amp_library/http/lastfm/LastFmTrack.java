package com.simplecity.amp_library.http.lastfm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
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
