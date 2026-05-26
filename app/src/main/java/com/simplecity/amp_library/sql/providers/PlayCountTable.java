package com.simplecity.amp_library.sql.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.simplecity.amp_library.BuildConfig;

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
            + "("
            + COLUMN_ID + " LONG NOT NULL UNIQUE ON CONFLICT REPLACE, " //NOSONAR
            + COLUMN_PLAY_COUNT + " INTEGER DEFAULT 0, " //NOSONAR
            + COLUMN_TIME_PLAYED + " LONG DEFAULT 0" //NOSONAR
            + ");";

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".play_count.contentprovider"; //NOSONAR

    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + "play_count"); //NOSONAR

    public PlayCountTable(Context context) { //NOSONAR
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //NOSONAR

        this.applicationContext = context.getApplicationContext(); //NOSONAR
    }

    @Override //NOSONAR
    public void onCreate(SQLiteDatabase database) { //NOSONAR
        database.execSQL(DATABASE_CREATE); //NOSONAR
    }

    @Override //NOSONAR
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //NOSONAR
        if (oldVersion < 2) { //NOSONAR

            String TABLE_BACKUP = "BACKUP"; //NOSONAR

            //Rename existing table to backup
            db.execSQL("ALTER TABLE " + TABLE_PLAY_COUNT + " RENAME TO " + TABLE_BACKUP + ";"); //NOSONAR

            //Create new table
            db.execSQL("CREATE TABLE " + TABLE_PLAY_COUNT + "(" //NOSONAR
                    + COLUMN_ID + " LONG NOT NULL UNIQUE ON CONFLICT REPLACE, " //NOSONAR
                    + COLUMN_PLAY_COUNT + " INTEGER DEFAULT 0, " //NOSONAR
                    + COLUMN_TIME_PLAYED + " LONG DEFAULT 0);"); //NOSONAR

            //Copy backup into new
            db.execSQL("INSERT OR REPLACE INTO " + TABLE_PLAY_COUNT + "(" + COLUMN_ID + ", " + COLUMN_PLAY_COUNT + ") " //NOSONAR
                    + "SELECT " + COLUMN_ID + "," + COLUMN_PLAY_COUNT + " FROM " + TABLE_BACKUP + "; "); //NOSONAR

            //Drop backup
            db.execSQL("DROP TABLE " + TABLE_BACKUP + "; "); //NOSONAR

            //We have to end this transaction so we can attach the count info table below
            db.setTransactionSuccessful(); //NOSONAR
            db.endTransaction(); //NOSONAR

            //Add rows from count info table
            try { //NOSONAR
                String COUNT_INFO_DATABASE = "count_info.db"; //NOSONAR
                String PATH_COUNT_INFO = applicationContext.getDatabasePath(COUNT_INFO_DATABASE).toString(); //NOSONAR
                String TABLE_COUNT_INFO = "COUNT_INFO"; //NOSONAR
                String COUNT_INFO_COLUMN_ID = "_id"; //NOSONAR
                String COUNT_INFO_COLUMN_TIME_PLAYED = "time_played"; //NOSONAR

                db.execSQL("ATTACH '" + PATH_COUNT_INFO + "' AS " + TABLE_COUNT_INFO + "; "); //NOSONAR

                //Now we have to begin a new transaction
                db.beginTransaction(); //NOSONAR
                db.execSQL("INSERT OR REPLACE INTO " //NOSONAR
                        + TABLE_PLAY_COUNT //NOSONAR
                        + "("
                        + COLUMN_ID //NOSONAR
                        + ", "
                        + COLUMN_PLAY_COUNT //NOSONAR
                        + ", "
                        + COLUMN_TIME_PLAYED //NOSONAR
                        + ") "
                        + "SELECT " //NOSONAR
                        + COUNT_INFO_COLUMN_ID //NOSONAR
                        + ","
                        + "(SELECT " //NOSONAR
                        + COLUMN_PLAY_COUNT //NOSONAR
                        + " FROM " //NOSONAR
                        + TABLE_PLAY_COUNT //NOSONAR
                        + " WHERE _id = " //NOSONAR
                        + COLUMN_ID //NOSONAR
                        + ")"
                        + ","
                        + COUNT_INFO_COLUMN_TIME_PLAYED //NOSONAR
                        + " FROM " //NOSONAR
                        + TABLE_COUNT_INFO //NOSONAR
                        + ";");
            } catch (SQLiteException ignored) { //NOSONAR
                // The count info table probably doesn't exist (it wasn't created in the previous version of the app)
                //  Nothing to do
            }
            //SQLiteOpenHelper will automatically setTransactionSuccessful & endTransaction for us.
        }
    }
}
