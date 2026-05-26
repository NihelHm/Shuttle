package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticCheckBox;
import com.afollestad.aesthetic.ColorIsDarkState;
import com.afollestad.aesthetic.Rx;
import com.afollestad.aesthetic.ViewTextColorAction;
import com.afollestad.aesthetic.ViewUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

/**
 * A Custom AestheticCheckbox which sets its text color to black.
 * (This was surprisingly difficult to achieve)
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomCheckbox extends AestheticCheckBox { //NOSONAR

    private CompositeDisposable subscriptions; //NOSONAR

    public CustomCheckbox(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public CustomCheckbox(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public CustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    @Override //NOSONAR
    protected void invalidateColors(ColorIsDarkState state) { //NOSONAR
        super.invalidateColors(state); //NOSONAR

        setTextColor(Color.BLACK); //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        subscriptions = new CompositeDisposable(); //NOSONAR
        //noinspection ConstantConditions
        subscriptions.add( //NOSONAR
                Observable.combineLatest( //NOSONAR
                        ViewUtil.getObservableForResId( //NOSONAR
                                getContext(), backgroundResId, Aesthetic.get(getContext()).colorAccent()), //NOSONAR
                        Aesthetic.get(getContext()).isDark(), //NOSONAR
                        ColorIsDarkState.creator()) //NOSONAR
                        .compose(Rx.<ColorIsDarkState>distinctToMainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                                colorIsDarkState -> invalidateColors(colorIsDarkState), //NOSONAR
                                onErrorLogAndRethrow())); //NOSONAR

        ViewTextColorAction.create(this).accept(Color.BLACK); //NOSONAR
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        subscriptions.clear(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    }
}
