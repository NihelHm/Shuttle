package com.simplecity.amp_library.sql.databases; // NOSONAR

import android.content.Context; // NOSONAR
import android.database.sqlite.SQLiteDatabase; // NOSONAR
import android.database.sqlite.SQLiteOpenHelper; // NOSONAR
import android.net.Uri; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomArtworkTable extends SQLiteOpenHelper { //NOSONAR

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".custom_artwork.contentprovider"; //NOSONAR
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + "custom_artwork"); //NOSONAR

    public static final String TABLE_ARTIST_ART = "custom_artwork"; //NOSONAR
    public static final String COLUMN_ID = "_id"; //NOSONAR
    public static final String COLUMN_KEY = "_key"; //NOSONAR
    public static final String COLUMN_TYPE = "type"; //NOSONAR
    public static final String COLUMN_PATH = "_data"; //NOSONAR

    private static final String DATABASE_NAME = "custom_artwork.db"; //NOSONAR
    private static final int DATABASE_VERSION = 5; //NOSONAR

    private static final String DATABASE_CREATE = "create table if not exists " //NOSONAR
            + TABLE_ARTIST_ART + "(" //NOSONAR
            + COLUMN_ID + " integer primary key autoincrement, " //NOSONAR
            + COLUMN_KEY + " text not null unique on conflict replace, " //NOSONAR
            + COLUMN_TYPE + " integer, " //NOSONAR
            + COLUMN_PATH + " text);"; //NOSONAR

    public CustomArtworkTable(Context context) { //NOSONAR
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(SQLiteDatabase database) { //NOSONAR
        database.execSQL(DATABASE_CREATE); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //NOSONAR
        if (oldVersion < 5) { //NOSONAR
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_ART); //NOSONAR
            onCreate(db); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
