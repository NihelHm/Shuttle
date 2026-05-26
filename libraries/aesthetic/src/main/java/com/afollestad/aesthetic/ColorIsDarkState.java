package com.afollestad.aesthetic;

import android.support.annotation.ColorInt;
import android.support.annotation.RestrictTo;

import io.reactivex.functions.BiFunction;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP)
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class ColorIsDarkState {

  @ColorInt private final int color;
  private final boolean isDark;

  public ColorIsDarkState(@ColorInt int color, boolean isDark) {
    this.color = color;
    this.isDark = isDark;
  }

  static ColorIsDarkState create(@ColorInt int color, boolean isDark) {
    return new ColorIsDarkState(color, isDark);
  }

  public static BiFunction<Integer, Boolean, ColorIsDarkState> creator() {
    return new BiFunction<Integer, Boolean, ColorIsDarkState>() {
      @Override
      public ColorIsDarkState apply(Integer integer, Boolean aBoolean) {
        return ColorIsDarkState.create(integer, aBoolean);
      }
    };
  }

  @ColorInt
  public int color() {
    return color;
  }

  public boolean isDark() {
    return isDark;
  }
}
