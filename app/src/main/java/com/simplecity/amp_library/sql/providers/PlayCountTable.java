package com.simplecity.amp_library.sql.providers; // NOSONAR

import android.content.Context; // NOSONAR
import android.database.sqlite.SQLiteDatabase; // NOSONAR
import android.database.sqlite.SQLiteException; // NOSONAR
import android.database.sqlite.SQLiteOpenHelper; // NOSONAR
import android.net.Uri; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlayCountTable extends SQLiteOpenHelper { //NOSONAR

    private Context applicationContext; //NOSONAR

    private static final String DATABASE_NAME = "play_count.db"; //NOSONAR
    private static final int DATABASE_VERSION = 2; //NOSONAR

    public static final String TABLE_PLAY_COUNT = "play_count"; //NOSONAR

    public static final String COLUMN_ID = "_id"; //NOSONAR
    public static final String COLUMN_PLAY_COUNT = "play_count"; //NOSONAR
    public static final String COLUMN_TIME_PLAYED = "time_played"; //NOSONAR

    private static final String DATABASE_CREATE = "create table if not exists " //NOSONAR
            + TABLE_PLAY_COUNT //NOSONAR
            + "(" // NOSONAR
            + COLUMN_ID + " LONG NOT NULL UNIQUE ON CONFLICT REPLACE, " //NOSONAR
            + COLUMN_PLAY_COUNT + " INTEGER DEFAULT 0, " //NOSONAR
            + COLUMN_TIME_PLAYED + " LONG DEFAULT 0" //NOSONAR
            + ");"; // NOSONAR

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".play_count.contentprovider"; //NOSONAR

    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + "play_count"); //NOSONAR

    public PlayCountTable(Context context) { //NOSONAR
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //NOSONAR

        this.applicationContext = context.getApplicationContext(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(SQLiteDatabase database) { //NOSONAR
        database.execSQL(DATABASE_CREATE); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //NOSONAR
        if (oldVersion < 2) { //NOSONAR

            String TABLE_BACKUP = "BACKUP"; //NOSONAR

            //Rename existing table to backup // NOSONAR
            db.execSQL("ALTER TABLE " + TABLE_PLAY_COUNT + " RENAME TO " + TABLE_BACKUP + ";"); //NOSONAR

            //Create new table // NOSONAR
            db.execSQL("CREATE TABLE " + TABLE_PLAY_COUNT + "(" //NOSONAR
                    + COLUMN_ID + " LONG NOT NULL UNIQUE ON CONFLICT REPLACE, " //NOSONAR
                    + COLUMN_PLAY_COUNT + " INTEGER DEFAULT 0, " //NOSONAR
                    + COLUMN_TIME_PLAYED + " LONG DEFAULT 0);"); //NOSONAR

            //Copy backup into new // NOSONAR
            db.execSQL("INSERT OR REPLACE INTO " + TABLE_PLAY_COUNT + "(" + COLUMN_ID + ", " + COLUMN_PLAY_COUNT + ") " //NOSONAR
                    + "SELECT " + COLUMN_ID + "," + COLUMN_PLAY_COUNT + " FROM " + TABLE_BACKUP + "; "); //NOSONAR

            //Drop backup // NOSONAR
            db.execSQL("DROP TABLE " + TABLE_BACKUP + "; "); //NOSONAR

            //We have to end this transaction so we can attach the count info table below // NOSONAR
            db.setTransactionSuccessful(); //NOSONAR
            db.endTransaction(); //NOSONAR

            //Add rows from count info table // NOSONAR
            try { //NOSONAR
                String COUNT_INFO_DATABASE = "count_info.db"; //NOSONAR
                String PATH_COUNT_INFO = applicationContext.getDatabasePath(COUNT_INFO_DATABASE).toString(); //NOSONAR
                String TABLE_COUNT_INFO = "COUNT_INFO"; //NOSONAR
                String COUNT_INFO_COLUMN_ID = "_id"; //NOSONAR
                String COUNT_INFO_COLUMN_TIME_PLAYED = "time_played"; //NOSONAR

                db.execSQL("ATTACH '" + PATH_COUNT_INFO + "' AS " + TABLE_COUNT_INFO + "; "); //NOSONAR

                //Now we have to begin a new transaction // NOSONAR
                db.beginTransaction(); //NOSONAR
                db.execSQL("INSERT OR REPLACE INTO " //NOSONAR
                        + TABLE_PLAY_COUNT //NOSONAR
                        + "(" // NOSONAR
                        + COLUMN_ID //NOSONAR
                        + ", " // NOSONAR
                        + COLUMN_PLAY_COUNT //NOSONAR
                        + ", " // NOSONAR
                        + COLUMN_TIME_PLAYED //NOSONAR
                        + ") " // NOSONAR
                        + "SELECT " //NOSONAR
                        + COUNT_INFO_COLUMN_ID //NOSONAR
                        + "," // NOSONAR
                        + "(SELECT " //NOSONAR
                        + COLUMN_PLAY_COUNT //NOSONAR
                        + " FROM " //NOSONAR
                        + TABLE_PLAY_COUNT //NOSONAR
                        + " WHERE _id = " //NOSONAR
                        + COLUMN_ID //NOSONAR
                        + ")" // NOSONAR
                        + "," // NOSONAR
                        + COUNT_INFO_COLUMN_TIME_PLAYED //NOSONAR
                        + " FROM " //NOSONAR
                        + TABLE_COUNT_INFO //NOSONAR
                        + ";"); // NOSONAR
            } catch (SQLiteException ignored) { //NOSONAR
                // The count info table probably doesn't exist (it wasn't created in the previous version of the app) // NOSONAR
                //  Nothing to do // NOSONAR
            } // NOSONAR
            //SQLiteOpenHelper will automatically setTransactionSuccessful & endTransaction for us. // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
