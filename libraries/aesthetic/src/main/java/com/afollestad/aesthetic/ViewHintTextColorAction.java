package com.afollestad.aesthetic;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class ViewHintTextColorAction implements Consumer<Integer> { //NOSONAR

  private final TextView view; //NOSONAR

  private ViewHintTextColorAction(TextView view) { //NOSONAR
    this.view = view; //NOSONAR
  }

  public static ViewHintTextColorAction create(@NonNull TextView view) { //NOSONAR
    return new ViewHintTextColorAction(view); //NOSONAR
  }

  @Override //NOSONAR
  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
    if (view != null) { //NOSONAR
      view.setHintTextColor(color); //NOSONAR
    }
  }
}
