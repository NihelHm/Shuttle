package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.res.Resources; // NOSONAR
import android.graphics.Rect; // NOSONAR
import android.support.v7.widget.GridLayoutManager; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.view.View; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.utils.ResourceUtils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SuggestedDividerDecoration extends RecyclerView.ItemDecoration { //NOSONAR

    private int spacing; //NOSONAR

    public SuggestedDividerDecoration(Resources res) { //NOSONAR
        this.spacing = ResourceUtils.toPixels(4); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) { //NOSONAR

        int spanIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanIndex( //NOSONAR
                parent.getChildAdapterPosition(view), ((GridLayoutManager) parent.getLayoutManager()).getSpanCount() //NOSONAR
        ); // NOSONAR

        switch (parent.getChildViewHolder(view).getItemViewType()) { //NOSONAR
            case ViewType.ALBUM_LIST_SMALL: //NOSONAR
                outRect.left = spacing; //NOSONAR
                outRect.right = spacing; //NOSONAR
                break; //NOSONAR
            case ViewType.ALBUM_CARD_LARGE: //NOSONAR
                if (spanIndex == 0) { //NOSONAR
                    outRect.left = spacing; //NOSONAR
                } else if (spanIndex == 3) { //NOSONAR
                    outRect.right = spacing; //NOSONAR
                } // NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
