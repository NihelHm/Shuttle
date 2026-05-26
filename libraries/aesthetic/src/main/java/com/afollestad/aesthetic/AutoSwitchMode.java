package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.AutoSwitchMode.AUTO; // NOSONAR
import static com.afollestad.aesthetic.AutoSwitchMode.OFF; // NOSONAR
import static com.afollestad.aesthetic.AutoSwitchMode.ON; // NOSONAR
import static java.lang.annotation.RetentionPolicy.SOURCE; // NOSONAR

import android.support.annotation.IntDef; // NOSONAR
import java.lang.annotation.Retention; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {OFF, ON, AUTO}) //NOSONAR
public @interface AutoSwitchMode { //NOSONAR
  int OFF = 0; //NOSONAR
  int ON = 1; //NOSONAR
  int AUTO = 2; //NOSONAR
} // NOSONAR
