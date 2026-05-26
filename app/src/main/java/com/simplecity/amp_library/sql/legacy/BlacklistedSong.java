package com.simplecity.amp_library.sql.legacy; // NOSONAR

import android.database.Cursor; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BlacklistedSong { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long songId; //NOSONAR

    public BlacklistedSong(Cursor cursor) { //NOSONAR
        id = cursor.getLong(0); //NOSONAR
        songId = cursor.getLong(1); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return String.valueOf(songId); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        BlacklistedSong that = (BlacklistedSong) o; //NOSONAR

        return id == that.id && songId == that.songId; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (int) (songId ^ (songId >>> 32)); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR
} // NOSONAR
