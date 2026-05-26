package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.NavigationViewMode.SELECTED_ACCENT; // NOSONAR
import static com.afollestad.aesthetic.NavigationViewMode.SELECTED_PRIMARY; // NOSONAR
import static java.lang.annotation.RetentionPolicy.SOURCE; // NOSONAR

import android.support.annotation.IntDef; // NOSONAR
import java.lang.annotation.Retention; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {SELECTED_PRIMARY, SELECTED_ACCENT}) //NOSONAR
public @interface NavigationViewMode { //NOSONAR
  int SELECTED_PRIMARY = 0; //NOSONAR
  int SELECTED_ACCENT = 1; //NOSONAR
} // NOSONAR
