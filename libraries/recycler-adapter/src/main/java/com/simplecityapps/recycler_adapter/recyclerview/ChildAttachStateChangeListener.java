package com.simplecityapps.recycler_adapter.recyclerview; // NOSONAR

import android.support.v7.widget.RecyclerView; // NOSONAR
import android.view.View; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ChildAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener { //NOSONAR

    private RecyclerView recyclerView; //NOSONAR

    public ChildAttachStateChangeListener(RecyclerView recyclerView) { //NOSONAR
        this.recyclerView = recyclerView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onChildViewAttachedToWindow(View view) { //NOSONAR
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view); //NOSONAR
        if (holder instanceof AttachStateViewHolder) { //NOSONAR
            ((AttachStateViewHolder) holder).onAttachedToWindow(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onChildViewDetachedFromWindow(View view) { //NOSONAR
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view); //NOSONAR
        if (holder instanceof AttachStateViewHolder) { //NOSONAR
            ((AttachStateViewHolder) holder).onDetachedFromWindow(); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
