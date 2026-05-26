@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.menu // NOSONAR

import android.content.Context // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract.View // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR

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
    } // NOSONAR

    override fun addToPlaylist(playlist: Playlist, songs: List<Song>) { //NOSONAR
        playlistManager.addToPlaylist(playlist, songs) { numSongs -> //NOSONAR
            view?.onSongsAddedToPlaylist(playlist, numSongs) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun addToQueue(songs: List<Song>) { //NOSONAR
        mediaManager.addToQueue(songs) { numSongs -> //NOSONAR
            view?.onSongsAddedToQueue(numSongs) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun playNext(songs: List<Song>) { //NOSONAR
        mediaManager.playNext(songs) { numSongs -> //NOSONAR
            view?.onSongsAddedToQueue(numSongs) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun blacklist(songs: List<Song>) { //NOSONAR
        blacklistRepository.addAllSongs(songs) //NOSONAR
    } // NOSONAR

    override fun delete(songs: List<Song>) { //NOSONAR
        view?.presentDeleteDialog(songs) //NOSONAR
    } // NOSONAR

    override fun songInfo(song: Song) { //NOSONAR
        view?.presentSongInfoDialog(song) //NOSONAR
    } // NOSONAR

    override fun setRingtone(song: Song) { //NOSONAR
        if (RingtoneManager.requiresDialog(context)) { //NOSONAR
            view?.presentRingtonePermissionDialog() //NOSONAR
        } else { //NOSONAR
            ringtoneManager.setRingtone(song) { view?.showRingtoneSetMessage() } //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun share(song: Song) { //NOSONAR
        view?.shareSong(song) //NOSONAR
    } // NOSONAR

    override fun editTags(song: Song) { //NOSONAR
        view?.presentTagEditorDialog(song) //NOSONAR
    } // NOSONAR

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
            )) // NOSONAR
    } // NOSONAR

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
            )) // NOSONAR
    } // NOSONAR

    override fun goToGenre(song: Song) { //NOSONAR
        addDisposable(song.getGenre(context) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { genre -> navigationEventRelay.sendEvent(NavigationEvent(NavigationEvent.Type.GO_TO_GENRE, genre, true)) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Failed to retrieve genre", error) } //NOSONAR
            )) // NOSONAR
    } // NOSONAR

    override fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            src //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe( //NOSONAR
                    { items -> dst(items) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Failed to transform src single", error) } //NOSONAR
                ) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "SongMenuPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
