package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ShuffleView extends BaseViewModel<ShuffleView.ViewHolder> { //NOSONAR

    public interface ShuffleClickListener { //NOSONAR
        void onShuffleItemClick(); //NOSONAR
    }

    @StringRes //NOSONAR
    private int titleResId = R.string.shuffle_all; //NOSONAR

    public void setTitleResId(int titleResId) { //NOSONAR
        this.titleResId = titleResId; //NOSONAR
    }

    @Nullable //NOSONAR
    private ShuffleClickListener listener; //NOSONAR

    public void setClickListener(@Nullable ShuffleClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.SHUFFLE; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_shuffle; //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.title.setText(titleResId); //NOSONAR
    }

    void onItemClick() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onShuffleItemClick(); //NOSONAR
        }
    }

    public static class ViewHolder extends BaseViewHolder<ShuffleView> { //NOSONAR

        @BindView(R.id.title) //NOSONAR
        TextView title; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onItemClick()); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "ShuffleView.ViewHolder"; //NOSONAR
        }
    }
}
