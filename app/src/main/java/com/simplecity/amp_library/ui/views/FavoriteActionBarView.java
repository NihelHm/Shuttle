package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
public class FavoriteActionBarView extends FrameLayout { //NOSONAR

    @BindView(R.id.imageView) //NOSONAR
    ImageView imageView; //NOSONAR

    boolean isFavorite = false; //NOSONAR

    private Drawable normalDrawable; //NOSONAR
    private Drawable selectedDrawable; //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    private int normalColor = Color.WHITE; //NOSONAR
    private int selectedColor = Color.WHITE; //NOSONAR

    public FavoriteActionBarView(@NonNull Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    @Override //NOSONAR
    protected void onFinishInflate() { //NOSONAR
        super.onFinishInflate(); //NOSONAR

        ButterKnife.bind(this); //NOSONAR

        normalDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_24dp).mutate()); //NOSONAR
        selectedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_24dp).mutate()); //NOSONAR

        imageView.setImageDrawable(isFavorite ? selectedDrawable : normalDrawable); //NOSONAR

        setIsFavorite(isFavorite); //NOSONAR
    }

    public void setIsFavorite(boolean isFavorite) { //NOSONAR
        if (isFavorite != this.isFavorite) { //NOSONAR
            this.isFavorite = isFavorite; //NOSONAR
            imageView.setImageDrawable(isFavorite ? selectedDrawable : normalDrawable); //NOSONAR
        }
    }

    public void toggle() { //NOSONAR
        setIsFavorite(!isFavorite); //NOSONAR
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

        this.normalColor = bgIconColorState.iconTitleColor.activeColor(); //NOSONAR
        this.selectedColor = bgIconColorState.iconTitleColor.activeColor(); //NOSONAR

        DrawableCompat.setTint(normalDrawable, normalColor); //NOSONAR
        DrawableCompat.setTint(selectedDrawable, selectedColor); //NOSONAR
    }
}
