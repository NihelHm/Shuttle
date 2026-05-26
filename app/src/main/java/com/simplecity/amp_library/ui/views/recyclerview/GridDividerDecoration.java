package com.simplecity.amp_library.ui.views.recyclerview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.simplecity.amp_library.utils.ResourceUtils;
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class GridDividerDecoration extends RecyclerView.ItemDecoration { //NOSONAR

    private int spacing; //NOSONAR
    private boolean includeEdge; //NOSONAR

    public GridDividerDecoration(Resources res, int spacingDp, boolean includeEdge) { //NOSONAR
        this.spacing = ResourceUtils.toPixels(spacingDp); //NOSONAR
        this.includeEdge = includeEdge; //NOSONAR
    }

    @Override //NOSONAR
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) { //NOSONAR
        if (!(parent.getLayoutManager() instanceof GridLayoutManager)) { //NOSONAR
            throw new IllegalStateException("GridDividerDecoration can only be used with GridLayoutManager"); //NOSONAR
        }

        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount(); //NOSONAR

        int position = parent.getChildAdapterPosition(view); //NOSONAR
        if (position < 0) { //NOSONAR
            return; //NOSONAR
        }

        int spanIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanIndex(position, spanCount); //NOSONAR
        int groupIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanGroupIndex(position, spanCount); //NOSONAR

        RecyclerView.Adapter adapter = parent.getAdapter(); //NOSONAR
        if (adapter instanceof ViewModelAdapter) { //NOSONAR
            ViewModel viewModel = ((ViewModelAdapter) adapter).items.get(position); //NOSONAR
            // IF we have a full-span item, don't appy any decoration (unless it's the first group,
            // in which case we add bottom spacing if includeEdge is true)
            if (viewModel.getSpanSize(spanCount) == spanCount) { //NOSONAR
                if (includeEdge && groupIndex == 0) { //NOSONAR
                    outRect.bottom = spacing; //NOSONAR
                }
                return; //NOSONAR
            }
        }

        if (includeEdge) { //NOSONAR
            outRect.left = spacing - spanIndex * spacing / spanCount; //NOSONAR
            outRect.right = (spanIndex + 1) * spacing / spanCount; //NOSONAR

            if (groupIndex == 0) { //NOSONAR
                outRect.top = spacing; //NOSONAR
            }
            outRect.bottom = spacing; //NOSONAR
        } else { //NOSONAR
            outRect.left = spanIndex * spacing / spanCount; //NOSONAR
            outRect.right = spacing - (spanIndex + 1) * spacing / spanCount; //NOSONAR
            if (groupIndex > 0) { //NOSONAR
                outRect.top = spacing; //NOSONAR
            }
        }
    }
}
