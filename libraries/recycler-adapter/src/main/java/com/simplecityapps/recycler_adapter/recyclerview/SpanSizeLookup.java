package com.simplecityapps.recycler_adapter.recyclerview; // NOSONAR

import android.support.v7.widget.GridLayoutManager; // NOSONAR

import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR

import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup { //NOSONAR

    private ViewModelAdapter viewModelAdapter; //NOSONAR

    private int spanCount; //NOSONAR

    public SpanSizeLookup(ViewModelAdapter viewModelAdapter, int spanCount) { //NOSONAR
        this.viewModelAdapter = viewModelAdapter; //NOSONAR
        this.spanCount = spanCount; //NOSONAR
    } // NOSONAR

    public void setSpanCount(int spanCount) { //NOSONAR
        this.spanCount = spanCount; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getSpanSize(int position) { //NOSONAR

        List<ViewModel> items = viewModelAdapter.items; //NOSONAR
        if (position >= 0 && position < items.size()) { //NOSONAR
            return items.get(position).getSpanSize(spanCount); //NOSONAR
        } // NOSONAR

        return 1; //NOSONAR
    } // NOSONAR
} // NOSONAR
