package com.simplecityapps.recycler_adapter.adapter; // NOSONAR

import android.support.v7.util.ListUpdateCallback; // NOSONAR

/** // NOSONAR
 * A custom {@link ListUpdateCallback} with an additional onComplete() methods, used for notifying when the // NOSONAR
 * diff result has been calculated and supplied to the adapter. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface CompletionListUpdateCallback extends ListUpdateCallback { //NOSONAR

    /** // NOSONAR
     * Called once the diff result has been calculated and supplied to the adapter. // NOSONAR
     */ // NOSONAR
    void onComplete(); //NOSONAR
} // NOSONAR
