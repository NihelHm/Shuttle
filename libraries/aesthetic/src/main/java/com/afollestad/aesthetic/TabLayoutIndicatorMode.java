package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.TabLayoutIndicatorMode.ACCENT; // NOSONAR
import static com.afollestad.aesthetic.TabLayoutIndicatorMode.PRIMARY; // NOSONAR
import static java.lang.annotation.RetentionPolicy.SOURCE; // NOSONAR

import android.support.annotation.IntDef; // NOSONAR
import java.lang.annotation.Retention; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {PRIMARY, ACCENT}) //NOSONAR
public @interface TabLayoutIndicatorMode { //NOSONAR
  int PRIMARY = 0; //NOSONAR
  int ACCENT = 1; //NOSONAR
} // NOSONAR
