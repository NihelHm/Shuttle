package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.widget.FrameLayout; // NOSONAR
import android.widget.ImageView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.BgIconColorState; // NOSONAR
import com.afollestad.aesthetic.Rx; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

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
    } // NOSONAR

    @Override //NOSONAR
    protected void onFinishInflate() { //NOSONAR
        super.onFinishInflate(); //NOSONAR

        ButterKnife.bind(this); //NOSONAR

        normalDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_24dp).mutate()); //NOSONAR
        selectedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_24dp).mutate()); //NOSONAR

        imageView.setImageDrawable(isFavorite ? selectedDrawable : normalDrawable); //NOSONAR

        setIsFavorite(isFavorite); //NOSONAR
    } // NOSONAR

    public void setIsFavorite(boolean isFavorite) { //NOSONAR
        if (isFavorite != this.isFavorite) { //NOSONAR
            this.isFavorite = isFavorite; //NOSONAR
            imageView.setImageDrawable(isFavorite ? selectedDrawable : normalDrawable); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void toggle() { //NOSONAR
        setIsFavorite(!isFavorite); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (isInEditMode()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (!":aesthetic_ignore".equals(getTag())) { //NOSONAR
            aestheticDisposable = Observable.combineLatest( //NOSONAR
                    Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
                    Aesthetic.get(getContext()).colorIconTitle(null), //NOSONAR
                    BgIconColorState.creator()) //NOSONAR
                    .compose(Rx.distinctToMainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            bgIconColorState -> invalidateColors(bgIconColorState), //NOSONAR
                            onErrorLogAndRethrow()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        if (aestheticDisposable != null) { //NOSONAR
            aestheticDisposable.dispose(); //NOSONAR
        } // NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR

    private void invalidateColors(BgIconColorState bgIconColorState) { //NOSONAR

        this.normalColor = bgIconColorState.iconTitleColor.activeColor(); //NOSONAR
        this.selectedColor = bgIconColorState.iconTitleColor.activeColor(); //NOSONAR

        DrawableCompat.setTint(normalDrawable, normalColor); //NOSONAR
        DrawableCompat.setTint(selectedDrawable, selectedColor); //NOSONAR
    } // NOSONAR
} // NOSONAR
