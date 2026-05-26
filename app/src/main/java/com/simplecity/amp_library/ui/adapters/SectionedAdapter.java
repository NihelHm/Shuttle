package com.simplecity.amp_library.ui.adapters;

import android.support.annotation.NonNull;
import com.simplecity.amp_library.ui.modelviews.SectionedView;
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class SectionedAdapter extends ViewModelAdapter implements FastScrollRecyclerView.SectionedAdapter {

    @NonNull
    @Override
    public String getSectionName(int position) {

        ViewModel viewModel = items.get(position);

        if (viewModel instanceof SectionedView) {
            return ((SectionedView) viewModel).getSectionName();
        }

        return "";
    }
}
