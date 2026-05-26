package com.simplecity.amp_library.sql.legacy;

import android.database.Cursor;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class BlacklistedSong {

    @SuppressWarnings("java:S1104")

    public long id;

    @SuppressWarnings("java:S1104")

    public long songId;

    public BlacklistedSong(Cursor cursor) {
        id = cursor.getLong(0);
        songId = cursor.getLong(1);
    }

    @Override
    public String toString() {
        return String.valueOf(songId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlacklistedSong that = (BlacklistedSong) o;

        return id == that.id && songId == that.songId;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (songId ^ (songId >>> 32));
        return result;
    }
}
