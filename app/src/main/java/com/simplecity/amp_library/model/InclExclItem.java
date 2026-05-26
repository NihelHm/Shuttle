package com.simplecity.amp_library.model;

import android.database.Cursor;
import com.simplecity.amp_library.sql.databases.BlacklistWhitelistDbOpenHelper;
import io.reactivex.annotations.NonNull;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class InclExclItem {

    public @interface Type {
        int INCLUDE = 0;
        int EXCLUDE = 1;
    }

    @SuppressWarnings("java:S1104")

    public long id;
    @NonNull
    @SuppressWarnings("java:S1104")
    public String path;
    @Type
    @SuppressWarnings("java:S1104")
    public int type;

    public InclExclItem(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(BlacklistWhitelistDbOpenHelper.COLUMN_ID));
        this.path = cursor.getString(cursor.getColumnIndex(BlacklistWhitelistDbOpenHelper.COLUMN_PATH));
        this.type = cursor.getInt(cursor.getColumnIndex(BlacklistWhitelistDbOpenHelper.COLUMN_TYPE));
    }

    public InclExclItem(String path, @Type int type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public String toString() {
        return "InclExclItem{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InclExclItem that = (InclExclItem) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + path.hashCode();
        result = 31 * result + type;
        return result;
    }
}
