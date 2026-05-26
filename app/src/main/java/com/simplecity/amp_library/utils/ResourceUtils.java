package com.simplecity.amp_library.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.simplecity.amp_library.glide.utils.Size;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ResourceUtils { //NOSONAR

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int toPixels(float dp) { //NOSONAR
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics(); //NOSONAR
        return (int) (dp * (metrics.densityDpi / 160f)); //NOSONAR
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float toDips(float px) { //NOSONAR
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics(); //NOSONAR
        return px / (metrics.densityDpi / 160f); //NOSONAR
    }

    public static Size getScreenSize() { //NOSONAR
        return new Size(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels); //NOSONAR
    }
}
