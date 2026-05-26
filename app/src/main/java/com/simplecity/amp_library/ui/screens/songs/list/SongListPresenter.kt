@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.list

import com.simplecity.amp_library.data.SongsRepository
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.songs.list.SongListContract.View
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.sorting.SortManager
import javax.inject.Inject

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
    }

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR
        songMenuPresenter.unbindView(view) //NOSONAR
    }

    override fun loadSongs(scrollToTop: Boolean) { //NOSONAR
        addDisposable(songsRepository.getSongs() //NOSONAR
            .map { songs -> //NOSONAR
                val songs = songs.toMutableList() //NOSONAR

                sortManager.sortSongs(songs) //NOSONAR

                if (!sortManager.songsAscending) { //NOSONAR
                    songs.reverse() //NOSONAR
                }
                songs //NOSONAR
            }
            .subscribe({ songs -> //NOSONAR
                this.songs = songs //NOSONAR
                view?.setData(songs, scrollToTop) //NOSONAR
            }, { error -> //NOSONAR
                LogUtils.logException(TAG, "Failed to load songs", error) //NOSONAR
            })
        )
    }

    override fun setSongsSortOrder(order: Int) { //NOSONAR
        sortManager.songsSortOrder = order //NOSONAR
        loadSongs(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    override fun setSongsAscending(ascending: Boolean) { //NOSONAR
        sortManager.songsAscending = ascending //NOSONAR
        loadSongs(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    override fun setShowArtwork(show: Boolean) { //NOSONAR
        settingsManager.setShowArtworkInSongList(show) //NOSONAR
        loadSongs(false) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    override fun play(song: Song) { //NOSONAR
        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.showPlaybackError() //NOSONAR
        }
    }

    override fun shuffleAll() { //NOSONAR
        mediaManager.shuffleAll(songsRepository.getSongs().firstOrError()) { //NOSONAR
            view?.showPlaybackError() //NOSONAR
        }
    }

    companion object { //NOSONAR
        const val TAG = "SongListPresenter" //NOSONAR
    }

}

