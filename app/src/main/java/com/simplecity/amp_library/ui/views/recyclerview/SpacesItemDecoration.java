package com.simplecity.amp_library.ui.views.recyclerview; // NOSONAR

import android.graphics.Rect; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.view.View; // NOSONAR
import com.simplecity.amp_library.utils.ResourceUtils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SpacesItemDecoration extends RecyclerView.ItemDecoration { //NOSONAR

    private int space; //NOSONAR

    public SpacesItemDecoration(int space) { //NOSONAR
        this.space = ResourceUtils.toPixels(space); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) { //NOSONAR

        if (parent.getChildLayoutPosition(view) == 0) { //NOSONAR
            outRect.left = 0; //NOSONAR
        } else { //NOSONAR
            outRect.left = space; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
