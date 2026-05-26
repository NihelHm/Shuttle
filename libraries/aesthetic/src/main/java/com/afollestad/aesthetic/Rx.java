package com.afollestad.aesthetic; // NOSONAR

import io.reactivex.Observable; // NOSONAR
import io.reactivex.ObservableSource; // NOSONAR
import io.reactivex.ObservableTransformer; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.exceptions.Exceptions; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class Rx { //NOSONAR

  public static Consumer<Throwable> onErrorLogAndRethrow() { //NOSONAR
    return new Consumer<Throwable>() { //NOSONAR
      @Override //NOSONAR
      public void accept(@NonNull Throwable throwable) throws Exception { //NOSONAR
        throwable.printStackTrace(); //NOSONAR
        throw Exceptions.propagate(throwable); //NOSONAR
      } // NOSONAR
    }; // NOSONAR
  } // NOSONAR

  public static <T> ObservableTransformer<T, T> distinctToMainThread() { //NOSONAR
    return new ObservableTransformer<T, T>() { //NOSONAR
      @Override //NOSONAR
      public ObservableSource<T> apply(@NonNull Observable<T> obs) { //NOSONAR
        return obs.observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged(); //NOSONAR
      } // NOSONAR
    }; // NOSONAR
  } // NOSONAR
} // NOSONAR
