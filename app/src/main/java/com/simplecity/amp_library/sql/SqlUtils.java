package com.simplecity.amp_library.sql;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.simplecity.amp_library.BuildConfig;
import com.simplecity.amp_library.model.Query;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.ThreadUtils;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SqlUtils { //NOSONAR

    private static final boolean ENABLE_LOGGING = false; //NOSONAR

    private static final String TAG = "SqlUtils"; //NOSONAR

    private SqlUtils() { //NOSONAR
        // Intentionally left empty.
    }

    @WorkerThread //NOSONAR
    public static Cursor createQuery(Context context, Query query) { //NOSONAR

        long time = System.currentTimeMillis(); //NOSONAR

        Cursor cursor = context.getContentResolver() //NOSONAR
                .query(query.uri, //NOSONAR
                        query.projection, //NOSONAR
                        query.selection, //NOSONAR
                        query.args, //NOSONAR
                        query.sort); //NOSONAR

        if (ENABLE_LOGGING && BuildConfig.DEBUG) { //NOSONAR
            Log.d(TAG, String.format("Query took %sms. %s", (System.currentTimeMillis() - time), query)); //NOSONAR
        }

        ThreadUtils.ensureNotOnMainThread(); //NOSONAR

        return cursor; //NOSONAR
    }

    public static <T> List<T> createQuery(Context context, Function<Cursor, T> mapper, Query query) { //NOSONAR

        List<T> items = new ArrayList<>(); //NOSONAR

        Cursor cursor = createQuery(context, query); //NOSONAR

        if (cursor != null) { //NOSONAR
            try { //NOSONAR
                if (cursor.moveToFirst()) { //NOSONAR
                    do { //NOSONAR
                        T item = mapper.apply(cursor); //NOSONAR
                        if (item == null) { //NOSONAR
                            throw new NullPointerException("Mapper returned null for row " + cursor.getPosition()); //NOSONAR
                        }
                        items.add(item); //NOSONAR
                    } while (cursor.moveToNext()); //NOSONAR
                }
            } catch (Exception e) { //NOSONAR
                LogUtils.logException(TAG, "createSingle threw an error", e); //NOSONAR
            } finally { //NOSONAR
                cursor.close(); //NOSONAR
            }
        }
        return items; //NOSONAR
    }

    public static void createActionableQuery(Context context, Consumer<Cursor> action, Query query) { //NOSONAR

        Cursor cursor = createQuery(context, query); //NOSONAR

        if (cursor != null) { //NOSONAR
            try { //NOSONAR
                if (cursor.moveToFirst()) { //NOSONAR
                    do { //NOSONAR
                        action.accept(cursor); //NOSONAR
                    } while (cursor.moveToNext()); //NOSONAR
                }
            } catch (Exception e) { //NOSONAR
                LogUtils.logException(TAG, "createActionableQuery threw an error", e); //NOSONAR
            } finally { //NOSONAR
                cursor.close(); //NOSONAR
            }
        }
    }

    public static <T> T createSingleQuery(Context context, Function<Cursor, T> mapper, Query query) { //NOSONAR
        return createSingleQuery(context, mapper, null, query); //NOSONAR
    }

    public static <T> T createSingleQuery(Context context, Function<Cursor, T> mapper, T defaultValue, Query query) { //NOSONAR

        T item = defaultValue; //NOSONAR

        Cursor cursor = createQuery(context, query); //NOSONAR

        if (cursor != null) { //NOSONAR
            try { //NOSONAR
                if (cursor.moveToFirst()) { //NOSONAR
                    item = mapper.apply(cursor); //NOSONAR
                    if (cursor.moveToNext()) { //NOSONAR
                        Log.e(TAG, "Cursor returned more than 1 row. Query: " + query); //NOSONAR
                    }
                }
            } catch (Exception e) { //NOSONAR
                LogUtils.logException(TAG, "createSingleQuery threw an error", e); //NOSONAR
                e.printStackTrace(); //NOSONAR
            } finally { //NOSONAR
                cursor.close(); //NOSONAR
            }
        }
        return item; //NOSONAR
    }
}
