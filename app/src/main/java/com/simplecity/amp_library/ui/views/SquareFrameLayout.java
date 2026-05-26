package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.simplecity.amp_library.R;

/**
 * A custom {@link android.widget.FrameLayout} that is sized to be a perfect square
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SquareFrameLayout extends FrameLayout { //NOSONAR

    boolean widthDominant = true; //NOSONAR

    /**
     * Constructor for <code>SquareFrameLayout</code>
     *
     * @param context The {@link android.content.Context} to use
     * @param attrs The attributes of the XML tag that is inflating the view
     */
    public SquareFrameLayout(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        TypedArray a = context.getTheme().obtainStyledAttributes( //NOSONAR
                attrs, //NOSONAR
                R.styleable.SquareFrameLayout, //NOSONAR
                0, 0); //NOSONAR

        if (a.hasValue(R.styleable.SquareFrameLayout_dominant_measurement)) { //NOSONAR
            int value = a.getInt(R.styleable.SquareFrameLayout_dominant_measurement, 0); //NOSONAR
            widthDominant = value == 0; //NOSONAR
        }

        a.recycle(); //NOSONAR
    }

    @Override //NOSONAR
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
        setMeasuredDimension(widthDominant ? getMeasuredWidth() : getMeasuredHeight(), widthDominant ? getMeasuredWidth() : getMeasuredHeight()); //NOSONAR
    }
}
