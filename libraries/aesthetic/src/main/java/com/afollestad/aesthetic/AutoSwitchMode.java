package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.AutoSwitchMode.AUTO;
import static com.afollestad.aesthetic.AutoSwitchMode.OFF;
import static com.afollestad.aesthetic.AutoSwitchMode.ON;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess") //NOSONAR
@Retention(SOURCE) //NOSONAR
@IntDef(value = {OFF, ON, AUTO}) //NOSONAR
public @interface AutoSwitchMode { //NOSONAR
  int OFF = 0; //NOSONAR
  int ON = 1; //NOSONAR
  int AUTO = 2; //NOSONAR
}
