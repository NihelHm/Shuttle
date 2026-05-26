package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DragHandle extends AestheticTintedImageView { //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    public DragHandle(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    @Override //NOSONAR
    protected Observable<Integer> getColorObservable() { //NOSONAR
        Observable<Integer> obs; //NOSONAR
        if (isActivated()) { //NOSONAR
            obs = Aesthetic.get(getContext()).colorAccent(); //NOSONAR
        } else { //NOSONAR
            obs = Aesthetic.get(getContext()).textColorSecondary(); //NOSONAR
        }
        return obs; //NOSONAR
    }

    @Override //NOSONAR
    public void setActivated(boolean activated) { //NOSONAR
        super.setActivated(activated); //NOSONAR
        if (!isInEditMode()) { //NOSONAR
            getColorObservable() //NOSONAR
                    .take(1) //NOSONAR
                    .subscribe(this::invalidateColors); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR
        if (!isInEditMode()) { //NOSONAR
            aestheticDisposable = getColorObservable() //NOSONAR
                    .subscribe(this::invalidateColors); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    }
}
