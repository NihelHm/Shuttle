@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.menu

import android.content.Context
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.extensions.getSongs
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GenreMenuPresenter @Inject constructor( //NOSONAR
    private val context: Context, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val playlistManager: PlaylistManager //NOSONAR
) : Presenter<GenreMenuContract.View>(), GenreMenuContract.Presenter { //NOSONAR
    override fun createPlaylist(genre: Genre) { //NOSONAR
        getSongs(genre) { songs -> //NOSONAR
            view?.presentCreatePlaylistDialog(songs) //NOSONAR
        }
    }

    override fun addToPlaylist(playlist: Playlist, genre: Genre) { //NOSONAR
        getSongs(genre) { songs -> //NOSONAR
            playlistManager.addToPlaylist(playlist, songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToPlaylist(playlist, numSongs) //NOSONAR
            }
        }
    }

    override fun addToQueue(genre: Genre) { //NOSONAR
        getSongs(genre) { songs -> //NOSONAR
            mediaManager.addToQueue(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            }
        }
    }

    override fun play(genre: Genre) { //NOSONAR
        mediaManager.playAll(genre.getSongs(context)) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        }
    }

    override fun playNext(genre: Genre) { //NOSONAR
        getSongs(genre) { songs -> //NOSONAR
            mediaManager.playNext(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            }
        }
    }

    private fun getSongs(genre: Genre, onSuccess: (songs: List<Song>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            genre.getSongs(context) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    onSuccess, //NOSONAR
                    { error -> LogUtils.logException(AlbumMenuPresenter.TAG, "Failed to retrieve songs", error) } //NOSONAR
                )
        )
    }

}
