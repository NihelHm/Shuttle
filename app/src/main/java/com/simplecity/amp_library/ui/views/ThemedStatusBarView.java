package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.Rx; // NOSONAR
import com.afollestad.aesthetic.ViewBackgroundAction; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ThemedStatusBarView extends StatusBarView { //NOSONAR

    private Disposable bgSubscription; //NOSONAR

    public ThemedStatusBarView(Context context) { //NOSONAR
        super(context); //NOSONAR
    } // NOSONAR

    public ThemedStatusBarView(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    } // NOSONAR

    public ThemedStatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        // Need to invalidate the colors as early as possible. When subscribing to the continuous observable // NOSONAR
        // below (subscription = ...), we're using distinctToMainThread(), which introduces a slight delay. During // NOSONAR
        // this delay, we see the original colors, which are then swapped once the emission is consumed. // NOSONAR
        // So, we'll just do a take(1), and since we're calling from the main thread, we don't need to worry // NOSONAR
        // about distinctToMainThread() for this call. This prevents the 'flickering' of colors. // NOSONAR

        Aesthetic.get(getContext()) //NOSONAR
                .colorStatusBar() //NOSONAR
                .take(1) //NOSONAR
                .subscribe( //NOSONAR
                        ViewBackgroundAction.create(this), onErrorLogAndRethrow() //NOSONAR
                ); // NOSONAR

        bgSubscription = Aesthetic.get(getContext()).colorStatusBar() //NOSONAR
                .compose(Rx.distinctToMainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        ViewBackgroundAction.create(this), onErrorLogAndRethrow() //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        bgSubscription.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR
} // NOSONAR
