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
public class ShuffleButton extends android.support.v7.widget.AppCompatImageButton { //NOSONAR

    @QueueManager.ShuffleMode //NOSONAR
    private int shuffleMode; //NOSONAR

    @Nullable //NOSONAR
    private Disposable aestheticDisposable; //NOSONAR

    int normalColor = Color.WHITE; //NOSONAR
    int selectedColor = Color.WHITE; //NOSONAR

    @NonNull //NOSONAR
    Drawable shuffleOff; //NOSONAR
    @NonNull //NOSONAR
    Drawable shuffleTracks; //NOSONAR

    public ShuffleButton(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    }

    public ShuffleButton(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    }

    public ShuffleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR

        shuffleOff = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_shuffle_24dp_scaled)).mutate(); //NOSONAR
        shuffleOff.setAlpha((int) (0.6 * 255)); //NOSONAR
        shuffleTracks = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_shuffle_24dp_scaled)).mutate(); //NOSONAR

        setShuffleMode(QueueManager.ShuffleMode.OFF); //NOSONAR
        setImageDrawable(shuffleOff); //NOSONAR
    }

    public void setShuffleMode(@QueueManager.ShuffleMode int shuffleMode) { //NOSONAR
        if (this.shuffleMode != shuffleMode) { //NOSONAR
            this.shuffleMode = shuffleMode; //NOSONAR

            invalidateColors(normalColor, selectedColor); //NOSONAR

            switch (shuffleMode) { //NOSONAR
                case QueueManager.ShuffleMode.OFF: //NOSONAR
                    setContentDescription(getResources().getString(R.string.btn_shuffle_off)); //NOSONAR
                    setImageDrawable(shuffleOff); //NOSONAR
                    break; //NOSONAR
                case QueueManager.ShuffleMode.ON: //NOSONAR
                    setContentDescription(getResources().getString(R.string.btn_shuffle_on)); //NOSONAR
                    setImageDrawable(shuffleTracks); //NOSONAR
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

        DrawableCompat.setTint(shuffleOff, normal); //NOSONAR
        DrawableCompat.setTint(shuffleTracks, selected); //NOSONAR
    }
}
