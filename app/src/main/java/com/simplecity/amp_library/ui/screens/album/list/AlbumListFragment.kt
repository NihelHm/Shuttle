@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.list

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.simplecity.amp_library.R
import com.simplecity.amp_library.R.string
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.QueueManager
import com.simplecity.amp_library.ui.adapters.SectionedAdapter
import com.simplecity.amp_library.ui.adapters.ViewType
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.dialog.AlbumBiographyDialog
import com.simplecity.amp_library.ui.dialog.DeleteDialog
import com.simplecity.amp_library.ui.modelviews.AlbumView
import com.simplecity.amp_library.ui.modelviews.EmptyView
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel
import com.simplecity.amp_library.ui.modelviews.ShuffleView
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog
import com.simplecity.amp_library.ui.views.ContextualToolbar
import com.simplecity.amp_library.ui.views.recyclerview.GridDividerDecoration
import com.simplecity.amp_library.utils.ArtworkDialog
import com.simplecity.amp_library.utils.ContextualToolbarHelper
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.menu.album.AlbumMenuUtils
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper
import com.simplecity.amp_library.utils.sorting.SortManager
import com.simplecity.amp_library.utils.withArgs
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallbackAdapter
import com.simplecityapps.recycler_adapter.model.ViewModel
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener
import com.simplecityapps.recycler_adapter.recyclerview.SpanSizeLookup
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AlbumListFragment : //NOSONAR
    AlbumListContract.View, //NOSONAR
    BaseFragment(), //NOSONAR
    AlbumView.ClickListener, //NOSONAR
    ShuffleView.ShuffleClickListener { //NOSONAR

    private lateinit var albumClickListener: AlbumClickListener //NOSONAR

    private lateinit var recyclerView: FastScrollRecyclerView //NOSONAR

    private lateinit var layoutManager: GridLayoutManager //NOSONAR

    private lateinit var adapter: SectionedAdapter //NOSONAR

    private lateinit var spanSizeLookup: SpanSizeLookup //NOSONAR

    private lateinit var shuffleView: ShuffleView //NOSONAR

    private var contextualToolbarHelper: ContextualToolbarHelper<Album>? = null //NOSONAR

    private var playlistMenuDisposable: Disposable? = null //NOSONAR

    private var setDataDisposable: Disposable? = null //NOSONAR

    @Inject lateinit var presenter: AlbumsPresenter //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    @Inject lateinit var sortManager: SortManager //NOSONAR

    @Inject lateinit var songsRepository: Repository.SongsRepository //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    interface AlbumClickListener { //NOSONAR
        fun onAlbumClicked(album: Album, transitionView: View) //NOSONAR
    }

    override fun onAttach(context: Context?) { //NOSONAR
        super.onAttach(context) //NOSONAR

        AndroidSupportInjection.inject(this) //NOSONAR

        val parentFragment = parentFragment //NOSONAR
        if (parentFragment is AlbumClickListener) { //NOSONAR
            albumClickListener = parentFragment //NOSONAR
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        setHasOptionsMenu(true) //NOSONAR

        adapter = SectionedAdapter() //NOSONAR
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        recyclerView = inflater.inflate(R.layout.fragment_recycler, container, false) as FastScrollRecyclerView //NOSONAR
        return recyclerView //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        val spanCount = settingsManager.getAlbumColumnCount(context) //NOSONAR
        layoutManager = GridLayoutManager(context, spanCount) //NOSONAR
        spanSizeLookup = SpanSizeLookup(adapter, spanCount) //NOSONAR
        spanSizeLookup.isSpanIndexCacheEnabled = true //NOSONAR
        layoutManager.spanSizeLookup = spanSizeLookup //NOSONAR

        recyclerView.adapter = adapter //NOSONAR
        recyclerView.layoutManager = layoutManager //NOSONAR
        recyclerView.addItemDecoration(GridDividerDecoration(resources, 4, true)) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR

        shuffleView = ShuffleView() //NOSONAR
        shuffleView.setTitleResId(R.string.shuffle_albums) //NOSONAR
        shuffleView.setClickListener(this) //NOSONAR

        presenter.bindView(this) //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadAlbums(false) //NOSONAR

        if (userVisibleHint) { //NOSONAR
            setupContextualToolbar() //NOSONAR
        }
    }

    override fun onPause() { //NOSONAR
        setDataDisposable?.dispose() //NOSONAR

        playlistMenuDisposable?.dispose() //NOSONAR

        super.onPause() //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        presenter.unbindView(this) //NOSONAR
        super.onDestroyView() //NOSONAR
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) { //NOSONAR
        super.onCreateOptionsMenu(menu, inflater) //NOSONAR

        inflater!!.inflate(R.menu.menu_sort_albums, menu) //NOSONAR
        inflater.inflate(R.menu.menu_view_as, menu) //NOSONAR

        menu!!.addSubMenu(0, MENU_GRID_SIZE, 0, R.string.menu_grid_size) //NOSONAR
        val subMenu = menu.findItem(MENU_GRID_SIZE).subMenu //NOSONAR

        val spanCountArray = resources.getIntArray(R.array.span_count) //NOSONAR
        for (i in spanCountArray.indices) { //NOSONAR
            subMenu.add(MENU_GROUP_GRID, spanCountArray[i], i, spanCountArray[i].toString()) //NOSONAR
        }
        subMenu.setGroupCheckable(MENU_GROUP_GRID, true, true) //NOSONAR
    }

    override fun onPrepareOptionsMenu(menu: Menu) { //NOSONAR
        super.onPrepareOptionsMenu(menu) //NOSONAR

        val sortOrder = sortManager.albumsSortOrder //NOSONAR

        when (sortOrder) { //NOSONAR
            SortManager.AlbumSort.DEFAULT -> menu.findItem(R.id.sort_album_default).isChecked = true //NOSONAR
            SortManager.AlbumSort.NAME -> menu.findItem(R.id.sort_album_name).isChecked = true //NOSONAR
            SortManager.AlbumSort.YEAR -> menu.findItem(R.id.sort_album_year).isChecked = true //NOSONAR
            SortManager.AlbumSort.ARTIST_NAME -> menu.findItem(R.id.sort_album_artist_name).isChecked = true //NOSONAR
        }

        menu.findItem(R.id.sort_album_ascending).isChecked = sortManager.albumsAscending //NOSONAR

        val displayType = settingsManager.getAlbumDisplayType(context) //NOSONAR
        when (displayType) { //NOSONAR
            ViewType.ALBUM_LIST -> menu.findItem(R.id.view_as_list).isChecked = true //NOSONAR
            ViewType.ALBUM_GRID -> menu.findItem(R.id.view_as_grid).isChecked = true //NOSONAR
            ViewType.ALBUM_CARD -> menu.findItem(R.id.view_as_grid_card).isChecked = true //NOSONAR
            ViewType.ALBUM_PALETTE -> menu.findItem(R.id.view_as_grid_palette).isChecked = true //NOSONAR
        }

        val gridMenuItem = menu.findItem(MENU_GRID_SIZE) //NOSONAR
        if (displayType == ViewType.ALBUM_LIST) { //NOSONAR
            gridMenuItem.isVisible = false //NOSONAR
        } else { //NOSONAR
            gridMenuItem.isVisible = true //NOSONAR
            gridMenuItem.subMenu?.findItem(settingsManager.getAlbumColumnCount(context))?.isChecked = true //NOSONAR
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //NOSONAR
        when (item!!.itemId) { //NOSONAR
            R.id.sort_album_default -> presenter.setAlbumsSortOrder(SortManager.AlbumSort.DEFAULT) //NOSONAR
            R.id.sort_album_name -> presenter.setAlbumsSortOrder(SortManager.AlbumSort.NAME) //NOSONAR
            R.id.sort_album_year -> presenter.setAlbumsSortOrder(SortManager.AlbumSort.YEAR) //NOSONAR
            R.id.sort_album_artist_name -> presenter.setAlbumsSortOrder(SortManager.AlbumSort.ARTIST_NAME) //NOSONAR
            R.id.sort_album_ascending -> presenter.setAlbumsAscending(!item.isChecked) //NOSONAR
            R.id.view_as_list -> { //NOSONAR
                val viewType = ViewType.ALBUM_LIST //NOSONAR
                settingsManager.setAlbumDisplayType(viewType) //NOSONAR
                setupListSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
            R.id.view_as_grid -> { //NOSONAR
                val viewType = ViewType.ALBUM_GRID //NOSONAR
                settingsManager.setAlbumDisplayType(viewType) //NOSONAR
                setupGridSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
            R.id.view_as_grid_card -> { //NOSONAR
                val viewType = ViewType.ALBUM_CARD //NOSONAR
                settingsManager.setAlbumDisplayType(viewType) //NOSONAR
                setupGridSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
            R.id.view_as_grid_palette -> { //NOSONAR
                val viewType = ViewType.ALBUM_PALETTE //NOSONAR
                settingsManager.setAlbumDisplayType(viewType) //NOSONAR
                setupGridSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
        }

        if (item.groupId == MENU_GROUP_GRID) { //NOSONAR
            settingsManager.setAlbumColumnCount(context, item.itemId) //NOSONAR
            spanSizeLookup.setSpanCount(item.itemId) //NOSONAR
            (recyclerView.layoutManager as GridLayoutManager).spanCount = settingsManager.getAlbumColumnCount(context) //NOSONAR
            adapter.notifyItemRangeChanged(0, adapter.itemCount) //NOSONAR
        }

        activity!!.invalidateOptionsMenu() //NOSONAR

        return super.onOptionsItemSelected(item) //NOSONAR
    }

    private fun setupGridSpan() { //NOSONAR
        val spanCount = settingsManager.getAlbumColumnCount(context) //NOSONAR
        spanSizeLookup.setSpanCount(spanCount) //NOSONAR
        layoutManager.spanCount = spanCount //NOSONAR
    }

    private fun setupListSpan() { //NOSONAR
        val spanCount = resources.getInteger(R.integer.list_num_columns) //NOSONAR
        spanSizeLookup.setSpanCount(spanCount) //NOSONAR
        layoutManager.spanCount = spanCount //NOSONAR
    }

    private fun updateViewType(@ViewType viewType: Int) { //NOSONAR
        adapter.items //NOSONAR
            .filter { viewModel -> viewModel is AlbumView } //NOSONAR
            .forEach { viewModel -> (viewModel as AlbumView).viewType = viewType } //NOSONAR
        adapter.notifyItemRangeChanged(0, adapter.itemCount) //NOSONAR
    }

    override fun onAlbumClick(position: Int, albumView: AlbumView, viewHolder: AlbumView.ViewHolder) { //NOSONAR
        if (!contextualToolbarHelper!!.handleClick(albumView, albumView.album)) { //NOSONAR
            albumClickListener.onAlbumClicked(albumView.album, viewHolder.imageOne) //NOSONAR
        }
    }

    override fun onAlbumLongClick(position: Int, albumView: AlbumView): Boolean { //NOSONAR
        return contextualToolbarHelper!!.handleLongClick(albumView, albumView.album) //NOSONAR
    }

    override fun onAlbumOverflowClicked(view: View, album: Album) { //NOSONAR
        val menu = PopupMenu(context!!, view) //NOSONAR
        menu.inflate(R.menu.menu_album) //NOSONAR
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR
        menu.setOnMenuItemClickListener( //NOSONAR
            AlbumMenuUtils.getAlbumMenuClickListener(album, presenter) //NOSONAR
        )
        menu.show() //NOSONAR
    }

    override fun onShuffleItemClick() { //NOSONAR
        // Note: For album-shuffle mode, we don't actually turn shuffle on.
        mediaManager.shuffleMode = QueueManager.ShuffleMode.OFF //NOSONAR

        mediaManager.playAll(songsRepository.getSongs(null as Function1<Song, Boolean>?) //NOSONAR
            .firstOrError() //NOSONAR
            .map { songs -> Operators.albumShuffleSongs(songs, sortManager) }) { //NOSONAR
            // To do later: Show playback failed toast
            Unit //NOSONAR
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) { //NOSONAR
        super.setUserVisibleHint(isVisibleToUser) //NOSONAR
        if (isVisibleToUser) { //NOSONAR
            setupContextualToolbar() //NOSONAR
        } else { //NOSONAR
            contextualToolbarHelper?.finish() //NOSONAR
        }
    }

    private fun setupContextualToolbar() { //NOSONAR
        val contextualToolbar = ContextualToolbar.findContextualToolbar(this) //NOSONAR
        if (contextualToolbar != null) { //NOSONAR
            contextualToolbar.menu.clear() //NOSONAR
            contextualToolbar.inflateMenu(R.menu.context_menu_general) //NOSONAR
            val submenu = contextualToolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
            playlistMenuDisposable?.dispose() //NOSONAR

            playlistMenuDisposable = playlistMenuHelper.createUpdatingPlaylistMenu(submenu) //NOSONAR
                .doOnError { throwable -> LogUtils.logException(TAG, "setupContextualToolbar error", throwable) } //NOSONAR
                .onErrorComplete() //NOSONAR
                .subscribe() //NOSONAR

            contextualToolbar.setOnMenuItemClickListener( //NOSONAR
                AlbumMenuUtils.getAlbumMenuClickListener( //NOSONAR
                    Single.defer { Single.just(contextualToolbarHelper!!.items) }, //NOSONAR
                    presenter //NOSONAR
                )
            )

            contextualToolbarHelper = ContextualToolbarHelper(context!!, contextualToolbar, object : ContextualToolbarHelper.Callback { //NOSONAR

                override fun notifyItemChanged(viewModel: SelectableViewModel) { //NOSONAR
                    val index = adapter.items.indexOf(viewModel as ViewModel<*>) //NOSONAR
                    if (index >= 0) { //NOSONAR
                        adapter.notifyItemChanged(index, 0) //NOSONAR
                    }
                }

                override fun notifyDatasetChanged() { //NOSONAR
                    adapter.notifyItemRangeChanged(0, adapter.items.size, 0) //NOSONAR
                }
            })
        }
    }

    // AlbumListContract.View Implementation

    override fun setData(albums: List<Album>, scrollToTop: Boolean) { //NOSONAR
        setDataDisposable?.dispose() //NOSONAR

        if (albums.isEmpty()) { //NOSONAR
            setDataDisposable = adapter.setItems(listOf(EmptyView(string.empty_albums))) //NOSONAR
        } else { //NOSONAR
            val viewModels = albums.map { album -> //NOSONAR
                val albumView = AlbumView(album, settingsManager.getAlbumDisplayType(context), requestManager, sortManager, settingsManager) //NOSONAR
                albumView.setClickListener(this) //NOSONAR
                albumView as ViewModel<*> //NOSONAR
            }.toMutableList() //NOSONAR

            viewModels.add(0, shuffleView) //NOSONAR

            setDataDisposable = adapter.setItems(viewModels, object : CompletionListUpdateCallbackAdapter() { //NOSONAR
                override fun onComplete() { //NOSONAR
                    super.onComplete() //NOSONAR
                    if (scrollToTop) { //NOSONAR
                        recyclerView.smoothScrollToPosition(0) //NOSONAR
                    }
                }
            })
        }
    }

    override fun invalidateOptionsMenu() { //NOSONAR
        activity?.invalidateOptionsMenu() //NOSONAR
    }

    // AlbumMenuContract.View Implementation

    override fun presentCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        CreatePlaylistDialog.newInstance(songs).show(childFragmentManager) //NOSONAR
    }

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoplaylist, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message
        Toast.makeText(context, R.string.emptyplaylist, Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun presentTagEditorDialog(album: Album) { //NOSONAR
        TaggerDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    }

    override fun presentDeleteAlbumsDialog(albums: List<Album>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListAlbumsRef { albums }).show(childFragmentManager) //NOSONAR
    }

    override fun presentAlbumInfoDialog(album: Album) { //NOSONAR
        AlbumBiographyDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    }

    override fun presentArtworkEditorDialog(album: Album) { //NOSONAR
        ArtworkDialog.build(context, album).show() //NOSONAR
    }

    // BaseFragment Implementation

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    }

    // Static

    companion object { //NOSONAR

        private const val TAG = "AlbumListFragment" //NOSONAR

        private const val ARG_TITLE = "title" //NOSONAR

        private const val MENU_GRID_SIZE = 100 //NOSONAR
        private const val MENU_GROUP_GRID = 1 //NOSONAR

        fun newInstance(title: String) = AlbumListFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        }
    }
}
