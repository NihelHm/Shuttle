@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.list

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.dialog.WeekSelectorDialog
import com.simplecity.amp_library.ui.modelviews.PlaylistView
import com.simplecity.amp_library.ui.screens.playlist.dialog.DeletePlaylistConfirmationDialog
import com.simplecity.amp_library.ui.screens.playlist.dialog.M3uPlaylistDialog
import com.simplecity.amp_library.ui.screens.playlist.dialog.RenamePlaylistDialog
import com.simplecity.amp_library.utils.menu.playlist.PlaylistMenuUtils
import com.simplecity.amp_library.utils.withArgs
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

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
    }

    // Lifecycle

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        if (parentFragment is PlaylistClickListener) { //NOSONAR
            playlistClickListener = parentFragment as PlaylistClickListener? //NOSONAR
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        adapter = ViewModelAdapter() //NOSONAR
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_recycler, container, false) //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        (view as RecyclerView).layoutManager = LinearLayoutManager(context) //NOSONAR
        view.setRecyclerListener(RecyclerListener()) //NOSONAR
        view.adapter = adapter //NOSONAR

        presenter.bindView(this) //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadData() //NOSONAR
    }

    override fun onPause() { //NOSONAR
        super.onPause() //NOSONAR

        refreshDisposable?.dispose() //NOSONAR

        disposables.clear() //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        presenter.unbindView(this) //NOSONAR
        super.onDestroyView() //NOSONAR
    }

    override fun onDetach() { //NOSONAR
        super.onDetach() //NOSONAR

        playlistClickListener = null //NOSONAR
    }


    // PlaylistView.PlaylistClickListener Implementation

    override fun onPlaylistClick(position: Int, playlistView: PlaylistView) { //NOSONAR
        playlistClickListener?.onPlaylistClicked(playlistView.playlist) //NOSONAR
    }

    override fun onPlaylistOverflowClick(position: Int, view: View, playlist: Playlist) { //NOSONAR
        val menu = PopupMenu(context!!, view) //NOSONAR
        PlaylistMenuUtils.setupPlaylistMenu(menu, playlist) //NOSONAR
        menu.setOnMenuItemClickListener(PlaylistMenuUtils.getPlaylistPopupMenuClickListener(playlist, presenter)) //NOSONAR
        menu.show() //NOSONAR
    }


    // PlaylistListContract.View Implementation

    override fun setData(playlists: List<Playlist>) { //NOSONAR
        adapter.setItems(playlists.map { playlist -> //NOSONAR
            PlaylistView(playlist).apply { setListener(this@PlaylistListFragment) } as com.simplecityapps.recycler_adapter.model.ViewModel<*> //NOSONAR
        })
    }


    // PlaylistMenuContract.View Implementation

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message
        Toast.makeText(context, R.string.empty_playlist, Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun presentEditDialog(playlist: Playlist) { //NOSONAR
        WeekSelectorDialog().show(childFragmentManager) //NOSONAR
    }

    override fun presentRenameDialog(playlist: Playlist) { //NOSONAR
        RenamePlaylistDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    }

    override fun presentM3uDialog(playlist: Playlist) { //NOSONAR
        M3uPlaylistDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    }

    override fun presentDeletePlaylistDialog(playlist: Playlist) { //NOSONAR
        DeletePlaylistConfirmationDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    }

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    }


    // BaseFragment Implementation

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    }

    // Static

    companion object { //NOSONAR

        private const val TAG = "PlaylistListFragment" //NOSONAR

        private const val ARG_TITLE = "title" //NOSONAR

        fun newInstance(title: String) = PlaylistListFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        }
    }
}
