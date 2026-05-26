package com.afollestad.aesthetic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticBottomNavigationView extends BottomNavigationView { //NOSONAR

  private Disposable modesSubscription; //NOSONAR
  private CompositeDisposable colorSubscriptions; //NOSONAR
  private int lastTextIconColor; //NOSONAR

  public AestheticBottomNavigationView(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticBottomNavigationView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  }

  public AestheticBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  }

  private void invalidateIconTextColor(int backgroundColor, int selectedColor) { //NOSONAR
    int baseColor = //NOSONAR
        ContextCompat.getColor( //NOSONAR
            getContext(), //NOSONAR
            Util.isColorLight(backgroundColor) ? R.color.ate_icon_light : R.color.ate_icon_dark); //NOSONAR
    int unselectedIconTextColor = Util.adjustAlpha(baseColor, .87f); //NOSONAR
    ColorStateList iconColor = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_checked}, new int[] {android.R.attr.state_checked} //NOSONAR
            },
            new int[] {unselectedIconTextColor, selectedColor}); //NOSONAR
    ColorStateList textColor = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_checked}, new int[] {android.R.attr.state_checked} //NOSONAR
            },
            new int[] {unselectedIconTextColor, selectedColor}); //NOSONAR
    setItemIconTintList(iconColor); //NOSONAR
    setItemTextColor(textColor); //NOSONAR
  }

  @Override //NOSONAR
  public void setBackgroundColor(@ColorInt int color) { //NOSONAR
    super.setBackgroundColor(color); //NOSONAR
    if (lastTextIconColor == Color.TRANSPARENT) { //NOSONAR
      lastTextIconColor = Util.isColorLight(color) ? Color.BLACK : Color.WHITE; //NOSONAR
    }
    invalidateIconTextColor(color, lastTextIconColor); //NOSONAR
  }

  private void onState(State state) { //NOSONAR
    if (colorSubscriptions != null) { //NOSONAR
      colorSubscriptions.clear(); //NOSONAR
    }
    colorSubscriptions = new CompositeDisposable(); //NOSONAR

    switch (state.iconTextMode) { //NOSONAR
      case BottomNavIconTextMode.SELECTED_PRIMARY: //NOSONAR
        colorSubscriptions.add( //NOSONAR
            Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    new Consumer<Integer>() { //NOSONAR
                      @Override //NOSONAR
                      public void accept(@NonNull Integer color) { //NOSONAR
                        lastTextIconColor = color; //NOSONAR
                      }
                    },
                    onErrorLogAndRethrow())); //NOSONAR
        break; //NOSONAR
      case BottomNavIconTextMode.SELECTED_ACCENT: //NOSONAR
        colorSubscriptions.add( //NOSONAR
            Aesthetic.get(getContext()) //NOSONAR
                .colorAccent() //NOSONAR
                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    new Consumer<Integer>() { //NOSONAR
                      @Override //NOSONAR
                      public void accept(@NonNull Integer color) { //NOSONAR
                        lastTextIconColor = color; //NOSONAR
                      }
                    },
                    onErrorLogAndRethrow())); //NOSONAR
        break; //NOSONAR
      case BottomNavIconTextMode.BLACK_WHITE_AUTO: //NOSONAR
        // We will automatically set the icon/text color when the background color is set
        lastTextIconColor = Color.TRANSPARENT; //NOSONAR
        break; //NOSONAR
      default: //NOSONAR
        throw new IllegalStateException("Unknown bottom nav icon/text mode: " + state.iconTextMode); //NOSONAR
    }

    switch (state.bgMode) { //NOSONAR
      case BottomNavBgMode.PRIMARY: //NOSONAR
        colorSubscriptions.add( //NOSONAR
            Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                .subscribe(ViewBackgroundAction.create(this), onErrorLogAndRethrow())); //NOSONAR
        break; //NOSONAR
      case BottomNavBgMode.PRIMARY_DARK: //NOSONAR
        colorSubscriptions.add( //NOSONAR
            Aesthetic.get(getContext()) //NOSONAR
                .colorStatusBar() //NOSONAR
                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                .subscribe(ViewBackgroundAction.create(this), onErrorLogAndRethrow())); //NOSONAR
        break; //NOSONAR
      case BottomNavBgMode.ACCENT: //NOSONAR
        colorSubscriptions.add( //NOSONAR
            Aesthetic.get(getContext()) //NOSONAR
                .colorAccent() //NOSONAR
                .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
                .subscribe(ViewBackgroundAction.create(this), onErrorLogAndRethrow())); //NOSONAR
        break; //NOSONAR
      case BottomNavBgMode.BLACK_WHITE_AUTO: //NOSONAR
        setBackgroundColor( //NOSONAR
            ContextCompat.getColor( //NOSONAR
                getContext(), //NOSONAR
                state.isDark //NOSONAR
                    ? R.color.ate_bottom_nav_default_dark_bg //NOSONAR
                    : R.color.ate_bottom_nav_default_light_bg)); //NOSONAR
        break; //NOSONAR
      default: //NOSONAR
        throw new IllegalStateException("Unknown bottom nav bg mode: " + state.bgMode); //NOSONAR
    }
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    modesSubscription = //NOSONAR
        Observable.combineLatest( //NOSONAR
                Aesthetic.get(getContext()).bottomNavigationBackgroundMode(), //NOSONAR
                Aesthetic.get(getContext()).bottomNavigationIconTextMode(), //NOSONAR
                Aesthetic.get(getContext()).isDark(), //NOSONAR
                State.creator()) //NOSONAR
            .compose(Rx.<State>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<State>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@android.support.annotation.NonNull State state) { //NOSONAR
                    onState(state); //NOSONAR
                  }
                },
                onErrorLogAndRethrow()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    modesSubscription.dispose(); //NOSONAR
    colorSubscriptions.clear(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }

  private static class State { //NOSONAR

    @BottomNavBgMode private final int bgMode; //NOSONAR
    @BottomNavIconTextMode private final int iconTextMode; //NOSONAR
    private final boolean isDark; //NOSONAR

    private State(int bgMode, int iconTextMode, boolean isDark) { //NOSONAR
      this.bgMode = bgMode; //NOSONAR
      this.iconTextMode = iconTextMode; //NOSONAR
      this.isDark = isDark; //NOSONAR
    }

    static State create( //NOSONAR
        @BottomNavBgMode int bgMode, @BottomNavIconTextMode int iconTextMode, boolean isDark) { //NOSONAR
      return new State(bgMode, iconTextMode, isDark); //NOSONAR
    }

    static Function3<Integer, Integer, Boolean, State> creator() { //NOSONAR
      return new Function3<Integer, Integer, Boolean, State>() { //NOSONAR
        @Override //NOSONAR
        public State apply(Integer integer, Integer integer2, Boolean aBoolean) { //NOSONAR
          return State.create(integer, integer2, aBoolean); //NOSONAR
        }
      };
    }
  }
}
