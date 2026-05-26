package com.simplecity.amp_library.model; // NOSONAR

import android.database.Cursor; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import java.io.Serializable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Genre implements Serializable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String name; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int numSongs; //NOSONAR

    public static String[] getProjection() { //NOSONAR
        return new String[] { //NOSONAR
                MediaStore.Audio.Genres._ID, //NOSONAR
                MediaStore.Audio.Genres.NAME //NOSONAR
        }; // NOSONAR
    } // NOSONAR

    public static Query getQuery() { //NOSONAR
        return new Query.Builder() //NOSONAR
                .uri(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI) //NOSONAR
                .projection(Genre.getProjection()) //NOSONAR
                .selection(null) //NOSONAR
                .args(null) //NOSONAR
                .sort(MediaStore.Audio.Genres.DEFAULT_SORT_ORDER) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    public Genre(Cursor cursor) { //NOSONAR
        this.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres._ID)); //NOSONAR
        this.name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME)); //NOSONAR
    } // NOSONAR

    public Genre(long genreId, String name) { //NOSONAR
        this.id = genreId; //NOSONAR
        this.name = name; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Genre genre = (Genre) o; //NOSONAR

        if (id != genre.id) return false; //NOSONAR
        if (numSongs != genre.numSongs) return false; //NOSONAR
        return name != null ? name.equals(genre.name) : genre.name == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (name != null ? name.hashCode() : 0); //NOSONAR
        result = 31 * result + numSongs; //NOSONAR
        return result; //NOSONAR
    } // NOSONAR
} // NOSONAR
