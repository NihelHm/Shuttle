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

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BreadcrumbsView extends BaseViewModel<BreadcrumbsView.ViewHolder> { //NOSONAR

    private String breadcrumbPath; //NOSONAR

    public BreadcrumbsView(String breadcrumbPath) { //NOSONAR
        this.breadcrumbPath = breadcrumbPath; //NOSONAR
    }

    public void setBreadcrumbsPath(String path) { //NOSONAR
        breadcrumbPath = path; //NOSONAR
    }

    @Nullable //NOSONAR
    private BreadcrumbListener listener; //NOSONAR

    public void setListener(@Nullable BreadcrumbListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.BREADCRUMBS; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_breadcrumbs; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        if (!TextUtils.isEmpty(breadcrumbPath)) { //NOSONAR
            holder.breadcrumbView.changeBreadcrumbPath(breadcrumbPath); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        bindView(holder); //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public void onBreadcrumbClick(BreadcrumbItem breadcrumbItem) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onBreadcrumbItemClick(breadcrumbItem); //NOSONAR
        }
    }

    public static class ViewHolder extends BaseViewHolder<BreadcrumbsView> { //NOSONAR

        private BreadcrumbView breadcrumbView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            breadcrumbView = itemView.findViewById(R.id.breadcrumbs); //NOSONAR
            breadcrumbView.addBreadcrumbListener(item -> viewModel.onBreadcrumbClick(item)); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "BreadcrumbsView.ViewHolder"; //NOSONAR
        }
    }
}
