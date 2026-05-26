package com.afollestad.aesthetic; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

import android.support.annotation.RestrictTo; // NOSONAR
import android.view.View; // NOSONAR
import io.reactivex.Observable; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class ViewObservablePair { //NOSONAR

  private final View view; //NOSONAR
  private final Observable<Integer> observable; //NOSONAR

  private ViewObservablePair(View view, Observable<Integer> observable) { //NOSONAR
    this.view = view; //NOSONAR
    this.observable = observable; //NOSONAR
  } // NOSONAR

  static ViewObservablePair create(View view, Observable<Integer> observable) { //NOSONAR
    return new ViewObservablePair(view, observable); //NOSONAR
  } // NOSONAR

  View view() { //NOSONAR
    return view; //NOSONAR
  } // NOSONAR

  Observable<Integer> observable() { //NOSONAR
    return observable; //NOSONAR
  } // NOSONAR
} // NOSONAR
