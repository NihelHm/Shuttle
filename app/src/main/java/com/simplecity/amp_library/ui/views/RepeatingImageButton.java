package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * A button that will repeatedly call a 'listener' method
 * as long as the button is pressed.
 */
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
    }

    public RepeatingImageButton(Context context, AttributeSet attrs) { //NOSONAR
        this(context, attrs, android.R.attr.imageButtonStyle); //NOSONAR
    }

    public RepeatingImageButton(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR
        setFocusable(true); //NOSONAR
        setLongClickable(true); //NOSONAR

        drawable = DrawableCompat.wrap(getDrawable().mutate()); //NOSONAR
        DrawableCompat.setTint(drawable, normalColor); //NOSONAR
        setImageDrawable(drawable); //NOSONAR
    }

    /**
     * Sets the listener to be called while the button is pressed and
     * the interval in milliseconds with which it will be called.
     *
     * @param l The listener that will be called
     */
    public void setRepeatListener(RepeatListener l) { //NOSONAR
        listener = l; //NOSONAR
    }

    @Override //NOSONAR
    public boolean performLongClick() { //NOSONAR
        startTime = SystemClock.elapsedRealtime(); //NOSONAR
        repeatCount = 0; //NOSONAR
        post(mRepeater); //NOSONAR
        return true; //NOSONAR
    }

    @Override //NOSONAR
    public boolean onTouchEvent(MotionEvent event) { //NOSONAR
        if (event.getAction() == MotionEvent.ACTION_UP) { //NOSONAR
            // remove the repeater, but call the hook one more time
            removeCallbacks(mRepeater); //NOSONAR
            if (startTime != 0) { //NOSONAR
                doRepeat(true); //NOSONAR
                startTime = 0; //NOSONAR
            }
        }
        return super.onTouchEvent(event); //NOSONAR
    }

    @Override //NOSONAR
    public boolean onKeyDown(int keyCode, KeyEvent event) { //NOSONAR
        switch (keyCode) { //NOSONAR
            case KeyEvent.KEYCODE_DPAD_CENTER: //NOSONAR
            case KeyEvent.KEYCODE_ENTER: //NOSONAR
                // need to call super to make long press work, but return
                // true so that the application doesn't get the down event.
                super.onKeyDown(keyCode, event); //NOSONAR
                return true; //NOSONAR
        }
        return super.onKeyDown(keyCode, event); //NOSONAR
    }

    @Override //NOSONAR
    public boolean onKeyUp(int keyCode, KeyEvent event) { //NOSONAR
        switch (keyCode) { //NOSONAR
            case KeyEvent.KEYCODE_DPAD_CENTER: //NOSONAR
            case KeyEvent.KEYCODE_ENTER: //NOSONAR
                // remove the repeater, but call the hook one more time
                removeCallbacks(mRepeater); //NOSONAR
                if (startTime != 0) { //NOSONAR
                    doRepeat(true); //NOSONAR
                    startTime = 0; //NOSONAR
                }
        }
        return super.onKeyUp(keyCode, event); //NOSONAR
    }

    private Runnable mRepeater = new Runnable() { //NOSONAR
        public void run() { //NOSONAR
            doRepeat(false); //NOSONAR
            if (isPressed()) { //NOSONAR
                postDelayed(this, interval); //NOSONAR
            }
        }
    };

    void doRepeat(boolean last) { //NOSONAR
        long now = SystemClock.elapsedRealtime(); //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onRepeat(this, now - startTime, last ? -1 : repeatCount++); //NOSONAR
        }
    }

    public interface RepeatListener { //NOSONAR
        /**
         * This method will be called repeatedly at roughly the interval
         * specified in setRepeatListener(), for as long as the button
         * is pressed.
         *
         * @param v The button as a View.
         * @param duration The number of milliseconds the button has been pressed so far.
         * @param repeatCount The number of previous calls in this sequence.
         * If this is going to be the last call in this sequence (i.e. the user
         * just stopped pressing the button), the value will be -1.
         */
        void onRepeat(View v, long duration, int repeatCount); //NOSONAR
    }

    public void invalidateColors(int normal) { //NOSONAR

        this.normalColor = normal; //NOSONAR

        DrawableCompat.setTint(drawable, normal); //NOSONAR
    }
}
