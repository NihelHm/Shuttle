package com.afollestad.aesthetic;

import android.support.annotation.NonNull;
import android.view.View;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ViewBackgroundAction implements Consumer<Integer> { //NOSONAR

  private final View view; //NOSONAR

  private ViewBackgroundAction(View view) { //NOSONAR
    this.view = view; //NOSONAR
  }

  public static ViewBackgroundAction create(@NonNull View view) { //NOSONAR
    return new ViewBackgroundAction(view); //NOSONAR
  }

  @Override //NOSONAR
  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
    if (view != null) { //NOSONAR
      view.setBackgroundColor(color); //NOSONAR
    }
  }
}
