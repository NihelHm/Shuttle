@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.detail

import android.support.v4.util.Pair
import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuPresenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.sorting.SortManager
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ArtistDetailPresenter @AssistedInject constructor( //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val artistsMenuPresenter: AlbumArtistMenuPresenter, //NOSONAR
    private val albumsMenuPresenter: AlbumMenuPresenter, //NOSONAR
    private val songsMenuPresenter: SongMenuPresenter, //NOSONAR
    @Assisted private val albumArtist: AlbumArtist //NOSONAR
) : Presenter<ArtistDetailView>(), //NOSONAR
    AlbumArtistMenuContract.Presenter by artistsMenuPresenter, //NOSONAR
    AlbumMenuContract.Presenter by albumsMenuPresenter, //NOSONAR
    SongMenuContract.Presenter by songsMenuPresenter { //NOSONAR

    @AssistedInject.Factory //NOSONAR
    interface Factory { //NOSONAR
        fun create(albumArtist: AlbumArtist): ArtistDetailPresenter //NOSONAR
    }

    private var songs: MutableList<Song> = mutableListOf() //NOSONAR

    override fun bindView(view: ArtistDetailView) { //NOSONAR
        super.bindView(view) //NOSONAR

        artistsMenuPresenter.bindView(view) //NOSONAR
        albumsMenuPresenter.bindView(view) //NOSONAR
        songsMenuPresenter.bindView(view) //NOSONAR
    }

    override fun unbindView(view: ArtistDetailView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        artistsMenuPresenter.unbindView(view) //NOSONAR
        albumsMenuPresenter.unbindView(view) //NOSONAR
        songsMenuPresenter.unbindView(view) //NOSONAR
    }

    fun loadData() { //NOSONAR
        addDisposable( //NOSONAR
            albumArtist.getSongsSingle(songsRepository) //NOSONAR
                .zipWith<MutableList<Album>, Pair<MutableList<Album>, MutableList<Song>>>( //NOSONAR
                    albumArtist //NOSONAR
                        .getSongsSingle(songsRepository) //NOSONAR
                        .map { songs -> Operators.songsToAlbums(songs) }, //NOSONAR
                    BiFunction { songs, albums -> Pair(albums, songs) }) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .doOnSuccess { pair -> //NOSONAR
                    sortAlbums(pair.first!!) //NOSONAR
                    sortSongs(pair.second!!) //NOSONAR
                }
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe { pair -> //NOSONAR
                    this.songs = pair.second!! //NOSONAR
                    view?.setData(pair.first!!, pair.second!!) //NOSONAR
                }
        )
    }

    fun closeContextualToolbar() { //NOSONAR
        view?.closeContextualToolbar() //NOSONAR
    }

    fun shuffleAll() { //NOSONAR
        mediaManager.shuffleAll(songs) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        }
    }

    fun songClicked(song: Song) { //NOSONAR
        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        }
    }

    private fun sortSongs(songs: MutableList<Song>) { //NOSONAR
        @SortManager.SongSort val songSort = sortManager.artistDetailSongsSortOrder //NOSONAR

        val songsAscending = sortManager.artistDetailSongsAscending //NOSONAR

        sortManager.sortSongs(songs, songSort) //NOSONAR
        if (!songsAscending) { //NOSONAR
            songs.reverse() //NOSONAR
        }
    }

    private fun sortAlbums(albums: MutableList<Album>) { //NOSONAR
        @SortManager.AlbumSort val albumSort = sortManager.artistDetailAlbumsSortOrder //NOSONAR

        val albumsAscending = sortManager.artistDetailAlbumsAscending //NOSONAR

        sortManager.sortAlbums(albums, albumSort) //NOSONAR
        if (!albumsAscending) { //NOSONAR
            albums.reverse() //NOSONAR
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
}
