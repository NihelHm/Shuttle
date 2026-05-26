package com.simplecity.amp_library.rx; // NOSONAR

import java.util.concurrent.Callable; // NOSONAR

/** // NOSONAR
 * A callable which does not throw on error. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface UnsafeCallable<T> extends Callable<T> { //NOSONAR

    @Override //NOSONAR
    T call(); //NOSONAR
} // NOSONAR
