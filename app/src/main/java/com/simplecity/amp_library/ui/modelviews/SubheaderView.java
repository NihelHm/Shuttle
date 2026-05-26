package com.simplecity.amp_library.ui.modelviews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;
import java.util.List;

import static com.simplecity.amp_library.R.id;
import static com.simplecity.amp_library.R.layout.list_item_subheader;
import static com.simplecity.amp_library.ui.adapters.ViewType.SUBHEADER;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SubheaderView extends BaseViewModel<SubheaderView.ViewHolder> { //NOSONAR

    protected String title; //NOSONAR

    public SubheaderView(String title) { //NOSONAR
        this.title = title; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return SUBHEADER; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_subheader; //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.textView.setText(title); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR

        holder.textView.setText(title); //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SubheaderView that = (SubheaderView) o; //NOSONAR

        return title != null ? title.equals(that.title) : that.title == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return title != null ? title.hashCode() : 0; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR

        if (other instanceof SubheaderView) { //NOSONAR
            return ((SubheaderView) other).title.equals(title); //NOSONAR
        }

        return false; //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        @BindView(id.textView) //NOSONAR
        TextView textView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR
        }
    }
}
