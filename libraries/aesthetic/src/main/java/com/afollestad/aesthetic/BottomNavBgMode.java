package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.BottomNavBgMode.ACCENT;
import static com.afollestad.aesthetic.BottomNavBgMode.BLACK_WHITE_AUTO;
import static com.afollestad.aesthetic.BottomNavBgMode.PRIMARY;
import static com.afollestad.aesthetic.BottomNavBgMode.PRIMARY_DARK;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {BLACK_WHITE_AUTO, PRIMARY, PRIMARY_DARK, ACCENT}) //NOSONAR
public @interface BottomNavBgMode { //NOSONAR
  int BLACK_WHITE_AUTO = 0; //NOSONAR
  int PRIMARY = 1; //NOSONAR
  int PRIMARY_DARK = 2; //NOSONAR
  int ACCENT = 3; //NOSONAR
}
