package com.simplecity.amp_library.http.lastfm; // NOSONAR

import com.simplecity.amp_library.BuildConfig; // NOSONAR
import retrofit2.Call; // NOSONAR
import retrofit2.http.GET; // NOSONAR
import retrofit2.http.Query; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface LastFmService { //NOSONAR

    String METHOD_TRACK = "track.getInfo"; //NOSONAR
    String METHOD_ARTIST = "artist.getInfo"; //NOSONAR
    String METHOD_ALBUM = "album.getInfo"; //NOSONAR

    @GET("?api_key=" + BuildConfig.LASTFM_API_KEY + "&format=json&autocorrect=1" + "&method=" + METHOD_TRACK) //NOSONAR
    Call<LastFmTrack> getLastFmTrackResult(@Query("artist") String artist, @Query("track") String track); //NOSONAR

    @GET("?api_key=" + BuildConfig.LASTFM_API_KEY + "&format=json&autocorrect=1" + "&method=" + METHOD_ALBUM) //NOSONAR
    Call<LastFmAlbum> getLastFmAlbumResult(@Query("artist") String artist, @Query("album") String album); //NOSONAR

    @GET("?api_key=" + BuildConfig.LASTFM_API_KEY + "&format=json&autocorrect=1" + "&method=" + METHOD_ARTIST) //NOSONAR
    Call<LastFmArtist> getLastFmArtistResult(@Query("artist") String artist); //NOSONAR
} // NOSONAR
