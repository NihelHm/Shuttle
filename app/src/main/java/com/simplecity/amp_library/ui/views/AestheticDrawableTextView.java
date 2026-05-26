package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.Rx;
import com.afollestad.aesthetic.ViewUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.resolveResId;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticDrawableTextView extends AppCompatTextView { //NOSONAR

    private static String IGNORE_TAG = ":aesthetic_ignore"; //NOSONAR

    private Disposable subscription; //NOSONAR

    private int textColorResId; //NOSONAR

    public AestheticDrawableTextView(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public AestheticDrawableTextView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
        init(context, attrs); //NOSONAR
    }

    public AestheticDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
        init(context, attrs); //NOSONAR
    }

    private void init(Context context, AttributeSet attrs) { //NOSONAR
        if (attrs != null) { //NOSONAR
            textColorResId = resolveResId(context, attrs, android.R.attr.textColor); //NOSONAR
        }
    }

    void invalidateColors(int color) { //NOSONAR

        setTextColor(color); //NOSONAR

        Drawable[] drawables = getCompoundDrawables(); //NOSONAR
        for (Drawable drawable : drawables) { //NOSONAR
            if (drawable == null) { //NOSONAR
                continue; //NOSONAR
            }
            drawable = DrawableCompat.wrap(drawable); //NOSONAR
            DrawableCompat.setTint(drawable, color); //NOSONAR
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]); //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (IGNORE_TAG.equals(getTag())) { //NOSONAR
            invalidateColors(getCurrentTextColor()); //NOSONAR
            return; //NOSONAR
        }

        Observable<Integer> obs = //NOSONAR
                ViewUtil.getObservableForResId( //NOSONAR
                        getContext(), textColorResId, Aesthetic.get(getContext()).textColorSecondary()); //NOSONAR
        //noinspection ConstantConditions
        subscription = //NOSONAR
                obs.compose(Rx.distinctToMainThread()) //NOSONAR
                        .subscribe(this::invalidateColors, onErrorLogAndRethrow()); //NOSONAR
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        if (subscription != null) { //NOSONAR
            subscription.dispose(); //NOSONAR
        }
        super.onDetachedFromWindow(); //NOSONAR
    }
}
