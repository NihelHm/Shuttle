package com.simplecity.amp_library.utils; // NOSONAR

import android.os.Environment; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import io.reactivex.Completable; // NOSONAR
import java.io.File; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LegacyUtils { //NOSONAR

    private LegacyUtils() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    public static Completable deleteOldResources(ShuttleApplication application) { //NOSONAR
        return Completable.fromAction(() -> { //NOSONAR
            //Delete albumthumbs/artists directory // NOSONAR
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //NOSONAR
                File file = new File(Environment.getExternalStorageDirectory() + "/albumthumbs/artists/"); //NOSONAR
                if (file.exists() && file.isDirectory()) { //NOSONAR
                    File[] files = file.listFiles(); //NOSONAR
                    if (files != null) { //NOSONAR
                        for (File child : files) { //NOSONAR
                            child.delete(); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                    file.delete(); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            //Delete old http cache // NOSONAR
            File oldHttpCache = application.getDiskCacheDir("http"); //NOSONAR
            if (oldHttpCache != null && oldHttpCache.exists()) { //NOSONAR
                oldHttpCache.delete(); //NOSONAR
            } // NOSONAR

            //Delete old thumbs cache // NOSONAR
            File oldThumbsCache = application.getDiskCacheDir("thumbs"); //NOSONAR
            if (oldThumbsCache != null && oldThumbsCache.exists()) { //NOSONAR
                oldThumbsCache.delete(); //NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR
} // NOSONAR
