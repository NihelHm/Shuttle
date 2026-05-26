package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.TabLayoutBgMode.ACCENT;
import static com.afollestad.aesthetic.TabLayoutBgMode.PRIMARY;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {PRIMARY, ACCENT}) //NOSONAR
public @interface TabLayoutBgMode { //NOSONAR
  int PRIMARY = 0; //NOSONAR
  int ACCENT = 1; //NOSONAR
}
