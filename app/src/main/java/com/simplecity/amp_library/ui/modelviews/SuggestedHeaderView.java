package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.SuggestedHeader; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SuggestedHeaderView extends BaseViewModel<SuggestedHeaderView.ViewHolder> { //NOSONAR

    public interface ClickListener { //NOSONAR
        void onSuggestedHeaderClick(SuggestedHeader suggestedHeader); //NOSONAR
    } // NOSONAR

    private SuggestedHeader suggestedHeader; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public SuggestedHeaderView(SuggestedHeader suggestedHeader) { //NOSONAR
        this.suggestedHeader = suggestedHeader; //NOSONAR
    } // NOSONAR

    public void setClickListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    void onClick() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onSuggestedHeaderClick(suggestedHeader); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.SUGGESTED_HEADER; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.suggested_header; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.titleOne.setText(suggestedHeader.title); //NOSONAR
        holder.titleTwo.setText(suggestedHeader.subtitle); //NOSONAR
        if (suggestedHeader.subtitle == null || suggestedHeader.subtitle.length() == 0) { //NOSONAR
            holder.titleTwo.setVisibility(View.GONE); //NOSONAR
        } else { //NOSONAR
            holder.titleTwo.setVisibility(View.VISIBLE); //NOSONAR
        } // NOSONAR

        holder.itemView.setContentDescription(suggestedHeader.title); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<SuggestedHeaderView> { //NOSONAR

        @BindView(R.id.text1) //NOSONAR
        TextView titleOne; //NOSONAR

        @BindView(R.id.text2) //NOSONAR
        TextView titleTwo; //NOSONAR

        @BindView(R.id.button) //NOSONAR
        TextView button; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onClick()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "SuggestedHeaderView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SuggestedHeaderView that = (SuggestedHeaderView) o; //NOSONAR

        return suggestedHeader != null ? suggestedHeader.equals(that.suggestedHeader) : that.suggestedHeader == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return suggestedHeader != null ? suggestedHeader.hashCode() : 0; //NOSONAR
    } // NOSONAR
} // NOSONAR
