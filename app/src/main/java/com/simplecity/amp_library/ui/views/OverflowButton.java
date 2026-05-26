package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.LightDarkColorState; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class OverflowButton extends NonScrollImageButton { //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Drawable drawable; //NOSONAR

    private boolean dark = false; //NOSONAR

    public OverflowButton(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OverflowButton, 0, 0); //NOSONAR
        if (typedArray.hasValue(R.styleable.OverflowButton_isDark)) { //NOSONAR
            dark = typedArray.getBoolean(R.styleable.OverflowButton_isDark, false); //NOSONAR
        } // NOSONAR

        drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_overflow_20dp)).mutate(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            setImageDrawable(drawable); //NOSONAR
        } else { //NOSONAR
            aestheticDisposable = Observable.combineLatest( //NOSONAR
                    Aesthetic.get(getContext()).textColorSecondary(), //NOSONAR
                    Observable.just(Color.WHITE), //NOSONAR
                    Observable.just(dark), //NOSONAR
                    LightDarkColorState.creator() //NOSONAR
            ).subscribe(lightDarkColorState -> { //NOSONAR
                DrawableCompat.setTint(drawable, lightDarkColorState.color()); //NOSONAR
                setImageDrawable(drawable); //NOSONAR
            }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR

        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR
} // NOSONAR
