@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.list

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
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.adapters.SectionedAdapter
import com.simplecity.amp_library.ui.adapters.ViewType
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.dialog.ArtistBiographyDialog
import com.simplecity.amp_library.ui.dialog.DeleteDialog
import com.simplecity.amp_library.ui.modelviews.AlbumArtistView
import com.simplecity.amp_library.ui.modelviews.EmptyView
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog
import com.simplecity.amp_library.ui.views.ContextualToolbar
import com.simplecity.amp_library.ui.views.recyclerview.GridDividerDecoration
import com.simplecity.amp_library.utils.ArtworkDialog
import com.simplecity.amp_library.utils.ContextualToolbarHelper
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.menu.albumartist.AlbumArtistMenuUtils
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

class AlbumArtistListFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    AlbumArtistView.ClickListener, //NOSONAR
    AlbumArtistListContract.View { //NOSONAR

    private lateinit var albumArtistClickListener: AlbumArtistClickListener //NOSONAR

    private lateinit var recyclerView: FastScrollRecyclerView //NOSONAR

    private lateinit var layoutManager: GridLayoutManager //NOSONAR

    private lateinit var adapter: SectionedAdapter //NOSONAR

    private lateinit var spanSizeLookup: SpanSizeLookup //NOSONAR

    private var contextualToolbarHelper: ContextualToolbarHelper<AlbumArtist>? = null //NOSONAR

    private var playlistMenuDisposable: Disposable? = null //NOSONAR

    private var setDataDisposable: Disposable? = null //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    @Inject lateinit var presenter: AlbumArtistListPresenter //NOSONAR

    @Inject lateinit var sortManager: SortManager //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    interface AlbumArtistClickListener { //NOSONAR
        fun onAlbumArtistClicked(albumArtist: AlbumArtist, transitionView: View) //NOSONAR
    }

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        val parentFragment = parentFragment //NOSONAR
        if (parentFragment is AlbumArtistClickListener) { //NOSONAR
            albumArtistClickListener = parentFragment //NOSONAR
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

        val spanCount = settingsManager.getArtistColumnCount(context) //NOSONAR
        layoutManager = GridLayoutManager(context, spanCount) //NOSONAR
        spanSizeLookup = SpanSizeLookup(adapter, spanCount) //NOSONAR
        spanSizeLookup.isSpanIndexCacheEnabled = true //NOSONAR
        layoutManager.spanSizeLookup = spanSizeLookup //NOSONAR

        recyclerView.adapter = adapter //NOSONAR
        recyclerView.layoutManager = layoutManager //NOSONAR
        recyclerView.addItemDecoration(GridDividerDecoration(resources, 4, true)) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR

        presenter.bindView(this) //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadAlbumArtists(false) //NOSONAR

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

        inflater!!.inflate(R.menu.menu_sort_artists, menu) //NOSONAR
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

        //Strip the 'asc' or 'desc' flag, we just want to know the sort type
        val sortOrder = sortManager.artistsSortOrder //NOSONAR

        when (sortOrder) { //NOSONAR
            SortManager.ArtistSort.DEFAULT -> menu.findItem(R.id.sort_artist_default)?.isChecked = true //NOSONAR
            SortManager.ArtistSort.NAME -> menu.findItem(R.id.sort_artist_name)?.isChecked = true //NOSONAR
        }

        menu.findItem(R.id.sort_artist_ascending)?.isChecked = sortManager.artistsAscending //NOSONAR

        val displayType = settingsManager.artistDisplayType //NOSONAR
        when (displayType) { //NOSONAR
            ViewType.ARTIST_LIST -> menu.findItem(R.id.view_as_list)?.isChecked = true //NOSONAR
            ViewType.ARTIST_GRID -> menu.findItem(R.id.view_as_grid)?.isChecked = true //NOSONAR
            ViewType.ARTIST_CARD -> menu.findItem(R.id.view_as_grid_card)?.isChecked = true //NOSONAR
            ViewType.ARTIST_PALETTE -> menu.findItem(R.id.view_as_grid_palette)?.isChecked = true //NOSONAR
        }

        val gridMenuItem = menu.findItem(MENU_GRID_SIZE) //NOSONAR
        if (displayType == ViewType.ARTIST_LIST) { //NOSONAR
            gridMenuItem.isVisible = false //NOSONAR
        } else { //NOSONAR
            gridMenuItem.isVisible = true //NOSONAR
            gridMenuItem.subMenu?.findItem(settingsManager.getArtistColumnCount(context))?.isChecked = true //NOSONAR
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //NOSONAR
        when (item.itemId) { //NOSONAR
            R.id.sort_artist_default -> presenter.setAlbumArtistsSortOrder(SortManager.ArtistSort.DEFAULT) //NOSONAR
            R.id.sort_artist_name -> presenter.setAlbumArtistsSortOrder(SortManager.ArtistSort.NAME) //NOSONAR
            R.id.sort_artist_ascending -> sortManager.artistsAscending = !item.isChecked //NOSONAR
            R.id.view_as_list -> { //NOSONAR
                val viewType = ViewType.ARTIST_LIST //NOSONAR
                settingsManager.artistDisplayType = viewType //NOSONAR
                setupListSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
            R.id.view_as_grid -> { //NOSONAR
                val viewType = ViewType.ARTIST_GRID //NOSONAR
                settingsManager.artistDisplayType = viewType //NOSONAR
                setupGridSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
            R.id.view_as_grid_card -> { //NOSONAR
                val viewType = ViewType.ARTIST_CARD //NOSONAR
                settingsManager.artistDisplayType = viewType //NOSONAR
                setupGridSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
            R.id.view_as_grid_palette -> { //NOSONAR
                val viewType = ViewType.ARTIST_PALETTE //NOSONAR
                settingsManager.artistDisplayType = viewType //NOSONAR
                setupGridSpan() //NOSONAR
                updateViewType(viewType) //NOSONAR
            }
        }

        if (item.groupId == MENU_GROUP_GRID) { //NOSONAR
            settingsManager.setArtistColumnCount(context, item.itemId) //NOSONAR
            spanSizeLookup.setSpanCount(item.itemId) //NOSONAR
            (recyclerView.layoutManager as GridLayoutManager).spanCount = settingsManager.getArtistColumnCount(context) //NOSONAR
            adapter.notifyItemRangeChanged(0, adapter.itemCount) //NOSONAR
        }

        activity!!.invalidateOptionsMenu() //NOSONAR

        return super.onOptionsItemSelected(item) //NOSONAR
    }

    private fun setupGridSpan() { //NOSONAR
        val spanCount = settingsManager.getArtistColumnCount(context) //NOSONAR
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
            .filter { viewModel -> viewModel is AlbumArtistView } //NOSONAR
            .forEach { viewModel -> (viewModel as AlbumArtistView).viewType = viewType } //NOSONAR
        adapter.notifyItemRangeChanged(0, adapter.itemCount) //NOSONAR
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
            val sub = contextualToolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
            playlistMenuDisposable?.dispose() //NOSONAR

            playlistMenuDisposable = playlistMenuHelper.createUpdatingPlaylistMenu(sub).subscribe( //NOSONAR
                {
                    // Intentionally left empty.
                },
                { throwable -> LogUtils.logException(TAG, "setupContextualToolbar", throwable) } //NOSONAR
            )

            contextualToolbar.setOnMenuItemClickListener( //NOSONAR
                AlbumArtistMenuUtils.getAlbumArtistMenuClickListener(Single.defer { Single.just(contextualToolbarHelper!!.items) }, presenter) //NOSONAR
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

    // AlbumArtistContract.View Implementation

    override fun setData(albumArtists: List<AlbumArtist>, scrollToTop: Boolean) { //NOSONAR
        setDataDisposable?.dispose() //NOSONAR

        if (albumArtists.isEmpty()) { //NOSONAR
            setDataDisposable = adapter.setItems(listOf(EmptyView(string.empty_artists))) //NOSONAR
        } else { //NOSONAR
            val viewModels = albumArtists //NOSONAR
                .map { albumArtist -> //NOSONAR
                    val albumArtistView = AlbumArtistView(albumArtist, settingsManager.artistDisplayType, requestManager, sortManager, settingsManager) //NOSONAR
                    albumArtistView.setClickListener(this) //NOSONAR
                    albumArtistView //NOSONAR
                }
                .toList() //NOSONAR

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

    // AlbumArtistView.ClickListener Implementation

    override fun onAlbumArtistClick(position: Int, albumArtistView: AlbumArtistView, viewholder: AlbumArtistView.ViewHolder) { //NOSONAR
        if (!contextualToolbarHelper!!.handleClick(albumArtistView, albumArtistView.albumArtist)) { //NOSONAR
            albumArtistClickListener.onAlbumArtistClicked(albumArtistView.albumArtist, viewholder.imageOne) //NOSONAR
        }
    }

    override fun onAlbumArtistLongClick(position: Int, albumArtistView: AlbumArtistView): Boolean { //NOSONAR
        return contextualToolbarHelper!!.handleLongClick(albumArtistView, albumArtistView.albumArtist) //NOSONAR
    }

    override fun onAlbumArtistOverflowClicked(v: View, albumArtist: AlbumArtist) { //NOSONAR
        val menu = PopupMenu(context!!, v) //NOSONAR
        menu.inflate(R.menu.menu_artist) //NOSONAR
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR
        menu.setOnMenuItemClickListener(AlbumArtistMenuUtils.getAlbumArtistClickListener(albumArtist, presenter)) //NOSONAR
        menu.show() //NOSONAR
    }

    // AlbumArtistMenuContract.View Implementation

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

    override fun presentTagEditorDialog(albumArtist: AlbumArtist) { //NOSONAR
        TaggerDialog.newInstance(albumArtist).show(childFragmentManager) //NOSONAR
    }

    override fun presentArtistDeleteDialog(albumArtists: List<AlbumArtist>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListArtistsRef { albumArtists }).show(childFragmentManager) //NOSONAR
    }

    override fun presentAlbumArtistInfoDialog(albumArtist: AlbumArtist) { //NOSONAR
        ArtistBiographyDialog.newInstance(albumArtist).show(childFragmentManager) //NOSONAR
    }

    override fun presentArtworkEditorDialog(albumArtist: AlbumArtist) { //NOSONAR
        ArtworkDialog.build(context, albumArtist).show() //NOSONAR
    }

    // BaseFragment Implementation

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    }

    // Static

    companion object { //NOSONAR

        private const val ARG_TITLE = "title" //NOSONAR

        private const val TAG = "AlbumArtistListFragment" //NOSONAR

        private const val MENU_GRID_SIZE = 100 //NOSONAR
        private const val MENU_GROUP_GRID = 1 //NOSONAR

        fun newInstance(title: String) = AlbumArtistListFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        }
    }
}
