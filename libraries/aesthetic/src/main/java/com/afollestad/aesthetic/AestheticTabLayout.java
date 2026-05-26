package com.afollestad.aesthetic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.TintHelper.createTintedDrawable;
import static com.afollestad.aesthetic.Util.adjustAlpha;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticTabLayout extends TabLayout { //NOSONAR

  private static final float UNFOCUSED_ALPHA = 0.5f; //NOSONAR
  private Disposable indicatorModeSubscription; //NOSONAR
  private Disposable bgModeSubscription; //NOSONAR
  private Disposable indicatorColorSubscription; //NOSONAR
  private Disposable bgColorSubscription; //NOSONAR

  public AestheticTabLayout(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticTabLayout(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  }

  public AestheticTabLayout(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  }

  private void setIconsColor(int color) { //NOSONAR
    final ColorStateList sl = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_selected}, new int[] {android.R.attr.state_selected} //NOSONAR
            },
            new int[] {adjustAlpha(color, UNFOCUSED_ALPHA), color}); //NOSONAR
    for (int i = 0; i < getTabCount(); i++) { //NOSONAR
      final TabLayout.Tab tab = getTabAt(i); //NOSONAR
      if (tab != null && tab.getIcon() != null) { //NOSONAR
        tab.setIcon(createTintedDrawable(tab.getIcon(), sl)); //NOSONAR
      }
    }
  }

  @Override //NOSONAR
  public void setBackgroundColor(@ColorInt int color) { //NOSONAR
    super.setBackgroundColor(color); //NOSONAR
    Aesthetic.get(getContext()) //NOSONAR
        .colorIconTitle(Observable.just(color)) //NOSONAR
        .take(1) //NOSONAR
        .subscribe( //NOSONAR
            new Consumer<ActiveInactiveColors>() { //NOSONAR
              @Override //NOSONAR
              public void accept(@NonNull ActiveInactiveColors activeInactiveColors) { //NOSONAR
                setIconsColor(activeInactiveColors.activeColor()); //NOSONAR
                setTabTextColors( //NOSONAR
                    adjustAlpha(activeInactiveColors.inactiveColor(), UNFOCUSED_ALPHA), //NOSONAR
                    activeInactiveColors.activeColor()); //NOSONAR
              }
            });
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR

      /* Begin workaround

      In some certain situations, the time between onAttachedToWindow and the color change observables
      emitting is too much, and the view gets displayed momentarily using the original colors.

      On top of that, Aesthetic seems to strip out or otherwise modify view 'theme' attributes,
      so even if this TabLayout's parent Toolbar is using a 'dark' theme, the text for the TabItems still
      comes out black.

      As a workaround, we setup the colors in a blocking fashion, to ensure the correct colors are applied
      when onAttachedToWindow is called.

      Lastly, for some reason, the 'setTabTextColors' has to be called after a delay, or else it doesn't
      seem to take effect. Hard to tell if that's because Aesthetic isn't producing the right colors immediately,
      or if it's something to do with TabLayout.

      It's really not ideal to have blocking RX calls happening on the main thread, but until a proper solution
      is found, this is the best I can do.

      Note: To reproduce this issue, it seems the TabLayout needs to belong to a nested fragment.
     */
      final Integer primaryColor = Aesthetic.get(getContext()) //NOSONAR
              .colorPrimary() //NOSONAR
              .blockingFirst(); //NOSONAR
      ViewBackgroundAction.create(AestheticTabLayout.this).accept(primaryColor); //NOSONAR

      getHandler().postDelayed(new Runnable() { //NOSONAR
          @Override //NOSONAR
          public void run() { //NOSONAR

              ActiveInactiveColors activeInactiveColors = Aesthetic.get(getContext()) //NOSONAR
                      .colorIconTitle(Observable.just(primaryColor)) //NOSONAR
                      .blockingFirst(); //NOSONAR

              setTabTextColors( //NOSONAR
                      adjustAlpha(activeInactiveColors.inactiveColor(), UNFOCUSED_ALPHA), //NOSONAR
                      activeInactiveColors.activeColor()); //NOSONAR
          }
      }, 50); //NOSONAR
      // End workaround

    bgModeSubscription = //NOSONAR
            Aesthetic.get(getContext()) //NOSONAR
                    .tabLayoutBackgroundMode() //NOSONAR
                    .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            new Consumer<Integer>() { //NOSONAR
                              @Override //NOSONAR
                              public void accept(@NonNull Integer mode) { //NOSONAR
                                if (bgColorSubscription != null) { //NOSONAR
                                  bgColorSubscription.dispose(); //NOSONAR
                                }
                    switch (mode) { //NOSONAR
                      case TabLayoutIndicatorMode.PRIMARY: //NOSONAR
                                bgColorSubscription = //NOSONAR
                                        Aesthetic.get(getContext()) //NOSONAR
                                                .colorPrimary() //NOSONAR
                                                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                                                .subscribe( //NOSONAR
                                                        ViewBackgroundAction.create(AestheticTabLayout.this), //NOSONAR
                                                        onErrorLogAndRethrow()); //NOSONAR
                        break; //NOSONAR
                      case TabLayoutIndicatorMode.ACCENT: //NOSONAR
                        bgColorSubscription = //NOSONAR
                            Aesthetic.get(getContext()) //NOSONAR
                                .colorAccent() //NOSONAR
                                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                                .subscribe( //NOSONAR
                                    ViewBackgroundAction.create(AestheticTabLayout.this), //NOSONAR
                                    onErrorLogAndRethrow()); //NOSONAR
                        break; //NOSONAR
                      default: //NOSONAR
                        throw new IllegalStateException("Unimplemented bg mode: " + mode); //NOSONAR
                    }
                              }
                            },
                            onErrorLogAndRethrow()); //NOSONAR

    indicatorModeSubscription = //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .tabLayoutIndicatorMode() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull Integer mode) { //NOSONAR
                    if (indicatorColorSubscription != null) { //NOSONAR
                      indicatorColorSubscription.dispose(); //NOSONAR
                    }
                    switch (mode) { //NOSONAR
                      case TabLayoutIndicatorMode.PRIMARY: //NOSONAR
                        indicatorColorSubscription = //NOSONAR
                            Aesthetic.get(getContext()) //NOSONAR
                                .colorPrimary() //NOSONAR
                                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                                .subscribe( //NOSONAR
                                    new Consumer<Integer>() { //NOSONAR
                                      @Override //NOSONAR
                                      public void accept(@NonNull Integer color) { //NOSONAR
                                        setSelectedTabIndicatorColor(color); //NOSONAR
                                      }
                                    },
                                    onErrorLogAndRethrow()); //NOSONAR
                        break; //NOSONAR
                      case TabLayoutIndicatorMode.ACCENT: //NOSONAR
                        indicatorColorSubscription = //NOSONAR
                            Aesthetic.get(getContext()) //NOSONAR
                                .colorAccent() //NOSONAR
                                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                                .subscribe( //NOSONAR
                                    new Consumer<Integer>() { //NOSONAR
                                      @Override //NOSONAR
                                      public void accept(@NonNull Integer color) { //NOSONAR
                                        setSelectedTabIndicatorColor(color); //NOSONAR
                                      }
                                    },
                                    onErrorLogAndRethrow()); //NOSONAR
                        break; //NOSONAR
                      default: //NOSONAR
                        throw new IllegalStateException("Unimplemented bg mode: " + mode); //NOSONAR
                    }
                  }
                },
                onErrorLogAndRethrow()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    if (bgModeSubscription != null) { //NOSONAR
      bgModeSubscription.dispose(); //NOSONAR
    }
    if (indicatorModeSubscription != null) { //NOSONAR
      indicatorModeSubscription.dispose(); //NOSONAR
    }
    if (bgColorSubscription != null) { //NOSONAR
      bgColorSubscription.dispose(); //NOSONAR
    }
    if (indicatorColorSubscription != null) { //NOSONAR
      indicatorColorSubscription.dispose(); //NOSONAR
    }
    super.onDetachedFromWindow(); //NOSONAR
  }
}
