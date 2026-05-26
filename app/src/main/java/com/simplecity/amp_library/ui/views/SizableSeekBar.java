package com.simplecity.amp_library.ui.views; // NOSONAR

import android.animation.ValueAnimator; // NOSONAR
import android.content.Context; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.graphics.drawable.ScaleDrawable; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.Gravity; // NOSONAR
import android.view.animation.AccelerateDecelerateInterpolator; // NOSONAR
import android.widget.SeekBar; // NOSONAR
import com.afollestad.aesthetic.AestheticSeekBar; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SizableSeekBar extends AestheticSeekBar { //NOSONAR

    private static final String TAG = "SizableSeekBar"; //NOSONAR

    private static final float maxThumbSizeRatio = 2.0f; //NOSONAR

    float currentThumbSizeRatio = 1.0f; //NOSONAR
    OnSeekBarChangeListener seekListener; //NOSONAR
    private Drawable pendingThumb; //NOSONAR
    Drawable thumb; //NOSONAR
    private ValueAnimator thumbGrowAnimator; //NOSONAR
    private ValueAnimator thumbShrinkAnimator; //NOSONAR
    private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator(); //NOSONAR

    public SizableSeekBar(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    } // NOSONAR

    public SizableSeekBar(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        super.setOnSeekBarChangeListener(internalListener); //NOSONAR

        setThumb(pendingThumb); //NOSONAR
        pendingThumb = null; //NOSONAR
    } // NOSONAR

    void startThumbGrowAnimation() { //NOSONAR
        if (thumbShrinkAnimator != null) { //NOSONAR
            thumbShrinkAnimator.cancel(); //NOSONAR
            thumbShrinkAnimator = null; //NOSONAR
        } // NOSONAR
        thumbGrowAnimator = ValueAnimator.ofFloat(currentThumbSizeRatio, maxThumbSizeRatio); //NOSONAR
        thumbGrowAnimator.setInterpolator(interpolator); //NOSONAR
        thumbGrowAnimator.addUpdateListener(mAnimatorListener); //NOSONAR
        thumbGrowAnimator.setDuration(300); //NOSONAR
        thumbGrowAnimator.start(); //NOSONAR
    } // NOSONAR

    void startThumbShrinkAnimation() { //NOSONAR
        if (thumbGrowAnimator != null) { //NOSONAR
            thumbGrowAnimator.cancel(); //NOSONAR
            thumbGrowAnimator = null; //NOSONAR
        } // NOSONAR
        thumbShrinkAnimator = ValueAnimator.ofFloat(currentThumbSizeRatio, 1.0f); //NOSONAR
        thumbShrinkAnimator.setInterpolator(interpolator); //NOSONAR
        thumbShrinkAnimator.addUpdateListener(mAnimatorListener); //NOSONAR
        thumbShrinkAnimator.setDuration(300); //NOSONAR
        thumbShrinkAnimator.start(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) { //NOSONAR
        seekListener = listener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setThumb(Drawable thumb) { //NOSONAR
        if (thumb == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (!(thumb instanceof ScaleDrawable)) { //NOSONAR
            thumb = new ScaleDrawable(thumb, Gravity.CENTER, 1.0F, 1.0F); //NOSONAR
        } // NOSONAR

        this.thumb = thumb; //NOSONAR
        int level = (int) (10000F * (1.0F / maxThumbSizeRatio)); //NOSONAR
        this.thumb.setLevel(level); //NOSONAR
        super.setThumb(this.thumb); //NOSONAR
    } // NOSONAR

    public Drawable getThumb() { //NOSONAR
        return thumb; //NOSONAR
    } // NOSONAR

    private ValueAnimator.AnimatorUpdateListener mAnimatorListener = new ValueAnimator.AnimatorUpdateListener() { //NOSONAR
        @Override //NOSONAR
        public void onAnimationUpdate(ValueAnimator valueAnimator) { //NOSONAR
            currentThumbSizeRatio = (Float) valueAnimator.getAnimatedValue(); //NOSONAR
            int level = (int) (10000F * (currentThumbSizeRatio / maxThumbSizeRatio)); //NOSONAR
            thumb.setLevel(level); //NOSONAR
            SizableSeekBar.this.invalidate(); //NOSONAR
        } // NOSONAR
    }; // NOSONAR

    private OnSeekBarChangeListener internalListener = new OnSeekBarChangeListener() { //NOSONAR

        @Override //NOSONAR
        public void onStopTrackingTouch(SeekBar seekBar) { //NOSONAR
            startThumbShrinkAnimation(); //NOSONAR
            if (seekListener != null) { //NOSONAR
                seekListener.onStopTrackingTouch(SizableSeekBar.this); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onStartTrackingTouch(SeekBar seekBar) { //NOSONAR
            startThumbGrowAnimation(); //NOSONAR
            if (seekListener != null) { //NOSONAR
                seekListener.onStartTrackingTouch(SizableSeekBar.this); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //NOSONAR
            if (seekListener != null) { //NOSONAR
                seekListener.onProgressChanged(SizableSeekBar.this, progress, fromUser); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    }; // NOSONAR
} // NOSONAR
