package com.simplecity.amp_library.utils.sorting;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.model.Playlist;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.utils.ComparisonUtils;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SortManager { //NOSONAR

    private SharedPreferences mPrefs; //NOSONAR

    @Inject //NOSONAR
    public SortManager(ShuttleApplication application) { //NOSONAR
        mPrefs = PreferenceManager.getDefaultSharedPreferences(application); //NOSONAR
    }

    private void setSortOrder(String key, int sortOrder) { //NOSONAR
        mPrefs.edit().putInt(key, sortOrder).apply(); //NOSONAR
    }

    private void setAscending(String key, boolean ascending) { //NOSONAR
        mPrefs.edit().putBoolean(key, ascending).apply(); //NOSONAR
    }

    public void setArtistsSortOrder(@ArtistSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ARTISTS, sortOrder); //NOSONAR
    }

    public int getArtistsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ARTISTS, ArtistSort.DEFAULT); //NOSONAR
    }

    public void setArtistsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ARTISTS_ASC, ascending); //NOSONAR
    }

    public boolean getArtistsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ARTISTS_ASC, true); //NOSONAR
    }

    public void setAlbumsSortOrder(@AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ALBUMS, sortOrder); //NOSONAR
    }

    public int getAlbumsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ALBUMS, AlbumSort.DEFAULT); //NOSONAR
    }

    public void setAlbumsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ALBUMS_ASC, ascending); //NOSONAR
    }

    public boolean getAlbumsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ALBUMS_ASC, true); //NOSONAR
    }

    public void setSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.SONGS, sortOrder); //NOSONAR
    }

    public int getSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.SONGS, SongSort.DEFAULT); //NOSONAR
    }

    public void setSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.SONGS_ASC, ascending); //NOSONAR
    }

    public boolean getSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.SONGS_ASC, true); //NOSONAR
    }

    //Detail Fragment:

    //  Songs sort order

    public void setArtistDetailSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ARTIST_DETAIL_SONGS, sortOrder); //NOSONAR
    }

    public int getArtistDetailSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ARTIST_DETAIL_SONGS, SongSort.DETAIL_DEFAULT); //NOSONAR
    }

    public void setAlbumDetailSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ALBUM_DETAIL_SONGS, sortOrder); //NOSONAR
    }

    public int getAlbumDetailSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ALBUM_DETAIL_SONGS, SongSort.DETAIL_DEFAULT); //NOSONAR
    }

    public void setPlaylistDetailSongsSortOrder(Playlist playlist, @SongSort int sortOrder) { //NOSONAR
        setSortOrder(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS, playlist.id), sortOrder); //NOSONAR
    }

    public int getPlaylistDetailSongsSortOrder(Playlist playlist) { //NOSONAR
        return mPrefs.getInt(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS, playlist.id), SongSort.DETAIL_DEFAULT); //NOSONAR
    }

    public void setGenreDetailSongsSortOrder(@SongSort int sortOrder) { //NOSONAR
        setSortOrder(Key.GENRE_DETAIL_SONGS, sortOrder); //NOSONAR
    }

    public int getGenreDetailSongsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.GENRE_DETAIL_SONGS, SongSort.DETAIL_DEFAULT); //NOSONAR
    }

    // Albums sort order

    public void setArtistDetailAlbumsSortOrder(@AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(Key.ARTIST_DETAIL_ALBUMS, sortOrder); //NOSONAR
    }

    public int getArtistDetailAlbumsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.ARTIST_DETAIL_ALBUMS, AlbumSort.DEFAULT); //NOSONAR
    }

    public void setPlaylistDetailAlbumsSortOrder(Playlist playlist, @AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS, playlist.id), sortOrder); //NOSONAR
    }

    public int getPlaylistDetailAlbumsSortOrder(Playlist playlist) { //NOSONAR
        return mPrefs.getInt(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS, playlist.id), AlbumSort.DEFAULT); //NOSONAR
    }

    public void setGenreDetailAlbumsSortOrder(@AlbumSort int sortOrder) { //NOSONAR
        setSortOrder(Key.GENRE_DETAIL_ALBUMS, sortOrder); //NOSONAR
    }

    public int getGenreDetailAlbumsSortOrder() { //NOSONAR
        return mPrefs.getInt(Key.GENRE_DETAIL_ALBUMS, AlbumSort.DEFAULT); //NOSONAR
    }

    // Asc/Desc songs sort order

    public void setArtistDetailSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ARTIST_DETAIL_SONGS_ASC, ascending); //NOSONAR
    }

    public boolean getArtistDetailSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ARTIST_DETAIL_SONGS_ASC, true); //NOSONAR
    }

    public void setAlbumDetailSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ALBUM_DETAIL_SONGS_ASC, ascending); //NOSONAR
    }

    public boolean getAlbumDetailSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ALBUM_DETAIL_SONGS_ASC, true); //NOSONAR
    }

    public void setPlaylistDetailSongsAscending(Playlist playlist, boolean ascending) { //NOSONAR
        setAscending(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS_ASC, playlist.id), ascending); //NOSONAR
    }

    public boolean getPlaylistDetailSongsAscending(Playlist playlist) { //NOSONAR
        return mPrefs.getBoolean(String.format("%s_%s", Key.PLAYLIST_DETAIL_SONGS_ASC, playlist.id), true); //NOSONAR
    }

    public void setGenreDetailSongsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.GENRE_DETAIL_SONGS_ASC, ascending); //NOSONAR
    }

    public boolean getGenreDetailSongsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.GENRE_DETAIL_SONGS_ASC, true); //NOSONAR
    }

    // Asc/Desc albums sort order

    public void setArtistDetailAlbumsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.ARTIST_DETAIL_ALBUMS_ASC, ascending); //NOSONAR
    }

    public boolean getArtistDetailAlbumsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.ARTIST_DETAIL_ALBUMS_ASC, true); //NOSONAR
    }

    public void setPlaylistDetailAlbumsAscending(Playlist playlist, boolean ascending) { //NOSONAR
        setAscending(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS_ASC, playlist.id), ascending); //NOSONAR
    }

    public boolean getPlaylistDetailAlbumsAscending(Playlist playlist) { //NOSONAR
        return mPrefs.getBoolean(String.format("%s_%s", Key.PLAYLIST_DETAIL_ALBUMS_ASC, playlist.id), true); //NOSONAR
    }

    public void setGenreDetailAlbumsAscending(boolean ascending) { //NOSONAR
        setAscending(Key.GENRE_DETAIL_ALBUMS_ASC, ascending); //NOSONAR
    }

    public boolean getGenreDetailAlbumsAscending() { //NOSONAR
        return mPrefs.getBoolean(Key.GENRE_DETAIL_ALBUMS_ASC, true); //NOSONAR
    }

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
    }

    public @interface ArtistSort { //NOSONAR
        int DEFAULT = 0; //NOSONAR
        int NAME = 1; //NOSONAR
    }

    public @interface AlbumSort { //NOSONAR
        int DEFAULT = 0; //NOSONAR
        int NAME = 1; //NOSONAR
        int YEAR = 2; //NOSONAR
        int ARTIST_NAME = 3; //NOSONAR
    }

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
    }

    public interface SortFiles { //NOSONAR
        String DEFAULT = "default"; //NOSONAR
        String FILE_NAME = "file_name"; //NOSONAR
        String SIZE = "size"; //NOSONAR
        String ARTIST_NAME = "artist_name"; //NOSONAR
        String ALBUM_NAME = "album_name"; //NOSONAR
        String TRACK_NAME = "track_name"; //NOSONAR
    }

    public interface SortFolders { //NOSONAR
        String DEFAULT = "default"; //NOSONAR
        String COUNT = "count"; //NOSONAR
    }

    public void sortAlbums(List<Album> albums) { //NOSONAR
        sortAlbums(albums, getAlbumsSortOrder()); //NOSONAR
    }

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
        }
    }

    public void sortSongs(List<Song> songs) { //NOSONAR
        sortSongs(songs, getSongsSortOrder()); //NOSONAR
    }

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
        }
    }

    public void sortAlbumArtists(List<AlbumArtist> albumArtists) { //NOSONAR
        int sortOrder = mPrefs.getInt(Key.ARTISTS, ArtistSort.DEFAULT); //NOSONAR
        switch (sortOrder) { //NOSONAR
            case ArtistSort.DEFAULT: //NOSONAR
                Collections.sort(albumArtists, AlbumArtist::compareTo); //NOSONAR
                break; //NOSONAR
            case ArtistSort.NAME: //NOSONAR
                Collections.sort(albumArtists, (a, b) -> ComparisonUtils.compare(a.name, b.name)); //NOSONAR
                break; //NOSONAR
        }
    }
}
