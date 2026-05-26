package com.simplecity.amp_library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.BuildConfig;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.constants.Config;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.model.BaseFileObject;
import com.simplecity.amp_library.model.Query;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.sql.SqlUtils;
import com.simplecity.amp_library.sql.providers.PlayCountTable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ShuttleUtils { //NOSONAR

    //Arguments supplied to various bundles

    private final static String TAG = "ShuttleUtils"; //NOSONAR

    @NonNull //NOSONAR
    public static Intent getShuttleStoreIntent(@NonNull String packageName) { //NOSONAR
        String uri; //NOSONAR
        if (isAmazonBuild()) { //NOSONAR
            uri = "amzn://apps/android?p=" + packageName; //NOSONAR
        } else { //NOSONAR
            uri = "market://details?id=" + packageName; //NOSONAR
        }
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); //NOSONAR
    }

    @NonNull //NOSONAR
    public static Intent getShuttleWebIntent(@NonNull String packageName) { //NOSONAR
        String uri; //NOSONAR
        if (isAmazonBuild()) { //NOSONAR
            uri = "http://www.amazon.com/gp/mas/dl/android?p=" + packageName; //NOSONAR
        } else { //NOSONAR
            uri = "https://play.google.com/store/apps/details?id=" + packageName; //NOSONAR
        }
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); //NOSONAR
    }

    public static void openShuttleLink(@NonNull Activity activity, @NonNull String packageName, PackageManager packageManager) { //NOSONAR
        Intent intent = getShuttleStoreIntent(packageName); //NOSONAR
        if (packageManager.resolveActivity(intent, 0) == null) { //NOSONAR
            intent = getShuttleWebIntent(packageName); //NOSONAR
        }
        activity.startActivity(intent); //NOSONAR
    }

    public static boolean isAmazonBuild() { //NOSONAR
        return BuildConfig.FLAVOR.equals("amazonFree") || BuildConfig.FLAVOR.equals("amazonPaid"); //NOSONAR
    }

    /**
     * Check whether we have an internet connection
     *
     * @param careAboutWifiOnly whether we care if the preference 'download via wifi only' is checked
     * @return true if we have a connection, false otherwise
     */
    public static boolean isOnline(Context context, boolean careAboutWifiOnly) { //NOSONAR

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); //NOSONAR

        //Check if we are restricted to download over wifi only
        boolean wifiOnly = prefs.getBoolean("pref_download_wifi_only", true); //NOSONAR

        //If we don't care whether wifi is allowed or not, set wifiOnly to false
        if (!careAboutWifiOnly) { //NOSONAR
            wifiOnly = false; //NOSONAR
        }

        final ConnectivityManager cm = (ConnectivityManager) context //NOSONAR
                .getSystemService(Context.CONNECTIVITY_SERVICE); //NOSONAR

        //Check the state of the wifi network
        final NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //NOSONAR
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) { //NOSONAR
            return true; //NOSONAR
        }

        //Check other networks
        final NetworkInfo netInfo = cm.getActiveNetworkInfo(); //NOSONAR
        return netInfo != null && netInfo.isConnectedOrConnecting() && !wifiOnly; //NOSONAR
    }

    public static boolean isUpgraded(ShuttleApplication application, SettingsManager settingsManager) { //NOSONAR

        if (application.getIsUpgraded()) { //NOSONAR
            return true; //NOSONAR
        }

        if (settingsManager.getIsLegacyUpgraded()) { //NOSONAR
            return true; //NOSONAR
        }

        try { //NOSONAR
            return application.getPackageName().equals(Config.PACKAGE_NAME_PRO); //NOSONAR
        } catch (Exception ignored) { //NOSONAR
            // Intentionally left empty.
        }

        //If something goes wrong, assume the user has the pro version
        return true; //NOSONAR
    }

    /**
     * @return true if device is running API >= 23
     */
    public static boolean hasMarshmallow() { //NOSONAR
        return Build.VERSION.SDK_INT >= 23; //NOSONAR
    }

    /**
     * @return true if device is running API >= 24
     */
    public static boolean hasNougat() { //NOSONAR
        return Build.VERSION.SDK_INT >= 24; //NOSONAR
    }

    /**
     * @return true if device is running API >= 26
     */
    public static boolean hasOreo() { //NOSONAR
        return Build.VERSION.SDK_INT >= 26; //NOSONAR
    }

    public static boolean isLandscape(Context context) { //NOSONAR
        final int orientation = context.getResources().getConfiguration().orientation; //NOSONAR
        return orientation == Configuration.ORIENTATION_LANDSCAPE; //NOSONAR
    }

    public static boolean isTablet(Context context) { //NOSONAR
        return context.getResources().getBoolean(R.bool.isTablet); //NOSONAR
    }

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
    }

    public static void incrementPlayCount(Context context, Song song) { //NOSONAR

        if (song == null) { //NOSONAR
            return; //NOSONAR
        }

        ContentValues values = new ContentValues(); //NOSONAR
        values.put(PlayCountTable.COLUMN_ID, song.id); //NOSONAR
        values.put(PlayCountTable.COLUMN_PLAY_COUNT, song.getPlayCount(context) + 1); //NOSONAR
        values.put(PlayCountTable.COLUMN_TIME_PLAYED, System.currentTimeMillis()); //NOSONAR

        try { //NOSONAR
            if (context.getContentResolver().update(PlayCountTable.URI, values, PlayCountTable.COLUMN_ID + " ='" + song.id + "'", null) < 1) { //NOSONAR
                context.getContentResolver().insert(PlayCountTable.URI, values); //NOSONAR
            }
        } catch (IllegalArgumentException e) { //NOSONAR
            Log.e(TAG, "Failed to increment play count: " + e.toString()); //NOSONAR
        }
    }

    public static String getIpAddr(Context context) { //NOSONAR
        @SuppressLint("WifiManagerLeak") //NOSONAR
        int i = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress(); //NOSONAR
        Object[] arrayOfObject = new Object[4]; //NOSONAR
        arrayOfObject[0] = i & 0xFF; //NOSONAR
        arrayOfObject[1] = 0xFF & i >> 8; //NOSONAR
        arrayOfObject[2] = 0xFF & i >> 16; //NOSONAR
        arrayOfObject[3] = 0xFF & i >> 24; //NOSONAR
        return String.format("%d.%d.%d.%d", arrayOfObject); //NOSONAR
    }

    public static boolean canDrawBehindStatusBar() { //NOSONAR
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH); //NOSONAR
    }
}
