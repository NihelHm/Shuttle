package com.simplecity.amp_library.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlayPauseView extends FrameLayout { //NOSONAR

    private static final String TAG = "PlayPauseView"; //NOSONAR

    private static final long PLAY_PAUSE_ANIMATION_DURATION = 200; //NOSONAR

    private final PlayPauseDrawable drawable; //NOSONAR
    private final Paint paint = new Paint(); //NOSONAR

    private Animator animator; //NOSONAR
    private int backgroundColor; //NOSONAR
    private int width; //NOSONAR
    private int height; //NOSONAR

    public PlayPauseView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
        setWillNotDraw(false); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR
        paint.setStyle(Paint.Style.FILL); //NOSONAR
        drawable = new PlayPauseDrawable(context); //NOSONAR
        drawable.setCallback(this); //NOSONAR
    }

    @Override //NOSONAR
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
        final int size = Math.min(getMeasuredWidth(), getMeasuredHeight()); //NOSONAR
        setMeasuredDimension(size, size); //NOSONAR
    }

    @Override //NOSONAR
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) { //NOSONAR
        super.onSizeChanged(w, h, oldw, oldh); //NOSONAR
        drawable.setBounds(0, 0, w, h); //NOSONAR
        width = w; //NOSONAR
        height = h; //NOSONAR

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
            setOutlineProvider(new ViewOutlineProvider() { //NOSONAR
                @TargetApi(Build.VERSION_CODES.LOLLIPOP) //NOSONAR
                @Override //NOSONAR
                public void getOutline(View view, Outline outline) { //NOSONAR
                    outline.setOval(0, 0, view.getWidth(), view.getHeight()); //NOSONAR
                }
            });
            setClipToOutline(true); //NOSONAR
        }
    }

    private void setColor(int color) { //NOSONAR
        backgroundColor = color; //NOSONAR
        invalidate(); //NOSONAR
    }

    private int getColor() { //NOSONAR
        return backgroundColor; //NOSONAR
    }

    public void setDrawableColor(int color) { //NOSONAR
        drawable.setColor(color); //NOSONAR
        invalidate(); //NOSONAR
    }

    @Override //NOSONAR
    protected boolean verifyDrawable(@NonNull Drawable who) { //NOSONAR
        return who == drawable || super.verifyDrawable(who); //NOSONAR
    }

    @Override //NOSONAR
    protected void onDraw(Canvas canvas) { //NOSONAR
        super.onDraw(canvas); //NOSONAR
        paint.setColor(backgroundColor); //NOSONAR
        final float radius = Math.min(width, height) / 2f; //NOSONAR
        canvas.drawCircle(width / 2f, height / 2f, radius, paint); //NOSONAR
        drawable.draw(canvas); //NOSONAR
    }

    public void update() { //NOSONAR
        // Intentionally left empty.
    }

    public void toggle(@Nullable Function0<Unit> completion) { //NOSONAR
        if (animator != null) { //NOSONAR
            animator.cancel(); //NOSONAR
        }

        animator = drawable.getPausePlayAnimator(); //NOSONAR
        animator.setInterpolator(new DecelerateInterpolator()); //NOSONAR
        animator.setDuration(PLAY_PAUSE_ANIMATION_DURATION); //NOSONAR
        animator.start(); //NOSONAR
        animator.addListener(new AnimatorListenerAdapter() { //NOSONAR
            @Override //NOSONAR
            public void onAnimationEnd(Animator animation) { //NOSONAR
                super.onAnimationEnd(animation); //NOSONAR
                animation.removeListener(this); //NOSONAR

                if (completion != null) { //NOSONAR
                    completion.invoke(); //NOSONAR
                }
            }
        });
    }

    public boolean isPlay() { //NOSONAR
        return drawable != null && drawable.isPlay(); //NOSONAR
    }
}
