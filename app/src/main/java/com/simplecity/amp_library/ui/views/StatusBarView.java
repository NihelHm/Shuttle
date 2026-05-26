package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.simplecity.amp_library.utils.ActionBarUtils;
import com.simplecity.amp_library.utils.ShuttleUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class StatusBarView extends View { //NOSONAR
    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    public StatusBarView(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public StatusBarView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    @Override //NOSONAR
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR

        if (ShuttleUtils.canDrawBehindStatusBar()) { //NOSONAR
            setMeasuredDimension(widthMeasureSpec, (int) ActionBarUtils.getStatusBarHeight(getContext())); //NOSONAR
        } else { //NOSONAR
            setMeasuredDimension(0, 0); //NOSONAR
        }
    }
}
