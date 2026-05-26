package com.simplecity.amp_library.sql.sqlbrite;

import android.database.Cursor;
import android.support.annotation.Nullable;
import com.squareup.sqlbrite2.SqlBrite;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class QueryToOneOperator<T> implements ObservableOperator<T, SqlBrite.Query> { //NOSONAR
    private final Function<Cursor, T> mapper; //NOSONAR
    private final T defaultValue; //NOSONAR

    /**
     * A null {@code defaultValue} means nothing will be emitted when empty.
     */
    QueryToOneOperator(Function<Cursor, T> mapper, @Nullable T defaultValue) { //NOSONAR
        this.mapper = mapper; //NOSONAR
        this.defaultValue = defaultValue; //NOSONAR
    }

    @Override //NOSONAR
    public Observer<? super SqlBrite.Query> apply(Observer<? super T> observer) { //NOSONAR
        return new MappingObserver<>(observer, mapper, defaultValue); //NOSONAR
    }

    static final class MappingObserver<T> extends DisposableObserver<SqlBrite.Query> { //NOSONAR
        private final Observer<? super T> downstream; //NOSONAR
        private final Function<Cursor, T> mapper; //NOSONAR
        private final T defaultValue; //NOSONAR

        MappingObserver(Observer<? super T> downstream, Function<Cursor, T> mapper, T defaultValue) { //NOSONAR
            this.downstream = downstream; //NOSONAR
            this.mapper = mapper; //NOSONAR
            this.defaultValue = defaultValue; //NOSONAR
        }

        @Override //NOSONAR
        protected void onStart() { //NOSONAR
            downstream.onSubscribe(this); //NOSONAR
        }

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
                            }
                            if (cursor.moveToNext()) { //NOSONAR
                                throw new IllegalStateException("Cursor returned more than 1 row"); //NOSONAR
                            }
                        }
                    } finally { //NOSONAR
                        cursor.close(); //NOSONAR
                    }
                }
                if (!isDisposed()) { //NOSONAR
                    if (item != null) { //NOSONAR
                        downstream.onNext(item); //NOSONAR
                    } else if (defaultValue != null) { //NOSONAR
                        downstream.onNext(defaultValue); //NOSONAR
                    }
                }
            } catch (Throwable e) { //NOSONAR
                Exceptions.throwIfFatal(e); //NOSONAR
                onError(e); //NOSONAR
            }
        }

        @Override //NOSONAR
        public void onComplete() { //NOSONAR
            if (!isDisposed()) { //NOSONAR
                downstream.onComplete(); //NOSONAR
            }
        }

        @Override //NOSONAR
        public void onError(Throwable e) { //NOSONAR
            if (isDisposed()) { //NOSONAR
                RxJavaPlugins.onError(e); //NOSONAR
            } else { //NOSONAR
                downstream.onError(e); //NOSONAR
            }
        }
    }
}
