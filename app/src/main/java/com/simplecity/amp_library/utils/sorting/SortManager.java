package com.simplecity.amp_library.utils.sorting; // NOSONAR

import android.content.SharedPreferences; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR
import javax.inject.Singleton; // NOSONAR

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SortManager { //NOSONAR

    private SharedPreferences mPrefs; //NOSONAR

    @Inject //NOSONAR
    public SortManager(ShuttleApplication application) { //NOSONAR
        mPrefs = PreferenceManager.getDefaultSharedPreferences(application); //NOSONAR
    } // NOSONAR

    private void setSortOrder(String key, int sortOrder) { //NOSONAR
        mPrefs.edit().putInt(key, sortOrder).apply(); //NOSONAR
    } // NOSONAR

    private void setAscending(String key, boolean ascending) { //NOSONAR
        mPrefs.edit().putBoolean(key, ascending).apply(); //NOSONAR
    } // NOSONAR

    public void setArtistsSortOrder(@ArtistSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ARTISTS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getArtistsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ARTISTS, ArtistSort.DEFAULT); //NOSONAR
    } // NOSONAR

    public void setArtistsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ARTISTS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getArtistsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ARTISTS_ASC, true); //NOSONAR
    } // NOSONAR

    public void setAlbumsSortOrder(@AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ALBUMS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getAlbumsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ALBUMS, AlbumSort.DEFAULT); //NOSONAR
    } // NOSONAR

    public void setAlbumsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ALBUMS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getAlbumsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ALBUMS_ASC, true); //NOSONAR
    } // NOSONAR

    public void setSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.SONGS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.SONGS, SongSort.DEFAULT); //NOSONAR
    } // NOSONAR

    public void setSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.SONGS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.SONGS_ASC, true); //NOSONAR
    } // NOSONAR

    //Detail Fragment: // NOSONAR

    //  Songs sort order // NOSONAR

    public void setArtistDetailSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ARTIST_DETAIL_SONGS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getArtistDetailSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ARTIST_DETAIL_SONGS, SongSort.DETAIL_DEFAULT); //NOSONAR
    } // NOSONAR

    public void setAlbumDetailSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ALBUM_DETAIL_SONGS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getAlbumDetailSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ALBUM_DETAIL_SONGS, SongSort.DETAIL_DEFAULT); //NOSONAR
    } // NOSONAR

    public void setPlaylistDetailSongsSortOrder(Playlist playlist, @SongSort int sortOrder) { //NOSONAR
        setSortOrder(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS, playlist.id), sortOrder); //NOSONAR
    } // NOSONAR

    public int getPlaylistDetailSongsSortOrder(Playlist playlist) { //NOSONAR
        return mPrefs.getInt(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS, playlist.id), SongSort.DETAIL_DEFAULT); //NOSONAR
    } // NOSONAR

    public void setGenreDetailSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.GENRE_DETAIL_SONGS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getGenreDetailSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.GENRE_DETAIL_SONGS, SongSort.DETAIL_DEFAULT); //NOSONAR
    } // NOSONAR

    // Albums sort order // NOSONAR

    public void setArtistDetailAlbumsSortOrder(@AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ARTIST_DETAIL_ALBUMS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getArtistDetailAlbumsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ARTIST_DETAIL_ALBUMS, AlbumSort.DEFAULT); //NOSONAR
    } // NOSONAR

    public void setPlaylistDetailAlbumsSortOrder(Playlist playlist, @AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS, playlist.id), sortOrder); //NOSONAR
    } // NOSONAR

    public int getPlaylistDetailAlbumsSortOrder(Playlist playlist) { //NOSONAR
        return mPrefs.getInt(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS, playlist.id), AlbumSort.DEFAULT); //NOSONAR
    } // NOSONAR

    public void setGenreDetailAlbumsSortOrder(@AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(Key.GENRE_DETAIL_ALBUMS, sortOrder); //NOSONAR
    } // NOSONAR

    public int getGenreDetailAlbumsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.GENRE_DETAIL_ALBUMS, AlbumSort.DEFAULT); //NOSONAR
    } // NOSONAR

    // Asc/Desc songs sort order // NOSONAR

    public void setArtistDetailSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ARTIST_DETAIL_SONGS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getArtistDetailSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ARTIST_DETAIL_SONGS_ASC, true); //NOSONAR
    } // NOSONAR

    public void setAlbumDetailSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ALBUM_DETAIL_SONGS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getAlbumDetailSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ALBUM_DETAIL_SONGS_ASC, true); //NOSONAR
    } // NOSONAR

    public void setPlaylistDetailSongsAscending(Playlist playlist, boolean ascending) { //NOSONAR
        setAscending(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS_ASC, playlist.id), ascending); //NOSONAR
    } // NOSONAR

    public boolean getPlaylistDetailSongsAscending(Playlist playlist) { //NOSONAR
        return mPrefs.getBoolean(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS_ASC, playlist.id), true); //NOSONAR
    } // NOSONAR

    public void setGenreDetailSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.GENRE_DETAIL_SONGS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getGenreDetailSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.GENRE_DETAIL_SONGS_ASC, true); //NOSONAR
    } // NOSONAR

    // Asc/Desc albums sort order // NOSONAR

    public void setArtistDetailAlbumsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ARTIST_DETAIL_ALBUMS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getArtistDetailAlbumsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ARTIST_DETAIL_ALBUMS_ASC, true); //NOSONAR
    } // NOSONAR

    public void setPlaylistDetailAlbumsAscending(Playlist playlist, boolean ascending) { //NOSONAR
        setAscending(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS_ASC, playlist.id), ascending); //NOSONAR
    } // NOSONAR

    public boolean getPlaylistDetailAlbumsAscending(Playlist playlist) { //NOSONAR
        return mPrefs.getBoolean(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS_ASC, playlist.id), true); //NOSONAR
    } // NOSONAR

    public void setGenreDetailAlbumsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.GENRE_DETAIL_ALBUMS_ASC, ascending); //NOSONAR
    } // NOSONAR

    public boolean getGenreDetailAlbumsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.GENRE_DETAIL_ALBUMS_ASC, true); //NOSONAR
    } // NOSONAR

    static int PREF_VERSION = 0; //NOSONAR

    public interface Key { //NOSONAR
        String ARTISTS = "key_artists_sort_order_" + PREF_VERSION; //NOSONAR
        String ALBUMS = "key_albums_sort_order_" + PREF_VERSION; //NOSONAR
        String SONGS = "key_songs_sort_order_" + PREF_VERSION; //NOSONAR

        String ARTIST_DETAIL_ALBUMS = "key_detail_albums_sort_order_" + PREF_VERSION; //NOSONAR
        String PLAYLIST_DETAIL_ALBUMS = "key_detail_playlist_albums_sort_order_" + PREF_VERSION; //NOSONAR
        String GENRE_DETAIL_ALBUMS = "key_genre_albums_sort_order_" + PREF_VERSION; //NOSONAR

        String ARTIST_DETAIL_SONGS = "key_detail_songs_sort_order_" + PREF_VERSION; //NOSONAR
        String ALBUM_DETAIL_SONGS = "key_detail_album_songs_sort_order_" + PREF_VERSION; //NOSONAR
        String PLAYLIST_DETAIL_SONGS = "key_detail_playlist_songs_sort_order_" + PREF_VERSION; //NOSONAR
        String GENRE_DETAIL_SONGS = "key_genre_songs_sort_order_" + PREF_VERSION; //NOSONAR

        String ARTISTS_ASC = "key_artists_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String ALBUMS_ASC = "key_albums_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String SONGS_ASC = "key_songs_sort_order_asc_" + PREF_VERSION; //NOSONAR

        String ARTIST_DETAIL_SONGS_ASC = "key_artist_detail_songs_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String ALBUM_DETAIL_SONGS_ASC = "key_album_detail_songs_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String PLAYLIST_DETAIL_SONGS_ASC = "key_playlist_songs_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String GENRE_DETAIL_SONGS_ASC = "key_genre_songs_sort_order_asc_" + PREF_VERSION; //NOSONAR

        String ARTIST_DETAIL_ALBUMS_ASC = "key_detail_albums_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String PLAYLIST_DETAIL_ALBUMS_ASC = "key_detail_playlist_albums_sort_order_asc_" + PREF_VERSION; //NOSONAR
        String GENRE_DETAIL_ALBUMS_ASC = "key_detail_genre_albums_sort_order_asc_" + PREF_VERSION; //NOSONAR
    } // NOSONAR

    public @interface ArtistSort { //NOSONAR
        int DEFAULT = 0; //NOSONAR
        int NAME = 1; //NOSONAR
    } // NOSONAR

    public @interface AlbumSort { //NOSONAR
        int DEFAULT = 0; //NOSONAR
        int NAME = 1; //NOSONAR
        int YEAR = 2; //NOSONAR
        int ARTIST_NAME = 3; //NOSONAR
    } // NOSONAR

    public @interface SongSort { //NOSONAR
        int DEFAULT = 0; //NOSONAR
        int NAME = 1; //NOSONAR
        int TRACK_NUMBER = 2; //NOSONAR
        int DURATION = 3; //NOSONAR
        int DATE = 4; //NOSONAR
        int YEAR = 5; //NOSONAR
        int ALBUM_NAME = 6; //NOSONAR
        int ARTIST_NAME = 7; //NOSONAR
        int DETAIL_DEFAULT = 8; //NOSONAR
    } // NOSONAR

    public interface SortFiles { //NOSONAR
        String DEFAULT = "default"; //NOSONAR
        String FILE_NAME = "file_name"; //NOSONAR
        String SIZE = "size"; //NOSONAR
        String ARTIST_NAME = "artist_name"; //NOSONAR
        String ALBUM_NAME = "album_name"; //NOSONAR
        String TRACK_NAME = "track_name"; //NOSONAR
    } // NOSONAR

    public interface SortFolders { //NOSONAR
        String DEFAULT = "default"; //NOSONAR
        String COUNT = "count"; //NOSONAR
    } // NOSONAR

    public void sortAlbums(List<Album> albums) { //NOSONAR
        sortAlbums(albums, getAlbumsSortOrder()); //NOSONAR
    } // NOSONAR

    public void sortAlbums(List<Album> albums, int key) { //NOSONAR
        switch (key) { //NOSONAR
            case AlbumSort.DEFAULT: //NOSONAR
                Collections.sort(albums, Album::compareTo); //NOSONAR
                break; //NOSONAR
            case AlbumSort.NAME: //NOSONAR
                Collections.sort(albums, (a, b) -> ComparisonUtils.compare(a.name, b.name)); //NOSONAR
                Collections.sort(albums, (a, b) -> ComparisonUtils.compare(a.name, b.name)); //NOSONAR
                break; //NOSONAR
            case AlbumSort.YEAR: //NOSONAR
                Collections.sort(albums, (a, b) -> ComparisonUtils.compareInt(b.year, a.year)); //NOSONAR
                break; //NOSONAR
            case AlbumSort.ARTIST_NAME: //NOSONAR
                Collections.sort(albums, (a, b) -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName)); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void sortSongs(List<Song> songs) { //NOSONAR
        sortSongs(songs, getSongsSortOrder()); //NOSONAR
    } // NOSONAR

    public void sortSongs(List<Song> songs, @SongSort int key) { //NOSONAR
        switch (key) { //NOSONAR
            case SongSort.DEFAULT: //NOSONAR
                Collections.sort(songs, Song::compareTo); //NOSONAR
                break; //NOSONAR
            case SongSort.NAME: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.name, b.name)); //NOSONAR
                break; //NOSONAR
            case SongSort.TRACK_NUMBER: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                break; //NOSONAR
            case SongSort.DURATION: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareLong(a.duration, b.duration)); //NOSONAR
                break; //NOSONAR
            case SongSort.DATE: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(b.dateAdded, a.dateAdded)); //NOSONAR
                break; //NOSONAR
            case SongSort.YEAR: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumName, b.albumName)); //NOSONAR
                Collections.sort(songs, Song::compareTo); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(b.year, a.year)); //NOSONAR
                break; //NOSONAR
            case SongSort.ALBUM_NAME: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumName, b.albumName)); //NOSONAR
                break; //NOSONAR
            case SongSort.ARTIST_NAME: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumName, b.albumName)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName)); //NOSONAR
                break; //NOSONAR
            case SongSort.DETAIL_DEFAULT: //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(b.year, a.year)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                Collections.sort(songs, (a, b) -> ComparisonUtils.compare(a.albumName, b.albumName)); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void sortAlbumArtists(List<AlbumArtist> albumArtists) { //NOSONAR
        int sortOrder = mPrefs.getInt(Key.ARTISTS, ArtistSort.DEFAULT); //NOSONAR
        switch (sortOrder) { //NOSONAR
            case ArtistSort.DEFAULT: //NOSONAR
                Collections.sort(albumArtists, AlbumArtist::compareTo); //NOSONAR
                break; //NOSONAR
            case ArtistSort.NAME: //NOSONAR
                Collections.sort(albumArtists, (a, b) -> ComparisonUtils.compare(a.name, b.name)); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
