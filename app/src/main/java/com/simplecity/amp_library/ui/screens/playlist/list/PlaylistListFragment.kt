@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.list // NOSONAR

import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v7.widget.LinearLayoutManager // NOSONAR
import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.RecyclerView // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.View // NOSONAR
import android.view.ViewGroup // NOSONAR
import android.widget.Toast // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment // NOSONAR
import com.simplecity.amp_library.ui.dialog.WeekSelectorDialog // NOSONAR
import com.simplecity.amp_library.ui.modelviews.PlaylistView // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.DeletePlaylistConfirmationDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.M3uPlaylistDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.RenamePlaylistDialog // NOSONAR
import com.simplecity.amp_library.utils.menu.playlist.PlaylistMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.withArgs // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import javax.inject.Inject // NOSONAR

class PlaylistListFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    PlaylistListContract.View, //NOSONAR
    PlaylistView.OnClickListener { //NOSONAR

    private lateinit var adapter: ViewModelAdapter //NOSONAR

    private var playlistClickListener: PlaylistClickListener? = null //NOSONAR

    private val refreshDisposable: Disposable? = null //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    @Inject lateinit var presenter: PlaylistListPresenter //NOSONAR

    @Inject lateinit var playlistsRepository: Repository.PlaylistsRepository //NOSONAR

    interface PlaylistClickListener { //NOSONAR

        fun onPlaylistClicked(playlist: Playlist) //NOSONAR
    } // NOSONAR

    // Lifecycle // NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        if (parentFragment is PlaylistClickListener) { //NOSONAR
            playlistClickListener = parentFragment as PlaylistClickListener? //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        adapter = ViewModelAdapter() //NOSONAR
    } // NOSONAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_recycler, container, false) //NOSONAR
    } // NOSONAR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        (view as RecyclerView).layoutManager = LinearLayoutManager(context) //NOSONAR
        view.setRecyclerListener(RecyclerListener()) //NOSONAR
        view.adapter = adapter //NOSONAR

        presenter.bindView(this) //NOSONAR
    } // NOSONAR

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadData() //NOSONAR
    } // NOSONAR

    override fun onPause() { //NOSONAR
        super.onPause() //NOSONAR

        refreshDisposable?.dispose() //NOSONAR

        disposables.clear() //NOSONAR
    } // NOSONAR

    override fun onDestroyView() { //NOSONAR
        presenter.unbindView(this) //NOSONAR
        super.onDestroyView() //NOSONAR
    } // NOSONAR

    override fun onDetach() { //NOSONAR
        super.onDetach() //NOSONAR

        playlistClickListener = null //NOSONAR
    } // NOSONAR


    // PlaylistView.PlaylistClickListener Implementation // NOSONAR

    override fun onPlaylistClick(position: Int, playlistView: PlaylistView) { //NOSONAR
        playlistClickListener?.onPlaylistClicked(playlistView.playlist) //NOSONAR
    } // NOSONAR

    override fun onPlaylistOverflowClick(position: Int, view: View, playlist: Playlist) { //NOSONAR
        val menu = PopupMenu(context!!, view) //NOSONAR
        PlaylistMenuUtils.setupPlaylistMenu(menu, playlist) //NOSONAR
        menu.setOnMenuItemClickListener(PlaylistMenuUtils.getPlaylistPopupMenuClickListener(playlist, presenter)) //NOSONAR
        menu.show() //NOSONAR
    } // NOSONAR


    // PlaylistListContract.View Implementation // NOSONAR

    override fun setData(playlists: List<Playlist>) { //NOSONAR
        adapter.setItems(playlists.map { playlist -> //NOSONAR
            PlaylistView(playlist).apply { setListener(this@PlaylistListFragment) } as com.simplecityapps.recycler_adapter.model.ViewModel<*> //NOSONAR
        }) // NOSONAR
    } // NOSONAR


    // PlaylistMenuContract.View Implementation // NOSONAR

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message // NOSONAR
        Toast.makeText(context, R.string.empty_playlist, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    override fun presentEditDialog(playlist: Playlist) { //NOSONAR
        WeekSelectorDialog().show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentRenameDialog(playlist: Playlist) { //NOSONAR
        RenamePlaylistDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentM3uDialog(playlist: Playlist) { //NOSONAR
        M3uPlaylistDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentDeletePlaylistDialog(playlist: Playlist) { //NOSONAR
        DeletePlaylistConfirmationDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR


    // BaseFragment Implementation // NOSONAR

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    } // NOSONAR

    // Static // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "PlaylistListFragment" //NOSONAR

        private const val ARG_TITLE = "title" //NOSONAR

        fun newInstance(title: String) = PlaylistListFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
