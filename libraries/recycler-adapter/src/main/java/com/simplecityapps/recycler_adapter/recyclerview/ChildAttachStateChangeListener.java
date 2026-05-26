package com.simplecityapps.recycler_adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class ChildAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener {

    private RecyclerView recyclerView;

    public ChildAttachStateChangeListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
        if (holder instanceof AttachStateViewHolder) {
            ((AttachStateViewHolder) holder).onAttachedToWindow();
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
        if (holder instanceof AttachStateViewHolder) {
            ((AttachStateViewHolder) holder).onDetachedFromWindow();
        }
    }
}
