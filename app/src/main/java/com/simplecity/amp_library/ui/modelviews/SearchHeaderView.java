package com.simplecity.amp_library.ui.modelviews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.simplecity.amp_library.model.Header;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

import static com.simplecity.amp_library.R.id.line_one;
import static com.simplecity.amp_library.R.layout.list_item_section_separator;
import static com.simplecity.amp_library.ui.adapters.ViewType.SEARCH_HEADER;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SearchHeaderView extends BaseViewModel<SearchHeaderView.ViewHolder> { //NOSONAR

    private Header header; //NOSONAR

    public SearchHeaderView(Header header) { //NOSONAR
        this.header = header; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return SEARCH_HEADER; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_section_separator; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(header.title); //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        TextView lineOne; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            lineOne = itemView.findViewById(line_one); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "SearchHeaderView.ViewHolder"; //NOSONAR
        }
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SearchHeaderView that = (SearchHeaderView) o; //NOSONAR

        return header != null ? header.equals(that.header) : that.header == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return header != null ? header.hashCode() : 0; //NOSONAR
    }
}
