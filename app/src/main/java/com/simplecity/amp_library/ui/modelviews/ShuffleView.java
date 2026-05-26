package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.StringRes; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ShuffleView extends BaseViewModel<ShuffleView.ViewHolder> { //NOSONAR

    public interface ShuffleClickListener { //NOSONAR
        void onShuffleItemClick(); //NOSONAR
    } // NOSONAR

    @StringRes //NOSONAR
    private int titleResId = R.string.shuffle_all; //NOSONAR

    public void setTitleResId(int titleResId) { //NOSONAR
        this.titleResId = titleResId; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    private ShuffleClickListener listener; //NOSONAR

    public void setClickListener(@Nullable ShuffleClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.SHUFFLE; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_shuffle; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.title.setText(titleResId); //NOSONAR
    } // NOSONAR

    void onItemClick() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onShuffleItemClick(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<ShuffleView> { //NOSONAR

        @BindView(R.id.title) //NOSONAR
        TextView title; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onItemClick()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "ShuffleView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
