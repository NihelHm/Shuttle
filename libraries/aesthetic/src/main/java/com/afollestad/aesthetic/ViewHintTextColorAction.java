package com.afollestad.aesthetic;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP)
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
class ViewHintTextColorAction implements Consumer<Integer> {

  private final TextView view;

  private ViewHintTextColorAction(TextView view) {
    this.view = view;
  }

  public static ViewHintTextColorAction create(@NonNull TextView view) {
    return new ViewHintTextColorAction(view);
  }

  @Override
  public void accept(@io.reactivex.annotations.NonNull Integer color) {
    if (view != null) {
      view.setHintTextColor(color);
    }
  }
}
