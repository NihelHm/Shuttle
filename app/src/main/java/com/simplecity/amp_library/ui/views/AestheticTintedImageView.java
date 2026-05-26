package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.support.v7.widget.AppCompatImageView; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticTintedImageView extends AppCompatImageView { //NOSONAR

    Disposable aestheticDisposable; //NOSONAR

    @Nullable //NOSONAR
    private Drawable drawable; //NOSONAR

    public AestheticTintedImageView(Context context) { //NOSONAR
        super(context); //NOSONAR
    } // NOSONAR

    public AestheticTintedImageView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    } // NOSONAR

    public AestheticTintedImageView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setImageDrawable(@Nullable Drawable drawable) { //NOSONAR

        if (drawable != null) { //NOSONAR
            drawable = DrawableCompat.wrap(drawable).mutate(); //NOSONAR
        } // NOSONAR

        this.drawable = drawable; //NOSONAR

        super.setImageDrawable(drawable); //NOSONAR
    } // NOSONAR

    protected Observable<Integer> getColorObservable() { //NOSONAR
        return Aesthetic.get(getContext()).colorAccent(); //NOSONAR
    } // NOSONAR

    void invalidateColors(int color) { //NOSONAR
        if (drawable != null) { //NOSONAR
            DrawableCompat.setTint(drawable, color); //NOSONAR
            setImageDrawable(drawable); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (!isInEditMode()) { //NOSONAR
            aestheticDisposable = getColorObservable().subscribe(this::invalidateColors); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR

        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR
} // NOSONAR
