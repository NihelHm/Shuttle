@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.suggested // NOSONAR

import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v7.widget.GridLayoutManager // NOSONAR
import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.RecyclerView // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.View // NOSONAR
import android.view.ViewGroup // NOSONAR
import android.widget.Toast // NOSONAR
import com.bumptech.glide.RequestManager // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.R.string // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.model.SuggestedHeader // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment // NOSONAR
import com.simplecity.amp_library.ui.dialog.AlbumBiographyDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.DeleteDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.SongInfoDialog // NOSONAR
import com.simplecity.amp_library.ui.modelviews.AlbumView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.EmptyView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.HorizontalRecyclerView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SuggestedHeaderView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SuggestedSongView // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragment // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.suggested.SuggestedPresenter.SuggestedData // NOSONAR
import com.simplecity.amp_library.ui.views.SuggestedDividerDecoration // NOSONAR
import com.simplecity.amp_library.utils.ArtworkDialog // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils // NOSONAR
import com.simplecity.amp_library.utils.extensions.share // NOSONAR
import com.simplecity.amp_library.utils.menu.album.AlbumMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.simplecity.amp_library.utils.withArgs // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import java.util.ArrayList // NOSONAR
import javax.inject.Inject // NOSONAR

class SuggestedFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    SuggestedHeaderView.ClickListener, //NOSONAR
    AlbumView.ClickListener, //NOSONAR
    SuggestedContract.View { //NOSONAR

    @Inject lateinit var presenter: SuggestedPresenter //NOSONAR

    @Inject lateinit var songsRepository: Repository.SongsRepository //NOSONAR

    @Inject lateinit var sortManager: SortManager //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var favoritesPlaylistManager: FavoritesPlaylistManager //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    private lateinit var adapter: ViewModelAdapter //NOSONAR

    private lateinit var favoriteRecyclerView: HorizontalRecyclerView //NOSONAR

    private lateinit var mostPlayedRecyclerView: HorizontalRecyclerView //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private val refreshDisposables = CompositeDisposable() //NOSONAR

    private var suggestedClickListener: SuggestedClickListener? = null //NOSONAR

    interface SuggestedClickListener { //NOSONAR

        fun onAlbumArtistClicked(albumArtist: AlbumArtist, transitionView: View) //NOSONAR

        fun onAlbumClicked(album: Album, transitionView: View) //NOSONAR
    } // NOSONAR


    // Lifecycle // NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        if (parentFragment is SuggestedClickListener) { //NOSONAR
            suggestedClickListener = parentFragment as SuggestedClickListener? //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        adapter = ViewModelAdapter() //NOSONAR
        mostPlayedRecyclerView = HorizontalRecyclerView("SuggestedFragment - mostPlayed") //NOSONAR
        favoriteRecyclerView = HorizontalRecyclerView("SuggestedFragment - favorite") //NOSONAR
    } // NOSONAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_suggested, container, false) as RecyclerView //NOSONAR
    } // NOSONAR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        view as RecyclerView //NOSONAR

        val spanCount = if (ShuttleUtils.isTablet(context!!)) 12 else 6 //NOSONAR

        val gridLayoutManager = GridLayoutManager(context, spanCount) //NOSONAR
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() { //NOSONAR
            override fun getSpanSize(position: Int): Int { //NOSONAR
                if (!adapter.items.isEmpty() && position >= 0) { //NOSONAR
                    val item = adapter.items[position] //NOSONAR
                    if (item is HorizontalRecyclerView //NOSONAR
                        || item is SuggestedHeaderView //NOSONAR
                        || item is AlbumView && item.getViewType() == ViewType.ALBUM_LIST //NOSONAR
                        || item is AlbumView && item.getViewType() == ViewType.ALBUM_LIST_SMALL //NOSONAR
                        || item is EmptyView //NOSONAR
                    ) { // NOSONAR
                        return spanCount //NOSONAR
                    } // NOSONAR
                    if (item is AlbumView && item.getViewType() == ViewType.ALBUM_CARD_LARGE) { //NOSONAR
                        return 3 //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                return 2 //NOSONAR
            } // NOSONAR
        } // NOSONAR

        view.addItemDecoration(SuggestedDividerDecoration(resources)) //NOSONAR
        view.setRecyclerListener(RecyclerListener()) //NOSONAR
        view.layoutManager = gridLayoutManager //NOSONAR
        view.adapter = adapter //NOSONAR

        presenter.bindView(this) //NOSONAR
    } // NOSONAR

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadData() //NOSONAR
    } // NOSONAR

    override fun onPause() { //NOSONAR

        disposables.clear() //NOSONAR

        refreshDisposables.clear() //NOSONAR

        super.onPause() //NOSONAR
    } // NOSONAR

    override fun onDestroyView() { //NOSONAR
        super.onDestroyView() //NOSONAR

        presenter.unbindView(this) //NOSONAR
    } // NOSONAR

    override fun onDetach() { //NOSONAR
        super.onDetach() //NOSONAR

        suggestedClickListener = null //NOSONAR
    } // NOSONAR

    inner class SongClickListener(val songs: List<Song>) : SuggestedSongView.ClickListener { //NOSONAR

        override fun onSongClick(song: Song, holder: SuggestedSongView.ViewHolder) { //NOSONAR
            mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
                onPlaybackFailed() //NOSONAR
            } // NOSONAR
        } // NOSONAR

        override fun onSongOverflowClicked(v: View, position: Int, song: Song) { //NOSONAR
            val popupMenu = PopupMenu(context!!, v) //NOSONAR
            SongMenuUtils.setupSongMenu(popupMenu, false, true, playlistMenuHelper) //NOSONAR
            popupMenu.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener(song, presenter)) //NOSONAR
            popupMenu.show() //NOSONAR
        } // NOSONAR
    } // NOSONAR


    // AlbumView.ClickListener implementation // NOSONAR

    override fun onAlbumClick(position: Int, albumView: AlbumView, viewHolder: AlbumView.ViewHolder) { //NOSONAR
        suggestedClickListener?.onAlbumClicked(albumView.album, viewHolder.imageOne) //NOSONAR
    } // NOSONAR

    override fun onAlbumLongClick(position: Int, albumView: AlbumView): Boolean { //NOSONAR
        return false //NOSONAR
    } // NOSONAR

    override fun onAlbumOverflowClicked(v: View, album: Album) { //NOSONAR
        val menu = PopupMenu(context!!, v) //NOSONAR
        AlbumMenuUtils.setupAlbumMenu(menu, playlistMenuHelper, true) //NOSONAR
        menu.setOnMenuItemClickListener(AlbumMenuUtils.getAlbumMenuClickListener(album, presenter)) //NOSONAR
        menu.show() //NOSONAR
    } // NOSONAR

    override fun onSuggestedHeaderClick(suggestedHeader: SuggestedHeader) { //NOSONAR
        navigationController.pushViewController(PlaylistDetailFragment.newInstance(suggestedHeader.playlist), "PlaylistListFragment") //NOSONAR
    } // NOSONAR


    // SuggestedContract.View implementation // NOSONAR

    override fun setData(suggestedData: SuggestedData) { //NOSONAR

        val viewModels = ArrayList<ViewModel<*>>() //NOSONAR

        if (suggestedData.mostPlayedSongs.isNotEmpty()) { //NOSONAR
            val mostPlayedHeader = SuggestedHeader(getString(string.mostplayed), getString(string.suggested_most_played_songs_subtitle), suggestedData.mostPlayedPlaylist) //NOSONAR
            val mostPlayedHeaderView = SuggestedHeaderView(mostPlayedHeader) //NOSONAR
            mostPlayedHeaderView.setClickListener(this) //NOSONAR
            viewModels.add(mostPlayedHeaderView) //NOSONAR
            viewModels.add(mostPlayedRecyclerView) //NOSONAR

            val songClickListener = SongClickListener(suggestedData.mostPlayedSongs) //NOSONAR

            mostPlayedRecyclerView.setItems(suggestedData.mostPlayedSongs //NOSONAR
                .map { song -> //NOSONAR
                    val suggestedSongView = SuggestedSongView(song, requestManager, settingsManager) //NOSONAR
                    suggestedSongView.setClickListener(songClickListener) //NOSONAR
                    suggestedSongView //NOSONAR
                }) // NOSONAR
        } // NOSONAR

        if (suggestedData.recentlyPlayedAlbums.isNotEmpty()) { //NOSONAR
            val recentlyPlayedHeader = SuggestedHeader(getString(string.suggested_recent_title), getString(string.suggested_recent_subtitle), suggestedData.recentlyPlayedPlaylist) //NOSONAR
            val recentlyPlayedHeaderView = SuggestedHeaderView(recentlyPlayedHeader) //NOSONAR
            recentlyPlayedHeaderView.setClickListener(this) //NOSONAR
            viewModels.add(recentlyPlayedHeaderView) //NOSONAR

            viewModels.addAll( //NOSONAR
                suggestedData.recentlyPlayedAlbums //NOSONAR
                    .map { album -> //NOSONAR
                        val albumView = AlbumView(album, ViewType.ALBUM_LIST_SMALL, requestManager, sortManager, settingsManager) //NOSONAR
                        albumView.setClickListener(this) //NOSONAR
                        albumView //NOSONAR
                    }.toList() //NOSONAR
            ) // NOSONAR
        } // NOSONAR

        if (suggestedData.favoriteSongs.isNotEmpty()) { //NOSONAR
            val favoriteSongsHeader = SuggestedHeader(getString(string.fav_title), getString(string.suggested_favorite_subtitle), suggestedData.favoriteSongsPlaylist) //NOSONAR
            val favoriteHeaderView = SuggestedHeaderView(favoriteSongsHeader) //NOSONAR
            favoriteHeaderView.setClickListener(this) //NOSONAR
            viewModels.add(favoriteHeaderView) //NOSONAR

            viewModels.add(favoriteRecyclerView) //NOSONAR

            val songClickListener = SongClickListener(suggestedData.favoriteSongs) //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, "favoriteRecyclerView.setItems()") //NOSONAR
            favoriteRecyclerView.setItems( //NOSONAR
                suggestedData.favoriteSongs //NOSONAR
                    .map { song -> //NOSONAR
                        val suggestedSongView = SuggestedSongView(song, requestManager, settingsManager) //NOSONAR
                        suggestedSongView.setClickListener(songClickListener) //NOSONAR
                        suggestedSongView //NOSONAR
                    }.toList() //NOSONAR
            ) // NOSONAR
        } // NOSONAR

        if (suggestedData.recentlyAddedAlbums.isNotEmpty()) { //NOSONAR
            val recentlyAddedHeader = SuggestedHeader(getString(string.recentlyadded), getString(string.suggested_recently_added_subtitle), suggestedData.recentlyAddedAlbumsPlaylist) //NOSONAR
            val recentlyAddedHeaderView = SuggestedHeaderView(recentlyAddedHeader) //NOSONAR
            recentlyAddedHeaderView.setClickListener(this) //NOSONAR
            viewModels.add(recentlyAddedHeaderView) //NOSONAR

            viewModels.addAll( //NOSONAR
                suggestedData.recentlyAddedAlbums //NOSONAR
                    .map { album -> //NOSONAR
                        val albumView = AlbumView(album, ViewType.ALBUM_CARD, requestManager, sortManager, settingsManager) //NOSONAR
                        albumView.setClickListener(this) //NOSONAR
                        albumView //NOSONAR
                    }.toList() //NOSONAR
            ) // NOSONAR
        } // NOSONAR

        if (viewModels.isEmpty()) { //NOSONAR
            refreshDisposables.add(adapter.setItems(listOf<ViewModel<*>>(EmptyView(R.string.empty_suggested)))) //NOSONAR
        } else { //NOSONAR
            refreshDisposables.add(adapter.setItems(viewModels)) //NOSONAR
        } // NOSONAR
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
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
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


    // AlbumMenuContract.View Implementation // NOSONAR

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message // NOSONAR
        Toast.makeText(context, R.string.emptyplaylist, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    override fun presentTagEditorDialog(album: Album) { //NOSONAR
        TaggerDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentDeleteAlbumsDialog(albums: List<Album>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListAlbumsRef { albums }).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentAlbumInfoDialog(album: Album) { //NOSONAR
        AlbumBiographyDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentArtworkEditorDialog(album: Album) { //NOSONAR
        ArtworkDialog.build(context, album).show() //NOSONAR
    } // NOSONAR


    // BaseFragment implementation // NOSONAR

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    } // NOSONAR


    // Static // NOSONAR

    companion object { //NOSONAR

        private const val ARG_TITLE = "title" //NOSONAR

        private const val TAG = "SuggestedFragment" //NOSONAR

        fun newInstance(title: String) = SuggestedFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
