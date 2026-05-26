package com.simplecity.amp_library.model;

import java.util.ArrayList;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class Suggestion {

    private AlbumArtist mostPlayedArtist;
    private Album mostPlayedAlbum;
    @SuppressWarnings("java:S1104")
    public Song mostPlayedSong;
    private ArrayList<Song> favouriteSongsOne= new ArrayList<>(3);
    private ArrayList<Song> favouriteSongsTwo= new ArrayList<>(3);
    @SuppressWarnings("java:S1104")
    public ArrayList<Album> recentlyPlayedAlbums = new ArrayList<>(4);
    private ArrayList<Album> recentlyAddedAlbumsOne= new ArrayList<>(2);
    private ArrayList<Album> recentlyAddedAlbumsTwo= new ArrayList<>(2);

    public Suggestion(AlbumArtist mostPlayedAlbumArtist,
            Album mostPlayedAlbum,
            Song mostPlayedSong,
            ArrayList<Song> favouriteSongsOne,
            ArrayList<Song> favouriteSongsTwo,
            ArrayList<Album> recentlyPlayedAlbums,
            ArrayList<Album> recentlyAddedAlbumsOne,
            ArrayList<Album> recentlyAddedAlbumsTwo) {

        this.mostPlayedArtist = mostPlayedAlbumArtist;
        this.mostPlayedAlbum = mostPlayedAlbum;
        this.mostPlayedSong = mostPlayedSong;
        this.favouriteSongsOne = favouriteSongsOne;
        this.favouriteSongsTwo = favouriteSongsTwo;
        this.recentlyPlayedAlbums = recentlyPlayedAlbums;
        this.recentlyAddedAlbumsOne = recentlyAddedAlbumsOne;
        this.recentlyAddedAlbumsTwo = recentlyAddedAlbumsTwo;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "mostPlayedArtist=" + mostPlayedArtist +
                ", mostPlayedAlbum=" + mostPlayedAlbum +
                ", mostPlayedSong=" + mostPlayedSong +
                ", favouriteSongsOne=" + favouriteSongsOne +
                ", favouriteSongsTwo=" + favouriteSongsTwo +
                ", recentlyPlayedAlbums=" + recentlyPlayedAlbums +
                ", recentlyAddedAlbumsOne=" + recentlyAddedAlbumsOne +
                ", recentlyAddedAlbumsTwo=" + recentlyAddedAlbumsTwo +
                '}';
    }
}

