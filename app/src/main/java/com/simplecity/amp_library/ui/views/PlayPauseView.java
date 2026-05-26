package com.simplecity.amp_library.ui.views; // NOSONAR

import android.animation.Animator; // NOSONAR
import android.animation.AnimatorListenerAdapter; // NOSONAR
import android.annotation.TargetApi; // NOSONAR
import android.content.Context; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Outline; // NOSONAR
import android.graphics.Paint; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Build; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewOutlineProvider; // NOSONAR
import android.view.animation.DecelerateInterpolator; // NOSONAR
import android.widget.FrameLayout; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function0; // NOSONAR

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
    } // NOSONAR

    @Override //NOSONAR
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
        final int size = Math.min(getMeasuredWidth(), getMeasuredHeight()); //NOSONAR
        setMeasuredDimension(size, size); //NOSONAR
    } // NOSONAR

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
                } // NOSONAR
            }); // NOSONAR
            setClipToOutline(true); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void setColor(int color) { //NOSONAR
        backgroundColor = color; //NOSONAR
        invalidate(); //NOSONAR
    } // NOSONAR

    private int getColor() { //NOSONAR
        return backgroundColor; //NOSONAR
    } // NOSONAR

    public void setDrawableColor(int color) { //NOSONAR
        drawable.setColor(color); //NOSONAR
        invalidate(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected boolean verifyDrawable(@NonNull Drawable who) { //NOSONAR
        return who == drawable || super.verifyDrawable(who); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDraw(Canvas canvas) { //NOSONAR
        super.onDraw(canvas); //NOSONAR
        paint.setColor(backgroundColor); //NOSONAR
        final float radius = Math.min(width, height) / 2f; //NOSONAR
        canvas.drawCircle(width / 2f, height / 2f, radius, paint); //NOSONAR
        drawable.draw(canvas); //NOSONAR
    } // NOSONAR

    public void update() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public void toggle(@Nullable Function0<Unit> completion) { //NOSONAR
        if (animator != null) { //NOSONAR
            animator.cancel(); //NOSONAR
        } // NOSONAR

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
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR

    public boolean isPlay() { //NOSONAR
        return drawable != null && drawable.isPlay(); //NOSONAR
    } // NOSONAR
} // NOSONAR
