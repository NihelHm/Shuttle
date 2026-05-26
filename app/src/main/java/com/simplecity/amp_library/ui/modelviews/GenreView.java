package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Genre; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.ui.views.NonScrollImageButton; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class GenreView extends BaseViewModel<GenreView.ViewHolder> implements //NOSONAR
        SectionedView { //NOSONAR

    public interface ClickListener { //NOSONAR

        void onItemClick(Genre genre); //NOSONAR

        void onOverflowClick(View v, Genre genre); //NOSONAR
    } // NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public Genre genre; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener clickListener; //NOSONAR

    public GenreView(Genre genre) { //NOSONAR
        this.genre = genre; //NOSONAR
    } // NOSONAR

    public void setClickListener(@Nullable ClickListener clickListener) { //NOSONAR
        this.clickListener = clickListener; //NOSONAR
    } // NOSONAR

    void onClick() { //NOSONAR
        if (clickListener != null) { //NOSONAR
            clickListener.onItemClick(genre); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void onOverflowClick(View v) { //NOSONAR
        if (clickListener != null) { //NOSONAR
            clickListener.onOverflowClick(v, genre); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.GENRE; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_two_lines; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(genre.name); //NOSONAR
        String albumAndSongsLabel = StringUtils.makeAlbumAndSongsLabel(holder.itemView.getContext(), -1, genre.numSongs); //NOSONAR
        if (!TextUtils.isEmpty(albumAndSongsLabel)) { //NOSONAR
            holder.lineTwo.setText(albumAndSongsLabel); //NOSONAR
            holder.lineTwo.setVisibility(View.VISIBLE); //NOSONAR
        } else { //NOSONAR
            holder.lineTwo.setVisibility(View.GONE); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getSectionName() { //NOSONAR

        String string = StringUtils.keyFor(genre.name); //NOSONAR
        if (!TextUtils.isEmpty(string)) { //NOSONAR
            string = string.substring(0, 1).toUpperCase(); //NOSONAR
        } else { //NOSONAR
            string = " "; //NOSONAR
        } // NOSONAR

        return string; //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<GenreView> { //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineOne; //NOSONAR

        @BindView(R.id.line_two) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineTwo; //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public NonScrollImageButton overflowButton; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onClick()); //NOSONAR

            overflowButton.setOnClickListener(v -> viewModel.onOverflowClick(v)); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "GenreView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        GenreView genreView = (GenreView) o; //NOSONAR

        return genre != null ? genre.equals(genreView.genre) : genreView.genre == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return genre != null ? genre.hashCode() : 0; //NOSONAR
    } // NOSONAR
} // NOSONAR
