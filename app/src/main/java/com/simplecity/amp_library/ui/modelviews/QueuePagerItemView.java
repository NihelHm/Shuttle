package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.ImageView; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.RequestManager; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QueuePagerItemView extends BaseViewModel<QueuePagerItemView.ViewHolder> { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Song song; //NOSONAR
    private RequestManager requestManager; //NOSONAR
    private SettingsManager settingsManager; //NOSONAR

    public QueuePagerItemView(Song song, RequestManager requestManager, SettingsManager settingsManager) { //NOSONAR
        this.song = song; //NOSONAR
        this.requestManager = requestManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.QUEUE_PAGER_ITEM; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_queue_pager; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        requestManager //NOSONAR
                .load(song) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
                .error(PlaceholderProvider.getInstance(holder.itemView.getContext()).getPlaceHolderDrawable(song.name, true, settingsManager)) //NOSONAR
                .into(holder.imageView); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<QueuePagerItemView> { //NOSONAR

        ImageView imageView; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            imageView = itemView.findViewById(R.id.imageView); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void recycle() { //NOSONAR
            super.recycle(); //NOSONAR

            Glide.clear(imageView); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        QueuePagerItemView that = (QueuePagerItemView) o; //NOSONAR

        return song != null ? song.equals(that.song) : that.song == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return song != null ? song.hashCode() : 0; //NOSONAR
    } // NOSONAR
} // NOSONAR
