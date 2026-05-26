package com.simplecity.amp_library.http.lastfm; // NOSONAR

import com.google.gson.annotations.SerializedName; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class LastFmTrack implements LastFmResult { //NOSONAR

    @SerializedName("track") //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public Track track; //NOSONAR

    public static class Track { //NOSONAR
        @SerializedName("album") //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TrackAlbum album; //NOSONAR

        public static class TrackAlbum { //NOSONAR

            @SerializedName("album") //NOSONAR
            @SuppressWarnings("java:S1104") //NOSONAR
            public TrackAlbum album; //NOSONAR

            @SerializedName("image") //NOSONAR
            @SuppressWarnings("java:S1104") //NOSONAR
            public List<LastFmImage> images = new ArrayList<>(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getImageUrl() { //NOSONAR
        return LastFmUtils.getBestImageUrl(track.album.images); //NOSONAR
    } // NOSONAR
} // NOSONAR
