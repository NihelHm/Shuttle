package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.view.View; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.ui.views.NonScrollImageButton; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class MultiItemView<VH extends MultiItemView.ViewHolder, T> extends BaseSelectableViewModel<VH> { //NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR

        switch (getViewType()) { //NOSONAR
            case ViewType.ARTIST_LIST: //NOSONAR
            case ViewType.ALBUM_LIST: //NOSONAR
                return R.layout.list_item_image; //NOSONAR
            case ViewType.ARTIST_CARD: //NOSONAR
            case ViewType.ARTIST_CARD_LARGE: //NOSONAR
            case ViewType.ALBUM_CARD: //NOSONAR
            case ViewType.ALBUM_CARD_LARGE: //NOSONAR
            case ViewType.SUGGESTED_SONG: //NOSONAR
                return R.layout.grid_item_card; //NOSONAR
            case ViewType.ARTIST_PALETTE: //NOSONAR
            case ViewType.ALBUM_PALETTE: //NOSONAR
                return R.layout.grid_item_palette; //NOSONAR
            case ViewType.ARTIST_GRID: //NOSONAR
            case ViewType.ALBUM_GRID: //NOSONAR
                return R.layout.grid_item; //NOSONAR
            case ViewType.ARTIST_LIST_SMALL: //NOSONAR
            case ViewType.ALBUM_LIST_SMALL: //NOSONAR
                return R.layout.list_item_small; //NOSONAR
        } // NOSONAR
        throw new IllegalStateException("getLayoutResId() invalid ViewType. Class: " + getClass().getSimpleName()); //NOSONAR
    } // NOSONAR

    public static class ViewHolder<T extends ViewModel> extends BaseViewHolder<T> { //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineOne; //NOSONAR

        @BindView(R.id.line_two) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineTwo; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.albumCount) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView albumCount; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.trackCount) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView trackCount; //NOSONAR

        @BindView(R.id.image) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public ImageView imageOne; //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public NonScrollImageButton overflowButton; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.bottom_container) //NOSONAR
        View bottomContainer; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.tickImage) //NOSONAR
        ImageView tickImageView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "MultiItemView.ViewHolder"; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void recycle() { //NOSONAR
            super.recycle(); //NOSONAR

            Glide.clear(imageOne); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(VH holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        if (holder.tickImageView != null) { //NOSONAR
            holder.tickImageView.setVisibility(isSelected() ? View.VISIBLE : View.GONE); //NOSONAR
        } // NOSONAR

        int viewType = getViewType(); //NOSONAR
        if (viewType == ViewType.ARTIST_GRID || viewType == ViewType.ALBUM_GRID) { //NOSONAR
            if (holder.bottomContainer != null) { //NOSONAR
                holder.bottomContainer.setBackgroundColor(0x90000000); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(VH holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR

        if (holder.tickImageView != null) { //NOSONAR
            holder.tickImageView.setVisibility(isSelected() ? View.VISIBLE : View.GONE); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
