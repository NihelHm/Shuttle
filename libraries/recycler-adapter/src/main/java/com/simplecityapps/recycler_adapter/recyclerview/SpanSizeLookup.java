package com.simplecityapps.recycler_adapter.recyclerview;

import android.support.v7.widget.GridLayoutManager;

import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;

import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private ViewModelAdapter viewModelAdapter;

    private int spanCount;

    public SpanSizeLookup(ViewModelAdapter viewModelAdapter, int spanCount) {
        this.viewModelAdapter = viewModelAdapter;
        this.spanCount = spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {

        List<ViewModel> items = viewModelAdapter.items;
        if (position >= 0 && position < items.size()) {
            return items.get(position).getSpanSize(spanCount);
        }

        return 1;
    }
}
