@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue // NOSONAR

import android.text.TextUtils // NOSONAR
import android.view.MotionEvent // NOSONAR
import android.view.View // NOSONAR
import android.view.ViewGroup // NOSONAR
import android.widget.ImageView // NOSONAR
import android.widget.TextView // NOSONAR
import butterknife.BindView // NOSONAR
import butterknife.ButterKnife // NOSONAR
import com.bumptech.glide.Glide // NOSONAR
import com.bumptech.glide.RequestManager // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType // NOSONAR
import com.simplecity.amp_library.ui.modelviews.BaseSelectableViewModel // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SectionedView // NOSONAR
import com.simplecity.amp_library.ui.views.NonScrollImageButton // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.StringUtils // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder // NOSONAR

class QueueViewBinder( //NOSONAR
    val queueItem: QueueItem, //NOSONAR
    private val requestManager: RequestManager, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val settingsManager: SettingsManager //NOSONAR
) : // NOSONAR
    BaseSelectableViewModel<QueueViewBinder.ViewHolder>(), //NOSONAR
    SectionedView { //NOSONAR

    interface ClickListener { //NOSONAR

        fun onQueueItemClick(position: Int, queueViewBinder: QueueViewBinder) //NOSONAR

        fun onQueueItemLongClick(position: Int, queueViewBinder: QueueViewBinder): Boolean //NOSONAR

        fun onQueueItemOverflowClick(position: Int, v: View, queueViewBinder: QueueViewBinder) //NOSONAR

        fun onStartDrag(holder: ViewHolder) //NOSONAR
    } // NOSONAR

    private var showAlbumArt: Boolean = false //NOSONAR

    var isCurrentTrack: Boolean = false //NOSONAR

    private var listener: ClickListener? = null //NOSONAR

    fun setClickListener(listener: ClickListener?) { //NOSONAR
        this.listener = listener //NOSONAR
    } // NOSONAR

    fun showAlbumArt(showAlbumArt: Boolean) { //NOSONAR
        this.showAlbumArt = showAlbumArt //NOSONAR
    } // NOSONAR

    internal fun onItemClick(position: Int) { //NOSONAR
        listener?.onQueueItemClick(position, this) //NOSONAR
    } // NOSONAR

    internal fun onOverflowClick(position: Int, v: View) { //NOSONAR
        listener?.onQueueItemOverflowClick(position, v, this) //NOSONAR
    } // NOSONAR

    internal fun onItemLongClick(position: Int): Boolean { //NOSONAR
        return listener?.onQueueItemLongClick(position, this) ?: false //NOSONAR
    } // NOSONAR

    internal fun onStartDrag(holder: ViewHolder) { //NOSONAR
        listener?.onStartDrag(holder) //NOSONAR
    } // NOSONAR

    override fun getViewType(): Int { //NOSONAR
        return ViewType.SONG_EDITABLE //NOSONAR
    } // NOSONAR

    override fun getLayoutResId(): Int { //NOSONAR
        return R.layout.list_item_edit //NOSONAR
    } // NOSONAR

    override fun bindView(holder: ViewHolder) { //NOSONAR
        super.bindView(holder) //NOSONAR

        holder.lineOne.text = queueItem.song.name //NOSONAR

        holder.lineTwo.text = String.format("%s • %s", queueItem.song.artistName, queueItem.song.albumName) //NOSONAR
        holder.lineTwo.visibility = View.VISIBLE //NOSONAR

        holder.lineThree.text = queueItem.song.getDurationLabel(holder.itemView.context) //NOSONAR

        holder.dragHandle.isActivated = isCurrentTrack //NOSONAR

        holder.artwork?.let { artwork -> //NOSONAR
            if (showAlbumArt && settingsManager.showArtworkInQueue()) { //NOSONAR
                artwork.visibility = View.VISIBLE //NOSONAR
                requestManager.load<Song>(queueItem.song) //NOSONAR
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                    .placeholder(PlaceholderProvider.getInstance(holder.itemView.context).getPlaceHolderDrawable(queueItem.song.albumName, false, settingsManager)) //NOSONAR
                    .into(artwork) //NOSONAR
            } else { //NOSONAR
                artwork.visibility = View.GONE //NOSONAR
            } // NOSONAR
        } // NOSONAR

        holder.overflowButton.contentDescription = holder.itemView.resources.getString(R.string.btn_options, queueItem.song.name) //NOSONAR
    } // NOSONAR

    override fun bindView(holder: ViewHolder, position: Int, payloads: List<*>) { //NOSONAR
        super.bindView(holder, position, payloads) //NOSONAR

        holder.dragHandle.isActivated = isCurrentTrack //NOSONAR
    } // NOSONAR

    override fun createViewHolder(parent: ViewGroup): ViewHolder { //NOSONAR
        return ViewHolder(createView(parent)) //NOSONAR
    } // NOSONAR

    override fun getSectionName(): String { //NOSONAR
        val sortOrder = sortManager.songsSortOrder //NOSONAR

        if (sortOrder != SortManager.SongSort.DATE //NOSONAR
            && sortOrder != SortManager.SongSort.DURATION //NOSONAR
            && sortOrder != SortManager.SongSort.TRACK_NUMBER //NOSONAR
        ) { // NOSONAR

            var string = "" //NOSONAR
            var requiresSubstring = true //NOSONAR
            when (sortOrder) { //NOSONAR
                SortManager.SongSort.DEFAULT -> string = StringUtils.keyFor(queueItem.song.name) //NOSONAR
                SortManager.SongSort.NAME -> string = queueItem.song.name //NOSONAR
                SortManager.SongSort.YEAR -> { //NOSONAR
                    string = queueItem.song.year.toString() //NOSONAR
                    if (string.length != 4) { //NOSONAR
                        string = "-" //NOSONAR
                    } else { //NOSONAR
                        string = string.substring(2, 4) //NOSONAR
                    } // NOSONAR
                    requiresSubstring = false //NOSONAR
                } // NOSONAR
                SortManager.SongSort.ALBUM_NAME -> string = StringUtils.keyFor(queueItem.song.albumName) //NOSONAR
                SortManager.SongSort.ARTIST_NAME -> string = StringUtils.keyFor(queueItem.song.artistName) //NOSONAR
            } // NOSONAR

            if (requiresSubstring) { //NOSONAR
                string = if (!TextUtils.isEmpty(string)) { //NOSONAR
                    string.substring(0, 1).toUpperCase() //NOSONAR
                } else { //NOSONAR
                    "" // NOSONAR
                } // NOSONAR
            } // NOSONAR
            return string //NOSONAR
        } // NOSONAR
        return "" //NOSONAR
    } // NOSONAR

    override fun areContentsEqual(other: Any): Boolean { //NOSONAR
        return super.areContentsEqual(other) && if (other is QueueViewBinder) { //NOSONAR
            queueItem == other.queueItem && isCurrentTrack == other.isCurrentTrack //NOSONAR
        } else { //NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun equals(other: Any?): Boolean { //NOSONAR
        if (this === other) return true //NOSONAR
        if (javaClass != other?.javaClass) return false //NOSONAR

        other as QueueViewBinder //NOSONAR

        if (queueItem != other.queueItem) return false //NOSONAR

        return true //NOSONAR
    } // NOSONAR

    override fun hashCode(): Int { //NOSONAR
        return queueItem.hashCode() //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "QueueViewBinder" //NOSONAR
    } // NOSONAR

    class ViewHolder constructor(itemView: View) : BaseViewHolder<QueueViewBinder>(itemView) { //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        lateinit var lineOne: TextView //NOSONAR

        @BindView(R.id.line_two) //NOSONAR
        lateinit var lineTwo: TextView //NOSONAR

        @BindView(R.id.line_three) //NOSONAR
        lateinit var lineThree: TextView //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        lateinit var overflowButton: NonScrollImageButton //NOSONAR

        @BindView(R.id.drag_handle) //NOSONAR
        lateinit var dragHandle: ImageView //NOSONAR

        @BindView(R.id.image) //NOSONAR
        @JvmField //NOSONAR
        var artwork: ImageView? = null //NOSONAR

        init { //NOSONAR
            ButterKnife.bind(this, itemView) //NOSONAR

            itemView.setOnClickListener { v -> viewModel.onItemClick(adapterPosition) } //NOSONAR
            itemView.setOnLongClickListener { v -> viewModel.onItemLongClick(adapterPosition) } //NOSONAR

            overflowButton.setOnClickListener { v -> viewModel.onOverflowClick(adapterPosition, v) } //NOSONAR

            dragHandle?.setOnTouchListener { v, event -> //NOSONAR
                if (event.actionMasked == MotionEvent.ACTION_DOWN) { //NOSONAR
                    viewModel.onStartDrag(this) //NOSONAR
                } // NOSONAR
                true //NOSONAR
            } // NOSONAR
        } // NOSONAR

        override fun toString(): String { //NOSONAR
            return "QueueVeewBinder.ViewHolder" //NOSONAR
        } // NOSONAR

        override fun recycle() { //NOSONAR
            super.recycle() //NOSONAR

            artwork?.let { artwork -> //NOSONAR
                Glide.clear(artwork) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
