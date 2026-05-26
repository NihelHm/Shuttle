@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.menu // NOSONAR

import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract.View // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay.NavigationEvent.Type // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.Operators // NOSONAR
import com.simplecity.amp_library.utils.extensions.getSongs // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR

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
        } // NOSONAR
    } // NOSONAR

    override fun addArtistsToPlaylist(playlist: Playlist, albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            playlistManager.addToPlaylist(playlist, songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToPlaylist(playlist, numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun addArtistsToQueue(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            mediaManager.addToQueue(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun playArtistsNext(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> //NOSONAR
            mediaManager.playNext(songs) { numSongs -> //NOSONAR
                view?.onSongsAddedToQueue(numSongs) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun play(albumArtist: AlbumArtist) { //NOSONAR
        mediaManager.playAll(albumArtist.getSongsSingle(songsRepository)) { view?.onPlaybackFailed() } //NOSONAR
    } // NOSONAR

    override fun editTags(albumArtist: AlbumArtist) { //NOSONAR
        view?.presentTagEditorDialog(albumArtist) //NOSONAR
    } // NOSONAR

    override fun albumArtistInfo(albumArtist: AlbumArtist) { //NOSONAR
        view?.presentAlbumArtistInfoDialog(albumArtist) //NOSONAR
    } // NOSONAR

    override fun editArtwork(albumArtist: AlbumArtist) { //NOSONAR
        view?.presentArtworkEditorDialog(albumArtist) //NOSONAR
    } // NOSONAR

    override fun blacklistArtists(albumArtists: List<AlbumArtist>) { //NOSONAR
        getSongs(albumArtists) { songs -> blacklistRepository.addAllSongs(songs) } //NOSONAR
    } // NOSONAR

    override fun deleteArtists(albumArtists: List<AlbumArtist>) { //NOSONAR
        view?.presentArtistDeleteDialog(albumArtists) //NOSONAR
    } // NOSONAR

    override fun goToArtist(albumArtist: AlbumArtist) { //NOSONAR
        navigationEventRelay.sendEvent(NavigationEvent(Type.GO_TO_ARTIST, albumArtist, true)) //NOSONAR
    } // NOSONAR

    override fun albumShuffle(albumArtist: AlbumArtist) { //NOSONAR
        mediaManager.playAll(albumArtist.getSongs(songsRepository) //NOSONAR
            .map { songs -> Operators.albumShuffleSongs(songs, sortManager) }) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
            Unit //NOSONAR
        } // NOSONAR
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

    private fun getSongs(albumArtists: List<AlbumArtist>, onSuccess: (songs: List<Song>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
            albumArtists.getSongs(songsRepository) //NOSONAR
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
