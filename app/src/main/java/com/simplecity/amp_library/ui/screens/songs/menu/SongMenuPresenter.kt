@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.menu

import android.content.Context
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract.View
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.RingtoneManager
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class SongMenuPresenter @Inject constructor( //NOSONAR
    private val context: Context, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val blacklistRepository: Repository.BlacklistRepository, //NOSONAR
    private val ringtoneManager: RingtoneManager, //NOSONAR
    private val albumArtistsRepository: AlbumArtistsRepository, //NOSONAR
    private val albumsRepository: Repository.AlbumsRepository, //NOSONAR
    private val navigationEventRelay: NavigationEventRelay //NOSONAR
) : Presenter<View>(), SongMenuContract.Presenter { //NOSONAR

    override fun createPlaylist(songs: List<Song>) { //NOSONAR
        view?.presentCreatePlaylistDialog(songs) //NOSONAR
    }

    override fun addToPlaylist(playlist: Playlist, songs: List<Song>) { //NOSONAR
        playlistManager.addToPlaylist(playlist, songs) { numSongs -> //NOSONAR
            view?.onSongsAddedToPlaylist(playlist, numSongs) //NOSONAR
        }
    }

    override fun addToQueue(songs: List<Song>) { //NOSONAR
        mediaManager.addToQueue(songs) { numSongs -> //NOSONAR
            view?.onSongsAddedToQueue(numSongs) //NOSONAR
        }
    }

    override fun playNext(songs: List<Song>) { //NOSONAR
        mediaManager.playNext(songs) { numSongs -> //NOSONAR
            view?.onSongsAddedToQueue(numSongs) //NOSONAR
        }
    }

    override fun blacklist(songs: List<Song>) { //NOSONAR
        blacklistRepository.addAllSongs(songs) //NOSONAR
    }

    override fun delete(songs: List<Song>) { //NOSONAR
        view?.presentDeleteDialog(songs) //NOSONAR
    }

    override fun songInfo(song: Song) { //NOSONAR
        view?.presentSongInfoDialog(song) //NOSONAR
    }

    override fun setRingtone(song: Song) { //NOSONAR
        if (RingtoneManager.requiresDialog(context)) { //NOSONAR
            view?.presentRingtonePermissionDialog() //NOSONAR
        } else { //NOSONAR
            ringtoneManager.setRingtone(song) { view?.showRingtoneSetMessage() } //NOSONAR
        }
    }

    override fun share(song: Song) { //NOSONAR
        view?.shareSong(song) //NOSONAR
    }

    override fun editTags(song: Song) { //NOSONAR
        view?.presentTagEditorDialog(song) //NOSONAR
    }

    override fun goToArtist(song: Song) { //NOSONAR
        addDisposable(albumArtistsRepository.getAlbumArtists() //NOSONAR
            .first(emptyList()) //NOSONAR
            .flatMapObservable { Observable.fromIterable(it) } //NOSONAR
            .filter { albumArtist -> albumArtist.name == song.albumArtist.name && albumArtist.albums.containsAll(song.albumArtist.albums) } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { albumArtist -> navigationEventRelay.sendEvent(NavigationEvent(NavigationEvent.Type.GO_TO_ARTIST, albumArtist, true)) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Failed to retrieve album artist", error) } //NOSONAR
            ))
    }

    override fun goToAlbum(song: Song) { //NOSONAR
        addDisposable(albumsRepository.getAlbums() //NOSONAR
            .first(emptyList()) //NOSONAR
            .flatMapObservable { Observable.fromIterable(it) } //NOSONAR
            .filter { album -> album.id == song.albumId } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { album -> navigationEventRelay.sendEvent(NavigationEvent(NavigationEvent.Type.GO_TO_ALBUM, album, true)) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Failed to retrieve album", error) } //NOSONAR
            ))
    }

    override fun goToGenre(song: Song) { //NOSONAR
        addDisposable(song.getGenre(context) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { genre -> navigationEventRelay.sendEvent(NavigationEvent(NavigationEvent.Type.GO_TO_GENRE, genre, true)) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Failed to retrieve genre", error) } //NOSONAR
            ))
    }

    override fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            src //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe( //NOSONAR
                    { items -> dst(items) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Failed to transform src single", error) } //NOSONAR
                )
        )
    }

    companion object { //NOSONAR
        const val TAG = "SongMenuPresenter" //NOSONAR
    }
}
