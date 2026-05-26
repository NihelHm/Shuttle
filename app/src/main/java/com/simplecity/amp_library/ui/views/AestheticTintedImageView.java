package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticTintedImageView extends AppCompatImageView { //NOSONAR

    Disposable aestheticDisposable; //NOSONAR

    @Nullable //NOSONAR
    private Drawable drawable; //NOSONAR

    public AestheticTintedImageView(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public AestheticTintedImageView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public AestheticTintedImageView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    @Override //NOSONAR
    public void setImageDrawable(@Nullable Drawable drawable) { //NOSONAR

        if (drawable != null) { //NOSONAR
            drawable = DrawableCompat.wrap(drawable).mutate(); //NOSONAR
        }

        this.drawable = drawable; //NOSONAR

        super.setImageDrawable(drawable); //NOSONAR
    }

    protected Observable<Integer> getColorObservable() { //NOSONAR
        return Aesthetic.get(getContext()).colorAccent(); //NOSONAR
    }

    void invalidateColors(int color) { //NOSONAR
        if (drawable != null) { //NOSONAR
            DrawableCompat.setTint(drawable, color); //NOSONAR
            setImageDrawable(drawable); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (!isInEditMode()) { //NOSONAR
            aestheticDisposable = getColorObservable().subscribe(this::invalidateColors); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR

        super.onDetachedFromWindow(); //NOSONAR
    }
}
