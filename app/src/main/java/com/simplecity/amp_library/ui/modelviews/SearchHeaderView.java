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

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class SearchHeaderView extends BaseViewModel<SearchHeaderView.ViewHolder> {

    private Header header;

    public SearchHeaderView(Header header) {
        this.header = header;
    }

    @Override
    public int getViewType() {
        return SEARCH_HEADER;
    }

    @Override
    public int getLayoutResId() {
        return list_item_section_separator;
    }

    @Override
    public void bindView(ViewHolder holder) {
        super.bindView(holder);

        holder.lineOne.setText(header.title);
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        TextView lineOne;

        public ViewHolder(View itemView) {
            super(itemView);

            lineOne = itemView.findViewById(line_one);
        }

        @Override
        public String toString() {
            return "SearchHeaderView.ViewHolder";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchHeaderView that = (SearchHeaderView) o;

        return header != null ? header.equals(that.header) : that.header == null;
    }

    @Override
    public int hashCode() {
        return header != null ? header.hashCode() : 0;
    }
}
