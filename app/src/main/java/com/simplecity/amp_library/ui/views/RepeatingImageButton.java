package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.SystemClock; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.KeyEvent; // NOSONAR
import android.view.MotionEvent; // NOSONAR
import android.view.View; // NOSONAR

/** // NOSONAR
 * A button that will repeatedly call a 'listener' method // NOSONAR
 * as long as the button is pressed. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RepeatingImageButton extends android.support.v7.widget.AppCompatImageButton { //NOSONAR

    private long startTime; //NOSONAR
    private int repeatCount; //NOSONAR
    private RepeatListener listener; //NOSONAR
    long interval = 500; //NOSONAR

    @NonNull //NOSONAR
    Drawable drawable; //NOSONAR

    int normalColor = Color.WHITE; //NOSONAR

    public RepeatingImageButton(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    } // NOSONAR

    public RepeatingImageButton(Context context, AttributeSet attrs) { //NOSONAR
        this(context, attrs, android.R.attr.imageButtonStyle); //NOSONAR
    } // NOSONAR

    public RepeatingImageButton(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR
        setFocusable(true); //NOSONAR
        setLongClickable(true); //NOSONAR

        drawable = DrawableCompat.wrap(getDrawable().mutate()); //NOSONAR
        DrawableCompat.setTint(drawable, normalColor); //NOSONAR
        setImageDrawable(drawable); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the listener to be called while the button is pressed and // NOSONAR
     * the interval in milliseconds with which it will be called. // NOSONAR
     * // NOSONAR
     * @param l The listener that will be called // NOSONAR
     */ // NOSONAR
    public void setRepeatListener(RepeatListener l) { //NOSONAR
        listener = l; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean performLongClick() { //NOSONAR
        startTime = SystemClock.elapsedRealtime(); //NOSONAR
        repeatCount = 0; //NOSONAR
        post(mRepeater); //NOSONAR
        return true; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onTouchEvent(MotionEvent event) { //NOSONAR
        if (event.getAction() == MotionEvent.ACTION_UP) { //NOSONAR
            // remove the repeater, but call the hook one more time // NOSONAR
            removeCallbacks(mRepeater); //NOSONAR
            if (startTime != 0) { //NOSONAR
                doRepeat(true); //NOSONAR
                startTime = 0; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return super.onTouchEvent(event); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onKeyDown(int keyCode, KeyEvent event) { //NOSONAR
        switch (keyCode) { //NOSONAR
            case KeyEvent.KEYCODE_DPAD_CENTER: //NOSONAR
            case KeyEvent.KEYCODE_ENTER: //NOSONAR
                // need to call super to make long press work, but return // NOSONAR
                // true so that the application doesn't get the down event. // NOSONAR
                super.onKeyDown(keyCode, event); //NOSONAR
                return true; //NOSONAR
        } // NOSONAR
        return super.onKeyDown(keyCode, event); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onKeyUp(int keyCode, KeyEvent event) { //NOSONAR
        switch (keyCode) { //NOSONAR
            case KeyEvent.KEYCODE_DPAD_CENTER: //NOSONAR
            case KeyEvent.KEYCODE_ENTER: //NOSONAR
                // remove the repeater, but call the hook one more time // NOSONAR
                removeCallbacks(mRepeater); //NOSONAR
                if (startTime != 0) { //NOSONAR
                    doRepeat(true); //NOSONAR
                    startTime = 0; //NOSONAR
                } // NOSONAR
        } // NOSONAR
        return super.onKeyUp(keyCode, event); //NOSONAR
    } // NOSONAR

    private Runnable mRepeater = new Runnable() { //NOSONAR
        public void run() { //NOSONAR
            doRepeat(false); //NOSONAR
            if (isPressed()) { //NOSONAR
                postDelayed(this, interval); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    }; // NOSONAR

    void doRepeat(boolean last) { //NOSONAR
        long now = SystemClock.elapsedRealtime(); //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onRepeat(this, now - startTime, last ? -1 : repeatCount++); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public interface RepeatListener { //NOSONAR
        /** // NOSONAR
         * This method will be called repeatedly at roughly the interval // NOSONAR
         * specified in setRepeatListener(), for as long as the button // NOSONAR
         * is pressed. // NOSONAR
         * // NOSONAR
         * @param v The button as a View. // NOSONAR
         * @param duration The number of milliseconds the button has been pressed so far. // NOSONAR
         * @param repeatCount The number of previous calls in this sequence. // NOSONAR
         * If this is going to be the last call in this sequence (i.e. the user // NOSONAR
         * just stopped pressing the button), the value will be -1. // NOSONAR
         */ // NOSONAR
        void onRepeat(View v, long duration, int repeatCount); //NOSONAR
    } // NOSONAR

    public void invalidateColors(int normal) { //NOSONAR

        this.normalColor = normal; //NOSONAR

        DrawableCompat.setTint(drawable, normal); //NOSONAR
    } // NOSONAR
} // NOSONAR
