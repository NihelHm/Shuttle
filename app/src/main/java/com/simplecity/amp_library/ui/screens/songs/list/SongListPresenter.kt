@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.list // NOSONAR

import com.simplecity.amp_library.data.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.list.SongListContract.View // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import javax.inject.Inject // NOSONAR

class SongListPresenter @Inject constructor( //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val settingsManager: SettingsManager, //NOSONAR
    private val songMenuPresenter: SongMenuPresenter //NOSONAR
) : Presenter<View>(), //NOSONAR
    SongListContract.Presenter, //NOSONAR
    SongMenuContract.Presenter by songMenuPresenter { //NOSONAR

    private var songs = mutableListOf<Song>() //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR
        songMenuPresenter.bindView(view) //NOSONAR
    } // NOSONAR

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR
        songMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    override fun loadSongs(scrollToTop: Boolean) { //NOSONAR
        addDisposable(songsRepository.getSongs() //NOSONAR
            .map { songs -> //NOSONAR
                val songs = songs.toMutableList() //NOSONAR

                sortManager.sortSongs(songs) //NOSONAR

                if (!sortManager.songsAscending) { //NOSONAR
                    songs.reverse() //NOSONAR
                } // NOSONAR
                songs //NOSONAR
            } // NOSONAR
            .subscribe({ songs -> //NOSONAR
                this.songs = songs //NOSONAR
                view?.setData(songs, scrollToTop) //NOSONAR
            }, { error -> //NOSONAR
                LogUtils.logException(TAG, "Failed to load songs", error) //NOSONAR
            }) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    override fun setSongsSortOrder(order: Int) { //NOSONAR
        sortManager.songsSortOrder = order //NOSONAR
        loadSongs(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    override fun setSongsAscending(ascending: Boolean) { //NOSONAR
        sortManager.songsAscending = ascending //NOSONAR
        loadSongs(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    override fun setShowArtwork(show: Boolean) { //NOSONAR
        settingsManager.setShowArtworkInSongList(show) //NOSONAR
        loadSongs(false) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    override fun play(song: Song) { //NOSONAR
        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.showPlaybackError() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun shuffleAll() { //NOSONAR
        mediaManager.shuffleAll(songsRepository.getSongs().firstOrError()) { //NOSONAR
            view?.showPlaybackError() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "SongListPresenter" //NOSONAR
    } // NOSONAR

} // NOSONAR

