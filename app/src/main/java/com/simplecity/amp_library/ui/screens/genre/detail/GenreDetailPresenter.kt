@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.detail

import android.content.Context
import android.support.v4.util.Pair
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuContract
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuPresenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.PermissionUtils
import com.simplecity.amp_library.utils.extensions.getSongsObservable
import com.simplecity.amp_library.utils.sorting.SortManager
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit

class GenreDetailPresenter @AssistedInject constructor( //NOSONAR
    private val context: Context, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val genreMenuPresenter: GenreMenuPresenter, //NOSONAR
    private val albumMenuPresenter: AlbumMenuPresenter, //NOSONAR
    private val songsMenuPresenter: SongMenuPresenter, //NOSONAR
    @Assisted private val genre: Genre //NOSONAR

) : Presenter<GenreDetailView>(), //NOSONAR
    GenreMenuContract.Presenter by genreMenuPresenter, //NOSONAR
    AlbumMenuContract.Presenter by albumMenuPresenter, //NOSONAR
    SongMenuContract.Presenter by songsMenuPresenter { //NOSONAR

    @AssistedInject.Factory //NOSONAR
    interface Factory { //NOSONAR
        fun create(genre: Genre): GenreDetailPresenter //NOSONAR
    }

    private var songs: MutableList<Song> = mutableListOf() //NOSONAR

    private var currentSlideShowAlbum: Album? = null //NOSONAR

    override fun bindView(view: GenreDetailView) { //NOSONAR
        super.bindView(view) //NOSONAR

        genreMenuPresenter.bindView(view) //NOSONAR
        albumMenuPresenter.bindView(view) //NOSONAR
        songsMenuPresenter.bindView(view) //NOSONAR

        startSlideShow() //NOSONAR
    }

    override fun unbindView(view: GenreDetailView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        genreMenuPresenter.unbindView(view) //NOSONAR
        albumMenuPresenter.unbindView(view) //NOSONAR
        songsMenuPresenter.unbindView(view) //NOSONAR
    }

    private fun sortSongs(songs: MutableList<Song>) { //NOSONAR
        @SortManager.SongSort val songSort = sortManager.genreDetailSongsSortOrder //NOSONAR

        val songsAscending = sortManager.genreDetailSongsAscending //NOSONAR

        sortManager.sortSongs(songs, songSort) //NOSONAR
        if (!songsAscending) { //NOSONAR
            songs.reverse() //NOSONAR
        }
    }

    private fun sortAlbums(albums: MutableList<Album>) { //NOSONAR
        @SortManager.AlbumSort val albumSort = sortManager.genreDetailAlbumsSortOrder //NOSONAR

        val albumsAscending = sortManager.genreDetailAlbumsAscending //NOSONAR

        sortManager.sortAlbums(albums, albumSort) //NOSONAR
        if (!albumsAscending) { //NOSONAR
            albums.reverse() //NOSONAR
        }
    }

    fun loadData() { //NOSONAR
        PermissionUtils.RequestStoragePermissions { //NOSONAR
            addDisposable( //NOSONAR
                genre.getSongsObservable(context) //NOSONAR
                    .zipWith<MutableList<Album>, Pair<MutableList<Album>, MutableList<Song>>>( //NOSONAR
                        genre.getSongsObservable(context).map { songs -> Operators.songsToAlbums(songs) }, //NOSONAR
                        BiFunction { songs, albums -> Pair(albums, songs.toMutableList()) }).subscribeOn(Schedulers.io()) //NOSONAR
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
    }

    private fun startSlideShow() { //NOSONAR
        val albumsObservable: Observable<List<Album>> = genre.getSongsObservable(context).toObservable() //NOSONAR
            .map { songs -> Operators.songsToAlbums(songs) } //NOSONAR

        val timer: Observable<Long> = io.reactivex.Observable.interval(8, TimeUnit.SECONDS) //NOSONAR
            // Load an image straight away
            .startWith(0L) //NOSONAR
            // If we have a 'current slideshowAlbum' then we're coming back from onResume. Don't load a new one immediately.
            .delay(if (currentSlideShowAlbum == null) 0L else 8L, TimeUnit.SECONDS) //NOSONAR

        addDisposable(Observable //NOSONAR
            .combineLatest(albumsObservable, timer, BiFunction { albums: List<Album>, aLong: Long -> albums }) //NOSONAR
            .map { albums -> //NOSONAR
                if (albums.isEmpty()) { //NOSONAR
                    currentSlideShowAlbum //NOSONAR
                } else { //NOSONAR
                    albums[(Random().nextInt(albums.size))] //NOSONAR
                }
            }
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe({ newAlbum -> //NOSONAR
                newAlbum?.let { //NOSONAR
                    view?.fadeInSlideShowAlbum(currentSlideShowAlbum, newAlbum) //NOSONAR
                    currentSlideShowAlbum = newAlbum //NOSONAR
                }
            }, { error -> //NOSONAR
                LogUtils.logException(TAG, "startSlideShow threw error", error) //NOSONAR
            })
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
        const val TAG = "GenreDetailPresenter" //NOSONAR
    }
}
