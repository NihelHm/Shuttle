package com.simplecity.amp_library.sql.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.simplecity.amp_library.BuildConfig;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class CustomArtworkTable extends SQLiteOpenHelper {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".custom_artwork.contentprovider";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + "custom_artwork");

    public static final String TABLE_ARTIST_ART = "custom_artwork";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEY = "_key";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PATH = "_data";

    private static final String DATABASE_NAME = "custom_artwork.db";
    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_ARTIST_ART + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_KEY + " text not null unique on conflict replace, "
            + COLUMN_TYPE + " integer, "
            + COLUMN_PATH + " text);";

    public CustomArtworkTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_ART);
            onCreate(db);
        }
    }
}
