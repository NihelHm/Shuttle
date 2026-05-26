package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

import static com.simplecity.amp_library.R.layout.list_item_loading; // NOSONAR
import static com.simplecity.amp_library.ui.adapters.ViewType.LOADING; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LoadingView extends BaseViewModel<LoadingView.ViewHolder> { //NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return LOADING; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_loading; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "LoadingView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
