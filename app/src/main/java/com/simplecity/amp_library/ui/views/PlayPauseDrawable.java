package com.simplecity.amp_library.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Property;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.utils.ResourceUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlayPauseDrawable extends Drawable { //NOSONAR

    private static final Property<PlayPauseDrawable, Float> PROGRESS = //NOSONAR
            new Property<PlayPauseDrawable, Float>(Float.class, "progress") { //NOSONAR
                @Override //NOSONAR
                public Float get(PlayPauseDrawable d) { //NOSONAR
                    return d.getProgress(); //NOSONAR
                }

                @Override //NOSONAR
                public void set(PlayPauseDrawable d, Float value) { //NOSONAR
                    d.setProgress(value); //NOSONAR
                }
            };

    private int color = Color.WHITE; //NOSONAR

    private final Path mLeftPauseBar = new Path(); //NOSONAR
    private final Path mRightPauseBar = new Path(); //NOSONAR
    private final Paint mPaint = new Paint(); //NOSONAR
    private final RectF mBounds = new RectF(); //NOSONAR
    private final float mPauseBarWidth; //NOSONAR
    private final float mPauseBarHeight; //NOSONAR
    private final float mPauseBarDistance; //NOSONAR

    private float mWidth; //NOSONAR
    private float mHeight; //NOSONAR

    private float mProgress; //NOSONAR
    boolean mIsPlay; //NOSONAR

    public PlayPauseDrawable(Context context) { //NOSONAR
        final Resources res = context.getResources(); //NOSONAR
        mPaint.setAntiAlias(true); //NOSONAR
        mPaint.setStyle(Paint.Style.FILL); //NOSONAR
        mPaint.setColor(color); //NOSONAR
        mPauseBarWidth = res.getDimensionPixelSize(R.dimen.pause_bar_width); //NOSONAR
        mPauseBarHeight = res.getDimensionPixelSize(R.dimen.pause_bar_height); //NOSONAR
        mPauseBarDistance = res.getDimensionPixelSize(R.dimen.pause_bar_distance); //NOSONAR
    }

    public void setColor(int color) { //NOSONAR
        this.color = color; //NOSONAR
        mPaint.setColor(color); //NOSONAR
        invalidateSelf(); //NOSONAR
    }

    @Override //NOSONAR
    protected void onBoundsChange(Rect bounds) { //NOSONAR
        super.onBoundsChange(bounds); //NOSONAR
        mBounds.set(bounds); //NOSONAR
        mWidth = mBounds.width(); //NOSONAR
        mHeight = mBounds.height(); //NOSONAR
    }

    @Override //NOSONAR
    public void draw(@NonNull Canvas canvas) { //NOSONAR
        mLeftPauseBar.rewind(); //NOSONAR
        mRightPauseBar.rewind(); //NOSONAR

        // The current distance between the two pause bars.
        final float barDist = lerp(mPauseBarDistance, -1, mProgress); //NOSONAR
        // The current width of each pause bar.
        final float barWidth = lerp(mPauseBarWidth, mPauseBarHeight / 2f, mProgress); //NOSONAR
        // The current position of the left pause bar's top left coordinate.
        final float firstBarTopLeft = lerp(0, barWidth, mProgress); //NOSONAR
        // The current position of the right pause bar's top right coordinate.
        final float secondBarTopRight = lerp(2 * barWidth + barDist, barWidth + barDist, mProgress); //NOSONAR
        // The new 'height' of the pause bar (which translates to width of triangle)
        final float pauseBarHeight = lerp(mPauseBarHeight, mPauseBarHeight - ResourceUtils.toPixels(2.5f), mProgress); //NOSONAR

        // Draw the left pause bar. The left pause bar transforms into the
        // top half of the play button triangle by animating the position of the
        // rectangle's top left coordinate and expanding its bottom width.
        mLeftPauseBar.moveTo(0, 0); //NOSONAR
        mLeftPauseBar.lineTo(firstBarTopLeft, -pauseBarHeight); //NOSONAR
        mLeftPauseBar.lineTo(barWidth, -pauseBarHeight); //NOSONAR
        mLeftPauseBar.lineTo(barWidth, 0); //NOSONAR
        mLeftPauseBar.close(); //NOSONAR

        // Draw the right pause bar. The right pause bar transforms into the
        // bottom half of the play button triangle by animating the position of the
        // rectangle's top right coordinate and expanding its bottom width.
        mRightPauseBar.moveTo(barWidth + barDist, 0); //NOSONAR
        mRightPauseBar.lineTo(barWidth + barDist, -pauseBarHeight); //NOSONAR
        mRightPauseBar.lineTo(secondBarTopRight, -pauseBarHeight); //NOSONAR
        mRightPauseBar.lineTo(2 * barWidth + barDist, 0); //NOSONAR
        mRightPauseBar.close(); //NOSONAR

        canvas.save(); //NOSONAR

        // Translate the play button a tiny bit to the right so it looks more centered.
        canvas.translate(lerp(0, ResourceUtils.toPixels(4), mProgress), 0); //NOSONAR

        // (1) Pause --> Play: rotate 0 to 90 degrees clockwise.
        // (2) Play --> Pause: rotate 90 to 180 degrees clockwise.
        final float rotationProgress = mIsPlay ? mProgress : 1 - mProgress; //NOSONAR
        final float startingRotation = mIsPlay ? 0 : 90; //NOSONAR
        canvas.rotate(lerp(startingRotation, startingRotation + 90, rotationProgress), mWidth / 2f, mHeight / 2f); //NOSONAR

        // Position the pause/play button in the center of the drawable's bounds.
        canvas.translate(mWidth / 2f - ((2 * barWidth + barDist) / 2f), mHeight / 2f + (mPauseBarHeight / 2f)); //NOSONAR

        // Draw the two bars that form the animated pause/play button.
        canvas.drawPath(mLeftPauseBar, mPaint); //NOSONAR
        canvas.drawPath(mRightPauseBar, mPaint); //NOSONAR

        canvas.restore(); //NOSONAR
    }

    public Animator getPausePlayAnimator() { //NOSONAR
        final Animator anim = ObjectAnimator.ofFloat(this, PROGRESS, mIsPlay ? 1 : 0, mIsPlay ? 0 : 1); //NOSONAR
        anim.addListener(new AnimatorListenerAdapter() { //NOSONAR

            @Override //NOSONAR
            public void onAnimationStart(Animator animation) { //NOSONAR
                mIsPlay = !mIsPlay; //NOSONAR
            }
        });
        return anim; //NOSONAR
    }

    public boolean isPlay() { //NOSONAR
        return mIsPlay; //NOSONAR
    }

    void setProgress(float progress) { //NOSONAR
        mProgress = progress; //NOSONAR
        invalidateSelf(); //NOSONAR
    }

    float getProgress() { //NOSONAR
        return mProgress; //NOSONAR
    }

    @Override //NOSONAR
    public void setAlpha(int alpha) { //NOSONAR
        mPaint.setAlpha(alpha); //NOSONAR
        invalidateSelf(); //NOSONAR
    }

    @Override //NOSONAR
    public void setColorFilter(ColorFilter cf) { //NOSONAR
        mPaint.setColorFilter(cf); //NOSONAR
        invalidateSelf(); //NOSONAR
    }

    @Override //NOSONAR
    public int getOpacity() { //NOSONAR
        return PixelFormat.TRANSLUCENT; //NOSONAR
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    private static float lerp(float a, float b, float t) { //NOSONAR
        return a + (b - a) * t; //NOSONAR
    }
}
