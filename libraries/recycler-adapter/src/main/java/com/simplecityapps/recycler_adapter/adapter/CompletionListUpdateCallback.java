package com.simplecityapps.recycler_adapter.adapter;

import android.support.v7.util.ListUpdateCallback;

/**
 * A custom {@link ListUpdateCallback} with an additional onComplete() methods, used for notifying when the
 * diff result has been calculated and supplied to the adapter.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface CompletionListUpdateCallback extends ListUpdateCallback {

    /**
     * Called once the diff result has been calculated and supplied to the adapter.
     */
    void onComplete();
}
