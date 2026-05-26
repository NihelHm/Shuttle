package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.ColorIsDarkState;
import com.afollestad.aesthetic.Rx;
import com.afollestad.aesthetic.Util;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.utils.ResourceUtils;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class SuggestedHeaderButton extends android.support.v7.widget.AppCompatTextView {

    private Disposable aestheticDisposable;

    private GradientDrawable backgroundDrawable;

    public SuggestedHeaderButton(Context context) {
        super(context);

        init();
    }

    public SuggestedHeaderButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SuggestedHeaderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    void init() {
        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setCornerRadius(ResourceUtils.toPixels(2));
        setBackground(backgroundDrawable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        aestheticDisposable = Aesthetic.get(getContext()).colorAccent()
                .map(colorAccent -> new ColorIsDarkState(colorAccent, !Util.isColorLight(colorAccent)))
                .compose(Rx.distinctToMainThread())
                .subscribe(colorIsDarkState -> {
                    backgroundDrawable.setColor(colorIsDarkState.color());
                    setTextColor(colorIsDarkState.isDark() ? Color.WHITE : Color.BLACK);
                });

        if (isInEditMode()) {
            backgroundDrawable.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        aestheticDisposable.dispose();
        super.onDetachedFromWindow();
    }
}
