package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Rect; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TextViewDrawableSize extends AestheticDrawableTextView { //NOSONAR

    private int drawableWidth; //NOSONAR
    private int drawableHeight; //NOSONAR

    public TextViewDrawableSize(Context context) { //NOSONAR
        super(context); //NOSONAR
        init(context, null, 0); //NOSONAR
    } // NOSONAR

    public TextViewDrawableSize(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
        init(context, attrs, 0); //NOSONAR
    } // NOSONAR

    public TextViewDrawableSize(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
        init(context, attrs, defStyleAttr); //NOSONAR
    } // NOSONAR

    private void init(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextViewDrawableSize, defStyleAttr, 0); //NOSONAR

        try { //NOSONAR
            drawableWidth = array.getDimensionPixelSize(R.styleable.TextViewDrawableSize_compoundDrawableWidth, -1); //NOSONAR
            drawableHeight = array.getDimensionPixelSize(R.styleable.TextViewDrawableSize_compoundDrawableHeight, -1); //NOSONAR
        } finally { //NOSONAR
            array.recycle(); //NOSONAR
        } // NOSONAR

        if (drawableWidth > 0 || drawableHeight > 0) { //NOSONAR
            initCompoundDrawableSize(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void initCompoundDrawableSize() { //NOSONAR
        Drawable[] drawables = getCompoundDrawables(); //NOSONAR
        for (Drawable drawable : drawables) { //NOSONAR
            if (drawable == null) { //NOSONAR
                continue; //NOSONAR
            } // NOSONAR

            Rect realBounds = drawable.getBounds(); //NOSONAR
            float scaleFactor = realBounds.height() / (float) realBounds.width(); //NOSONAR

            float drawableWidth = realBounds.width(); //NOSONAR
            float drawableHeight = realBounds.height(); //NOSONAR

            if (this.drawableWidth > 0) { //NOSONAR
                if (drawableWidth > this.drawableWidth) { //NOSONAR
                    drawableWidth = this.drawableWidth; //NOSONAR
                    drawableHeight = drawableWidth * scaleFactor; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            if (this.drawableHeight > 0) { //NOSONAR
                if (drawableHeight > this.drawableHeight) { //NOSONAR
                    drawableHeight = this.drawableHeight; //NOSONAR
                    drawableWidth = drawableHeight / scaleFactor; //NOSONAR
                } // NOSONAR
            } // NOSONAR

            realBounds.right = realBounds.left + Math.round(drawableWidth); //NOSONAR
            realBounds.bottom = realBounds.top + Math.round(drawableHeight); //NOSONAR

            drawable.setBounds(realBounds); //NOSONAR
        } // NOSONAR
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]); //NOSONAR
    } // NOSONAR
} // NOSONAR
