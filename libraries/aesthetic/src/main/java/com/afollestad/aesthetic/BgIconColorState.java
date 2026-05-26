package com.afollestad.aesthetic; // NOSONAR

import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.functions.BiFunction; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class BgIconColorState { //NOSONAR

  @ColorInt public final int bgColor; //NOSONAR
  public final ActiveInactiveColors iconTitleColor; //NOSONAR

  private BgIconColorState(@ColorInt int bgColor, ActiveInactiveColors iconTitleColor) { //NOSONAR
    this.bgColor = bgColor; //NOSONAR
    this.iconTitleColor = iconTitleColor; //NOSONAR
  } // NOSONAR

  static BgIconColorState create(@ColorInt int color, ActiveInactiveColors iconTitleColors) { //NOSONAR
    return new BgIconColorState(color, iconTitleColors); //NOSONAR
  } // NOSONAR

  public static BiFunction<Integer, ActiveInactiveColors, BgIconColorState> creator() { //NOSONAR
    return new BiFunction<Integer, ActiveInactiveColors, BgIconColorState>() { //NOSONAR
      @Override //NOSONAR
      public BgIconColorState apply( //NOSONAR
          @NonNull Integer integer, ActiveInactiveColors activeInactiveColors) { //NOSONAR
        return BgIconColorState.create(integer, activeInactiveColors); //NOSONAR
      } // NOSONAR
    }; // NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  int bgColor() { //NOSONAR
    return bgColor; //NOSONAR
  } // NOSONAR

  @Nullable //NOSONAR
  ActiveInactiveColors iconTitleColor() { //NOSONAR
    return iconTitleColor; //NOSONAR
  } // NOSONAR
} // NOSONAR
