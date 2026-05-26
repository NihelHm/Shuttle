package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.simplecity.amp_library.utils.ActionBarUtils;
import com.simplecity.amp_library.utils.ShuttleUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
class StatusBarView extends View {
    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StatusBarView(Context context) {
        super(context);
    }

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (ShuttleUtils.canDrawBehindStatusBar()) {
            setMeasuredDimension(widthMeasureSpec, (int) ActionBarUtils.getStatusBarHeight(getContext()));
        } else {
            setMeasuredDimension(0, 0);
        }
    }
}
