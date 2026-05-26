package com.simplecity.amp_library.ui.modelviews;

import android.view.View;
import android.view.ViewGroup;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

import static com.simplecity.amp_library.R.layout.list_item_loading;
import static com.simplecity.amp_library.ui.adapters.ViewType.LOADING;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LoadingView extends BaseViewModel<LoadingView.ViewHolder> { //NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return LOADING; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_loading; //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "LoadingView.ViewHolder"; //NOSONAR
        }
    }
}
