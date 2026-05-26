package com.simplecity.amp_library.rx;

import io.reactivex.functions.Consumer;

/**
 * A Consumer which does not throw on error.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface UnsafeConsumer<T> extends Consumer<T> {

    @Override
    void accept(T t);
}
