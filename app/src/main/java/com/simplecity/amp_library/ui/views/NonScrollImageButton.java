package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * A custom {@link ImageButton} which prevents parent ScrollView scrolling when used as the
 * anchor for a {@link android.support.v7.widget.PopupMenu}
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class NonScrollImageButton extends android.support.v7.widget.AppCompatImageButton {

    public NonScrollImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        return false;
    }
}
