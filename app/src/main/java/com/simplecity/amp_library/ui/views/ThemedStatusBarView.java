package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.Rx;
import com.afollestad.aesthetic.ViewBackgroundAction;
import io.reactivex.disposables.Disposable;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ThemedStatusBarView extends StatusBarView { //NOSONAR

    private Disposable bgSubscription; //NOSONAR

    public ThemedStatusBarView(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public ThemedStatusBarView(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public ThemedStatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            return; //NOSONAR
        }

        // Need to invalidate the colors as early as possible. When subscribing to the continuous observable
        // below (subscription = ...), we're using distinctToMainThread(), which introduces a slight delay. During
        // this delay, we see the original colors, which are then swapped once the emission is consumed.
        // So, we'll just do a take(1), and since we're calling from the main thread, we don't need to worry
        // about distinctToMainThread() for this call. This prevents the 'flickering' of colors.

        Aesthetic.get(getContext()) //NOSONAR
                .colorStatusBar() //NOSONAR
                .take(1) //NOSONAR
                .subscribe( //NOSONAR
                        ViewBackgroundAction.create(this), onErrorLogAndRethrow() //NOSONAR
                );

        bgSubscription = Aesthetic.get(getContext()).colorStatusBar() //NOSONAR
                .compose(Rx.distinctToMainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        ViewBackgroundAction.create(this), onErrorLogAndRethrow() //NOSONAR
                );
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        bgSubscription.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    }
}
