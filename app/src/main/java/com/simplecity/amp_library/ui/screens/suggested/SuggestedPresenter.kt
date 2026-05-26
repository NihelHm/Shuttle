@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.suggested

import com.simplecity.amp_library.data.Repository.PlaylistsRepository
import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.ui.screens.suggested.SuggestedContract.View
import com.simplecity.amp_library.utils.ComparisonUtils
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.extensions.getSongsSingle
import com.simplecity.amp_library.utils.menu.album.AlbumsMenuCallbacks
import com.simplecity.amp_library.utils.menu.song.SongsMenuCallbacks
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SuggestedPresenter @Inject constructor( //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val playlistRepository: PlaylistsRepository, //NOSONAR
    private val favoritesPlaylistManager: FavoritesPlaylistManager, //NOSONAR
    private val songMenuPresenter: SongMenuPresenter, //NOSONAR
    private val albumMenuPresenter: AlbumMenuPresenter //NOSONAR
) :
    Presenter<SuggestedContract.View>(), //NOSONAR
    SuggestedContract.Presenter, //NOSONAR
    SongsMenuCallbacks by songMenuPresenter, //NOSONAR
    AlbumsMenuCallbacks by albumMenuPresenter { //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR

        songMenuPresenter.bindView(view) //NOSONAR
        albumMenuPresenter.bindView(view) //NOSONAR
    }

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR

        songMenuPresenter.unbindView(view) //NOSONAR
        albumMenuPresenter.unbindView(view) //NOSONAR
    }

    data class SuggestedData( //NOSONAR
        val mostPlayedPlaylist: Playlist, val mostPlayedSongs: List<Song>, //NOSONAR
        val recentlyPlayedPlaylist: Playlist, val recentlyPlayedAlbums: List<Album>, //NOSONAR
        val favoriteSongsPlaylist: Playlist, val favoriteSongs: List<Song>, //NOSONAR
        val recentlyAddedAlbumsPlaylist: Playlist, val recentlyAddedAlbums: List<Album> //NOSONAR
    )

    override fun loadData() { //NOSONAR

        val mostPlayedPlaylist = playlistRepository.getMostPlayedPlaylist() //NOSONAR
        val recentlyPlayedPlaylist = playlistRepository.getRecentlyPlayedPlaylist() //NOSONAR
        val recentlyAddedAlbumsPlaylist = playlistRepository.getRecentlyAddedPlaylist() //NOSONAR
        lateinit var favoriteSongsPlaylist: Playlist //NOSONAR

        val mostPlayedSongs = songsRepository.getSongs(mostPlayedPlaylist) //NOSONAR
            .take(20) //NOSONAR

        val recentlyPlayedAlbums = songsRepository.getSongs(recentlyPlayedPlaylist) //NOSONAR
            .flatMap { songs -> Observable.just(Operators.songsToAlbums(songs)) } //NOSONAR
            .flatMapSingle { albums -> //NOSONAR
                Observable.fromIterable(albums) //NOSONAR
                    .sorted { a, b -> ComparisonUtils.compareLong(b.lastPlayed, a.lastPlayed) } //NOSONAR
                    .concatMapSingle { album -> //NOSONAR
                        album.getSongsSingle(songsRepository) //NOSONAR
                            .map { songs -> //NOSONAR
                                album.numSongs = songs.size //NOSONAR
                                album //NOSONAR
                            }
                            .filter { a -> a.numSongs > 0 } //NOSONAR
                            .toSingle() //NOSONAR
                    }
                    .sorted { a, b -> ComparisonUtils.compareLong(b.lastPlayed, a.lastPlayed) } //NOSONAR
                    .take(6) //NOSONAR
                    .toList() //NOSONAR
            }

        val favoriteSongs = favoritesPlaylistManager.getFavoritesPlaylist() //NOSONAR
            .flatMapObservable { playlist -> //NOSONAR
                favoriteSongsPlaylist = playlist //NOSONAR
                songsRepository.getSongs(favoriteSongsPlaylist) //NOSONAR
                    .take(20) //NOSONAR
            }

        val recentlyAddedAlbums = songsRepository.getSongs(recentlyAddedAlbumsPlaylist) //NOSONAR
            .flatMap { songs -> Observable.just(Operators.songsToAlbums(songs)) } //NOSONAR
            .flatMapSingle { source -> //NOSONAR
                Observable.fromIterable(source) //NOSONAR
                    .sorted { a, b -> ComparisonUtils.compareLong(b.dateAdded, a.dateAdded) } //NOSONAR
                    .take(10) //NOSONAR
                    .toList() //NOSONAR
            }

        addDisposable( //NOSONAR
            Observable.combineLatest(mostPlayedSongs, recentlyPlayedAlbums, favoriteSongs, recentlyAddedAlbums, //NOSONAR
                Function4<List<Song>, List<Album>, List<Song>, List<Album>, SuggestedData> { mostPlayedSongs, recentlyPlayedAlbums, favoriteSongs, recentlyAddedAlbums -> //NOSONAR
                    SuggestedData( //NOSONAR
                        mostPlayedPlaylist, mostPlayedSongs, //NOSONAR
                        recentlyPlayedPlaylist, recentlyPlayedAlbums, //NOSONAR
                        favoriteSongsPlaylist, favoriteSongs, //NOSONAR
                        recentlyAddedAlbumsPlaylist, recentlyAddedAlbums //NOSONAR
                    )
                })
                .subscribe( //NOSONAR
                    { suggestedData -> view?.setData(suggestedData) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Failed to load data", error) } //NOSONAR
                )
        )
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

    companion object { //NOSONAR
        private const val TAG = "SuggestedPresenter" //NOSONAR
    }
}
