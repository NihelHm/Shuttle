package com.simplecity.amp_library.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Helpers for the {@link android.app.ActionBar}
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class ActionBarUtils {

    /**
     * The attribute depicting the Size of the {@link android.app.ActionBar}
     */
    private static final int[] ACTION_BAR_SIZE = new int[] {
            android.R.attr.actionBarSize
    };

    /* This class is never initialized */
    private ActionBarUtils() {
        // Intentionally left empty.
    }

    /**
     * @return The height of the {@link android.app.ActionBar}
     */
    public static float getActionBarHeight(Context context) {
        final TypedArray actionBarSize = context.obtainStyledAttributes(ACTION_BAR_SIZE);
        final int actionBarHeight = actionBarSize.getDimensionPixelSize(0, 0);
        actionBarSize.recycle();
        return actionBarHeight;
    }

    /**
     * @return The height of the StatusBar
     */
    public static float getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
