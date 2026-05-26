@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.menu

import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlaylistMenuPresenter @Inject constructor( //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val favoritesPlaylistManager: FavoritesPlaylistManager //NOSONAR
) :
    Presenter<PlaylistMenuContract.View>(), //NOSONAR
    PlaylistMenuContract.Presenter { //NOSONAR

    override fun playNext(playlist: Playlist) { //NOSONAR
        getSongs(playlist) { songs -> //NOSONAR
            mediaManager.playNext(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            }
        }
    }

    override fun play(playlist: Playlist) { //NOSONAR
        getSongs(playlist) { songs -> //NOSONAR
            mediaManager.playAll(songsRepository.getSongs(playlist).first(emptyList())) { view?.onPlaybackFailed() } //NOSONAR
        }
    }

    override fun delete(playlist: Playlist) { //NOSONAR
        getSongs(playlist) { songs -> //NOSONAR
            view?.presentDeletePlaylistDialog(playlist) //NOSONAR
        }
    }

    override fun edit(playlist: Playlist) { //NOSONAR
        view?.presentEditDialog(playlist) //NOSONAR
    }

    override fun rename(playlist: Playlist) { //NOSONAR
        view?.presentRenameDialog(playlist) //NOSONAR
    }

    override fun createM3uPlaylist(playlist: Playlist) { //NOSONAR
        view?.presentM3uDialog(playlist) //NOSONAR
    }

    override fun clear(playlist: Playlist) { //NOSONAR
        playlist.clear(playlistManager, favoritesPlaylistManager) //NOSONAR
    }

    private fun getSongs(playlist: Playlist, onSuccess: (songs: List<Song>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            songsRepository.getSongs(playlist).first(emptyList()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    onSuccess, //NOSONAR
                    { error -> LogUtils.logException(AlbumMenuPresenter.TAG, "Failed to retrieve songs", error) } //NOSONAR
                )
        )
    }
}
