package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.playback.QueueManager;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RepeatButton extends android.support.v7.widget.AppCompatImageButton { //NOSONAR

    @QueueManager.RepeatMode //NOSONAR
    private int repeatMode; //NOSONAR

    @Nullable //NOSONAR
    private Disposable aestheticDisposable; //NOSONAR

    int normalColor = Color.WHITE; //NOSONAR
    int selectedColor = Color.WHITE; //NOSONAR

    @NonNull //NOSONAR
    Drawable offDrawable; //NOSONAR

    @NonNull //NOSONAR
    Drawable oneDrawable; //NOSONAR

    @NonNull //NOSONAR
    Drawable allDrawable; //NOSONAR

    public RepeatButton(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    }

    public RepeatButton(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    }

    public RepeatButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR

        offDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_repeat_24dp_scaled)).mutate(); //NOSONAR
        offDrawable.setAlpha((int) (0.6 * 255)); //NOSONAR
        oneDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_repeat_one_24dp_scaled)).mutate(); //NOSONAR
        allDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_repeat_24dp_scaled)).mutate(); //NOSONAR

        setRepeatMode(QueueManager.RepeatMode.OFF); //NOSONAR
        setImageDrawable(offDrawable); //NOSONAR
    }

    public void setRepeatMode(@QueueManager.RepeatMode int repeatMode) { //NOSONAR

        if (repeatMode != this.repeatMode) { //NOSONAR
            this.repeatMode = repeatMode; //NOSONAR

            invalidateColors(normalColor, selectedColor); //NOSONAR

            switch (repeatMode) { //NOSONAR
                case QueueManager.RepeatMode.ALL: //NOSONAR
                    setContentDescription(getResources().getString(R.string.btn_repeat_all)); //NOSONAR
                    setImageDrawable(allDrawable); //NOSONAR
                    break; //NOSONAR
                case QueueManager.RepeatMode.ONE: //NOSONAR
                    setContentDescription(getResources().getString(R.string.btn_repeat_current)); //NOSONAR
                    setImageDrawable(oneDrawable); //NOSONAR
                    break; //NOSONAR
                case QueueManager.RepeatMode.OFF: //NOSONAR
                    setContentDescription(getResources().getString(R.string.btn_repeat_off)); //NOSONAR
                    setImageDrawable(offDrawable); //NOSONAR
                    break; //NOSONAR
            }
        }
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            return; //NOSONAR
        }

        if (!":aesthetic_ignore".equals(getTag())) { //NOSONAR
            aestheticDisposable = Aesthetic.get(getContext()).colorAccent() //NOSONAR
                    .subscribe(colorAccent -> { //NOSONAR
                        selectedColor = colorAccent; //NOSONAR
                        invalidateColors(Color.WHITE, selectedColor); //NOSONAR
                    });
        }
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        if (aestheticDisposable != null) { //NOSONAR
            aestheticDisposable.dispose(); //NOSONAR
        }
        super.onDetachedFromWindow(); //NOSONAR
    }

    public void invalidateColors(int normal, int selected) { //NOSONAR

        this.normalColor = normal; //NOSONAR
        this.selectedColor = selected; //NOSONAR

        DrawableCompat.setTint(offDrawable, normal); //NOSONAR
        DrawableCompat.setTint(oneDrawable, selected); //NOSONAR
        DrawableCompat.setTint(allDrawable, selected); //NOSONAR
    }
}
