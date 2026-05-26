package com.afollestad.aesthetic;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.support.annotation.RestrictTo;
import android.view.View;
import io.reactivex.Observable;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP)
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
final class ViewObservablePair {

  private final View view;
  private final Observable<Integer> observable;

  private ViewObservablePair(View view, Observable<Integer> observable) {
    this.view = view;
    this.observable = observable;
  }

  static ViewObservablePair create(View view, Observable<Integer> observable) {
    return new ViewObservablePair(view, observable);
  }

  View view() {
    return view;
  }

  Observable<Integer> observable() {
    return observable;
  }
}
