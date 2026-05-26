@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.list // NOSONAR

import com.simplecity.amp_library.data.Repository.PlaylistsRepository // NOSONAR
import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListContract.View // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.menu.playlist.PlaylistMenuCallbacks // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import javax.inject.Inject // NOSONAR

class PlaylistListPresenter @Inject constructor( //NOSONAR
    private val playlistsRepository: PlaylistsRepository, //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val playlistMenuPresenter: PlaylistMenuPresenter //NOSONAR
) : // NOSONAR
    Presenter<PlaylistListContract.View>(), //NOSONAR
    PlaylistListContract.Presenter, //NOSONAR
    PlaylistMenuCallbacks by playlistMenuPresenter { //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR

        playlistMenuPresenter.bindView(view) //NOSONAR
    } // NOSONAR

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR

        playlistMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    override fun loadData() { //NOSONAR
        addDisposable(playlistsRepository.getAllPlaylists(songsRepository) //NOSONAR
            .map { playlists -> //NOSONAR
                playlists.apply { //NOSONAR
                    sortBy { playlist -> playlist.name } //NOSONAR
                    sortBy { playlist -> playlist.type } //NOSONAR
                } // NOSONAR
            } // NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { playlists -> view?.setData(playlists) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Failed to load data", error) } //NOSONAR
            ) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        private const val TAG = "PlaylistListPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
