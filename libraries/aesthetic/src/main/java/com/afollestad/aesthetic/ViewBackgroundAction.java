package com.afollestad.aesthetic;

import android.support.annotation.NonNull;
import android.view.View;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess")
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class ViewBackgroundAction implements Consumer<Integer> {

  private final View view;

  private ViewBackgroundAction(View view) {
    this.view = view;
  }

  public static ViewBackgroundAction create(@NonNull View view) {
    return new ViewBackgroundAction(view);
  }

  @Override
  public void accept(@io.reactivex.annotations.NonNull Integer color) {
    if (view != null) {
      view.setBackgroundColor(color);
    }
  }
}
