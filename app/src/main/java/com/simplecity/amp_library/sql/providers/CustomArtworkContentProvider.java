package com.simplecity.amp_library.sql.providers; // NOSONAR

import android.content.ContentProvider; // NOSONAR
import android.content.ContentValues; // NOSONAR
import android.content.UriMatcher; // NOSONAR
import android.database.Cursor; // NOSONAR
import android.database.sqlite.SQLiteDatabase; // NOSONAR
import android.database.sqlite.SQLiteQueryBuilder; // NOSONAR
import android.net.Uri; // NOSONAR
import android.text.TextUtils; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR
import com.simplecity.amp_library.sql.databases.CustomArtworkTable; // NOSONAR
import java.util.Arrays; // NOSONAR
import java.util.HashSet; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomArtworkContentProvider extends ContentProvider { //NOSONAR

    private CustomArtworkTable database; //NOSONAR

    // Used for the Uri Matcher // NOSONAR
    private static final int CUSTOM_ARTWORK = 10; //NOSONAR

    private static final int CUSTOM_ARTWORK_ID = 20; //NOSONAR

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".custom_artwork.contentprovider"; //NOSONAR

    private static final String BASE_PATH = "custom_artwork"; //NOSONAR

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH); //NOSONAR

    static { //NOSONAR
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, CUSTOM_ARTWORK); //NOSONAR
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CUSTOM_ARTWORK_ID); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onCreate() { //NOSONAR
        database = new CustomArtworkTable(getContext()); //NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Cursor query(Uri uri, String[] projection, String selection, //NOSONAR
            String[] selectionArgs, String sortOrder) { //NOSONAR

        // Using SQLiteQueryBuilder instead of query() method // NOSONAR
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder(); //NOSONAR

        // Check if the caller has requested a column which does not exists // NOSONAR
        checkColumns(projection); //NOSONAR

        // Set the table // NOSONAR
        queryBuilder.setTables(CustomArtworkTable.TABLE_ARTIST_ART); //NOSONAR

        int uriType = sURIMatcher.match(uri); //NOSONAR
        switch (uriType) { //NOSONAR
            case CUSTOM_ARTWORK: //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        } // NOSONAR

        SQLiteDatabase db = database.getWritableDatabase(); //NOSONAR
        Cursor cursor = queryBuilder.query(db, projection, selection, //NOSONAR
                selectionArgs, null, null, sortOrder); //NOSONAR
        // Make sure that potential listeners are getting notified // NOSONAR
        cursor.setNotificationUri(getContext().getContentResolver(), uri); //NOSONAR

        return cursor; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getType(Uri uri) { //NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Uri insert(Uri uri, ContentValues values) { //NOSONAR
        int uriType = sURIMatcher.match(uri); //NOSONAR
        SQLiteDatabase sqlDB = database.getWritableDatabase(); //NOSONAR
        long id; //NOSONAR
        switch (uriType) { //NOSONAR
            case CUSTOM_ARTWORK: //NOSONAR
                id = sqlDB.insert(CustomArtworkTable.TABLE_ARTIST_ART, null, values); //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        } // NOSONAR
        getContext().getContentResolver().notifyChange(uri, null); //NOSONAR
        return Uri.parse(BASE_PATH + "/" + id); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int delete(Uri uri, String selection, String[] selectionArgs) { //NOSONAR
        int uriType = sURIMatcher.match(uri); //NOSONAR
        SQLiteDatabase sqlDB = database.getWritableDatabase(); //NOSONAR
        int rowsDeleted; //NOSONAR
        switch (uriType) { //NOSONAR
            case CUSTOM_ARTWORK: //NOSONAR
                rowsDeleted = sqlDB.delete(CustomArtworkTable.TABLE_ARTIST_ART, selection, //NOSONAR
                        selectionArgs); //NOSONAR
                break; //NOSONAR
            case CUSTOM_ARTWORK_ID: //NOSONAR
                String id = uri.getLastPathSegment(); //NOSONAR
                if (TextUtils.isEmpty(selection)) { //NOSONAR
                    rowsDeleted = sqlDB.delete(CustomArtworkTable.TABLE_ARTIST_ART, //NOSONAR
                            CustomArtworkTable.COLUMN_ID + "=" + id, //NOSONAR
                            null); //NOSONAR
                } else { //NOSONAR
                    rowsDeleted = sqlDB.delete(CustomArtworkTable.TABLE_ARTIST_ART, //NOSONAR
                            CustomArtworkTable.COLUMN_ID + "=" + id //NOSONAR
                                    + " and " + selection, //NOSONAR
                            selectionArgs //NOSONAR
                    ); // NOSONAR
                } // NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        } // NOSONAR
        getContext().getContentResolver().notifyChange(uri, null); //NOSONAR
        return rowsDeleted; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { //NOSONAR

        int uriType = sURIMatcher.match(uri); //NOSONAR
        SQLiteDatabase sqlDB = database.getWritableDatabase(); //NOSONAR
        int rowsUpdated; //NOSONAR
        switch (uriType) { //NOSONAR
            case CUSTOM_ARTWORK: //NOSONAR
                rowsUpdated = sqlDB.update(CustomArtworkTable.TABLE_ARTIST_ART, //NOSONAR
                        values, //NOSONAR
                        selection, //NOSONAR
                        selectionArgs); //NOSONAR
                break; //NOSONAR
            case CUSTOM_ARTWORK_ID: //NOSONAR
                String id = uri.getLastPathSegment(); //NOSONAR
                if (TextUtils.isEmpty(selection)) { //NOSONAR
                    rowsUpdated = sqlDB.update(CustomArtworkTable.TABLE_ARTIST_ART, //NOSONAR
                            values, //NOSONAR
                            CustomArtworkTable.COLUMN_ID + "=" + id, //NOSONAR
                            null); //NOSONAR
                } else { //NOSONAR
                    rowsUpdated = sqlDB.update(CustomArtworkTable.TABLE_ARTIST_ART, //NOSONAR
                            values, //NOSONAR
                            CustomArtworkTable.COLUMN_ID + "=" + id //NOSONAR
                                    + " and " //NOSONAR
                                    + selection, //NOSONAR
                            selectionArgs //NOSONAR
                    ); // NOSONAR
                } // NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                throw new IllegalArgumentException("Unknown URI: " + uri); //NOSONAR
        } // NOSONAR
        getContext().getContentResolver().notifyChange(uri, null); //NOSONAR
        return rowsUpdated; //NOSONAR
    } // NOSONAR

    private void checkColumns(String[] projection) { //NOSONAR
        String[] available = { //NOSONAR
                CustomArtworkTable.COLUMN_ID, //NOSONAR
                CustomArtworkTable.COLUMN_KEY, //NOSONAR
                CustomArtworkTable.COLUMN_PATH, //NOSONAR
                CustomArtworkTable.COLUMN_TYPE //NOSONAR
        }; // NOSONAR
        if (projection != null) { //NOSONAR
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection)); //NOSONAR
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available)); //NOSONAR
            // check if all columns which are requested are available // NOSONAR
            if (!availableColumns.containsAll(requestedColumns)) { //NOSONAR
                throw new IllegalArgumentException("Unknown columns in PROJECTION"); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
