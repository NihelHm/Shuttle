package com.afollestad.aesthetic; // NOSONAR

import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR

import io.reactivex.functions.BiFunction; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ColorIsDarkState { //NOSONAR

  @ColorInt private final int color; //NOSONAR
  private final boolean isDark; //NOSONAR

  public ColorIsDarkState(@ColorInt int color, boolean isDark) { //NOSONAR
    this.color = color; //NOSONAR
    this.isDark = isDark; //NOSONAR
  } // NOSONAR

  static ColorIsDarkState create(@ColorInt int color, boolean isDark) { //NOSONAR
    return new ColorIsDarkState(color, isDark); //NOSONAR
  } // NOSONAR

  public static BiFunction<Integer, Boolean, ColorIsDarkState> creator() { //NOSONAR
    return new BiFunction<Integer, Boolean, ColorIsDarkState>() { //NOSONAR
      @Override //NOSONAR
      public ColorIsDarkState apply(Integer integer, Boolean aBoolean) { //NOSONAR
        return ColorIsDarkState.create(integer, aBoolean); //NOSONAR
      } // NOSONAR
    }; // NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  public int color() { //NOSONAR
    return color; //NOSONAR
  } // NOSONAR

  public boolean isDark() { //NOSONAR
    return isDark; //NOSONAR
  } // NOSONAR
} // NOSONAR
