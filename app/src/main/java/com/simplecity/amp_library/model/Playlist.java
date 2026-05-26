package com.simplecity.amp_library.model; // NOSONAR

import android.content.Context; // NOSONAR
import android.database.Cursor; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager; // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.annotations.Nullable; // NOSONAR
import java.io.Serializable; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function1; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Playlist implements Serializable { //NOSONAR

    private static final String TAG = "Playlist"; //NOSONAR

    public @interface Type { //NOSONAR
        int PODCAST = 0; //NOSONAR
        int RECENTLY_ADDED = 1; //NOSONAR
        int MOST_PLAYED = 2; //NOSONAR
        int RECENTLY_PLAYED = 3; //NOSONAR
        int FAVORITES = 4; //NOSONAR
        int USER_CREATED = 5; //NOSONAR
    } // NOSONAR

    @Type //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int type; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String name; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public boolean canEdit = true; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public boolean canClear = false; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public boolean canDelete = true; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public boolean canRename = true; //NOSONAR
    private boolean canSort= true; //NOSONAR

    // These are the Playlist rows that we will retrieve. // NOSONAR
    public static final String[] PROJECTION = new String[] { //NOSONAR
            MediaStore.Audio.Playlists._ID, //NOSONAR
            MediaStore.Audio.Playlists.NAME //NOSONAR
    }; // NOSONAR

    public static Query getQuery() { //NOSONAR
        return new Query.Builder() //NOSONAR
                .uri(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI) //NOSONAR
                .projection(PROJECTION) //NOSONAR
                .selection(null) //NOSONAR
                .sort(null) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    public Playlist(@Type int type, long id, String name, boolean canEdit, boolean canClear, boolean canDelete, boolean canRename, boolean canSort) { //NOSONAR
        this.type = type; //NOSONAR
        this.id = id; //NOSONAR
        this.name = name; //NOSONAR
        this.canEdit = canEdit; //NOSONAR
        this.canClear = canClear; //NOSONAR
        this.canDelete = canDelete; //NOSONAR
        this.canRename = canRename; //NOSONAR
        this.canSort = canSort; //NOSONAR
    } // NOSONAR

    public Playlist(Context context, Cursor cursor) { //NOSONAR
        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)); //NOSONAR
        name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME)); //NOSONAR
        type = Type.USER_CREATED; //NOSONAR
        canClear = true; //NOSONAR

        if (context.getString(R.string.fav_title).equals(name)) { //NOSONAR
            type = Type.FAVORITES; //NOSONAR
            canDelete = false; //NOSONAR
            canRename = false; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void clear(PlaylistManager playlistManager, FavoritesPlaylistManager favoritesPlaylistManager) { //NOSONAR
        switch (type) { //NOSONAR
            case Playlist.Type.FAVORITES: //NOSONAR
                favoritesPlaylistManager.clearFavorites(); //NOSONAR
                break; //NOSONAR
            case Playlist.Type.MOST_PLAYED: //NOSONAR
                playlistManager.clearMostPlayed(); //NOSONAR
                break; //NOSONAR
            case Playlist.Type.USER_CREATED: //NOSONAR
                playlistManager.clearPlaylist(id); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void removeSong(@NonNull Song song, PlaylistManager playlistManager, @Nullable Function1<Boolean, Unit> success) { //NOSONAR
        playlistManager.removeFromPlaylist(this, song, success); //NOSONAR
    } // NOSONAR

    public boolean moveSong(Context context, int from, int to) { //NOSONAR
        return MediaStore.Audio.Playlists.Members.moveItem(context.getContentResolver(), id, from, to); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Playlist playlist = (Playlist) o; //NOSONAR

        if (id != playlist.id) return false; //NOSONAR
        return name != null ? name.equals(playlist.name) : playlist.name == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (name != null ? name.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "Playlist{" + //NOSONAR
                "id=" + id + //NOSONAR
                ", name='" + name + '\'' + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR

    public static Song createSongFromPlaylistCursor(Cursor cursor) { //NOSONAR
        Song song = new Song(cursor); //NOSONAR
        song.id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID)); //NOSONAR
        song.playlistSongId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members._ID)); //NOSONAR
        song.playlistSongPlayOrder = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.PLAY_ORDER)); //NOSONAR
        return song; //NOSONAR
    } // NOSONAR
} // NOSONAR
