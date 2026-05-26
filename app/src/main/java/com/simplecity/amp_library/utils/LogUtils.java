package com.simplecity.amp_library.utils;

import android.support.annotation.Nullable;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.simplecity.amp_library.BuildConfig;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class LogUtils {

    private LogUtils() {
        //no instance
    }

    public static void logException(String tag, String message, @Nullable Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message + "\nThrowable: " + (throwable != null ? throwable.getMessage() : null));
            if (throwable != null) {
                throwable.printStackTrace();
            }
        } else {
            Crashlytics.log(Log.ERROR, tag, message + "\nThrowable: " + (throwable != null ? throwable.getMessage() : null));
            Crashlytics.logException(throwable);
        }
    }
}
