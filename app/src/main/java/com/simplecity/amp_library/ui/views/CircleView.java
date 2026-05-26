package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.utils.ResourceUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CircleView extends View { //NOSONAR

    private Paint paint; //NOSONAR

    private Drawable tickDrawable; //NOSONAR

    public CircleView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        setWillNotDraw(false); //NOSONAR

        paint = new Paint(); //NOSONAR
        paint.setStyle(Paint.Style.FILL); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            paint.setColor(Color.RED); //NOSONAR
        }

        tickDrawable = ContextCompat.getDrawable(context, R.drawable.ic_check_24dp); //NOSONAR
    }

    public void setColor(int color) { //NOSONAR
        paint.setColor(color); //NOSONAR
        invalidate(); //NOSONAR
    }

    @Override //NOSONAR
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //NOSONAR
    }

    @Override //NOSONAR
    protected void onDraw(Canvas canvas) { //NOSONAR
        super.onDraw(canvas); //NOSONAR

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint); //NOSONAR

        if (isActivated()) { //NOSONAR
            int padding = ResourceUtils.toPixels(4); //NOSONAR
            tickDrawable.setBounds(padding, padding, getWidth() - padding, getHeight() - padding); //NOSONAR
            tickDrawable.draw(canvas); //NOSONAR
        }
    }
}
