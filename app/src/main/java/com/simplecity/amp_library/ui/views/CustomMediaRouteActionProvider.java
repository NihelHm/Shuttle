package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.v7.app.MediaRouteActionProvider; // NOSONAR
import android.support.v7.app.MediaRouteButton; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.ActiveInactiveColors; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.Rx; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.annotations.Nullable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomMediaRouteActionProvider extends MediaRouteActionProvider { //NOSONAR

    /** // NOSONAR
     * Creates the action provider. // NOSONAR
     * // NOSONAR
     * @param context The context. // NOSONAR
     */ // NOSONAR
    public CustomMediaRouteActionProvider(Context context) { //NOSONAR
        super(context); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public MediaRouteButton onCreateMediaRouteButton() { //NOSONAR
        return new CustomMediaRouteButton(getContext()); //NOSONAR
    } // NOSONAR

    public static class CustomMediaRouteButton extends MediaRouteButton { //NOSONAR

        private Disposable subscription; //NOSONAR

        @Nullable //NOSONAR
        Drawable drawable; //NOSONAR

        public CustomMediaRouteButton(Context context) { //NOSONAR
            super(context); //NOSONAR
        } // NOSONAR

        public CustomMediaRouteButton(Context context, AttributeSet attrs) { //NOSONAR
            super(context, attrs); //NOSONAR
        } // NOSONAR

        public CustomMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
            super(context, attrs, defStyleAttr); //NOSONAR
        } // NOSONAR

        private void invalidateColors(@NonNull ActiveInactiveColors colors, Drawable icon) { //NOSONAR
            if (icon != null) { //NOSONAR
                drawable.setTintList(colors.toEnabledSl()); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void setRemoteIndicatorDrawable(Drawable d) { //NOSONAR
            super.setRemoteIndicatorDrawable(d); //NOSONAR

            this.drawable = d; //NOSONAR

            Aesthetic.get(getContext()) //NOSONAR
                    .colorIconTitle(null) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .take(1) //NOSONAR
                    .subscribe( //NOSONAR
                            colors -> invalidateColors(colors, drawable), //NOSONAR
                            onErrorLogAndRethrow()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onAttachedToWindow() { //NOSONAR
            super.onAttachedToWindow(); //NOSONAR
            subscription = //NOSONAR
                    Aesthetic.get(getContext()) //NOSONAR
                            .colorIconTitle(null) //NOSONAR
                            .compose(Rx.distinctToMainThread()) //NOSONAR
                            .subscribe( //NOSONAR
                                    colors -> invalidateColors(colors, drawable), //NOSONAR
                                    onErrorLogAndRethrow()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onDetachedFromWindow() { //NOSONAR
            subscription.dispose(); //NOSONAR
            super.onDetachedFromWindow(); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
