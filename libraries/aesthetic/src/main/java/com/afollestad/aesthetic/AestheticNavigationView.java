package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.ColorStateList; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.ColorDrawable; // NOSONAR
import android.graphics.drawable.StateListDrawable; // NOSONAR
import android.support.design.widget.NavigationView; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("RestrictedApi") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticNavigationView extends NavigationView { //NOSONAR

  private Disposable modeSubscription; //NOSONAR
  private Disposable colorSubscription; //NOSONAR

  public AestheticNavigationView(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticNavigationView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticNavigationView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  } // NOSONAR

  private void invalidateColors(ColorIsDarkState state) { //NOSONAR
    int selectedColor = state.color(); //NOSONAR
    boolean isDark = state.isDark(); //NOSONAR
    int baseColor = isDark ? Color.WHITE : Color.BLACK; //NOSONAR
    int unselectedIconColor = Util.adjustAlpha(baseColor, .54f); //NOSONAR
    int unselectedTextColor = Util.adjustAlpha(baseColor, .87f); //NOSONAR
    int selectedItemBgColor = //NOSONAR
        ContextCompat.getColor( //NOSONAR
            getContext(), //NOSONAR
            isDark //NOSONAR
                ? R.color.ate_navigation_drawer_selected_dark //NOSONAR
                : R.color.ate_navigation_drawer_selected_light); //NOSONAR

    final ColorStateList iconSl = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_checked}, new int[] {android.R.attr.state_checked} //NOSONAR
            }, // NOSONAR
            new int[] {unselectedIconColor, selectedColor}); //NOSONAR
    final ColorStateList textSl = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_checked}, new int[] {android.R.attr.state_checked} //NOSONAR
            }, // NOSONAR
            new int[] {unselectedTextColor, selectedColor}); //NOSONAR
    setItemTextColor(textSl); //NOSONAR
    setItemIconTintList(iconSl); //NOSONAR

    StateListDrawable bgDrawable = new StateListDrawable(); //NOSONAR
    bgDrawable.addState( //NOSONAR
        new int[] {android.R.attr.state_checked}, new ColorDrawable(selectedItemBgColor)); //NOSONAR
    setItemBackground(bgDrawable); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    modeSubscription = //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .navigationViewMode() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull Integer mode) { //NOSONAR
                    switch (mode) { //NOSONAR
                      case NavigationViewMode.SELECTED_PRIMARY: //NOSONAR
                        colorSubscription = //NOSONAR
                            Observable.combineLatest( //NOSONAR
                                    Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
                                    Aesthetic.get(getContext()).isDark(), //NOSONAR
                                    ColorIsDarkState.creator()) //NOSONAR
                                .compose(Rx.<ColorIsDarkState>distinctToMainThread()) //NOSONAR
                                .subscribe( //NOSONAR
                                    new Consumer<ColorIsDarkState>() { //NOSONAR
                                      @Override //NOSONAR
                                      public void accept( //NOSONAR
                                          @NonNull ColorIsDarkState colorIsDarkState) { //NOSONAR
                                        invalidateColors(colorIsDarkState); //NOSONAR
                                      } // NOSONAR
                                    }, // NOSONAR
                                    onErrorLogAndRethrow()); //NOSONAR
                        break; //NOSONAR
                      case NavigationViewMode.SELECTED_ACCENT: //NOSONAR
                        colorSubscription = //NOSONAR
                            Observable.combineLatest( //NOSONAR
                                    Aesthetic.get(getContext()).colorAccent(), //NOSONAR
                                    Aesthetic.get(getContext()).isDark(), //NOSONAR
                                    ColorIsDarkState.creator()) //NOSONAR
                                .compose(Rx.<ColorIsDarkState>distinctToMainThread()) //NOSONAR
                                .subscribe( //NOSONAR
                                    new Consumer<ColorIsDarkState>() { //NOSONAR
                                      @Override //NOSONAR
                                      public void accept( //NOSONAR
                                          @NonNull ColorIsDarkState colorIsDarkState) { //NOSONAR
                                        invalidateColors(colorIsDarkState); //NOSONAR
                                      } // NOSONAR
                                    }, // NOSONAR
                                    onErrorLogAndRethrow()); //NOSONAR
                        break; //NOSONAR
                      default: //NOSONAR
                        throw new IllegalStateException("Unknown nav view mode: " + mode); //NOSONAR
                    } // NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    if (modeSubscription != null) { //NOSONAR
      modeSubscription.dispose(); //NOSONAR
    } // NOSONAR
    if (colorSubscription != null) { //NOSONAR
      colorSubscription.dispose(); //NOSONAR
    } // NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
