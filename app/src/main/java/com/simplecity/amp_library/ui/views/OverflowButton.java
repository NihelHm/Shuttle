package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.LightDarkColorState;
import com.simplecity.amp_library.R;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class OverflowButton extends NonScrollImageButton {

    private Disposable aestheticDisposable;

    @SuppressWarnings("java:S1104")

    public Drawable drawable;

    private boolean dark = false;

    public OverflowButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OverflowButton, 0, 0);
        if (typedArray.hasValue(R.styleable.OverflowButton_isDark)) {
            dark = typedArray.getBoolean(R.styleable.OverflowButton_isDark, false);
        }

        drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_overflow_20dp)).mutate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            setImageDrawable(drawable);
        } else {
            aestheticDisposable = Observable.combineLatest(
                    Aesthetic.get(getContext()).textColorSecondary(),
                    Observable.just(Color.WHITE),
                    Observable.just(dark),
                    LightDarkColorState.creator()
            ).subscribe(lightDarkColorState -> {
                DrawableCompat.setTint(drawable, lightDarkColorState.color());
                setImageDrawable(drawable);
            });
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        aestheticDisposable.dispose();

        super.onDetachedFromWindow();
    }
}
