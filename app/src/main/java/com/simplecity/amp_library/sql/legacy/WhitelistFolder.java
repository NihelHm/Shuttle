package com.simplecity.amp_library.sql.legacy;

import android.database.Cursor;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class WhitelistFolder {

    @SuppressWarnings("java:S1104")

    public long id;
    @SuppressWarnings("java:S1104")
    public String folder;

    public WhitelistFolder(Cursor cursor) {
        id = cursor.getLong(0);
        folder = cursor.getString(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WhitelistFolder that = (WhitelistFolder) o;

        if (id != that.id) return false;
        return folder != null ? folder.equals(that.folder) : that.folder == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (folder != null ? folder.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return folder;
    }
}
