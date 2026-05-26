package com.simplecity.amp_library.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Helpers for the {@link android.app.ActionBar}
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ActionBarUtils { //NOSONAR

    /**
     * The attribute depicting the Size of the {@link android.app.ActionBar}
     */
    private static final int[] ACTION_BAR_SIZE = new int[] { //NOSONAR
            android.R.attr.actionBarSize //NOSONAR
    };

    /* This class is never initialized */
    private ActionBarUtils() { //NOSONAR
        // Intentionally left empty.
    }

    /**
     * @return The height of the {@link android.app.ActionBar}
     */
    public static float getActionBarHeight(Context context) { //NOSONAR
        final TypedArray actionBarSize = context.obtainStyledAttributes(ACTION_BAR_SIZE); //NOSONAR
        final int actionBarHeight = actionBarSize.getDimensionPixelSize(0, 0); //NOSONAR
        actionBarSize.recycle(); //NOSONAR
        return actionBarHeight; //NOSONAR
    }

    /**
     * @return The height of the StatusBar
     */
    public static float getStatusBarHeight(Context context) { //NOSONAR
        int result = 0; //NOSONAR
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android"); //NOSONAR
        if (resourceId > 0) { //NOSONAR
            result = context.getResources().getDimensionPixelSize(resourceId); //NOSONAR
        }
        return result; //NOSONAR
    }
}
