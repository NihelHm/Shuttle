@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.menu

import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract.View
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent.Type
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.extensions.getSongs
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import com.simplecity.amp_library.utils.sorting.SortManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlbumArtistMenuPresenter @Inject constructor( //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val songsRepository: Repository.SongsRepository, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val blacklistRepository: Repository.BlacklistRepository, //NOSONAR
    private val navigationEventRelay: NavigationEventRelay, //NOSONAR
    private val sortManager: SortManager //NOSONAR

) : Presenter<View>(), AlbumArtistMenuContract.Presenter { //NOSONAR

    override fun createArtistsPlaylist(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            view?.presentCreatePlaylistDialog(songs) //NOSONAR
        }
    }

    override fun addArtistsToPlaylist(playlist: Playlist, albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            playlistManager.addToPlaylist(playlist, songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToPlaylist(playlist, numSongs) //NOSONAR
            }
        }
    }

    override fun addArtistsToQueue(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            mediaManager.addToQueue(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            }
        }
    }

    override fun playArtistsNext(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            mediaManager.playNext(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            }
        }
    }

    override fun play(albumArtist: AlbumArtist) { //NOSONAR
        mediaManager.playAll(albumArtist.getSongsSingle(songsRepository)) { view?.onPlaybackFailed() } //NOSONAR
    }

    override fun editTags(albumArtist: AlbumArtist) { //NOSONAR
        view?.presentTagEditorDialog(albumArtist) //NOSONAR
    }

    override fun albumArtistInfo(albumArtist: AlbumArtist) { //NOSONAR
        view?.presentAlbumArtistInfoDialog(albumArtist) //NOSONAR
    }

    override fun editArtwork(albumArtist: AlbumArtist) { //NOSONAR
        view?.presentArtworkEditorDialog(albumArtist) //NOSONAR
    }

    override fun blacklistArtists(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> blacklistRepository.addAllSongs(songs) } //NOSONAR
    }

    override fun deleteArtists(albumArtists: List<AlbumArtist>) { //NOSONAR
        view?.presentArtistDeleteDialog(albumArtists) //NOSONAR
    }

    override fun goToArtist(albumArtist: AlbumArtist) { //NOSONAR
        navigationEventRelay.sendEvent(NavigationEvent(Type.GO_TO_ARTIST, albumArtist, true)) //NOSONAR
    }

    override fun albumShuffle(albumArtist: AlbumArtist) { //NOSONAR
        mediaManager.playAll(albumArtist.getSongs(songsRepository) //NOSONAR
            .map { songs -> Operators.albumShuffleSongs(songs, sortManager) }) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
            Unit //NOSONAR
        }
    }

    override fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            src //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe( //NOSONAR
                    { items -> dst(items) }, //NOSONAR
                    { error -> LogUtils.logException(SongMenuPresenter.TAG, "Failed to transform src single", error) } //NOSONAR
                )
        )
    }

    private fun getSongs(albumArtists: List<AlbumArtist>, onSuccess: (songs: List<Song>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            albumArtists.getSongs(songsRepository) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    onSuccess, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Failed to retrieve songs", error) } //NOSONAR
                )
        )
    }

    companion object { //NOSONAR
        const val TAG = "AlbumMenuContract" //NOSONAR
    }

}
