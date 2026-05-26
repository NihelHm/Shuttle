package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.BgIconColorState;
import com.afollestad.aesthetic.Rx;
import com.simplecity.amp_library.R;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LockActionBarView extends FrameLayout { //NOSONAR

    private static final String TAG = "LockActionBarView"; //NOSONAR

    @BindView(R.id.imageView) //NOSONAR
    ImageView imageView; //NOSONAR

    private boolean locked = false; //NOSONAR

    private AnimatedVectorDrawableCompat toLockedAnim; //NOSONAR
    private AnimatedVectorDrawableCompat toUnlockedAnim; //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    public LockActionBarView(@NonNull Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    @Override //NOSONAR
    protected void onFinishInflate() { //NOSONAR
        super.onFinishInflate(); //NOSONAR

        ButterKnife.bind(this); //NOSONAR

        toLockedAnim = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.lock_anim); //NOSONAR
        toUnlockedAnim = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.unlock_anim); //NOSONAR
    }

    public void setLocked(boolean locked, boolean animate) { //NOSONAR
        if (animate) { //NOSONAR
            AnimatedVectorDrawableCompat currentDrawable = locked ? toLockedAnim : toUnlockedAnim; //NOSONAR
            imageView.setImageDrawable(currentDrawable); //NOSONAR
            currentDrawable.start(); //NOSONAR
        } else { //NOSONAR
            AnimatedVectorDrawableCompat currentDrawable = locked ? toUnlockedAnim : toLockedAnim; //NOSONAR
            imageView.setImageDrawable(currentDrawable); //NOSONAR
        }

        TooltipCompat.setTooltipText(this, getResources().getString(locked ? R.string.menu_queue_swipe_enable : R.string.menu_queue_swipe_disable)); //NOSONAR

        this.locked = locked; //NOSONAR
    }

    public boolean isLocked() { //NOSONAR
        return locked; //NOSONAR
    }

    public void toggle() { //NOSONAR
        setLocked(!locked, true); //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            return; //NOSONAR
        }

        if (!":aesthetic_ignore".equals(getTag())) { //NOSONAR
            aestheticDisposable = Observable.combineLatest( //NOSONAR
                    Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
                    Aesthetic.get(getContext()).colorIconTitle(null), //NOSONAR
                    BgIconColorState.creator()) //NOSONAR
                    .compose(Rx.distinctToMainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            bgIconColorState -> invalidateColors(bgIconColorState), //NOSONAR
                            onErrorLogAndRethrow()); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        if (aestheticDisposable != null) { //NOSONAR
            aestheticDisposable.dispose(); //NOSONAR
        }
        super.onDetachedFromWindow(); //NOSONAR
    }

    private void invalidateColors(BgIconColorState bgIconColorState) { //NOSONAR
        int normalColor = bgIconColorState.iconTitleColor.activeColor(); //NOSONAR
        DrawableCompat.setTint(toLockedAnim, normalColor); //NOSONAR
        DrawableCompat.setTint(toUnlockedAnim, normalColor); //NOSONAR
    }
}
