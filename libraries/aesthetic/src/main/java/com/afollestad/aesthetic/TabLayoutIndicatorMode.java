package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.TabLayoutIndicatorMode.ACCENT;
import static com.afollestad.aesthetic.TabLayoutIndicatorMode.PRIMARY;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {PRIMARY, ACCENT}) //NOSONAR
public @interface TabLayoutIndicatorMode { //NOSONAR
  int PRIMARY = 0; //NOSONAR
  int ACCENT = 1; //NOSONAR
}
