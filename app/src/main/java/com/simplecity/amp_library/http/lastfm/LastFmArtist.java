package com.simplecity.amp_library.http.lastfm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LastFmArtist implements LastFmResult { //NOSONAR

    @SerializedName("artist") //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public Artist artist; //NOSONAR

    public static class Artist { //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public String name; //NOSONAR
        @SerializedName("image") //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public List<LastFmImage> images = new ArrayList<>(); //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public Bio bio; //NOSONAR
    }

    @Override //NOSONAR
    public String getImageUrl() { //NOSONAR
        if (artist == null || artist.images == null || artist.images.isEmpty()) { //NOSONAR
            return null; //NOSONAR
        }
        return LastFmUtils.getBestImageUrl(artist.images); //NOSONAR
    }

    public static class Bio { //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public String summary; //NOSONAR
    }
}
