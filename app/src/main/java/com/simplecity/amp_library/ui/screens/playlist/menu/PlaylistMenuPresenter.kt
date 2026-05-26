@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.menu // NOSONAR

import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR

class PlaylistMenuPresenter @Inject constructor( //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val favoritesPlaylistManager: FavoritesPlaylistManager //NOSONAR
) : // NOSONAR
    Presenter<PlaylistMenuContract.View>(), //NOSONAR
    PlaylistMenuContract.Presenter { //NOSONAR

    override fun playNext(playlist: Playlist) { //NOSONAR
        getSongs(playlist) { songs -> //NOSONAR
            mediaManager.playNext(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun play(playlist: Playlist) { //NOSONAR
        getSongs(playlist) { songs -> //NOSONAR
            mediaManager.playAll(songsRepository.getSongs(playlist).first(emptyList())) { view?.onPlaybackFailed() } //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun delete(playlist: Playlist) { //NOSONAR
        getSongs(playlist) { songs -> //NOSONAR
            view?.presentDeletePlaylistDialog(playlist) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun edit(playlist: Playlist) { //NOSONAR
        view?.presentEditDialog(playlist) //NOSONAR
    } // NOSONAR

    override fun rename(playlist: Playlist) { //NOSONAR
        view?.presentRenameDialog(playlist) //NOSONAR
    } // NOSONAR

    override fun createM3uPlaylist(playlist: Playlist) { //NOSONAR
        view?.presentM3uDialog(playlist) //NOSONAR
    } // NOSONAR

    override fun clear(playlist: Playlist) { //NOSONAR
        playlist.clear(playlistManager, favoritesPlaylistManager) //NOSONAR
    } // NOSONAR

    private fun getSongs(playlist: Playlist, onSuccess: (songs: List<Song>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            songsRepository.getSongs(playlist).first(emptyList()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    onSuccess, //NOSONAR
                    { error -> LogUtils.logException(AlbumMenuPresenter.TAG, "Failed to retrieve songs", error) } //NOSONAR
                ) // NOSONAR
        ) // NOSONAR
    } // NOSONAR
} // NOSONAR
