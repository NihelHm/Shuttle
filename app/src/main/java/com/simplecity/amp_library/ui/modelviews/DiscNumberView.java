package com.simplecity.amp_library.ui.modelviews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

import static com.simplecity.amp_library.R.id;
import static com.simplecity.amp_library.R.layout.list_item_disc_number;
import static com.simplecity.amp_library.R.string.disc_number_label;
import static com.simplecity.amp_library.ui.adapters.ViewType.DISC_NUMBER;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DiscNumberView extends BaseViewModel<DiscNumberView.ViewHolder> { //NOSONAR

    private int discNumber = 0; //NOSONAR

    public DiscNumberView(int discNumber) { //NOSONAR
        this.discNumber = discNumber; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return DISC_NUMBER; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_disc_number; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR
        holder.textView.setText(holder.itemView.getContext().getString(disc_number_label, discNumber)); //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        TextView textView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            textView = itemView.findViewById(id.textView); //NOSONAR
        }
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        DiscNumberView that = (DiscNumberView) o; //NOSONAR

        return discNumber == that.discNumber; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return discNumber; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        return equals(other); //NOSONAR
    }
}
