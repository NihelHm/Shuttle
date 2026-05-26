package com.simplecity.amp_library.sql.databases; // NOSONAR

import android.content.Context; // NOSONAR
import android.database.sqlite.SQLiteDatabase; // NOSONAR
import android.database.sqlite.SQLiteOpenHelper; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BlacklistWhitelistDbOpenHelper extends SQLiteOpenHelper { //NOSONAR

    private static final String TAG = "BlacklistWhitelistDbOpenHelper"; //NOSONAR

    public static final String COLUMN_ID = "_id"; //NOSONAR
    public static final String COLUMN_PATH = "path"; //NOSONAR
    public static final String COLUMN_TYPE = "type"; //NOSONAR

    private static final String DATABASE_NAME = "inclexcl.db"; //NOSONAR
    public static final String TABLE_NAME = "inclexcl"; //NOSONAR

    private static final int DATABASE_VERSION = 1; //NOSONAR

    private static final String DATABASE_CREATE_WHITELIST = "CREATE TABLE IF NOT EXISTS " //NOSONAR
            + TABLE_NAME + "(" //NOSONAR
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //NOSONAR
            + COLUMN_PATH + " TEXT NOT NULL, " //NOSONAR
            + COLUMN_TYPE + " INTEGER DEFAULT 0" //NOSONAR
            + ");"; // NOSONAR

    public BlacklistWhitelistDbOpenHelper(Context context) { //NOSONAR
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(SQLiteDatabase database) { //NOSONAR
        database.execSQL(DATABASE_CREATE_WHITELIST); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR
} // NOSONAR
