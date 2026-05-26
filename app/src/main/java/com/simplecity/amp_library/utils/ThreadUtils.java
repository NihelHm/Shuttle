package com.simplecity.amp_library.utils; // NOSONAR

import android.os.Looper; // NOSONAR
import com.crashlytics.android.core.CrashlyticsCore; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ThreadUtils { //NOSONAR

    private ThreadUtils() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public static void ensureNotOnMainThread() { //NOSONAR
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) { //NOSONAR
            if (BuildConfig.DEBUG) { //NOSONAR
                throw new IllegalStateException("ensureNotOnMainThread failed."); //NOSONAR
            } else { //NOSONAR
                CrashlyticsCore.getInstance().log("ThreadUtils ensureNotOnMainThread() failed"); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
