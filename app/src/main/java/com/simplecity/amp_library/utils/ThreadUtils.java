package com.simplecity.amp_library.utils;

import android.os.Looper;
import com.crashlytics.android.core.CrashlyticsCore;
import com.simplecity.amp_library.BuildConfig;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class ThreadUtils {

    private ThreadUtils() {
        // Intentionally left empty.
    }

    public static void ensureNotOnMainThread() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException("ensureNotOnMainThread failed.");
            } else {
                CrashlyticsCore.getInstance().log("ThreadUtils ensureNotOnMainThread() failed");
            }
        }
    }
}
