package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.simplecity.amp_library.model.Playlist;
import com.simplecity.amp_library.ui.views.NonScrollImageButton;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

import static com.simplecity.amp_library.R.id.btn_overflow;
import static com.simplecity.amp_library.R.id.line_one;
import static com.simplecity.amp_library.R.layout.list_item_one_line;
import static com.simplecity.amp_library.R.string.btn_options;
import static com.simplecity.amp_library.ui.adapters.ViewType.PLAYLIST;
import static com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragment.PlaylistClickListener;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlaylistView extends BaseViewModel<PlaylistView.ViewHolder> { //NOSONAR

    public interface OnClickListener { //NOSONAR

        void onPlaylistClick(int position, PlaylistView playlistView); //NOSONAR

        void onPlaylistOverflowClick(int position, View v, Playlist playlist); //NOSONAR
    }

    @SuppressWarnings("java:S1104") //NOSONAR

    public Playlist playlist; //NOSONAR

    @Nullable //NOSONAR
    private OnClickListener listener; //NOSONAR

    public PlaylistView(Playlist playlist) { //NOSONAR
        this.playlist = playlist; //NOSONAR
    }

    public void setListener(@Nullable OnClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return PLAYLIST; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return list_item_one_line; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(playlist.name); //NOSONAR
        holder.overflowButton.setContentDescription(holder.itemView.getResources().getString(btn_options, playlist.name)); //NOSONAR
    }

    void onPlaylistClicked(int position) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onPlaylistClick(position, this); //NOSONAR
        }
    }

    void onOverflowClicked(int position, View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onPlaylistOverflowClick(position, v, playlist); //NOSONAR
        }
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder<PlaylistView> { //NOSONAR

        @SuppressWarnings("java:S1104") //NOSONAR

        public TextView lineOne; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public NonScrollImageButton overflowButton; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public PlaylistClickListener listener; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            lineOne = itemView.findViewById(line_one); //NOSONAR
            overflowButton = itemView.findViewById(btn_overflow); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onPlaylistClicked(getAdapterPosition())); //NOSONAR
            overflowButton.setOnClickListener(v -> viewModel.onOverflowClicked(getAdapterPosition(), v)); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "PlaylistView.ViewHolder"; //NOSONAR
        }
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        PlaylistView that = (PlaylistView) o; //NOSONAR

        return playlist != null ? playlist.equals(that.playlist) : that.playlist == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return playlist != null ? playlist.hashCode() : 0; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        return equals(other); //NOSONAR
    }
}
