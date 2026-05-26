package com.simplecityapps.recycler_adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * A custom RecyclerListener that calls recycle() on the ViewHolder, if it implements {@link RecyclingViewHolder}
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class RecyclerListener implements RecyclerView.RecyclerListener {

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof RecyclingViewHolder) {
            ((RecyclingViewHolder) holder).recycle();
        }
    }
}
