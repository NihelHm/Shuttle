package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.BottomNavBgMode.ACCENT; // NOSONAR
import static com.afollestad.aesthetic.BottomNavBgMode.BLACK_WHITE_AUTO; // NOSONAR
import static com.afollestad.aesthetic.BottomNavBgMode.PRIMARY; // NOSONAR
import static com.afollestad.aesthetic.BottomNavBgMode.PRIMARY_DARK; // NOSONAR
import static java.lang.annotation.RetentionPolicy.SOURCE; // NOSONAR

import android.support.annotation.IntDef; // NOSONAR
import java.lang.annotation.Retention; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {BLACK_WHITE_AUTO, PRIMARY, PRIMARY_DARK, ACCENT}) //NOSONAR
public @interface BottomNavBgMode { //NOSONAR
  int BLACK_WHITE_AUTO = 0; //NOSONAR
  int PRIMARY = 1; //NOSONAR
  int PRIMARY_DARK = 2; //NOSONAR
  int ACCENT = 3; //NOSONAR
} // NOSONAR
