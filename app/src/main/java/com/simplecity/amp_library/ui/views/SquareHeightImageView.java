package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SquareHeightImageView extends AppCompatImageView { //NOSONAR

    public SquareHeightImageView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    @Override //NOSONAR
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(heightMeasureSpec, heightMeasureSpec); //NOSONAR
    }
}
