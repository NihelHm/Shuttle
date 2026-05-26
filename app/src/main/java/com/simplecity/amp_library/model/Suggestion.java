package com.simplecity.amp_library.model; // NOSONAR

import java.util.ArrayList; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Suggestion { //NOSONAR

    private AlbumArtist mostPlayedArtist; //NOSONAR
    private Album mostPlayedAlbum; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public Song mostPlayedSong; //NOSONAR
    private ArrayList<Song> favouriteSongsOne= new ArrayList<>(3); //NOSONAR
    private ArrayList<Song> favouriteSongsTwo= new ArrayList<>(3); //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public ArrayList<Album> recentlyPlayedAlbums = new ArrayList<>(4); //NOSONAR
    private ArrayList<Album> recentlyAddedAlbumsOne= new ArrayList<>(2); //NOSONAR
    private ArrayList<Album> recentlyAddedAlbumsTwo= new ArrayList<>(2); //NOSONAR

    public Suggestion(AlbumArtist mostPlayedAlbumArtist, //NOSONAR
            Album mostPlayedAlbum, //NOSONAR
            Song mostPlayedSong, //NOSONAR
            ArrayList<Song> favouriteSongsOne, //NOSONAR
            ArrayList<Song> favouriteSongsTwo, //NOSONAR
            ArrayList<Album> recentlyPlayedAlbums, //NOSONAR
            ArrayList<Album> recentlyAddedAlbumsOne, //NOSONAR
            ArrayList<Album> recentlyAddedAlbumsTwo) { //NOSONAR

        this.mostPlayedArtist = mostPlayedAlbumArtist; //NOSONAR
        this.mostPlayedAlbum = mostPlayedAlbum; //NOSONAR
        this.mostPlayedSong = mostPlayedSong; //NOSONAR
        this.favouriteSongsOne = favouriteSongsOne; //NOSONAR
        this.favouriteSongsTwo = favouriteSongsTwo; //NOSONAR
        this.recentlyPlayedAlbums = recentlyPlayedAlbums; //NOSONAR
        this.recentlyAddedAlbumsOne = recentlyAddedAlbumsOne; //NOSONAR
        this.recentlyAddedAlbumsTwo = recentlyAddedAlbumsTwo; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "Suggestion{" + //NOSONAR
                "mostPlayedArtist=" + mostPlayedArtist + //NOSONAR
                ", mostPlayedAlbum=" + mostPlayedAlbum + //NOSONAR
                ", mostPlayedSong=" + mostPlayedSong + //NOSONAR
                ", favouriteSongsOne=" + favouriteSongsOne + //NOSONAR
                ", favouriteSongsTwo=" + favouriteSongsTwo + //NOSONAR
                ", recentlyPlayedAlbums=" + recentlyPlayedAlbums + //NOSONAR
                ", recentlyAddedAlbumsOne=" + recentlyAddedAlbumsOne + //NOSONAR
                ", recentlyAddedAlbumsTwo=" + recentlyAddedAlbumsTwo + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR
} // NOSONAR

