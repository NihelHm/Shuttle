package com.afollestad.aesthetic;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ActiveInactiveColors { //NOSONAR

  @ColorInt private final int activeColor; //NOSONAR
  @ColorInt private final int inactiveColor; //NOSONAR

  private ActiveInactiveColors(@ColorInt int activeColor, @ColorInt int inactiveColor) { //NOSONAR
    this.activeColor = activeColor; //NOSONAR
    this.inactiveColor = inactiveColor; //NOSONAR
  }

  public static ActiveInactiveColors create( //NOSONAR
      @ColorInt int activeColor, @ColorInt int inactiveColor) { //NOSONAR
    return new ActiveInactiveColors(activeColor, inactiveColor); //NOSONAR
  }

  @ColorInt //NOSONAR
  public int activeColor() { //NOSONAR
    return activeColor; //NOSONAR
  }

  @ColorInt //NOSONAR
  public int inactiveColor() { //NOSONAR
    return inactiveColor; //NOSONAR
  }

  public ColorStateList toEnabledSl() { //NOSONAR
    return new ColorStateList( //NOSONAR
        new int[][] { //NOSONAR
          new int[] {android.R.attr.state_enabled}, new int[] {-android.R.attr.state_enabled} //NOSONAR
        },
        new int[] {activeColor(), inactiveColor()}); //NOSONAR
  }

  //  public ColorStateList toCheckedSl() {
  //    return new ColorStateList(
  //        new int[][] {
  //          new int[] {android.R.attr.state_checked}, new int[] {-android.R.attr.state_checked}
  //        },
  //        new int[] {activeColor(), inactiveColor()});
  //  }
}
