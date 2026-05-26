package com.simplecity.amp_library.sql.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class BlacklistWhitelistDbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "BlacklistWhitelistDbOpenHelper";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_TYPE = "type";

    private static final String DATABASE_NAME = "inclexcl.db";
    public static final String TABLE_NAME = "inclexcl";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_WHITELIST = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PATH + " TEXT NOT NULL, "
            + COLUMN_TYPE + " INTEGER DEFAULT 0"
            + ");";

    public BlacklistWhitelistDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_WHITELIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Intentionally left empty.
    }
}
