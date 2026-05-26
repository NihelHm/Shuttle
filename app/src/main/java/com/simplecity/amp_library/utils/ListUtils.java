package com.simplecity.amp_library.utils;

import java.util.List;

import static java.util.Collections.emptyList;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class ListUtils {

    public static <T> List<T> emptyIfNull(List<T> data) {
        return data == null ? emptyList() : data;
    }

    private ListUtils() {
        throw new IllegalStateException("no instances");
    }
}
