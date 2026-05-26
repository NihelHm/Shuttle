package com.simplecity.amp_library.model; // NOSONAR

import android.database.Cursor; // NOSONAR
import com.simplecity.amp_library.sql.databases.BlacklistWhitelistDbOpenHelper; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class InclExclItem { //NOSONAR

    public @interface Type { //NOSONAR
        int INCLUDE = 0; //NOSONAR
        int EXCLUDE = 1; //NOSONAR
    } // NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @NonNull //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String path; //NOSONAR
    @Type //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int type; //NOSONAR

    public InclExclItem(Cursor cursor) { //NOSONAR
        this.id = cursor.getLong(cursor.getColumnIndex(BlacklistWhitelistDbOpenHelper.COLUMN_ID)); //NOSONAR
        this.path = cursor.getString(cursor.getColumnIndex(BlacklistWhitelistDbOpenHelper.COLUMN_PATH)); //NOSONAR
        this.type = cursor.getInt(cursor.getColumnIndex(BlacklistWhitelistDbOpenHelper.COLUMN_TYPE)); //NOSONAR
    } // NOSONAR

    public InclExclItem(String path, @Type int type) { //NOSONAR
        this.path = path; //NOSONAR
        this.type = type; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "InclExclItem{" + //NOSONAR
                "id=" + id + //NOSONAR
                ", path='" + path + '\'' + //NOSONAR
                ", type=" + type + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        InclExclItem that = (InclExclItem) o; //NOSONAR

        if (id != that.id) return false; //NOSONAR
        if (type != that.type) return false; //NOSONAR
        return path.equals(that.path); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + path.hashCode(); //NOSONAR
        result = 31 * result + type; //NOSONAR
        return result; //NOSONAR
    } // NOSONAR
} // NOSONAR
