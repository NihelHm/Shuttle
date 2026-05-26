package com.simplecity.amp_library.utils; // NOSONAR

import java.util.List; // NOSONAR

import static java.util.Collections.emptyList; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ListUtils { //NOSONAR

    public static <T> List<T> emptyIfNull(List<T> data) { //NOSONAR
        return data == null ? emptyList() : data; //NOSONAR
    } // NOSONAR

    private ListUtils() { //NOSONAR
        throw new IllegalStateException("no instances"); //NOSONAR
    } // NOSONAR
} // NOSONAR
