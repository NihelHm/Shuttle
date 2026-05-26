package com.simplecity.multisheetview.ui.behavior;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> { //NOSONAR

    private static final String TAG = "CustomBottomSheetBehavi"; //NOSONAR

    private boolean allowDragging = true; //NOSONAR

    public CustomBottomSheetBehavior() { //NOSONAR
        // Intentionally left empty.
    }

    public CustomBottomSheetBehavior(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public void setAllowDragging(boolean allowDragging) { //NOSONAR
        this.allowDragging = allowDragging; //NOSONAR
    }

    @Override //NOSONAR
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) { //NOSONAR
        if (!allowDragging) { //NOSONAR
            return false; //NOSONAR
        }

        return super.onInterceptTouchEvent(parent, child, event); //NOSONAR
    }


}
