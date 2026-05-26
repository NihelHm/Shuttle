package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.MotionEvent; // NOSONAR
import com.afollestad.aesthetic.AestheticToolbar; // NOSONAR

/** // NOSONAR
 * A Toolbar which does not consume touch events. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class NonClickableToolbar extends AestheticToolbar { //NOSONAR
    public NonClickableToolbar(Context context) { //NOSONAR
        super(context); //NOSONAR
    } // NOSONAR

    public NonClickableToolbar(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    } // NOSONAR

    public NonClickableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onTouchEvent(MotionEvent ev) { //NOSONAR
        return false; //NOSONAR
    } // NOSONAR
} // NOSONAR
