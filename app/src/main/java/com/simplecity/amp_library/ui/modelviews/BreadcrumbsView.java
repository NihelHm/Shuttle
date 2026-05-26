package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.interfaces.BreadcrumbListener;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.ui.views.BreadcrumbItem;
import com.simplecity.amp_library.ui.views.BreadcrumbView;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class BreadcrumbsView extends BaseViewModel<BreadcrumbsView.ViewHolder> {

    private String breadcrumbPath;

    public BreadcrumbsView(String breadcrumbPath) {
        this.breadcrumbPath = breadcrumbPath;
    }

    public void setBreadcrumbsPath(String path) {
        breadcrumbPath = path;
    }

    @Nullable
    private BreadcrumbListener listener;

    public void setListener(@Nullable BreadcrumbListener listener) {
        this.listener = listener;
    }

    @Override
    public int getViewType() {
        return ViewType.BREADCRUMBS;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.list_item_breadcrumbs;
    }

    @Override
    public void bindView(ViewHolder holder) {
        super.bindView(holder);

        if (!TextUtils.isEmpty(breadcrumbPath)) {
            holder.breadcrumbView.changeBreadcrumbPath(breadcrumbPath);
        }
    }

    @Override
    public void bindView(ViewHolder holder, int position, List payloads) {
        bindView(holder);
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(createView(parent));
    }

    public void onBreadcrumbClick(BreadcrumbItem breadcrumbItem) {
        if (listener != null) {
            listener.onBreadcrumbItemClick(breadcrumbItem);
        }
    }

    public static class ViewHolder extends BaseViewHolder<BreadcrumbsView> {

        private BreadcrumbView breadcrumbView;

        public ViewHolder(View itemView) {
            super(itemView);

            breadcrumbView = itemView.findViewById(R.id.breadcrumbs);
            breadcrumbView.addBreadcrumbListener(item -> viewModel.onBreadcrumbClick(item));
        }

        @Override
        public String toString() {
            return "BreadcrumbsView.ViewHolder";
        }
    }
}
