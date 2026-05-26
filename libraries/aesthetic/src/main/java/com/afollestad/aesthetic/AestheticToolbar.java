package com.afollestad.aesthetic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.TintHelper.createTintedDrawable;
import static com.afollestad.aesthetic.Util.setOverflowButtonColor;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticToolbar extends Toolbar { //NOSONAR

  private BgIconColorState lastState; //NOSONAR
  private Disposable subscription; //NOSONAR
  private PublishSubject<Integer> onColorUpdated; //NOSONAR

  private boolean transparentBackground = false; //NOSONAR

  public AestheticToolbar(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticToolbar(Context context, @Nullable AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR

    init(context, attrs); //NOSONAR
  }

  public AestheticToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR

    init(context, attrs); //NOSONAR
  }

  private void init(Context context, @Nullable AttributeSet attrs){ //NOSONAR
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AestheticToolbar); //NOSONAR
    transparentBackground = a.getBoolean(R.styleable.AestheticToolbar_transparentBackground, false); //NOSONAR
    a.recycle(); //NOSONAR
  }

  private void invalidateColors(BgIconColorState state) { //NOSONAR
    lastState = state; //NOSONAR
    if (!transparentBackground) { //NOSONAR
      setBackgroundColor(state.bgColor()); //NOSONAR
    }
    setTitleTextColor(state.iconTitleColor().activeColor()); //NOSONAR
    setSubtitleTextColor(state.iconTitleColor().activeColor()); //NOSONAR
    setOverflowButtonColor(this, state.iconTitleColor().activeColor()); //NOSONAR
    if (getNavigationIcon() != null) { //NOSONAR
      setNavigationIcon(getNavigationIcon()); //NOSONAR
    }
    onColorUpdated.onNext(state.bgColor()); //NOSONAR
    ViewUtil.tintToolbarMenu(this, getMenu(), state.iconTitleColor()); //NOSONAR
  }

  public Observable<Integer> colorUpdated() { //NOSONAR
    return onColorUpdated; //NOSONAR
  }

  @Override //NOSONAR
  public void setNavigationIcon(@Nullable Drawable icon) { //NOSONAR
    if (lastState == null) { //NOSONAR
      super.setNavigationIcon(icon); //NOSONAR
      return; //NOSONAR
    }
    super.setNavigationIcon(createTintedDrawable(icon, lastState.iconTitleColor().toEnabledSl())); //NOSONAR
  }

  public void setNavigationIcon(@Nullable Drawable icon, @ColorInt int color) { //NOSONAR
    if (lastState == null) { //NOSONAR
      super.setNavigationIcon(icon); //NOSONAR
      return; //NOSONAR
    }
    super.setNavigationIcon(createTintedDrawable(icon, color)); //NOSONAR
  }

  public void setTransparentBackground(boolean transparentBackground) { //NOSONAR
    this.transparentBackground = transparentBackground; //NOSONAR
    setBackgroundColor(Color.TRANSPARENT); //NOSONAR
  }

  @SuppressWarnings("ConstantConditions") //NOSONAR
  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    onColorUpdated = PublishSubject.create(); //NOSONAR

    // Need to invalidate the colors as early as possible. When subscribing to the continuous observable
    // below (subscription = ...), we're using distinctToMainThread(), which introduces a slight delay. During
    // this delay, we see the original colors, which are then swapped once the emission is consumed.
    // So, we'll just do a take(1), and since we're calling from the main thread, we don't need to worry
    // about distinctToMainThread() for this call. This prevents the 'flickering' of colors.

    Observable.combineLatest( //NOSONAR
            Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
            Aesthetic.get(getContext()).colorIconTitle(null), //NOSONAR
            BgIconColorState.creator()) //NOSONAR
            .take(1) //NOSONAR
            .subscribe(new Consumer<BgIconColorState>() { //NOSONAR
              @Override //NOSONAR
              public void accept(BgIconColorState bgIconColorState) throws Exception { //NOSONAR
                invalidateColors(bgIconColorState); //NOSONAR
              }
            });

    subscription = //NOSONAR
            Observable.combineLatest( //NOSONAR
                    Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
                    Aesthetic.get(getContext()).colorIconTitle(null), //NOSONAR
                    BgIconColorState.creator()) //NOSONAR
                    .compose(Rx.<BgIconColorState>distinctToMainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            new Consumer<BgIconColorState>() { //NOSONAR
                              @Override //NOSONAR
                              public void accept(@NonNull BgIconColorState bgIconColorState) { //NOSONAR
                                invalidateColors(bgIconColorState); //NOSONAR
                              }
                            },
                            onErrorLogAndRethrow()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    lastState = null; //NOSONAR
    onColorUpdated = null; //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }
}
