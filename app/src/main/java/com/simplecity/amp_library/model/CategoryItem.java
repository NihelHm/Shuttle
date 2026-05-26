package com.simplecity.amp_library.model; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.support.annotation.StringRes; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.list.AlbumListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.folders.FolderFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.list.GenreListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.list.SongListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.suggested.SuggestedFragment; // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CategoryItem { //NOSONAR

    public @interface Type { //NOSONAR
        int GENRES = 0; //NOSONAR
        int SUGGESTED = 1; //NOSONAR
        int ARTISTS = 2; //NOSONAR
        int ALBUMS = 3; //NOSONAR
        int SONGS = 4; //NOSONAR
        int PLAYLISTS = 5; //NOSONAR
        int FOLDERS = 6; //NOSONAR
    } // NOSONAR

    @Type //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int type; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public int sortOrder; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public boolean isChecked; //NOSONAR

    private CategoryItem(@Type int type, SharedPreferences sharedPreferences) { //NOSONAR
        this.type = type; //NOSONAR
        isChecked = sharedPreferences.getBoolean(getEnabledKey(), isEnabledByDefault()); //NOSONAR
        sortOrder = sharedPreferences.getInt(getSortKey(), 0); //NOSONAR
    } // NOSONAR

    public static List<CategoryItem> getCategoryItems(SharedPreferences sharedPreferences) { //NOSONAR
        List<CategoryItem> items = new ArrayList<>(); //NOSONAR
        items.add(new CategoryItem(Type.GENRES, sharedPreferences)); //NOSONAR
        items.add(new CategoryItem(Type.SUGGESTED, sharedPreferences)); //NOSONAR
        items.add(new CategoryItem(Type.ARTISTS, sharedPreferences)); //NOSONAR
        items.add(new CategoryItem(Type.ALBUMS, sharedPreferences)); //NOSONAR
        items.add(new CategoryItem(Type.SONGS, sharedPreferences)); //NOSONAR
        items.add(new CategoryItem(Type.FOLDERS, sharedPreferences)); //NOSONAR
        items.add(new CategoryItem(Type.PLAYLISTS, sharedPreferences)); //NOSONAR
        Collections.sort(items, (a, b) -> ComparisonUtils.compareInt(a.sortOrder, b.sortOrder)); //NOSONAR
        return items; //NOSONAR
    } // NOSONAR

    public void savePrefs(SharedPreferences.Editor editor) { //NOSONAR
        editor.putBoolean(getEnabledKey(), isChecked); //NOSONAR
        editor.putInt(getSortKey(), sortOrder); //NOSONAR
        editor.apply(); //NOSONAR
    } // NOSONAR

    @StringRes //NOSONAR
    public int getTitleResId() { //NOSONAR
        switch (type) { //NOSONAR
            case Type.GENRES: //NOSONAR
                return R.string.genres_title; //NOSONAR
            case Type.SUGGESTED: //NOSONAR
                return R.string.suggested_title; //NOSONAR
            case Type.ARTISTS: //NOSONAR
                return R.string.artists_title; //NOSONAR
            case Type.ALBUMS: //NOSONAR
                return R.string.albums_title; //NOSONAR
            case Type.SONGS: //NOSONAR
                return R.string.tracks_title; //NOSONAR
            case Type.FOLDERS: //NOSONAR
                return R.string.folders_title; //NOSONAR
            case Type.PLAYLISTS: //NOSONAR
                return R.string.playlists_title; //NOSONAR
        } // NOSONAR
        return -1; //NOSONAR
    } // NOSONAR

    public String getKey() { //NOSONAR
        switch (type) { //NOSONAR
            case Type.GENRES: //NOSONAR
                return "genres"; //NOSONAR
            case Type.SUGGESTED: //NOSONAR
                return "suggested"; //NOSONAR
            case Type.ARTISTS: //NOSONAR
                return "artists"; //NOSONAR
            case Type.ALBUMS: //NOSONAR
                return "albums"; //NOSONAR
            case Type.SONGS: //NOSONAR
                return "songs"; //NOSONAR
            case Type.FOLDERS: //NOSONAR
                return "folders"; //NOSONAR
            case Type.PLAYLISTS: //NOSONAR
                return "playlists"; //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    public boolean isEnabledByDefault() { //NOSONAR
        switch (type) { //NOSONAR
            case Type.GENRES: //NOSONAR
                return true; //NOSONAR
            case Type.SUGGESTED: //NOSONAR
                return true; //NOSONAR
            case Type.ARTISTS: //NOSONAR
                return true; //NOSONAR
            case Type.ALBUMS: //NOSONAR
                return true; //NOSONAR
            case Type.SONGS: //NOSONAR
                return true; //NOSONAR
            case Type.FOLDERS: //NOSONAR
                return false; //NOSONAR
            case Type.PLAYLISTS: //NOSONAR
                return false; //NOSONAR
        } // NOSONAR
        return true; //NOSONAR
    } // NOSONAR

    public String getSortKey() { //NOSONAR
        return getKey() + "_sort"; //NOSONAR
    } // NOSONAR

    public String getEnabledKey() { //NOSONAR
        return getKey() + "_enabled"; //NOSONAR
    } // NOSONAR

    public Fragment getFragment(Context context) { //NOSONAR
        switch (type) { //NOSONAR
            case Type.GENRES: //NOSONAR
                return GenreListFragment.Companion.newInstance(context.getString(getTitleResId())); //NOSONAR
            case Type.SUGGESTED: //NOSONAR
                return SuggestedFragment.Companion.newInstance(context.getString(getTitleResId())); //NOSONAR
            case Type.ARTISTS: //NOSONAR
                return AlbumArtistListFragment.Companion.newInstance(context.getString(getTitleResId())); //NOSONAR
            case Type.ALBUMS: //NOSONAR
                return AlbumListFragment.Companion.newInstance(context.getString(getTitleResId())); //NOSONAR
            case Type.SONGS: //NOSONAR
                return SongListFragment.Companion.newInstance(context.getString(getTitleResId())); //NOSONAR
            case Type.FOLDERS: //NOSONAR
                return FolderFragment.newInstance(context.getString(getTitleResId()), true); //NOSONAR
            case Type.PLAYLISTS: //NOSONAR
                return PlaylistListFragment.Companion.newInstance(context.getString(getTitleResId())); //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        CategoryItem that = (CategoryItem) o; //NOSONAR

        return type == that.type; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return type; //NOSONAR
    } // NOSONAR
} // NOSONAR
