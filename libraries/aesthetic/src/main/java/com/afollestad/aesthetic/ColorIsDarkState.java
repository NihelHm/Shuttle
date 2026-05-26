package com.afollestad.aesthetic;

import android.support.annotation.ColorInt;
import android.support.annotation.RestrictTo;

import io.reactivex.functions.BiFunction;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ColorIsDarkState { //NOSONAR

  @ColorInt private final int color; //NOSONAR
  private final boolean isDark; //NOSONAR

  public ColorIsDarkState(@ColorInt int color, boolean isDark) { //NOSONAR
    this.color = color; //NOSONAR
    this.isDark = isDark; //NOSONAR
  }

  static ColorIsDarkState create(@ColorInt int color, boolean isDark) { //NOSONAR
    return new ColorIsDarkState(color, isDark); //NOSONAR
  }

  public static BiFunction<Integer, Boolean, ColorIsDarkState> creator() { //NOSONAR
    return new BiFunction<Integer, Boolean, ColorIsDarkState>() { //NOSONAR
      @Override //NOSONAR
      public ColorIsDarkState apply(Integer integer, Boolean aBoolean) { //NOSONAR
        return ColorIsDarkState.create(integer, aBoolean); //NOSONAR
      }
    };
  }

  @ColorInt //NOSONAR
  public int color() { //NOSONAR
    return color; //NOSONAR
  }

  public boolean isDark() { //NOSONAR
    return isDark; //NOSONAR
  }
}
