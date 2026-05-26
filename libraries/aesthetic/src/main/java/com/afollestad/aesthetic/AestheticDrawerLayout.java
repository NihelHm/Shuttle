package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.widget.DrawerLayout; // NOSONAR
import android.support.v7.app.ActionBarDrawerToggle; // NOSONAR
import android.support.v7.graphics.drawable.DrawerArrowDrawable; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticDrawerLayout extends DrawerLayout { //NOSONAR

  private ActiveInactiveColors lastState; //NOSONAR
  private DrawerArrowDrawable arrowDrawable; //NOSONAR
  private Disposable subscription; //NOSONAR

  public AestheticDrawerLayout(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticDrawerLayout(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticDrawerLayout(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
    super(context, attrs, defStyle); //NOSONAR
  } // NOSONAR

  private void invalidateColor(ActiveInactiveColors colors) { //NOSONAR
    if (colors == null) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    this.lastState = colors; //NOSONAR
    if (this.arrowDrawable != null) { //NOSONAR
      this.arrowDrawable.setColor(lastState.activeColor()); //NOSONAR
    } // NOSONAR
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
                  public void accept( //NOSONAR
                      @io.reactivex.annotations.NonNull ActiveInactiveColors colors) { //NOSONAR
                    invalidateColor(colors); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void addDrawerListener(@NonNull DrawerListener listener) { //NOSONAR
    super.addDrawerListener(listener); //NOSONAR
    if (listener instanceof ActionBarDrawerToggle) { //NOSONAR
      this.arrowDrawable = ((ActionBarDrawerToggle) listener).getDrawerArrowDrawable(); //NOSONAR
    } // NOSONAR
    invalidateColor(lastState); //NOSONAR
  } // NOSONAR

  @SuppressWarnings("deprecation") //NOSONAR
  @Override //NOSONAR
  public void setDrawerListener(DrawerListener listener) { //NOSONAR
    super.setDrawerListener(listener); //NOSONAR
    if (listener instanceof ActionBarDrawerToggle) { //NOSONAR
      this.arrowDrawable = ((ActionBarDrawerToggle) listener).getDrawerArrowDrawable(); //NOSONAR
    } // NOSONAR
    invalidateColor(lastState); //NOSONAR
  } // NOSONAR
} // NOSONAR
