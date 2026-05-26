package com.afollestad.aesthetic;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class Rx {

  public static Consumer<Throwable> onErrorLogAndRethrow() {
    return new Consumer<Throwable>() {
      @Override
      public void accept(@NonNull Throwable throwable) throws Exception {
        throwable.printStackTrace();
        throw Exceptions.propagate(throwable);
      }
    };
  }

  public static <T> ObservableTransformer<T, T> distinctToMainThread() {
    return new ObservableTransformer<T, T>() {
      @Override
      public ObservableSource<T> apply(@NonNull Observable<T> obs) {
        return obs.observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged();
      }
    };
  }
}
