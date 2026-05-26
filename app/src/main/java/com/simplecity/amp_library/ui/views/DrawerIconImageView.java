package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class DrawerIconImageView extends AppCompatImageView {

    Disposable aestheticDisposable;

    @Nullable
    private Drawable drawable;

    public DrawerIconImageView(Context context) {
        super(context);
    }

    public DrawerIconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerIconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int normalColor = -1;
    private int selectedColor = -1;

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable).mutate();
        }

        this.drawable = drawable;

        super.setImageDrawable(drawable);
    }

    protected Observable<Pair> getColorObservable() {
        return Observable.combineLatest(
                Aesthetic.get(getContext()).textColorPrimary(),
                Aesthetic.get(getContext()).colorAccent(),
                Pair::new);
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);

        invalidateColors(normalColor, selectedColor);
    }

    void invalidateColors(int normalColor, int selectedColor) {

        if (normalColor == -1 || selectedColor == -1) {
            return;
        }

        if (drawable != null) {
            DrawableCompat.setTint(drawable, isActivated() ? selectedColor : normalColor);
            setImageDrawable(drawable);
        }

        this.normalColor = normalColor;
        this.selectedColor = selectedColor;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!isInEditMode()) {
            aestheticDisposable = getColorObservable().subscribe(pair -> invalidateColors((int) pair.first, (int) pair.second));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        aestheticDisposable.dispose();

        super.onDetachedFromWindow();
    }
}
