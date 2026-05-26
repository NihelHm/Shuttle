package com.simplecity.amp_library.utils; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.util.Log; // NOSONAR
import com.crashlytics.android.Crashlytics; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LogUtils { //NOSONAR

    private LogUtils() { //NOSONAR
        //no instance // NOSONAR
    } // NOSONAR

    public static void logException(String tag, String message, @Nullable Throwable throwable) { //NOSONAR
        if (BuildConfig.DEBUG) { //NOSONAR
            Log.e(tag, message + "\nThrowable: " + (throwable != null ? throwable.getMessage() : null)); //NOSONAR
            if (throwable != null) { //NOSONAR
                throwable.printStackTrace(); //NOSONAR
            } // NOSONAR
        } else { //NOSONAR
            Crashlytics.log(Log.ERROR, tag, message + "\nThrowable: " + (throwable != null ? throwable.getMessage() : null)); //NOSONAR
            Crashlytics.logException(throwable); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
