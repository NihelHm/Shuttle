package com.simplecity.amp_library.sql.legacy; // NOSONAR

import android.database.Cursor; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class WhitelistFolder { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String folder; //NOSONAR

    public WhitelistFolder(Cursor cursor) { //NOSONAR
        id = cursor.getLong(0); //NOSONAR
        folder = cursor.getString(1); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        WhitelistFolder that = (WhitelistFolder) o; //NOSONAR

        if (id != that.id) return false; //NOSONAR
        return folder != null ? folder.equals(that.folder) : that.folder == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (folder != null ? folder.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return folder; //NOSONAR
    } // NOSONAR
} // NOSONAR
