package com.simplecity.amp_library.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import com.simplecity.amp_library.ShuttleApplication;
import io.reactivex.Completable;
import java.io.File;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class LegacyUtils {

    private LegacyUtils() {
        // Intentionally left empty.
    }

    @NonNull
    public static Completable deleteOldResources(ShuttleApplication application) {
        return Completable.fromAction(() -> {
            //Delete albumthumbs/artists directory
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory() + "/albumthumbs/artists/");
                if (file.exists() && file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File child : files) {
                            child.delete();
                        }
                    }
                    file.delete();
                }
            }

            //Delete old http cache
            File oldHttpCache = application.getDiskCacheDir("http");
            if (oldHttpCache != null && oldHttpCache.exists()) {
                oldHttpCache.delete();
            }

            //Delete old thumbs cache
            File oldThumbsCache = application.getDiskCacheDir("thumbs");
            if (oldThumbsCache != null && oldThumbsCache.exists()) {
                oldThumbsCache.delete();
            }
        });
    }
}
