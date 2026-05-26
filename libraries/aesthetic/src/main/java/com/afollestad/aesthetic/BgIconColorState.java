package com.afollestad.aesthetic;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP)
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class BgIconColorState {

  @ColorInt public final int bgColor;
  public final ActiveInactiveColors iconTitleColor;

  private BgIconColorState(@ColorInt int bgColor, ActiveInactiveColors iconTitleColor) {
    this.bgColor = bgColor;
    this.iconTitleColor = iconTitleColor;
  }

  static BgIconColorState create(@ColorInt int color, ActiveInactiveColors iconTitleColors) {
    return new BgIconColorState(color, iconTitleColors);
  }

  public static BiFunction<Integer, ActiveInactiveColors, BgIconColorState> creator() {
    return new BiFunction<Integer, ActiveInactiveColors, BgIconColorState>() {
      @Override
      public BgIconColorState apply(
          @NonNull Integer integer, ActiveInactiveColors activeInactiveColors) {
        return BgIconColorState.create(integer, activeInactiveColors);
      }
    };
  }

  @ColorInt
  int bgColor() {
    return bgColor;
  }

  @Nullable
  ActiveInactiveColors iconTitleColor() {
    return iconTitleColor;
  }
}
