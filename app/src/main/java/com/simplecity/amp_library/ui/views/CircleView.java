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

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class CircleView extends View {

    private Paint paint;

    private Drawable tickDrawable;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        if (isInEditMode()) {
            paint.setColor(Color.RED);
        }

        tickDrawable = ContextCompat.getDrawable(context, R.drawable.ic_check_24dp);
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);

        if (isActivated()) {
            int padding = ResourceUtils.toPixels(4);
            tickDrawable.setBounds(padding, padding, getWidth() - padding, getHeight() - padding);
            tickDrawable.draw(canvas);
        }
    }
}
