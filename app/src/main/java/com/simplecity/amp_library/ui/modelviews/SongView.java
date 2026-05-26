package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.view.MotionEvent; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.RequestManager; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.format.PrefixHighlighter; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.ui.views.NonScrollImageButton; // NOSONAR
import com.simplecity.amp_library.ui.views.PlayCountView; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR
import java.util.Arrays; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SongView extends BaseSelectableViewModel<SongView.ViewHolder> implements SectionedView { //NOSONAR

    public interface ClickListener { //NOSONAR

        void onSongClick(int position, SongView songView); //NOSONAR

        boolean onSongLongClick(int position, SongView songView); //NOSONAR

        void onSongOverflowClick(int position, View v, Song song); //NOSONAR

        void onStartDrag(ViewHolder holder); //NOSONAR
    } // NOSONAR

    private static final String TAG = "SongView"; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Song song; //NOSONAR

    private RequestManager requestManager; //NOSONAR

    private SortManager sortManager; //NOSONAR

    private PrefixHighlighter prefixHighlighter; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private char[] prefix; //NOSONAR

    private boolean editable; //NOSONAR

    private boolean showAlbumArt; //NOSONAR

    private boolean showPlayCount; //NOSONAR

    private boolean showTrackNumber; //NOSONAR

    private boolean showArtistName = true; //NOSONAR

    private boolean showAlbumName = true; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public SongView(Song song, RequestManager requestManager, SortManager sortManager, SettingsManager settingsManager) { //NOSONAR
        this.song = song; //NOSONAR
        this.requestManager = requestManager; //NOSONAR
        this.sortManager = sortManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    public void setClickListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    public void setEditable(boolean editable) { //NOSONAR
        this.editable = editable; //NOSONAR
    } // NOSONAR

    public void showAlbumArt(boolean showAlbumArt) { //NOSONAR
        this.showAlbumArt = showAlbumArt; //NOSONAR
    } // NOSONAR

    public boolean getShowAlbumArt() { //NOSONAR
        return showAlbumArt; //NOSONAR
    } // NOSONAR

    public void showPlayCount(boolean showPlayCount) { //NOSONAR
        this.showPlayCount = showPlayCount; //NOSONAR
    } // NOSONAR

    public void showArtistName(boolean showArtistName) { //NOSONAR
        this.showArtistName = showArtistName; //NOSONAR
    } // NOSONAR

    public void showAlbumName(boolean showAlbumName) { //NOSONAR
        this.showAlbumName = showAlbumName; //NOSONAR
    } // NOSONAR

    public void setPrefix(PrefixHighlighter prefixHighlighter, char[] prefix) { //NOSONAR
        this.prefixHighlighter = prefixHighlighter; //NOSONAR
        this.prefix = prefix; //NOSONAR
    } // NOSONAR

    public void setShowTrackNumber(boolean showTrackNumber) { //NOSONAR
        this.showTrackNumber = showTrackNumber; //NOSONAR
    } // NOSONAR

    void onItemClick(int position) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onSongClick(position, this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void onOverflowClick(int position, View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onSongOverflowClick(position, v, song); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    boolean onItemLongClick(int position) { //NOSONAR
        if (listener != null) { //NOSONAR
            return listener.onSongLongClick(position, this); //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    void onStartDrag(ViewHolder holder) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onStartDrag(holder); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return editable ? ViewType.SONG_EDITABLE : ViewType.SONG; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return editable ? R.layout.list_item_edit : R.layout.list_item_two_lines; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(song.name); //NOSONAR

        if (holder.playCount != null) { //NOSONAR
            if (showPlayCount && song.playCount > 1) { //NOSONAR
                holder.playCount.setVisibility(View.VISIBLE); //NOSONAR
                holder.playCount.setCount(song.playCount); //NOSONAR
            } else { //NOSONAR
                holder.playCount.setVisibility(View.GONE); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (showArtistName && showAlbumName) { //NOSONAR
            holder.lineTwo.setText(String.format("%s - %s", song.artistName, song.albumName)); //NOSONAR
            holder.lineTwo.setVisibility(View.VISIBLE); //NOSONAR
        } else if (showAlbumName) { //NOSONAR
            holder.lineTwo.setText(song.albumName); //NOSONAR
            holder.lineTwo.setVisibility(View.VISIBLE); //NOSONAR
        } else { //NOSONAR
            holder.lineTwo.setVisibility(View.GONE); //NOSONAR
        } // NOSONAR

        holder.lineThree.setText(song.getDurationLabel(holder.itemView.getContext())); //NOSONAR

        if (holder.artwork != null) { //NOSONAR
            if (showAlbumArt && settingsManager.showArtworkInQueue()) { //NOSONAR
                holder.artwork.setVisibility(View.VISIBLE); //NOSONAR
                requestManager.load(song) //NOSONAR
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                        .placeholder(PlaceholderProvider.getInstance(holder.itemView.getContext()).getPlaceHolderDrawable(song.albumName, false, settingsManager)) //NOSONAR
                        .into(holder.artwork); //NOSONAR
            } else { //NOSONAR
                holder.artwork.setVisibility(View.GONE); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        holder.overflowButton.setContentDescription(holder.itemView.getResources().getString(R.string.btn_options, song.name)); //NOSONAR

        if (prefixHighlighter != null) { //NOSONAR
            prefixHighlighter.setText(holder.lineOne, prefix); //NOSONAR
            prefixHighlighter.setText(holder.lineTwo, prefix); //NOSONAR
        } // NOSONAR

        if (holder.trackNumber != null) { //NOSONAR
            if (showTrackNumber) { //NOSONAR
                holder.trackNumber.setVisibility(View.VISIBLE); //NOSONAR
                holder.trackNumber.setText(String.valueOf(song.track)); //NOSONAR
            } else { //NOSONAR
                holder.trackNumber.setVisibility(View.GONE); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR

        //A partial bind. Due to the areContentsEqual implementation, the only reason this is called // NOSONAR
        //is because the prefix changed. Update accordingly. // NOSONAR
        if (prefixHighlighter != null) { //NOSONAR
            prefixHighlighter.setText(holder.lineOne, prefix); //NOSONAR
            prefixHighlighter.setText(holder.lineTwo, prefix); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getSectionName() { //NOSONAR
        int sortOrder = sortManager.getSongsSortOrder(); //NOSONAR

        if (sortOrder != SortManager.SongSort.DATE //NOSONAR
                && sortOrder != SortManager.SongSort.DURATION //NOSONAR
                && sortOrder != SortManager.SongSort.TRACK_NUMBER) { //NOSONAR

            String string = null; //NOSONAR
            boolean requiresSubstring = true; //NOSONAR
            switch (sortOrder) { //NOSONAR
                case SortManager.SongSort.DEFAULT: //NOSONAR
                    string = StringUtils.keyFor(song.name); //NOSONAR
                    break; //NOSONAR
                case SortManager.SongSort.NAME: //NOSONAR
                    string = song.name; //NOSONAR
                    break; //NOSONAR
                case SortManager.SongSort.YEAR: //NOSONAR
                    string = String.valueOf(song.year); //NOSONAR
                    if (string.length() != 4) { //NOSONAR
                        string = "-"; //NOSONAR
                    } else { //NOSONAR
                        string = string.substring(2, 4); //NOSONAR
                    } // NOSONAR
                    requiresSubstring = false; //NOSONAR
                    break; //NOSONAR
                case SortManager.SongSort.ALBUM_NAME: //NOSONAR
                    string = StringUtils.keyFor(song.albumName); //NOSONAR
                    break; //NOSONAR
                case SortManager.SongSort.ARTIST_NAME: //NOSONAR
                    string = StringUtils.keyFor(song.artistName); //NOSONAR
                    break; //NOSONAR
            } // NOSONAR

            if (requiresSubstring) { //NOSONAR
                if (!TextUtils.isEmpty(string)) { //NOSONAR
                    string = string.substring(0, 1).toUpperCase(); //NOSONAR
                } else { //NOSONAR
                    string = " "; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            return string; //NOSONAR
        } // NOSONAR
        return ""; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        if (other instanceof SongView) { //NOSONAR
            return this.song.equals(((SongView) other).song) //NOSONAR
                    && Arrays.equals(prefix, ((SongView) other).prefix); //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SongView songView = (SongView) o; //NOSONAR

        if (editable != songView.editable) return false; //NOSONAR
        if (showAlbumArt != songView.showAlbumArt) return false; //NOSONAR
        if (showPlayCount != songView.showPlayCount) return false; //NOSONAR
        if (showTrackNumber != songView.showTrackNumber) return false; //NOSONAR
        if (showArtistName != songView.showArtistName) return false; //NOSONAR
        if (showAlbumName != songView.showAlbumName) return false; //NOSONAR
        return song != null ? song.equals(songView.song) : songView.song == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = song != null ? song.hashCode() : 0; //NOSONAR
        result = 31 * result + (editable ? 1 : 0); //NOSONAR
        result = 31 * result + (showAlbumArt ? 1 : 0); //NOSONAR
        result = 31 * result + (showPlayCount ? 1 : 0); //NOSONAR
        result = 31 * result + (showTrackNumber ? 1 : 0); //NOSONAR
        result = 31 * result + (showArtistName ? 1 : 0); //NOSONAR
        result = 31 * result + (showAlbumName ? 1 : 0); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<SongView> { //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        TextView lineOne; //NOSONAR

        @BindView(R.id.line_two) //NOSONAR
        TextView lineTwo; //NOSONAR

        @BindView(R.id.line_three) //NOSONAR
        TextView lineThree; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.trackNumber) //NOSONAR
        TextView trackNumber; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.play_count) //NOSONAR
        PlayCountView playCount; //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public NonScrollImageButton overflowButton; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.drag_handle) //NOSONAR
        ImageView dragHandle; //NOSONAR

        @Nullable //NOSONAR
        @BindView(R.id.image) //NOSONAR
        ImageView artwork; //NOSONAR

        ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onItemClick(getAdapterPosition())); //NOSONAR
            itemView.setOnLongClickListener(v -> viewModel.onItemLongClick(getAdapterPosition())); //NOSONAR

            overflowButton.setOnClickListener(v -> viewModel.onOverflowClick(getAdapterPosition(), v)); //NOSONAR

            if (dragHandle != null) { //NOSONAR
                dragHandle.setOnTouchListener((v, event) -> { //NOSONAR
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) { //NOSONAR
                        viewModel.onStartDrag(this); //NOSONAR
                    } // NOSONAR
                    return true; //NOSONAR
                }); // NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "SongView.ViewHolder"; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void recycle() { //NOSONAR
            super.recycle(); //NOSONAR

            if (artwork != null) { //NOSONAR
                Glide.clear(artwork); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
