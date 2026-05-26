package com.simplecity.amp_library.ui.views.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;
import java.util.List;

/**
 * A custom {@link GridLayoutManager.SpanSizeLookup} which determines the span size from the {@link ViewModel}
 * at the position of the lookup.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class AdaptableSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private List<ViewModel> items;
    private int spanCount;

    public AdaptableSpanSizeLookup(ViewModelAdapter ViewModelAdapter, int spanCount) {
        this.items = ViewModelAdapter.items;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {

        if (position >= 0 && position < items.size()) {
            return items.get(position).getSpanSize(spanCount);
        }
        return 1;
    }
}
