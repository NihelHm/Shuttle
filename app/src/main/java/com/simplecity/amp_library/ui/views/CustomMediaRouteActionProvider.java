package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;
import com.afollestad.aesthetic.ActiveInactiveColors;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.Rx;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomMediaRouteActionProvider extends MediaRouteActionProvider { //NOSONAR

    /**
     * Creates the action provider.
     *
     * @param context The context.
     */
    public CustomMediaRouteActionProvider(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    @Override //NOSONAR
    public MediaRouteButton onCreateMediaRouteButton() { //NOSONAR
        return new CustomMediaRouteButton(getContext()); //NOSONAR
    }

    public static class CustomMediaRouteButton extends MediaRouteButton { //NOSONAR

        private Disposable subscription; //NOSONAR

        @Nullable //NOSONAR
        Drawable drawable; //NOSONAR

        public CustomMediaRouteButton(Context context) { //NOSONAR
            super(context); //NOSONAR
        }

        public CustomMediaRouteButton(Context context, AttributeSet attrs) { //NOSONAR
            super(context, attrs); //NOSONAR
        }

        public CustomMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
            super(context, attrs, defStyleAttr); //NOSONAR
        }

        private void invalidateColors(@NonNull ActiveInactiveColors colors, Drawable icon) { //NOSONAR
            if (icon != null) { //NOSONAR
                drawable.setTintList(colors.toEnabledSl()); //NOSONAR
            }
        }

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
        }

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
        }

        @Override //NOSONAR
        public void onDetachedFromWindow() { //NOSONAR
            subscription.dispose(); //NOSONAR
            super.onDetachedFromWindow(); //NOSONAR
        }
    }
}
