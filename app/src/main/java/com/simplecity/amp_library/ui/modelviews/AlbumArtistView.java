package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.format.PrefixHighlighter;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.utils.PlaceholderProvider;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.StringUtils;
import com.simplecity.amp_library.utils.sorting.SortManager;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AlbumArtistView extends MultiItemView<AlbumArtistView.ViewHolder, AlbumArtist> implements //NOSONAR
        SectionedView { //NOSONAR

    public interface ClickListener { //NOSONAR

        void onAlbumArtistClick(int position, AlbumArtistView albumArtistView, ViewHolder viewholder); //NOSONAR

        boolean onAlbumArtistLongClick(int position, AlbumArtistView albumArtistView); //NOSONAR

        void onAlbumArtistOverflowClicked(View v, AlbumArtist albumArtist); //NOSONAR
    }

    private static final String TAG = "AlbumArtistView"; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public AlbumArtist albumArtist; //NOSONAR

    private int viewType; //NOSONAR

    private RequestManager requestManager; //NOSONAR

    private SortManager sortManager; //NOSONAR

    private PrefixHighlighter prefixHighlighter; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private char[] prefix; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public AlbumArtistView(AlbumArtist albumArtist, @ViewType int viewType, RequestManager requestManager, SortManager sortManager, SettingsManager settingsManager) { //NOSONAR
        this.albumArtist = albumArtist; //NOSONAR
        this.viewType = viewType; //NOSONAR
        this.requestManager = requestManager; //NOSONAR
        this.sortManager = sortManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    public void setClickListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    public void setPrefix(PrefixHighlighter prefixHighlighter, char[] prefix) { //NOSONAR
        this.prefixHighlighter = prefixHighlighter; //NOSONAR
        this.prefix = prefix; //NOSONAR
    }

    void onItemClick(int position, ViewHolder holder) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onAlbumArtistClick(position, this, holder); //NOSONAR
        }
    }

    void onOverflowClick(View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onAlbumArtistOverflowClicked(v, albumArtist); //NOSONAR
        }
    }

    boolean onItemLongClick(int positon) { //NOSONAR
        if (listener != null) { //NOSONAR
            return listener.onAlbumArtistLongClick(positon, this); //NOSONAR
        }
        return false; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return viewType; //NOSONAR
    }

    public void setViewType(int viewType) { //NOSONAR
        this.viewType = viewType; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(final ViewHolder holder) { //NOSONAR

        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(albumArtist.name); //NOSONAR

        if (holder.trackCount != null) { //NOSONAR
            holder.lineTwo.setVisibility(View.GONE); //NOSONAR
            holder.trackCount.setVisibility(View.VISIBLE); //NOSONAR
            holder.trackCount.setText(String.valueOf(albumArtist.getNumSongs())); //NOSONAR
        }
        if (holder.albumCount != null) { //NOSONAR
            holder.albumCount.setVisibility(View.VISIBLE); //NOSONAR
            holder.albumCount.setText(String.valueOf(albumArtist.getNumAlbums())); //NOSONAR
        }

        if (getViewType() == ViewType.ARTIST_PALETTE) { //NOSONAR
            if (holder.bottomContainer != null) { //NOSONAR
                holder.bottomContainer.setBackgroundColor(0x20000000); //NOSONAR
            }
        }

        requestManager.load(albumArtist) //NOSONAR
                .listener(getViewType() == ViewType.ARTIST_PALETTE ? GlidePalette.with(albumArtist.getArtworkKey()) //NOSONAR
                        .use(BitmapPalette.Profile.MUTED_DARK) //NOSONAR
                        .intoBackground(holder.bottomContainer) //NOSONAR
                        .crossfade(true) //NOSONAR
                        : null) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .placeholder(PlaceholderProvider.getInstance(holder.itemView.getContext()).getPlaceHolderDrawable(albumArtist.name, false, settingsManager)) //NOSONAR
                .into(holder.imageOne); //NOSONAR

        holder.overflowButton.setContentDescription(holder.itemView.getResources().getString(R.string.btn_options, albumArtist.name)); //NOSONAR

        if (prefixHighlighter != null) { //NOSONAR
            prefixHighlighter.setText(holder.lineOne, prefix); //NOSONAR
        }

        ViewCompat.setTransitionName(holder.imageOne, albumArtist.getArtworkKey()); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR
        //A partial bind. Due to the areContentsEqual implementation, the only reason this is called
        //is because the prefix changed. Update accordingly.
        if (prefixHighlighter != null) { //NOSONAR
            prefixHighlighter.setText(holder.lineOne, prefix); //NOSONAR
        }
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    @Override //NOSONAR
    public int getSpanSize(int spanCount) { //NOSONAR
        return 1; //NOSONAR
    }

    @Override //NOSONAR
    public String getSectionName() { //NOSONAR
        int sortOrder = sortManager.getArtistsSortOrder(); //NOSONAR

        String string = null; //NOSONAR
        switch (sortOrder) { //NOSONAR
            case SortManager.ArtistSort.DEFAULT: //NOSONAR
                string = StringUtils.keyFor(albumArtist.name); //NOSONAR
                break; //NOSONAR
            case SortManager.ArtistSort.NAME: //NOSONAR
                string = albumArtist.name; //NOSONAR
                break; //NOSONAR
        }

        if (!TextUtils.isEmpty(string)) { //NOSONAR
            string = string.substring(0, 1).toUpperCase(); //NOSONAR
        } else { //NOSONAR
            string = " "; //NOSONAR
        }

        return string; //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        AlbumArtistView that = (AlbumArtistView) o; //NOSONAR

        if (viewType != that.viewType) return false; //NOSONAR
        return albumArtist != null ? albumArtist.equals(that.albumArtist) : that.albumArtist == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = albumArtist != null ? albumArtist.hashCode() : 0; //NOSONAR
        result = 31 * result + viewType; //NOSONAR
        return result; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        if (other instanceof AlbumArtistView) { //NOSONAR
            return albumArtist.equals(((AlbumArtistView) other).albumArtist) && Arrays.equals(prefix, ((AlbumArtistView) other).prefix); //NOSONAR
        }
        return false; //NOSONAR
    }

    public static class ViewHolder extends MultiItemView.ViewHolder<AlbumArtistView> { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onItemClick(getAdapterPosition(), this)); //NOSONAR

            itemView.setOnLongClickListener(v -> viewModel.onItemLongClick(getAdapterPosition())); //NOSONAR

            overflowButton.setOnClickListener(v -> viewModel.onOverflowClick(v)); //NOSONAR
        }
    }
}
