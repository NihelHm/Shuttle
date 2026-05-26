package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.simplecity.amp_library.model.Header; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

import static com.simplecity.amp_library.R.id.line_one; // NOSONAR
import static com.simplecity.amp_library.R.layout.list_item_section_separator; // NOSONAR
import static com.simplecity.amp_library.ui.adapters.ViewType.SEARCH_HEADER; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SearchHeaderView extends BaseViewModel<SearchHeaderView.ViewHolder> { //NOSONAR

    private Header header; //NOSONAR

    public SearchHeaderView(Header header) { //NOSONAR
        this.header = header; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return SEARCH_HEADER; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_section_separator; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(header.title); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        TextView lineOne; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            lineOne = itemView.findViewById(line_one); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "SearchHeaderView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SearchHeaderView that = (SearchHeaderView) o; //NOSONAR

        return header != null ? header.equals(that.header) : that.header == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return header != null ? header.hashCode() : 0; //NOSONAR
    } // NOSONAR
} // NOSONAR
