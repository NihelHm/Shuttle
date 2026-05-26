package com.simplecityapps.recycler_adapter.recyclerview; // NOSONAR

import android.support.v7.widget.RecyclerView; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface RecyclingViewHolder { //NOSONAR

    /** // NOSONAR
     * If a {@link RecyclerView.RecyclerListener} is attached to the RecyclerView and implemented so as to call this method // NOSONAR
     * on the ViewHolder to be recycled, we can clean up resources here. // NOSONAR
     */ // NOSONAR
    void recycle(); //NOSONAR

} // NOSONAR
