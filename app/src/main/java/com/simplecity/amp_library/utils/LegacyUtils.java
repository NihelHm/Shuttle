package com.simplecity.amp_library.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import com.simplecity.amp_library.ShuttleApplication;
import io.reactivex.Completable;
import java.io.File;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LegacyUtils { //NOSONAR

    private LegacyUtils() { //NOSONAR
        // Intentionally left empty.
    }

    @NonNull //NOSONAR
    public static Completable deleteOldResources(ShuttleApplication application) { //NOSONAR
        return Completable.fromAction(() -> { //NOSONAR
            //Delete albumthumbs/artists directory
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //NOSONAR
                File file = new File(Environment.getExternalStorageDirectory() + "/albumthumbs/artists/"); //NOSONAR
                if (file.exists() && file.isDirectory()) { //NOSONAR
                    File[] files = file.listFiles(); //NOSONAR
                    if (files != null) { //NOSONAR
                        for (File child : files) { //NOSONAR
                            child.delete(); //NOSONAR
                        }
                    }
                    file.delete(); //NOSONAR
                }
            }

            //Delete old http cache
            File oldHttpCache = application.getDiskCacheDir("http"); //NOSONAR
            if (oldHttpCache != null && oldHttpCache.exists()) { //NOSONAR
                oldHttpCache.delete(); //NOSONAR
            }

            //Delete old thumbs cache
            File oldThumbsCache = application.getDiskCacheDir("thumbs"); //NOSONAR
            if (oldThumbsCache != null && oldThumbsCache.exists()) { //NOSONAR
                oldThumbsCache.delete(); //NOSONAR
            }
        });
    }
}
