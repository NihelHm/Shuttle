package com.afollestad.aesthetic;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.CardView;
import android.view.View;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.observers.DisposableObserver;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class ViewBackgroundSubscriber extends DisposableObserver<Integer> { //NOSONAR

  private final View view; //NOSONAR

  private ViewBackgroundSubscriber(@NonNull View view) { //NOSONAR
    this.view = view; //NOSONAR
  }

  public static ViewBackgroundSubscriber create(@NonNull View view) { //NOSONAR
    return new ViewBackgroundSubscriber(view); //NOSONAR
  }

  @Override //NOSONAR
  public void onError(Throwable e) { //NOSONAR
    throw Exceptions.propagate(e); //NOSONAR
  }

  @Override //NOSONAR
  public void onComplete() { //NOSONAR
      // Intentionally left empty.
  }

  @Override //NOSONAR
  public void onNext(Integer color) { //NOSONAR
    if (view instanceof CardView) { //NOSONAR
      ((CardView) view).setCardBackgroundColor(color); //NOSONAR
    } else { //NOSONAR
      view.setBackgroundColor(color); //NOSONAR
    }
  }
}
