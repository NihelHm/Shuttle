package com.afollestad.aesthetic; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR
import android.widget.TextView; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class ViewHintTextColorAction implements Consumer<Integer> { //NOSONAR

  private final TextView view; //NOSONAR

  private ViewHintTextColorAction(TextView view) { //NOSONAR
    this.view = view; //NOSONAR
  } // NOSONAR

  public static ViewHintTextColorAction create(@NonNull TextView view) { //NOSONAR
    return new ViewHintTextColorAction(view); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
    if (view != null) { //NOSONAR
      view.setHintTextColor(color); //NOSONAR
    } // NOSONAR
  } // NOSONAR
} // NOSONAR
