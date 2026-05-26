package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

import static com.simplecity.amp_library.R.id; // NOSONAR
import static com.simplecity.amp_library.R.layout.list_item_disc_number; // NOSONAR
import static com.simplecity.amp_library.R.string.disc_number_label; // NOSONAR
import static com.simplecity.amp_library.ui.adapters.ViewType.DISC_NUMBER; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DiscNumberView extends BaseViewModel<DiscNumberView.ViewHolder> { //NOSONAR

    private int discNumber = 0; //NOSONAR

    public DiscNumberView(int discNumber) { //NOSONAR
        this.discNumber = discNumber; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return DISC_NUMBER; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_disc_number; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR
        holder.textView.setText(holder.itemView.getContext().getString(disc_number_label, discNumber)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        TextView textView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            textView = itemView.findViewById(id.textView); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        DiscNumberView that = (DiscNumberView) o; //NOSONAR

        return discNumber == that.discNumber; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return discNumber; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        return equals(other); //NOSONAR
    } // NOSONAR
} // NOSONAR
