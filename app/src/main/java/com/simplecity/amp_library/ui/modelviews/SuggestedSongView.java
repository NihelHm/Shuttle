package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import com.bumptech.glide.RequestManager; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SuggestedSongView extends MultiItemView<SuggestedSongView.ViewHolder, Song> { //NOSONAR

    public interface ClickListener { //NOSONAR

        void onSongClick(Song song, ViewHolder holder); //NOSONAR

        void onSongOverflowClicked(View v, int position, Song song); //NOSONAR
    } // NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Song song; //NOSONAR

    private RequestManager requestManager; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public SuggestedSongView(Song song, RequestManager requestManager, SettingsManager settingsManager) { //NOSONAR
        this.song = song; //NOSONAR
        this.requestManager = requestManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    void onItemClick(ViewHolder holder) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onSongClick(song, holder); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void onOverflowClick(View v, ViewHolder viewHolder) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onSongOverflowClicked(v, viewHolder.getAdapterPosition(), song); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void setClickListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.grid_item_horizontal; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.SUGGESTED_SONG; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(final ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(song.name); //NOSONAR
        holder.lineTwo.setText(song.artistName); //NOSONAR
        holder.lineTwo.setVisibility(View.VISIBLE); //NOSONAR
        if (holder.albumCount != null) { //NOSONAR
            holder.albumCount.setVisibility(View.GONE); //NOSONAR
        } // NOSONAR
        if (holder.trackCount != null) { //NOSONAR
            holder.trackCount.setVisibility(View.GONE); //NOSONAR
        } // NOSONAR

        requestManager.load(song) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .placeholder(PlaceholderProvider.getInstance(holder.imageOne.getContext()).getPlaceHolderDrawable(song.albumName, false, settingsManager)) //NOSONAR
                .into(holder.imageOne); //NOSONAR

        holder.overflowButton.setContentDescription(holder.itemView.getResources().getString(R.string.btn_options, song.name)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SuggestedSongView that = (SuggestedSongView) o; //NOSONAR

        return song != null ? song.equals(that.song) : that.song == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return song != null ? song.hashCode() : 0; //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends MultiItemView.ViewHolder<SuggestedSongView> { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onItemClick(this)); //NOSONAR

            overflowButton.setOnClickListener(v -> viewModel.onOverflowClick(v, this)); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
