@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.menu // NOSONAR

import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract.View // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent.Type // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.extensions.getSongs // NOSONAR
import com.simplecity.amp_library.utils.extensions.getSongsSingle // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR

class AlbumMenuPresenter @Inject constructor( //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val songsRepository: Repository.SongsRepository, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val blacklistRepository: Repository.BlacklistRepository, //NOSONAR
    private val albumArtistsRepository: Repository.AlbumArtistsRepository, //NOSONAR
    private val navigationEventRelay: NavigationEventRelay //NOSONAR
) : Presenter<View>(), AlbumMenuContract.Presenter { //NOSONAR

    override fun createPlaylistFromAlbums(albums: List<Album>) { //NOSONAR
        getSongs(albums) { songs -> //NOSONAR
            view?.presentCreatePlaylistDialog(songs) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun addAlbumsToPlaylist(playlist: Playlist, albums: List<Album>) { //NOSONAR
        getSongs(albums) { songs -> //NOSONAR
            playlistManager.addToPlaylist(playlist, songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToPlaylist(playlist, numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun addAlbumsToQueue(albums: List<Album>) { //NOSONAR
        getSongs(albums) { songs -> //NOSONAR
            mediaManager.addToQueue(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun playAlbumsNext(albums: List<Album>) { //NOSONAR
        getSongs(albums) { songs -> //NOSONAR
            mediaManager.playNext(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun play(album: Album) { //NOSONAR
        mediaManager.playAll(album.getSongsSingle(songsRepository)) { view?.onPlaybackFailed() } //NOSONAR
    } // NOSONAR

    override fun editTags(album: Album) { //NOSONAR
        view?.presentTagEditorDialog(album) //NOSONAR
    } // NOSONAR

    override fun albumInfo(album: Album) { //NOSONAR
        view?.presentAlbumInfoDialog(album) //NOSONAR
    } // NOSONAR

    override fun editArtwork(album: Album) { //NOSONAR
        view?.presentArtworkEditorDialog(album) //NOSONAR
    } // NOSONAR

    override fun blacklistAlbums(albums: List<Album>) { //NOSONAR
        getSongs(albums) { songs -> blacklistRepository.addAllSongs(songs) } //NOSONAR
    } // NOSONAR

    override fun deleteAlbums(albums: List<Album>) { //NOSONAR
        view?.presentDeleteAlbumsDialog(albums) //NOSONAR
    } // NOSONAR

    override fun goToArtist(album: Album) { //NOSONAR
        addDisposable(albumArtistsRepository.getAlbumArtists() //NOSONAR
            .first(emptyList()) //NOSONAR
            .flatMapObservable { Observable.fromIterable(it) } //NOSONAR
            .filter { albumArtist -> albumArtist.name == album.albumArtist.name && albumArtist.albums.contains(album) } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { albumArtist -> navigationEventRelay.sendEvent(NavigationEvent(Type.GO_TO_ARTIST, albumArtist, true)) }, //NOSONAR
                { error -> LogUtils.logException(com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter.TAG, "Failed to retrieve album artist", error) } //NOSONAR
            )) // NOSONAR
    } // NOSONAR

    override fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            src //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe( //NOSONAR
                    { items -> dst(items) }, //NOSONAR
                    { error -> LogUtils.logException(SongMenuPresenter.TAG, "Failed to transform src single", error) } //NOSONAR
                ) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    private fun getSongs(albums: List<Album>, onSuccess: (songs: List<Song>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            albums.getSongs(songsRepository) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    onSuccess, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Failed to retrieve songs", error) } //NOSONAR
                ) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "AlbumMenuContract" //NOSONAR
    } // NOSONAR

} // NOSONAR
