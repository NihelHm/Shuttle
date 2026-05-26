package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Rect; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.widget.ImageButton; // NOSONAR

/** // NOSONAR
 * A custom {@link ImageButton} which prevents parent ScrollView scrolling when used as the // NOSONAR
 * anchor for a {@link android.support.v7.widget.PopupMenu} // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class NonScrollImageButton extends android.support.v7.widget.AppCompatImageButton { //NOSONAR

    public NonScrollImageButton(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) { //NOSONAR
        return false; //NOSONAR
    } // NOSONAR
} // NOSONAR
