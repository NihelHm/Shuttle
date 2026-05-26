package com.afollestad.aesthetic;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.support.annotation.RestrictTo;
import android.view.View;
import io.reactivex.Observable;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class ViewObservablePair { //NOSONAR

  private final View view; //NOSONAR
  private final Observable<Integer> observable; //NOSONAR

  private ViewObservablePair(View view, Observable<Integer> observable) { //NOSONAR
    this.view = view; //NOSONAR
    this.observable = observable; //NOSONAR
  }

  static ViewObservablePair create(View view, Observable<Integer> observable) { //NOSONAR
    return new ViewObservablePair(view, observable); //NOSONAR
  }

  View view() { //NOSONAR
    return view; //NOSONAR
  }

  Observable<Integer> observable() { //NOSONAR
    return observable; //NOSONAR
  }
}
