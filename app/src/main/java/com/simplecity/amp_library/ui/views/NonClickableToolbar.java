package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.afollestad.aesthetic.AestheticToolbar;

/**
 * A Toolbar which does not consume touch events.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class NonClickableToolbar extends AestheticToolbar { //NOSONAR
    public NonClickableToolbar(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public NonClickableToolbar(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public NonClickableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    @Override //NOSONAR
    public boolean onTouchEvent(MotionEvent ev) { //NOSONAR
        return false; //NOSONAR
    }
}
