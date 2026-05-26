package com.simplecity.amp_library.utils;

import android.graphics.Color;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ColorUtils { //NOSONAR

    private ColorUtils() { //NOSONAR
        // Intentionally left empty.
    }

    public static int adjustAlpha(int color, float factor) { //NOSONAR
        int alpha = Math.round(Color.alpha(color) * factor); //NOSONAR
        int red = Color.red(color); //NOSONAR
        int green = Color.green(color); //NOSONAR
        int blue = Color.blue(color); //NOSONAR
        return Color.argb(alpha, red, green, blue); //NOSONAR
    }
}
