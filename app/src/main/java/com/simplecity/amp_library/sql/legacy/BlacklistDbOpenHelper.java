package com.simplecity.amp_library.sql.legacy; // NOSONAR

import android.content.Context; // NOSONAR
import android.database.sqlite.SQLiteDatabase; // NOSONAR
import android.database.sqlite.SQLiteOpenHelper; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BlacklistDbOpenHelper extends SQLiteOpenHelper { //NOSONAR

    public static final String TABLE_SONGS = "songs"; //NOSONAR
    public static final String COLUMN_ID = "_id"; //NOSONAR
    public static final String COLUMN_SONG_ID = "song_id"; //NOSONAR

    public static final String DATABASE_NAME = "songblacklist.db"; //NOSONAR
    private static final int DATABASE_VERSION = 1; //NOSONAR

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " //NOSONAR
            + TABLE_SONGS + "(" //NOSONAR
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //NOSONAR
            + COLUMN_SONG_ID + " LONG NOT NULL" + ");"; //NOSONAR

    public BlacklistDbOpenHelper(Context context) { //NOSONAR
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(SQLiteDatabase db) { //NOSONAR
        db.execSQL(DATABASE_CREATE); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //NOSONAR
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS); //NOSONAR
        onCreate(db); //NOSONAR
    } // NOSONAR
} // NOSONAR
