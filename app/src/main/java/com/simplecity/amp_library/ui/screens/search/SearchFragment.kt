@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.SearchView
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.annimon.stream.Stream
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.format.PrefixHighlighter
import com.simplecity.amp_library.model.*
import com.simplecity.amp_library.ui.adapters.ViewType
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.dialog.*
import com.simplecity.amp_library.ui.modelviews.*
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistDetailFragment
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog
import com.simplecity.amp_library.ui.views.ContextualToolbar
import com.simplecity.amp_library.ui.views.ContextualToolbarHost
import com.simplecity.amp_library.utils.*
import com.simplecity.amp_library.utils.extensions.getSongsSingle
import com.simplecity.amp_library.utils.extensions.share
import com.simplecity.amp_library.utils.menu.album.AlbumMenuUtils
import com.simplecity.amp_library.utils.menu.albumartist.AlbumArtistMenuUtils
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper
import com.simplecity.amp_library.utils.sorting.SortManager
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallbackAdapter
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter
import com.simplecityapps.recycler_adapter.model.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_search.contextualToolbar as ctxToolbar

class SearchFragment : //NOSONAR
        BaseFragment(), //NOSONAR
        com.simplecity.amp_library.ui.screens.search.SearchView, //NOSONAR
        ContextualToolbarHost { //NOSONAR

    private var query = "" //NOSONAR

    private val adapter = ViewModelAdapter() //NOSONAR

    private val loadingView = LoadingView() //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private var contextualToolbarHelper: ContextualToolbarHelper<Single<List<Song>>>? = null //NOSONAR

    private val emptyView = EmptyView(R.string.empty_search) //NOSONAR

    private lateinit var artistsHeader: SearchHeaderView //NOSONAR
    private lateinit var albumsHeader: SearchHeaderView //NOSONAR
    private lateinit var songsHeader: SearchHeaderView //NOSONAR

    private var prefixHighlighter: PrefixHighlighter? = null //NOSONAR

    @Inject //NOSONAR
    lateinit var requestManager: RequestManager //NOSONAR

    @Inject //NOSONAR
    lateinit var songsRepository: Repository.SongsRepository //NOSONAR

    @Inject //NOSONAR
    lateinit var sortManager: SortManager //NOSONAR

    @Inject //NOSONAR
    lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject //NOSONAR
    lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    @Inject //NOSONAR
    lateinit var presenter: SearchPresenter //NOSONAR

    private var setDataDisposable: Disposable? = null //NOSONAR

    private lateinit var searchView: SearchView //NOSONAR

    @SuppressLint("InlinedApi") //NOSONAR
    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        prefixHighlighter = PrefixHighlighter(context) //NOSONAR

        requestManager = Glide.with(this) //NOSONAR

        query = arguments!!.getString(ARG_QUERY, "") //NOSONAR

        emptyView.setHeight(ResourceUtils.toPixels(96f)) //NOSONAR

        artistsHeader = SearchHeaderView(Header(context!!.getString(R.string.artists_title))) //NOSONAR
        albumsHeader = SearchHeaderView(Header(context!!.getString(R.string.albums_title))) //NOSONAR
        songsHeader = SearchHeaderView(Header(context!!.getString(R.string.tracks_title))) //NOSONAR
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_search, container, false) //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        toolbar!!.inflateMenu(R.menu.menu_search) //NOSONAR
        toolbar!!.setOnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.search_fuzzy -> { //NOSONAR
                    item.isChecked = !item.isChecked //NOSONAR
                    presenter.setSearchFuzzy(item.isChecked) //NOSONAR
                }
                R.id.search_artist -> { //NOSONAR
                    item.isChecked = !item.isChecked //NOSONAR
                    presenter.setSearchArtists(item.isChecked) //NOSONAR
                }
                R.id.search_album -> { //NOSONAR
                    item.isChecked = !item.isChecked //NOSONAR
                    presenter.setSearchAlbums(item.isChecked) //NOSONAR
                }
            }
            false //NOSONAR
        }

        setupContextualToolbar() //NOSONAR

        val searchItem = toolbar!!.menu.findItem(R.id.search) //NOSONAR
        searchItem.expandActionView() //NOSONAR
        searchView = searchItem.actionView as SearchView //NOSONAR

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener { //NOSONAR
            override fun onMenuItemActionExpand(item: MenuItem): Boolean { //NOSONAR
                return false //NOSONAR
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean { //NOSONAR
                val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //NOSONAR
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0) //NOSONAR
                searchItem.actionView!!.handler.postDelayed({ navigationController.popViewController() }, 150) //NOSONAR
                return false //NOSONAR
            }
        })

        recyclerView!!.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView!!.adapter = adapter //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.bindView(this) //NOSONAR

        disposables.add(RxSearchView.queryTextChangeEvents(searchView) //NOSONAR
                .skip(1) //NOSONAR
                .debounce(200, TimeUnit.MILLISECONDS) //NOSONAR
                .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                .subscribe { searchViewQueryTextEvent -> //NOSONAR
                    query = searchViewQueryTextEvent.queryText().toString() //NOSONAR
                    presenter.queryChanged(query) //NOSONAR
                })

        presenter.queryChanged(query) //NOSONAR
    }

    override fun onPause() { //NOSONAR
        disposables.clear() //NOSONAR
        presenter.unbindView(this) //NOSONAR

        super.onPause() //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        super.onDestroyView() //NOSONAR

        if (setDataDisposable != null) { //NOSONAR
            setDataDisposable!!.dispose() //NOSONAR
        }
    }

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    }

    override fun setLoading(loading: Boolean) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "setLoading..") //NOSONAR
        adapter.setItems(listOf(loadingView)) //NOSONAR
    }

    override fun setData(searchResult: SearchResult) { //NOSONAR
        val prefix = query.toUpperCase().toCharArray() //NOSONAR

        val viewModels = ArrayList<ViewModel<*>>() //NOSONAR

        if (!searchResult.albumArtists.isEmpty()) { //NOSONAR
            viewModels.add(artistsHeader) //NOSONAR
            viewModels.addAll(Stream.of(searchResult.albumArtists) //NOSONAR
                    .map { albumArtist -> //NOSONAR
                        val albumArtistView = AlbumArtistView(albumArtist, ViewType.ARTIST_LIST, requestManager, sortManager, settingsManager) //NOSONAR
                        albumArtistView.setClickListener(albumArtistClickListener) //NOSONAR
                        albumArtistView.setPrefix(prefixHighlighter, prefix) //NOSONAR
                        albumArtistView as ViewModel<*> //NOSONAR
                    }
                    .toList()) //NOSONAR
        }

        if (!searchResult.albums.isEmpty()) { //NOSONAR
            viewModels.add(albumsHeader) //NOSONAR
            viewModels.addAll(Stream.of(searchResult.albums).map { album -> //NOSONAR
                val albumView = AlbumView(album, ViewType.ALBUM_LIST, requestManager, sortManager, settingsManager) //NOSONAR
                albumView.setClickListener(albumViewClickListener) //NOSONAR
                albumView.setPrefix(prefixHighlighter, prefix) //NOSONAR
                albumView //NOSONAR
            }.toList()) //NOSONAR
        }

        if (!searchResult.songs.isEmpty()) { //NOSONAR
            viewModels.add(songsHeader) //NOSONAR
            viewModels.addAll(Stream.of(searchResult.songs).map { song -> //NOSONAR
                val songView = SongView(song, requestManager, sortManager, settingsManager) //NOSONAR
                songView.setClickListener(songViewClickListener) //NOSONAR
                songView.setPrefix(prefixHighlighter, prefix) //NOSONAR
                songView //NOSONAR
            }.toList()) //NOSONAR
        }

        if (viewModels.isEmpty()) { //NOSONAR
            viewModels.add(emptyView) //NOSONAR
        }

        analyticsManager!!.dropBreadcrumb(TAG, "setData..") //NOSONAR
        setDataDisposable = adapter.setItems(viewModels, object : CompletionListUpdateCallbackAdapter() { //NOSONAR
            override fun onComplete() { //NOSONAR
                super.onComplete() //NOSONAR

                recyclerView!!.scrollToPosition(0) //NOSONAR
            }
        })
    }

    override fun setFilterFuzzyChecked(checked: Boolean) { //NOSONAR
        toolbar!!.menu.findItem(R.id.search_fuzzy).isChecked = checked //NOSONAR
    }

    override fun setFilterArtistsChecked(checked: Boolean) { //NOSONAR
        toolbar!!.menu.findItem(R.id.search_artist).isChecked = checked //NOSONAR
    }

    override fun setFilterAlbumsChecked(checked: Boolean) { //NOSONAR
        toolbar!!.menu.findItem(R.id.search_album).isChecked = checked //NOSONAR
    }

    override fun showPlaybackError() { //NOSONAR
        // To do later: Implement
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


    // AlbumMenuContract.View Implementation

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


    // SongMenuContract.View Implementation

    override fun presentSongInfoDialog(song: Song) { //NOSONAR
        SongInfoDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    }

    override fun presentTagEditorDialog(song: Song) { //NOSONAR
        TaggerDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    }

    override fun presentDeleteDialog(songs: List<Song>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListSongsRef { songs }).show(childFragmentManager) //NOSONAR
    }

    override fun shareSong(song: Song) { //NOSONAR
        song.share(context!!) //NOSONAR
    }

    override fun presentRingtonePermissionDialog() { //NOSONAR
        RingtoneManager.getDialog(context!!).show() //NOSONAR
    }

    override fun showRingtoneSetMessage() { //NOSONAR
        Toast.makeText(context, R.string.ringtone_set_new, Toast.LENGTH_SHORT).show() //NOSONAR
    }


    override fun goToArtist(albumArtist: AlbumArtist, transitionView: View) { //NOSONAR
        val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //NOSONAR
        inputMethodManager.hideSoftInputFromWindow(this.view!!.windowToken, 0) //NOSONAR
        val transitionName = ViewCompat.getTransitionName(transitionView) //NOSONAR
        searchView.handler.postDelayed({ pushDetailFragment(ArtistDetailFragment.newInstance(albumArtist, transitionName!!), transitionView) }, 50) //NOSONAR
    }

    override fun goToAlbum(album: Album, transitionView: View) { //NOSONAR
        val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //NOSONAR
        inputMethodManager.hideSoftInputFromWindow(this.view!!.windowToken, 0) //NOSONAR
        val transitionName = ViewCompat.getTransitionName(transitionView) //NOSONAR
        searchView.handler.postDelayed({ pushDetailFragment(AlbumDetailFragment.newInstance(album, transitionName!!), transitionView) }, 50) //NOSONAR
    }

    override fun showUpgradeDialog() { //NOSONAR
        UpgradeDialog().show(childFragmentManager) //NOSONAR
    }

    private fun pushDetailFragment(fragment: Fragment, transitionView: View?) { //NOSONAR

        val transitions = ArrayList<Pair<View, String>>() //NOSONAR

        if (transitionView != null) { //NOSONAR
            val transitionName = ViewCompat.getTransitionName(transitionView) //NOSONAR
            transitions.add(Pair(transitionView, transitionName)) //NOSONAR

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
                val moveTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_transition) //NOSONAR
                fragment.sharedElementEnterTransition = moveTransition //NOSONAR
                fragment.sharedElementReturnTransition = moveTransition //NOSONAR
            }
        }

        navigationController.pushViewController(fragment, "DetailFragment", transitions) //NOSONAR
    }

    override fun getContextualToolbar(): ContextualToolbar? { //NOSONAR
        return ctxToolbar as ContextualToolbar? //NOSONAR
    }

    private fun setupContextualToolbar() { //NOSONAR

        val contextualToolbar = ContextualToolbar.findContextualToolbar(this) //NOSONAR
        if (contextualToolbar != null) { //NOSONAR

            contextualToolbar.menu.clear() //NOSONAR
            contextualToolbar.inflateMenu(R.menu.context_menu_general) //NOSONAR
            val sub = contextualToolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
            disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(sub).subscribe()) //NOSONAR

            contextualToolbar.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener( //NOSONAR
                    Single.defer { Operators.reduceSongSingles(contextualToolbarHelper!!.items) }, //NOSONAR
                    presenter //NOSONAR
            ))

            contextualToolbarHelper = object : ContextualToolbarHelper<Single<List<Song>>>(context!!, contextualToolbar, object : ContextualToolbarHelper.Callback { //NOSONAR

                override fun notifyItemChanged(viewModel: SelectableViewModel) { //NOSONAR
                    val index = adapter.items.indexOf(viewModel as ViewModel<*>) //NOSONAR
                    if (index >= 0) { //NOSONAR
                        adapter.notifyItemChanged(index, 0) //NOSONAR
                    }
                }

                override fun notifyDatasetChanged() { //NOSONAR
                    adapter.notifyItemRangeChanged(0, adapter.items.size, 0) //NOSONAR
                }
            }) {
                override fun start() { //NOSONAR
                    super.start() //NOSONAR

                    toolbar!!.visibility = View.GONE //NOSONAR
                }

                override fun finish() { //NOSONAR
                    if (toolbar != null) { //NOSONAR
                        toolbar!!.visibility = View.VISIBLE //NOSONAR
                    }
                    super.finish() //NOSONAR
                }
            }
        }
    }

    private val songViewClickListener = object : SongView.ClickListener { //NOSONAR

        override fun onSongClick(position: Int, songView: SongView) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(songView, Single.just(listOf(songView.song)))) { //NOSONAR
                presenter.onSongClick( //NOSONAR
                        adapter.items //NOSONAR
                                .filter { item -> item is SongView } //NOSONAR
                                .map { item -> (item as SongView).song }.toList(), //NOSONAR
                        songView.song //NOSONAR
                )
            }
        }

        override fun onSongLongClick(position: Int, songView: SongView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(songView, Single.just(listOf(songView.song))) //NOSONAR
        }

        override fun onSongOverflowClick(position: Int, v: View, song: Song) { //NOSONAR
            val menu = PopupMenu(v.context, v) //NOSONAR
            SongMenuUtils.setupSongMenu(menu, false, true, playlistMenuHelper) //NOSONAR
            menu.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener(song, presenter)) //NOSONAR
            menu.show() //NOSONAR
        }

        override fun onStartDrag(holder: SongView.ViewHolder) { //NOSONAR
            // Intentionally left empty.
        }
    }

    private val albumViewClickListener = object : AlbumView.ClickListener { //NOSONAR
        override fun onAlbumClick(position: Int, albumView: AlbumView, viewHolder: AlbumView.ViewHolder) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(albumView, albumView.album.getSongsSingle(songsRepository))) { //NOSONAR
                presenter.onAlbumClick(albumView, viewHolder) //NOSONAR
            }
        }

        override fun onAlbumLongClick(position: Int, albumView: AlbumView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(albumView, albumView.album.getSongsSingle(songsRepository)) //NOSONAR
        }

        override fun onAlbumOverflowClicked(v: View, album: Album) { //NOSONAR
            val menu = PopupMenu(v.context, v) //NOSONAR
            AlbumMenuUtils.setupAlbumMenu(menu, playlistMenuHelper, true) //NOSONAR
            menu.setOnMenuItemClickListener(AlbumMenuUtils.getAlbumMenuClickListener(album, presenter)) //NOSONAR
            menu.show() //NOSONAR
        }
    }

    private val albumArtistClickListener = object : AlbumArtistView.ClickListener { //NOSONAR
        override fun onAlbumArtistClick(position: Int, albumArtistView: AlbumArtistView, viewholder: AlbumArtistView.ViewHolder) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(albumArtistView, albumArtistView.albumArtist.getSongsSingle(songsRepository))) { //NOSONAR
                presenter.onArtistClicked(albumArtistView, viewholder) //NOSONAR
            }
        }

        override fun onAlbumArtistLongClick(position: Int, albumArtistView: AlbumArtistView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(albumArtistView, albumArtistView.albumArtist.getSongsSingle(songsRepository)) //NOSONAR
        }

        override fun onAlbumArtistOverflowClicked(v: View, albumArtist: AlbumArtist) { //NOSONAR
            val menu = PopupMenu(v.context, v) //NOSONAR
            menu.inflate(R.menu.menu_artist) //NOSONAR
            menu.setOnMenuItemClickListener( //NOSONAR
                    AlbumArtistMenuUtils.getAlbumArtistClickListener(albumArtist, presenter)) //NOSONAR
            menu.show() //NOSONAR
        }
    }

    companion object { //NOSONAR

        private const val TAG = "SearchFragment" //NOSONAR

        const val ARG_QUERY = "query" //NOSONAR

        fun newInstance(query: String?) = SearchFragment().withArgs { //NOSONAR
            putString(ARG_QUERY, query) //NOSONAR
        }
    }
}
