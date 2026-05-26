package com.simplecity.amp_library.utils; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.app.Activity; // NOSONAR
import android.app.AlertDialog; // NOSONAR
import android.content.ContentResolver; // NOSONAR
import android.content.ContentUris; // NOSONAR
import android.content.ContentValues; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.content.pm.PackageManager; // NOSONAR
import android.content.res.Configuration; // NOSONAR
import android.database.Cursor; // NOSONAR
import android.net.ConnectivityManager; // NOSONAR
import android.net.NetworkInfo; // NOSONAR
import android.net.Uri; // NOSONAR
import android.net.wifi.WifiManager; // NOSONAR
import android.os.Build; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import android.provider.BaseColumns; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import android.provider.Settings; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.util.Log; // NOSONAR
import android.widget.Toast; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.constants.Config; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.BaseFileObject; // NOSONAR
import com.simplecity.amp_library.model.Query; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.sql.SqlUtils; // NOSONAR
import com.simplecity.amp_library.sql.providers.PlayCountTable; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.io.File; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ShuttleUtils { //NOSONAR

    //Arguments supplied to various bundles // NOSONAR

    private final static String TAG = "ShuttleUtils"; //NOSONAR

    @NonNull //NOSONAR
    public static Intent getShuttleStoreIntent(@NonNull String packageName) { //NOSONAR
        String uri; //NOSONAR
        if (isAmazonBuild()) { //NOSONAR
            uri = "amzn://apps/android?p=" + packageName; //NOSONAR
        } else { //NOSONAR
            uri = "market://details?id=" + packageName; //NOSONAR
        } // NOSONAR
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    public static Intent getShuttleWebIntent(@NonNull String packageName) { //NOSONAR
        String uri; //NOSONAR
        if (isAmazonBuild()) { //NOSONAR
            uri = "http://www.amazon.com/gp/mas/dl/android?p=" + packageName; //NOSONAR
        } else { //NOSONAR
            uri = "https://play.google.com/store/apps/details?id=" + packageName; //NOSONAR
        } // NOSONAR
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); //NOSONAR
    } // NOSONAR

    public static void openShuttleLink(@NonNull Activity activity, @NonNull String packageName, PackageManager packageManager) { //NOSONAR
        Intent intent = getShuttleStoreIntent(packageName); //NOSONAR
        if (packageManager.resolveActivity(intent, 0) == null) { //NOSONAR
            intent = getShuttleWebIntent(packageName); //NOSONAR
        } // NOSONAR
        activity.startActivity(intent); //NOSONAR
    } // NOSONAR

    public static boolean isAmazonBuild() { //NOSONAR
        return BuildConfig.FLAVOR.equals("amazonFree") || BuildConfig.FLAVOR.equals("amazonPaid"); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Check whether we have an internet connection // NOSONAR
     * // NOSONAR
     * @param careAboutWifiOnly whether we care if the preference 'download via wifi only' is checked // NOSONAR
     * @return true if we have a connection, false otherwise // NOSONAR
     */ // NOSONAR
    public static boolean isOnline(Context context, boolean careAboutWifiOnly) { //NOSONAR

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); //NOSONAR

        //Check if we are restricted to download over wifi only // NOSONAR
        boolean wifiOnly = prefs.getBoolean("pref_download_wifi_only", true); //NOSONAR

        //If we don't care whether wifi is allowed or not, set wifiOnly to false // NOSONAR
        if (!careAboutWifiOnly) { //NOSONAR
            wifiOnly = false; //NOSONAR
        } // NOSONAR

        final ConnectivityManager cm = (ConnectivityManager) context //NOSONAR
                .getSystemService(Context.CONNECTIVITY_SERVICE); //NOSONAR

        //Check the state of the wifi network // NOSONAR
        final NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //NOSONAR
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        //Check other networks // NOSONAR
        final NetworkInfo netInfo = cm.getActiveNetworkInfo(); //NOSONAR
        return netInfo != null && netInfo.isConnectedOrConnecting() && !wifiOnly; //NOSONAR
    } // NOSONAR

    public static boolean isUpgraded(ShuttleApplication application, SettingsManager settingsManager) { //NOSONAR

        if (application.getIsUpgraded()) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        if (settingsManager.getIsLegacyUpgraded()) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        try { //NOSONAR
            return application.getPackageName().equals(Config.PACKAGE_NAME_PRO); //NOSONAR
        } catch (Exception ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        //If something goes wrong, assume the user has the pro version // NOSONAR
        return true; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return true if device is running API >= 23 // NOSONAR
     */ // NOSONAR
    public static boolean hasMarshmallow() { //NOSONAR
        return Build.VERSION.SDK_INT >= 23; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return true if device is running API >= 24 // NOSONAR
     */ // NOSONAR
    public static boolean hasNougat() { //NOSONAR
        return Build.VERSION.SDK_INT >= 24; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return true if device is running API >= 26 // NOSONAR
     */ // NOSONAR
    public static boolean hasOreo() { //NOSONAR
        return Build.VERSION.SDK_INT >= 26; //NOSONAR
    } // NOSONAR

    public static boolean isLandscape(Context context) { //NOSONAR
        final int orientation = context.getResources().getConfiguration().orientation; //NOSONAR
        return orientation == Configuration.ORIENTATION_LANDSCAPE; //NOSONAR
    } // NOSONAR

    public static boolean isTablet(Context context) { //NOSONAR
        return context.getResources().getBoolean(R.bool.isTablet); //NOSONAR
    } // NOSONAR

    public static Single<List<Song>> getSongsForFileObjects(Repository.SongsRepository songsRepository, List<BaseFileObject> fileObjects) { //NOSONAR

        List<Single<List<Song>>> observables = Stream.of(fileObjects) //NOSONAR
                .map(fileObject -> FileHelper.getSongList(songsRepository, new File(fileObject.path), true, false)) //NOSONAR
                .toList(); //NOSONAR

        return Single.concat(observables) //NOSONAR
                .reduce((songs, songs2) -> { //NOSONAR
                    List<Song> allSongs = new ArrayList<>(); //NOSONAR
                    allSongs.addAll(songs); //NOSONAR
                    allSongs.addAll(songs2); //NOSONAR
                    return allSongs; //NOSONAR
                }).toSingle(); //NOSONAR
    } // NOSONAR

    public static void incrementPlayCount(Context context, Song song) { //NOSONAR

        if (song == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        ContentValues values = new ContentValues(); //NOSONAR
        values.put(PlayCountTable.COLUMN_ID, song.id); //NOSONAR
        values.put(PlayCountTable.COLUMN_PLAY_COUNT, song.getPlayCount(context) + 1); //NOSONAR
        values.put(PlayCountTable.COLUMN_TIME_PLAYED, System.currentTimeMillis()); //NOSONAR

        try { //NOSONAR
            if (context.getContentResolver().update(PlayCountTable.URI, values, PlayCountTable.COLUMN_ID + " ='" + song.id + "'", null) < 1) { //NOSONAR
                context.getContentResolver().insert(PlayCountTable.URI, values); //NOSONAR
            } // NOSONAR
        } catch (IllegalArgumentException e) { //NOSONAR
            Log.e(TAG, "Failed to increment play count: " + e.toString()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public static String getIpAddr(Context context) { //NOSONAR
        @SuppressLint("WifiManagerLeak") //NOSONAR
        int i = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress(); //NOSONAR
        Object[] arrayOfObject = new Object[4]; //NOSONAR
        arrayOfObject[0] = i & 0xFF; //NOSONAR
        arrayOfObject[1] = 0xFF & i >> 8; //NOSONAR
        arrayOfObject[2] = 0xFF & i >> 16; //NOSONAR
        arrayOfObject[3] = 0xFF & i >> 24; //NOSONAR
        return String.format("%d.%d.%d.%d", arrayOfObject); //NOSONAR
    } // NOSONAR

    public static boolean canDrawBehindStatusBar() { //NOSONAR
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH); //NOSONAR
    } // NOSONAR
} // NOSONAR
