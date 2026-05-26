package com.afollestad.aesthetic;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.widget.TextView;

import io.reactivex.functions.Consumer;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ViewTextColorAction implements Consumer<Integer> { //NOSONAR

  private final TextView view; //NOSONAR

  private ViewTextColorAction(TextView view) { //NOSONAR
    this.view = view; //NOSONAR
  }

  public static ViewTextColorAction create(@NonNull TextView view) { //NOSONAR
    return new ViewTextColorAction(view); //NOSONAR
  }

  @Override //NOSONAR
  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
    if (view != null) { //NOSONAR
      view.setTextColor(color); //NOSONAR
    }
  }
}
