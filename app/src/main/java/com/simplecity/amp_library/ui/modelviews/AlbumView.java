package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.GlidePalette;
import com.simplecity.amp_library.format.PrefixHighlighter;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.utils.PlaceholderProvider;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.StringUtils;
import com.simplecity.amp_library.utils.sorting.SortManager;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AlbumView extends MultiItemView<AlbumView.ViewHolder, Album> implements SectionedView { //NOSONAR

    public interface ClickListener { //NOSONAR

        void onAlbumClick(int position, AlbumView albumView, ViewHolder viewHolder); //NOSONAR

        boolean onAlbumLongClick(int position, AlbumView albumView); //NOSONAR

        void onAlbumOverflowClicked(View v, Album album); //NOSONAR
    }

    private static final String TAG = "AlbumView"; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Album album; //NOSONAR

    private int viewType; //NOSONAR

    private RequestManager requestManager; //NOSONAR

    private SortManager sortManager; //NOSONAR

    private PrefixHighlighter prefixHighlighter; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private char[] prefix; //NOSONAR

    private boolean showYear; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public AlbumView(Album album, @ViewType int viewType, RequestManager requestManager, SortManager sortManager, SettingsManager settingsManager) { //NOSONAR
        this.album = album; //NOSONAR
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
            listener.onAlbumClick(position, this, holder); //NOSONAR
        }
    }

    void onOverflowClick(View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onAlbumOverflowClicked(v, album); //NOSONAR
        }
    }

    boolean onAlbumLongclick(int position) { //NOSONAR
        if (listener != null) { //NOSONAR
            return listener.onAlbumLongClick(position, this); //NOSONAR
        }
        return false; //NOSONAR
    }

    public void showYear(boolean showYear) { //NOSONAR
        this.showYear = showYear; //NOSONAR
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

        holder.lineOne.setText(album.name); //NOSONAR

        holder.lineTwo.setVisibility(View.VISIBLE); //NOSONAR

        if (holder.albumCount != null) { //NOSONAR
            holder.albumCount.setVisibility(View.GONE); //NOSONAR
        }
        if (holder.trackCount != null) { //NOSONAR
            holder.trackCount.setVisibility(View.GONE); //NOSONAR
        }

        if (showYear) { //NOSONAR
            holder.lineTwo.setText(StringUtils.makeYearLabel(holder.itemView.getContext(), album.year)); //NOSONAR
        } else { //NOSONAR
            holder.lineTwo.setText(album.albumArtistName); //NOSONAR
        }

        if (getViewType() == ViewType.ALBUM_PALETTE) { //NOSONAR
            if (holder.bottomContainer != null) { //NOSONAR
                holder.bottomContainer.setBackgroundColor(0x20000000); //NOSONAR
            }
        }

        requestManager.load(album) //NOSONAR
                .listener(getViewType() == ViewType.ALBUM_PALETTE ? GlidePalette.with(album.getArtworkKey()) //NOSONAR
                        .use(GlidePalette.Profile.MUTED_DARK) //NOSONAR
                        .intoBackground(holder.bottomContainer) //NOSONAR
                        .crossfade(true) //NOSONAR
                        : null) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .placeholder(PlaceholderProvider.getInstance(holder.itemView.getContext()).getPlaceHolderDrawable(album.name, false, settingsManager)) //NOSONAR
                .into(holder.imageOne); //NOSONAR

        holder.overflowButton.setContentDescription(holder.itemView.getResources().getString(com.simplecity.amp_library.R.string.btn_options, album.name)); //NOSONAR

        if (prefixHighlighter != null) { //NOSONAR
            prefixHighlighter.setText(holder.lineOne, prefix); //NOSONAR
            prefixHighlighter.setText(holder.lineTwo, prefix); //NOSONAR
        }

        ViewCompat.setTransitionName(holder.imageOne, album.getArtworkKey()); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR
        //A partial bind. Due to the areContentsEqual implementation, the only reason this is called
        //is because the prefix changed. Update accordingly.
        if (prefixHighlighter != null) { //NOSONAR
            prefixHighlighter.setText(holder.lineOne, prefix); //NOSONAR
            prefixHighlighter.setText(holder.lineTwo, prefix); //NOSONAR
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

        int sortOrder = sortManager.getAlbumsSortOrder(); //NOSONAR
        String string = null; //NOSONAR
        boolean requiresSubstring = true; //NOSONAR
        switch (sortOrder) { //NOSONAR
            case SortManager.AlbumSort.DEFAULT: //NOSONAR
                string = StringUtils.keyFor(album.name); //NOSONAR
                break; //NOSONAR
            case SortManager.AlbumSort.NAME: //NOSONAR
                string = album.name; //NOSONAR
                break; //NOSONAR
            case SortManager.AlbumSort.ARTIST_NAME: //NOSONAR
                string = album.albumArtistName; //NOSONAR
                break; //NOSONAR
            case SortManager.AlbumSort.YEAR: //NOSONAR
                string = String.valueOf(album.year); //NOSONAR
                if (string.length() != 4) { //NOSONAR
                    string = "-"; //NOSONAR
                } else { //NOSONAR
                    string = string.substring(2, 4); //NOSONAR
                }
                requiresSubstring = false; //NOSONAR
                break; //NOSONAR
        }

        if (requiresSubstring) { //NOSONAR
            if (!TextUtils.isEmpty(string)) { //NOSONAR
                string = string.substring(0, 1).toUpperCase(); //NOSONAR
            } else { //NOSONAR
                string = " "; //NOSONAR
            }
        }

        return string; //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        AlbumView albumView = (AlbumView) o; //NOSONAR

        if (viewType != albumView.viewType) return false; //NOSONAR
        return album != null ? album.equals(albumView.album) : albumView.album == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = album != null ? album.hashCode() : 0; //NOSONAR
        result = 31 * result + viewType; //NOSONAR
        return result; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        if (other instanceof AlbumView) { //NOSONAR
            return album.equals(((AlbumView) other).album) && Arrays.equals(prefix, ((AlbumView) other).prefix); //NOSONAR
        }
        return false; //NOSONAR
    }

    public static class ViewHolder extends MultiItemView.ViewHolder<AlbumView> { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onItemClick(getAdapterPosition(), this)); //NOSONAR

            itemView.setOnLongClickListener(v -> viewModel.onAlbumLongclick(getAdapterPosition())); //NOSONAR

            overflowButton.setOnClickListener(v -> viewModel.onOverflowClick(v)); //NOSONAR
        }
    }
}
