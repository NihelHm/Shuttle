package com.simplecity.amp_library.sql.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.simplecity.amp_library.BuildConfig;
import java.util.Arrays;
import java.util.HashSet;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlayCountContentProvider extends ContentProvider { //NOSONAR

    private static final String TAG = "PlayCountContentProvide"; //NOSONAR

    private PlayCountTable database; //NOSONAR

    // Used for the Uri Matcher
    private static final int PLAY_COUNT = 10; //NOSONAR

    private static final int PLAY_COUNT_ID = 20; //NOSONAR

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".play_count.contentprovider"; //NOSONAR

    private static final String BASE_PATH = "play_count"; //NOSONAR

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH); //NOSONAR

    static { //NOSONAR
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PLAY_COUNT); //NOSONAR
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PLAY_COUNT_ID); //NOSONAR
    }

    @Override //NOSONAR
    public boolean onCreate() { //NOSONAR
        database = new PlayCountTable(getContext()); //NOSONAR
        return false; //NOSONAR
    }

    @Override //NOSONAR
    public Cursor query(Uri uri, String[] projection, String selection, //NOSONAR
            String[] selectionArgs, String sortOrder) { //NOSONAR

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder(); //NOSONAR

        // Check if the caller has requested a column which does not exists
        checkColumns(projection); //NOSONAR

        // Set the table
        queryBuilder.setTables(PlayCountTable.TABLE_PLAY_COUNT); //NOSONAR

        int uriType = sURIMatcher.match(uri); //NOSONAR
        switch (uriType) { //NOSONAR
            case PLAY_COUNT: //NOSONAR
                break; //NOSONAR
            case PLAY_COUNT_ID: //NOSONAR
                // Adding the ID to the original query
                queryBuilder.appendWhere(PlayCountTable.COLUMN_ID + "=" + uri.getLastPathSegment()); //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        }

        SQLiteDatabase db = database.getWritableDatabase(); //NOSONAR
        Cursor cursor = queryBuilder.query(db, projection, selection, //NOSONAR
                selectionArgs, null, null, sortOrder); //NOSONAR
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri); //NOSONAR

        return cursor; //NOSONAR
    }

    @Override //NOSONAR
    public String getType(@NonNull Uri uri) { //NOSONAR
        return null; //NOSONAR
    }

    @Override //NOSONAR
    public Uri insert(Uri uri, ContentValues values) { //NOSONAR
        int uriType = sURIMatcher.match(uri); //NOSONAR
        SQLiteDatabase sqlDB = database.getWritableDatabase(); //NOSONAR
        long id; //NOSONAR
        switch (uriType) { //NOSONAR
            case PLAY_COUNT: //NOSONAR
                id = sqlDB.insert(PlayCountTable.TABLE_PLAY_COUNT, null, values); //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        }
        if (id != -1) { //NOSONAR
            getContext().getContentResolver().notifyChange(uri, null); //NOSONAR
        }
        return Uri.parse(BASE_PATH + "/" + id); //NOSONAR
    }

    @Override //NOSONAR
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) { //NOSONAR
        int uriType = sURIMatcher.match(uri); //NOSONAR
        SQLiteDatabase sqlDB = database.getWritableDatabase(); //NOSONAR
        int rowsDeleted; //NOSONAR
        switch (uriType) { //NOSONAR
            case PLAY_COUNT: //NOSONAR
                rowsDeleted = sqlDB.delete(PlayCountTable.TABLE_PLAY_COUNT, selection, selectionArgs); //NOSONAR
                break; //NOSONAR
            case PLAY_COUNT_ID: //NOSONAR
                String id = uri.getLastPathSegment(); //NOSONAR
                if (TextUtils.isEmpty(selection)) { //NOSONAR
                    rowsDeleted = sqlDB.delete(PlayCountTable.TABLE_PLAY_COUNT, //NOSONAR
                            PlayCountTable.COLUMN_ID + "=" + id, //NOSONAR
                            null); //NOSONAR
                } else { //NOSONAR
                    rowsDeleted = sqlDB.delete(PlayCountTable.TABLE_PLAY_COUNT, //NOSONAR
                            PlayCountTable.COLUMN_ID + "=" + id //NOSONAR
                                    + " and " + selection, //NOSONAR
                            selectionArgs //NOSONAR
                    );
                }
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        }
        getContext().getContentResolver().notifyChange(uri, null); //NOSONAR
        return rowsDeleted; //NOSONAR
    }

    @Override //NOSONAR
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { //NOSONAR

        int uriType = sURIMatcher.match(uri); //NOSONAR
        SQLiteDatabase sqlDB = database.getWritableDatabase(); //NOSONAR
        int rowsUpdated; //NOSONAR
        switch (uriType) { //NOSONAR
            case PLAY_COUNT: //NOSONAR
                rowsUpdated = sqlDB.update(PlayCountTable.TABLE_PLAY_COUNT, //NOSONAR
                        values, //NOSONAR
                        selection, //NOSONAR
                        selectionArgs); //NOSONAR
                break; //NOSONAR
            case PLAY_COUNT_ID: //NOSONAR
                String id = uri.getLastPathSegment(); //NOSONAR
                if (TextUtils.isEmpty(selection)) { //NOSONAR
                    rowsUpdated = sqlDB.update(PlayCountTable.TABLE_PLAY_COUNT, //NOSONAR
                            values, //NOSONAR
                            PlayCountTable.COLUMN_ID + "=" + id, //NOSONAR
                            null); //NOSONAR
                } else { //NOSONAR
                    rowsUpdated = sqlDB.update(PlayCountTable.TABLE_PLAY_COUNT, //NOSONAR
                            values, //NOSONAR
                            PlayCountTable.COLUMN_ID + "=" + id //NOSONAR
                                    + " and " //NOSONAR
                                    + selection, //NOSONAR
                            selectionArgs //NOSONAR
                    );
                }
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        }
        if (rowsUpdated > 0) { //NOSONAR
            getContext().getContentResolver().notifyChange(uri, null); //NOSONAR
        }
        return rowsUpdated; //NOSONAR
    }

    private void checkColumns(String[] projection) { //NOSONAR
        String[] available = { //NOSONAR
                PlayCountTable.COLUMN_ID, //NOSONAR
                PlayCountTable.COLUMN_PLAY_COUNT, //NOSONAR
                PlayCountTable.COLUMN_TIME_PLAYED //NOSONAR
        };

        if (projection != null) //NOSONAR

        {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection)); //NOSONAR
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available)); //NOSONAR
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) { //NOSONAR
                throw new IllegalArgumentException("Unknown columns in PROJECTION"); //NOSONAR
            }
        }
    }
}
