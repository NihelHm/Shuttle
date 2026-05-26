package com.simplecity.amp_library.sql.sqlbrite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.simplecity.amp_library.BuildConfig;
import com.simplecity.amp_library.model.Query;
import com.squareup.sqlbrite2.BriteContentResolver;
import com.squareup.sqlbrite2.SqlBrite;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class SqlBriteUtils { //NOSONAR

    static final boolean LOGGING_ENABLED = false; //NOSONAR

    private SqlBriteUtils() { //NOSONAR
        // Intentionally left empty.
    }

    private static final String TAG = "SqlBriteUtils"; //NOSONAR

    private static BriteContentResolver wrapContentProvider(@NonNull Context context) { //NOSONAR
        final SqlBrite sqlBrite = new SqlBrite.Builder().build(); //NOSONAR
        BriteContentResolver briteContentResolver = sqlBrite.wrapContentProvider(context.getContentResolver(), Schedulers.io()); //NOSONAR
        briteContentResolver.setLoggingEnabled(LOGGING_ENABLED && BuildConfig.DEBUG); //NOSONAR
        return briteContentResolver; //NOSONAR
    }

    private static Observable<SqlBrite.Query> createObservable(@NonNull Context context, @NonNull Query query) { //NOSONAR
        return wrapContentProvider(context) //NOSONAR
                .createQuery(query.uri, query.projection, query.selection, query.args, query.sort, false) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .doOnError(error -> Log.e(TAG, "Query failed.\nError:" + error.toString() + "\nQuery: " + query.toString())); //NOSONAR
    }

    /**
     * Creates an {@link Observable} that emits new items when subscribed and when the content provider notifies of a change.
     */
    public static <T> Observable<T> createObservable(@NonNull Context context, @NonNull Function<Cursor, T> mapper, @NonNull Query query, T defaultValue) { //NOSONAR
        return createObservable(context, query) //NOSONAR
                .lift(new QueryToOneOperator<>(mapper, defaultValue)); //NOSONAR
    }

    /**
     * Creates a {@link Single} that emits an item.
     */
    public static <T> Single<T> createSingle(@NonNull Context context, @NonNull Function<Cursor, T> mapper, @NonNull Query query, T defaultValue) { //NOSONAR
        return createObservable(context, mapper, query, defaultValue) //NOSONAR
                .firstOrError(); //NOSONAR
    }

    /**
     * Creates an {@link Observable} that emits new lists when subscribed and when the content provider notifies of a change.
     */
    public static <T> Observable<List<T>> createObservableList(@NonNull Context context, @NonNull Function<Cursor, T> mapper, @NonNull Query query) { //NOSONAR
        return createObservable(context, query) //NOSONAR
                .lift(new QueryToListOperator<>(mapper)); //NOSONAR
    }

    /**
     * Creates a {@link Single} that emits a list.
     */
    public static <T> Single<List<T>> createSingleList(@NonNull Context context, @NonNull Function<Cursor, T> mapper, @NonNull Query query) { //NOSONAR
        return createObservableList(context, mapper, query) //NOSONAR
                .first(Collections.emptyList()); //NOSONAR
    }
}
