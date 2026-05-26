package com.simplecity.amp_library.model;

import android.database.Cursor;
import android.provider.MediaStore;
import java.io.Serializable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class Genre implements Serializable {

    @SuppressWarnings("java:S1104")

    public long id;
    @SuppressWarnings("java:S1104")
    public String name;
    @SuppressWarnings("java:S1104")
    public int numSongs;

    public static String[] getProjection() {
        return new String[] {
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME
        };
    }

    public static Query getQuery() {
        return new Query.Builder()
                .uri(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI)
                .projection(Genre.getProjection())
                .selection(null)
                .args(null)
                .sort(MediaStore.Audio.Genres.DEFAULT_SORT_ORDER)
                .build();
    }

    public Genre(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres._ID));
        this.name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));
    }

    public Genre(long genreId, String name) {
        this.id = genreId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        if (id != genre.id) return false;
        if (numSongs != genre.numSongs) return false;
        return name != null ? name.equals(genre.name) : genre.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + numSongs;
        return result;
    }
}
