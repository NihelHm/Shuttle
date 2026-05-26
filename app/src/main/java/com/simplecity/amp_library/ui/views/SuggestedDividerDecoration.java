package com.simplecity.amp_library.ui.views;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.utils.ResourceUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class SuggestedDividerDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public SuggestedDividerDecoration(Resources res) {
        this.spacing = ResourceUtils.toPixels(4);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int spanIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanIndex(
                parent.getChildAdapterPosition(view), ((GridLayoutManager) parent.getLayoutManager()).getSpanCount()
        );

        switch (parent.getChildViewHolder(view).getItemViewType()) {
            case ViewType.ALBUM_LIST_SMALL:
                outRect.left = spacing;
                outRect.right = spacing;
                break;
            case ViewType.ALBUM_CARD_LARGE:
                if (spanIndex == 0) {
                    outRect.left = spacing;
                } else if (spanIndex == 3) {
                    outRect.right = spacing;
                }
                break;
        }
    }
}
