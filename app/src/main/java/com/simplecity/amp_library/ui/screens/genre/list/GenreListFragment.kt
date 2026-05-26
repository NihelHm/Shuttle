@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.list

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.adapters.SectionedAdapter
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.modelviews.EmptyView
import com.simplecity.amp_library.ui.modelviews.GenreView
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog
import com.simplecity.amp_library.ui.settings.SettingsParentFragment.ARG_TITLE
import com.simplecity.amp_library.utils.menu.genre.GenreMenuUtils
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper
import com.simplecity.amp_library.utils.withArgs
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class GenreListFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    GenreView.ClickListener, //NOSONAR
    GenreListContract.View { //NOSONAR

    private var genreClickListener: GenreClickListener? = null //NOSONAR

    private lateinit var recyclerView: FastScrollRecyclerView //NOSONAR

    private lateinit var adapter: SectionedAdapter //NOSONAR

    private var refreshDisposable: Disposable? = null //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    @Inject lateinit var presenter: GenreListPresenter //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    interface GenreClickListener { //NOSONAR
        fun onGenreClicked(genre: Genre) //NOSONAR
    }

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        if (parentFragment is GenreClickListener) { //NOSONAR
            genreClickListener = parentFragment as GenreClickListener? //NOSONAR
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        adapter = SectionedAdapter() //NOSONAR
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        recyclerView = inflater.inflate(R.layout.fragment_recycler, container, false) as FastScrollRecyclerView //NOSONAR
        return recyclerView //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR
        recyclerView.adapter = adapter //NOSONAR

        presenter.bindView(this) //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadGenres() //NOSONAR
    }

    override fun onPause() { //NOSONAR

        refreshDisposable?.dispose() //NOSONAR

        disposables.clear() //NOSONAR

        super.onPause() //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        super.onDestroyView() //NOSONAR

        presenter.unbindView(this) //NOSONAR
    }

    override fun onItemClick(genre: Genre) { //NOSONAR
        genreClickListener?.onGenreClicked(genre) //NOSONAR
    }

    override fun onOverflowClick(v: View, genre: Genre) { //NOSONAR
        val popupMenu = PopupMenu(context!!, v) //NOSONAR
        popupMenu.inflate(R.menu.menu_genre) //NOSONAR

        // Add playlist menu
        val subMenu = popupMenu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR

        popupMenu.setOnMenuItemClickListener(GenreMenuUtils.getGenreClickListener(genre, presenter)) //NOSONAR
        popupMenu.show() //NOSONAR
    }

    // GenreListContract.View Implementation

    override fun setData(genres: List<Genre>) { //NOSONAR
        if (genres.isEmpty()) { //NOSONAR
            adapter.setItems(listOf(EmptyView(R.string.empty_genres))) //NOSONAR
        } else { //NOSONAR
            adapter.setItems(genres.map { //NOSONAR
                val genreView = GenreView(it) //NOSONAR
                genreView.setClickListener(this) //NOSONAR
                genreView //NOSONAR
            })
        }
    }

    // GenreMenuContract.View Implementation

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

    // BaseFragment Implementation

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    }

    // Static

    companion object { //NOSONAR

        private const val TAG = "GenreListFragment" //NOSONAR

        fun newInstance(title: String) = GenreListFragment().withArgs { //NOSONAR
            putString(ARG_TITLE, title) //NOSONAR
        }
    }
}
