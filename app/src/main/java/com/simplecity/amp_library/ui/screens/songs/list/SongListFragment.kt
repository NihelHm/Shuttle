@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.list // NOSONAR

import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v7.widget.LinearLayoutManager // NOSONAR
import android.support.v7.widget.PopupMenu // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.Menu // NOSONAR
import android.view.MenuInflater // NOSONAR
import android.view.MenuItem // NOSONAR
import android.view.View // NOSONAR
import android.view.ViewGroup // NOSONAR
import android.widget.Toast // NOSONAR
import com.bumptech.glide.RequestManager // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.adapters.SectionedAdapter // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment // NOSONAR
import com.simplecity.amp_library.ui.dialog.DeleteDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.SongInfoDialog // NOSONAR
import com.simplecity.amp_library.ui.modelviews.EmptyView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel // NOSONAR
import com.simplecity.amp_library.ui.modelviews.ShuffleView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SongView // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbar // NOSONAR
import com.simplecity.amp_library.utils.ContextualToolbarHelper // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.extensions.share // NOSONAR
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.SongSortHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.simplecity.amp_library.utils.withArgs // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallbackAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import kotlinx.android.synthetic.main.fragment_recycler.recyclerView // NOSONAR
import javax.inject.Inject // NOSONAR

class SongListFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    SongView.ClickListener, //NOSONAR
    ShuffleView.ShuffleClickListener, //NOSONAR
    SongListContract.View, //NOSONAR
    SongMenuContract.View { //NOSONAR

    private val adapter = SectionedAdapter() //NOSONAR

    private val shuffleView = ShuffleView() //NOSONAR

    private var contextualToolbarHelper: ContextualToolbarHelper<Song>? = null //NOSONAR

    private var setDataDisposable: Disposable? = null //NOSONAR

    private var playlistMenuDisposable: Disposable? = null //NOSONAR

    private val menuDisposables = CompositeDisposable() //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    @Inject lateinit var songsPresenter: SongListPresenter //NOSONAR

    @Inject lateinit var sortManager: SortManager //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    // Lifecycle // NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    } // NOSONAR

    override fun onCreate(icicle: Bundle?) { //NOSONAR
        super.onCreate(icicle) //NOSONAR

        setHasOptionsMenu(true) //NOSONAR

        shuffleView.setClickListener(this) //NOSONAR
    } // NOSONAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_recycler, container, false) //NOSONAR
    } // NOSONAR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR
        recyclerView.adapter = adapter //NOSONAR

        songsPresenter.bindView(this) //NOSONAR
    } // NOSONAR

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        songsPresenter.loadSongs() //NOSONAR

        if (userVisibleHint) { //NOSONAR
            setupContextualToolbar() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onPause() { //NOSONAR

        setDataDisposable?.dispose() //NOSONAR

        playlistMenuDisposable?.dispose() //NOSONAR

        menuDisposables.clear() //NOSONAR

        super.onPause() //NOSONAR
    } // NOSONAR

    override fun onDestroyView() { //NOSONAR
        songsPresenter.unbindView(this) //NOSONAR
        super.onDestroyView() //NOSONAR
    } // NOSONAR


    // Options Menu // NOSONAR

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) { //NOSONAR
        super.onCreateOptionsMenu(menu, inflater) //NOSONAR

        inflater!!.inflate(R.menu.menu_sort_songs, menu) //NOSONAR
    } // NOSONAR

    override fun onPrepareOptionsMenu(menu: Menu?) { //NOSONAR
        super.onPrepareOptionsMenu(menu) //NOSONAR
        SongSortHelper.updateSongSortMenuItems(menu!!, sortManager.songsSortOrder, sortManager.songsAscending) //NOSONAR
        menu.findItem(R.id.showArtwork).isChecked = settingsManager.showArtworkInSongList() //NOSONAR
    } // NOSONAR

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //NOSONAR
        val songSortOder = SongSortHelper.handleSongMenuSortOrderClicks(item!!) //NOSONAR
        if (songSortOder != null) { //NOSONAR
            songsPresenter.setSongsSortOrder(songSortOder) //NOSONAR
            return true //NOSONAR
        } // NOSONAR
        val songsAsc = SongSortHelper.handleSongDetailMenuSortOrderAscClicks(item) //NOSONAR
        if (songsAsc != null) { //NOSONAR
            songsPresenter.setSongsAscending(songsAsc) //NOSONAR
            return true //NOSONAR
        } // NOSONAR

        if (item.itemId == R.id.showArtwork) { //NOSONAR
            songsPresenter.setShowArtwork(!item.isChecked) //NOSONAR
        } // NOSONAR

        return super.onOptionsItemSelected(item) //NOSONAR
    } // NOSONAR

    override fun setUserVisibleHint(isVisibleToUser: Boolean) { //NOSONAR
        super.setUserVisibleHint(isVisibleToUser) //NOSONAR
        if (isVisibleToUser) { //NOSONAR
            setupContextualToolbar() //NOSONAR
        } else { //NOSONAR
            contextualToolbarHelper?.finish() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun setupContextualToolbar() { //NOSONAR
        val contextualToolbar = ContextualToolbar.findContextualToolbar(this) //NOSONAR
        if (contextualToolbar != null) { //NOSONAR
            contextualToolbar.menu.clear() //NOSONAR
            contextualToolbar.inflateMenu(R.menu.context_menu_general) //NOSONAR
            val sub = contextualToolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR

            playlistMenuDisposable?.dispose() //NOSONAR
            playlistMenuDisposable = playlistMenuHelper.createUpdatingPlaylistMenu(sub) //NOSONAR
                .doOnError { throwable -> LogUtils.logException(TAG, "setupContextualToolbar error", throwable) } //NOSONAR
                .subscribe() //NOSONAR

            contextualToolbarHelper = ContextualToolbarHelper(context, contextualToolbar, object : ContextualToolbarHelper.Callback { //NOSONAR
                override fun notifyItemChanged(viewModel: SelectableViewModel) { //NOSONAR
                    val index = adapter.items.indexOf(viewModel as ViewModel<*>) //NOSONAR
                    if (index >= 0) { //NOSONAR
                        adapter.notifyItemChanged(index, 0) //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                override fun notifyDatasetChanged() { //NOSONAR
                    adapter.notifyItemRangeChanged(0, adapter.items.size, 0) //NOSONAR
                } // NOSONAR
            }) // NOSONAR

            contextualToolbar.setOnMenuItemClickListener( //NOSONAR
                SongMenuUtils.getSongMenuClickListener( //NOSONAR
                    Single.defer { Single.just(contextualToolbarHelper!!.items) }, //NOSONAR
                    songsPresenter //NOSONAR
                ) // NOSONAR
            ) // NOSONAR
        } // NOSONAR
    } // NOSONAR


    // SongListContract.View Implementation // NOSONAR

    override fun setData(songs: List<Song>, scrollToTop: Boolean) { //NOSONAR
        setDataDisposable?.dispose() //NOSONAR

        val showArtwork = settingsManager.showArtworkInSongList() //NOSONAR

        if (songs.isEmpty()) { //NOSONAR
            setDataDisposable = adapter.setItems(listOf(EmptyView(R.string.empty_songlist))) //NOSONAR
        } else { //NOSONAR

            val viewModels = mutableListOf<ViewModel<*>>(shuffleView) //NOSONAR
            viewModels.addAll( //NOSONAR
                songs //NOSONAR
                    .map { song -> //NOSONAR
                        val songView = SongView(song, requestManager, sortManager, settingsManager) //NOSONAR
                        songView.setClickListener(this) //NOSONAR
                        songView.showAlbumArt(showArtwork) //NOSONAR
                        songView as ViewModel<*> //NOSONAR
                    } // NOSONAR
                    .toList()) //NOSONAR

            setDataDisposable = adapter.setItems(viewModels, object : CompletionListUpdateCallbackAdapter() { //NOSONAR
                override fun onComplete() { //NOSONAR
                    super.onComplete() //NOSONAR
                    if (scrollToTop) { //NOSONAR
                        recyclerView.smoothScrollToPosition(0) //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            }) // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun invalidateOptionsMenu() { //NOSONAR
        activity?.invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    override fun showPlaybackError() { //NOSONAR
        Toast.makeText(context, R.string.empty_playlist, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR


    // SongMenuContract.View Implementation // NOSONAR

    override fun presentCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        CreatePlaylistDialog.newInstance(songs).show(childFragmentManager, "CreatePlaylistDialog") //NOSONAR
    } // NOSONAR

    override fun presentSongInfoDialog(song: Song) { //NOSONAR
        SongInfoDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoplaylist, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        val string = context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs) //NOSONAR
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    override fun presentTagEditorDialog(song: Song) { //NOSONAR
        TaggerDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentDeleteDialog(songs: List<Song>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListSongsRef { songs }).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun shareSong(song: Song) { //NOSONAR
        song.share(context!!) //NOSONAR
    } // NOSONAR

    override fun presentRingtonePermissionDialog() { //NOSONAR
        RingtoneManager.getDialog(context!!).show() //NOSONAR
    } // NOSONAR

    override fun showRingtoneSetMessage() { //NOSONAR
        Toast.makeText(context, R.string.ringtone_set_new, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    // SongView.ClickListener Implementation // NOSONAR

    override fun onSongClick(position: Int, songView: SongView) { //NOSONAR
        if (!contextualToolbarHelper!!.handleClick(songView, songView.song)) { //NOSONAR
            songsPresenter.play(songView.song) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onSongOverflowClick(position: Int, view: View, song: Song) { //NOSONAR
        val menu = PopupMenu(context!!, view) //NOSONAR
        SongMenuUtils.setupSongMenu(menu, false, true, playlistMenuHelper) //NOSONAR
        menu.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener(song, songsPresenter)) //NOSONAR
        menu.show() //NOSONAR
    } // NOSONAR

    override fun onSongLongClick(position: Int, songView: SongView): Boolean { //NOSONAR
        return contextualToolbarHelper!!.handleLongClick(songView, songView.song) //NOSONAR
    } // NOSONAR

    override fun onStartDrag(viewHolder: SongView.ViewHolder) { //NOSONAR
        // Nothing to do // NOSONAR
    } // NOSONAR


    // ShuffleView.OnClickListener Implementation // NOSONAR

    override fun onShuffleItemClick() { //NOSONAR
        songsPresenter.shuffleAll() //NOSONAR
    } // NOSONAR


    // BaseFragment Implementation // NOSONAR

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    } // NOSONAR


    // Static // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "SongFragment" //NOSONAR

        private const val ARG_TITLE = "title" //NOSONAR

        fun newInstance(title: String) = SongListFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
