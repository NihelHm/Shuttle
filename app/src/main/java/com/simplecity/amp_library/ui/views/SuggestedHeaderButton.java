package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.GradientDrawable; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.ColorIsDarkState; // NOSONAR
import com.afollestad.aesthetic.Rx; // NOSONAR
import com.afollestad.aesthetic.Util; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.utils.ResourceUtils; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SuggestedHeaderButton extends android.support.v7.widget.AppCompatTextView { //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    private GradientDrawable backgroundDrawable; //NOSONAR

    public SuggestedHeaderButton(Context context) { //NOSONAR
        super(context); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    public SuggestedHeaderButton(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    public SuggestedHeaderButton(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    void init() { //NOSONAR
        backgroundDrawable = new GradientDrawable(); //NOSONAR
        backgroundDrawable.setCornerRadius(ResourceUtils.toPixels(2)); //NOSONAR
        setBackground(backgroundDrawable); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        aestheticDisposable = Aesthetic.get(getContext()).colorAccent() //NOSONAR
                .map(colorAccent -> new ColorIsDarkState(colorAccent, !Util.isColorLight(colorAccent))) //NOSONAR
                .compose(Rx.distinctToMainThread()) //NOSONAR
                .subscribe(colorIsDarkState -> { //NOSONAR
                    backgroundDrawable.setColor(colorIsDarkState.color()); //NOSONAR
                    setTextColor(colorIsDarkState.isDark() ? Color.WHITE : Color.BLACK); //NOSONAR
                }); // NOSONAR

        if (isInEditMode()) { //NOSONAR
            backgroundDrawable.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR
} // NOSONAR
