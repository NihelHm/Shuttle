package com.simplecity.amp_library.sql.sqlbrite; // NOSONAR

import android.database.Cursor; // NOSONAR
import com.squareup.sqlbrite2.SqlBrite; // NOSONAR
import io.reactivex.ObservableOperator; // NOSONAR
import io.reactivex.Observer; // NOSONAR
import io.reactivex.exceptions.Exceptions; // NOSONAR
import io.reactivex.functions.Function; // NOSONAR
import io.reactivex.observers.DisposableObserver; // NOSONAR
import io.reactivex.plugins.RxJavaPlugins; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class QueryToListOperator<T> implements ObservableOperator<List<T>, SqlBrite.Query> { //NOSONAR
    private final Function<Cursor, T> mapper; //NOSONAR

    QueryToListOperator(Function<Cursor, T> mapper) { //NOSONAR
        this.mapper = mapper; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Observer<? super SqlBrite.Query> apply(Observer<? super List<T>> observer) { //NOSONAR
        return new MappingObserver<>(observer, mapper); //NOSONAR
    } // NOSONAR

    static final class MappingObserver<T> extends DisposableObserver<SqlBrite.Query> { //NOSONAR
        private final Observer<? super List<T>> downstream; //NOSONAR
        private final Function<Cursor, T> mapper; //NOSONAR

        MappingObserver(Observer<? super List<T>> downstream, Function<Cursor, T> mapper) { //NOSONAR
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
                Cursor cursor = query.run(); //NOSONAR
                if (cursor == null || isDisposed()) { //NOSONAR
                    return; //NOSONAR
                } // NOSONAR
                List<T> items = new ArrayList<>(cursor.getCount()); //NOSONAR
                try { //NOSONAR
                    while (cursor.moveToNext()) { //NOSONAR
                        items.add(mapper.apply(cursor)); //NOSONAR
                    } // NOSONAR
                } finally { //NOSONAR
                    cursor.close(); //NOSONAR
                } // NOSONAR
                if (!isDisposed()) { //NOSONAR
                    downstream.onNext(items); //NOSONAR
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
