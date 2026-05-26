package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.interfaces.BreadcrumbListener; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.ui.views.BreadcrumbItem; // NOSONAR
import com.simplecity.amp_library.ui.views.BreadcrumbView; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BreadcrumbsView extends BaseViewModel<BreadcrumbsView.ViewHolder> { //NOSONAR

    private String breadcrumbPath; //NOSONAR

    public BreadcrumbsView(String breadcrumbPath) { //NOSONAR
        this.breadcrumbPath = breadcrumbPath; //NOSONAR
    } // NOSONAR

    public void setBreadcrumbsPath(String path) { //NOSONAR
        breadcrumbPath = path; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    private BreadcrumbListener listener; //NOSONAR

    public void setListener(@Nullable BreadcrumbListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.BREADCRUMBS; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_breadcrumbs; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        if (!TextUtils.isEmpty(breadcrumbPath)) { //NOSONAR
            holder.breadcrumbView.changeBreadcrumbPath(breadcrumbPath); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        bindView(holder); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public void onBreadcrumbClick(BreadcrumbItem breadcrumbItem) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onBreadcrumbItemClick(breadcrumbItem); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<BreadcrumbsView> { //NOSONAR

        private BreadcrumbView breadcrumbView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            breadcrumbView = itemView.findViewById(R.id.breadcrumbs); //NOSONAR
            breadcrumbView.addBreadcrumbListener(item -> viewModel.onBreadcrumbClick(item)); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "BreadcrumbsView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
