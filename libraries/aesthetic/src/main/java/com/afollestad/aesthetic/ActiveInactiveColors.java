package com.afollestad.aesthetic;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess")
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class ActiveInactiveColors {

  @ColorInt private final int activeColor;
  @ColorInt private final int inactiveColor;

  private ActiveInactiveColors(@ColorInt int activeColor, @ColorInt int inactiveColor) {
    this.activeColor = activeColor;
    this.inactiveColor = inactiveColor;
  }

  public static ActiveInactiveColors create(
      @ColorInt int activeColor, @ColorInt int inactiveColor) {
    return new ActiveInactiveColors(activeColor, inactiveColor);
  }

  @ColorInt
  public int activeColor() {
    return activeColor;
  }

  @ColorInt
  public int inactiveColor() {
    return inactiveColor;
  }

  public ColorStateList toEnabledSl() {
    return new ColorStateList(
        new int[][] {
          new int[] {android.R.attr.state_enabled}, new int[] {-android.R.attr.state_enabled}
        },
        new int[] {activeColor(), inactiveColor()});
  }

  //  public ColorStateList toCheckedSl() {
  //    return new ColorStateList(
  //        new int[][] {
  //          new int[] {android.R.attr.state_checked}, new int[] {-android.R.attr.state_checked}
  //        },
  //        new int[] {activeColor(), inactiveColor()});
  //  }
}
