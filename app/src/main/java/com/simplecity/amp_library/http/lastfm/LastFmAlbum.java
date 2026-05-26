package com.simplecity.amp_library.http.lastfm; // NOSONAR

import com.google.gson.annotations.SerializedName; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LastFmAlbum implements LastFmResult { //NOSONAR

    @SerializedName("album") //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public Album album; //NOSONAR

    public static class Album { //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public String name; //NOSONAR
        @SerializedName("image") //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public List<LastFmImage> images = new ArrayList<>(); //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public Wiki wiki; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getImageUrl() { //NOSONAR
        if (album != null) { //NOSONAR
            return LastFmUtils.getBestImageUrl(album.images); //NOSONAR
        } else { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public static class Wiki { //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public String summary; //NOSONAR
    } // NOSONAR
} // NOSONAR
