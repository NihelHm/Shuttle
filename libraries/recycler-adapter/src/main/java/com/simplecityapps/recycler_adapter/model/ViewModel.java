package com.simplecityapps.recycler_adapter.model;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface ViewModel<VH extends RecyclerView.ViewHolder> extends ContentsComparator {

    int getViewType();

    void bindView(VH holder);

    void bindView(VH holder, int position, List payloads);

    VH createViewHolder(ViewGroup parent);

    int getSpanSize(int spanCount);
}
