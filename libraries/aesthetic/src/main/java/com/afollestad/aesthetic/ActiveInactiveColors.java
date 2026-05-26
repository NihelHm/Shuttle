package com.afollestad.aesthetic; // NOSONAR

import android.content.res.ColorStateList; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ActiveInactiveColors { //NOSONAR

  @ColorInt private final int activeColor; //NOSONAR
  @ColorInt private final int inactiveColor; //NOSONAR

  private ActiveInactiveColors(@ColorInt int activeColor, @ColorInt int inactiveColor) { //NOSONAR
    this.activeColor = activeColor; //NOSONAR
    this.inactiveColor = inactiveColor; //NOSONAR
  } // NOSONAR

  public static ActiveInactiveColors create( //NOSONAR
      @ColorInt int activeColor, @ColorInt int inactiveColor) { //NOSONAR
    return new ActiveInactiveColors(activeColor, inactiveColor); //NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  public int activeColor() { //NOSONAR
    return activeColor; //NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  public int inactiveColor() { //NOSONAR
    return inactiveColor; //NOSONAR
  } // NOSONAR

  public ColorStateList toEnabledSl() { //NOSONAR
    return new ColorStateList( //NOSONAR
        new int[][] { //NOSONAR
          new int[] {android.R.attr.state_enabled}, new int[] {-android.R.attr.state_enabled} //NOSONAR
        }, // NOSONAR
        new int[] {activeColor(), inactiveColor()}); //NOSONAR
  } // NOSONAR

  //  public ColorStateList toCheckedSl() { // NOSONAR
  //    return new ColorStateList( // NOSONAR
  //        new int[][] { // NOSONAR
  //          new int[] {android.R.attr.state_checked}, new int[] {-android.R.attr.state_checked} // NOSONAR
  //        }, // NOSONAR
  //        new int[] {activeColor(), inactiveColor()}); // NOSONAR
  //  } // NOSONAR
} // NOSONAR
