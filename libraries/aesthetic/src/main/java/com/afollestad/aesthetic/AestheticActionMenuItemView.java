package com.afollestad.aesthetic; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.ColorStateList; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.v7.view.menu.ActionMenuItemView; // NOSONAR
import android.util.AttributeSet; // NOSONAR

import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR
import static com.afollestad.aesthetic.TintHelper.createTintedDrawable; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("RestrictedApi") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class AestheticActionMenuItemView extends ActionMenuItemView { //NOSONAR

  private Drawable icon; //NOSONAR
  private Disposable subscription; //NOSONAR

  public AestheticActionMenuItemView(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticActionMenuItemView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticActionMenuItemView(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
    super(context, attrs, defStyle); //NOSONAR
  } // NOSONAR

  private void invalidateColors(@NonNull ActiveInactiveColors colors, Drawable icon) { //NOSONAR
    if (icon != null) { //NOSONAR
      setIcon(icon, colors.toEnabledSl()); //NOSONAR
    } // NOSONAR
    setTextColor(colors.activeColor()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void setIcon(final Drawable icon) { //NOSONAR
    super.setIcon(icon); //NOSONAR

    // We need to retrieve the color again here. // NOSONAR
    // For some reason, without this, a transparent color is used and the icon disappears // NOSONAR
    // when the overflow menu opens. // NOSONAR
    Aesthetic.get(getContext()) //NOSONAR
        .colorIconTitle(null) //NOSONAR
        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
        .take(1) //NOSONAR
        .subscribe( //NOSONAR
            new Consumer<ActiveInactiveColors>() { //NOSONAR
              @Override //NOSONAR
              public void accept(@NonNull ActiveInactiveColors colors) { //NOSONAR
                invalidateColors(colors, icon); //NOSONAR
              } // NOSONAR
            }, // NOSONAR
            onErrorLogAndRethrow()); //NOSONAR
  } // NOSONAR

  public void setIcon(final Drawable icon, ColorStateList colors) { //NOSONAR
    this.icon = icon; //NOSONAR
    super.setIcon(createTintedDrawable(icon, colors)); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subscription = //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .colorIconTitle(null) //NOSONAR
            .compose(Rx.<ActiveInactiveColors>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<ActiveInactiveColors>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull ActiveInactiveColors colors) { //NOSONAR
                    invalidateColors(colors, icon); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
