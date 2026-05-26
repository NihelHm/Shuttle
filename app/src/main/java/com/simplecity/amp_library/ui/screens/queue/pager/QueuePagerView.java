package com.simplecity.amp_library.ui.screens.queue.pager; // NOSONAR

import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface QueuePagerView { //NOSONAR

    void loadData(List<ViewModel> items, int position); //NOSONAR

    void updateQueuePosition(int position); //NOSONAR
} // NOSONAR
