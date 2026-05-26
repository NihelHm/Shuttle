package com.simplecity.amp_library.sql.sqlbrite; // NOSONAR

import android.database.Cursor; // NOSONAR
import com.annimon.stream.Optional; // NOSONAR
import com.squareup.sqlbrite2.SqlBrite; // NOSONAR
import io.reactivex.ObservableOperator; // NOSONAR
import io.reactivex.Observer; // NOSONAR
import io.reactivex.exceptions.Exceptions; // NOSONAR
import io.reactivex.functions.Function; // NOSONAR
import io.reactivex.observers.DisposableObserver; // NOSONAR
import io.reactivex.plugins.RxJavaPlugins; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class QueryToOptionalOperator<T> implements ObservableOperator<Optional<T>, SqlBrite.Query> { //NOSONAR
    private final Function<Cursor, T> mapper; //NOSONAR

    QueryToOptionalOperator(Function<Cursor, T> mapper) { //NOSONAR
        this.mapper = mapper; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Observer<? super SqlBrite.Query> apply(Observer<? super Optional<T>> observer) { //NOSONAR
        return new MappingObserver<>(observer, mapper); //NOSONAR
    } // NOSONAR

    static final class MappingObserver<T> extends DisposableObserver<SqlBrite.Query> { //NOSONAR
        private final Observer<? super Optional<T>> downstream; //NOSONAR
        private final Function<Cursor, T> mapper; //NOSONAR

        MappingObserver(Observer<? super Optional<T>> downstream, Function<Cursor, T> mapper) { //NOSONAR
            this.downstream = downstream; //NOSONAR
            this.mapper = mapper; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        protected void onStart() { //NOSONAR
            downstream.onSubscribe(this); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onNext(SqlBrite.Query query) { //NOSONAR
            try { //NOSONAR
                T item = null; //NOSONAR
                Cursor cursor = query.run(); //NOSONAR
                if (cursor != null) { //NOSONAR
                    try { //NOSONAR
                        if (cursor.moveToNext()) { //NOSONAR
                            item = mapper.apply(cursor); //NOSONAR
                            if (item == null) { //NOSONAR
                                downstream.onError(new NullPointerException("QueryToOne mapper returned null")); //NOSONAR
                                return; //NOSONAR
                            } // NOSONAR
                            if (cursor.moveToNext()) { //NOSONAR
                                throw new IllegalStateException("Cursor returned more than 1 row"); //NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } finally { //NOSONAR
                        cursor.close(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
                if (!isDisposed()) { //NOSONAR
                    downstream.onNext(Optional.ofNullable(item)); //NOSONAR
                } // NOSONAR
            } catch (Throwable e) { //NOSONAR
                Exceptions.throwIfFatal(e); //NOSONAR
                onError(e); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onComplete() { //NOSONAR
            if (!isDisposed()) { //NOSONAR
                downstream.onComplete(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onError(Throwable e) { //NOSONAR
            if (isDisposed()) { //NOSONAR
                RxJavaPlugins.onError(e); //NOSONAR
            } else { //NOSONAR
                downstream.onError(e); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
