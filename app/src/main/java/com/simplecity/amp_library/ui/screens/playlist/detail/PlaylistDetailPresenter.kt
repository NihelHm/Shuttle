@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.detail

import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuPresenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.ComparisonUtils
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.PermissionUtils
import com.simplecity.amp_library.utils.sorting.SortManager
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit

class PlaylistDetailPresenter @AssistedInject constructor( //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val playlistMenuPresenter: PlaylistMenuPresenter, //NOSONAR
    private val songsMenuPresenter: SongMenuPresenter, //NOSONAR
    @Assisted private val playlist: Playlist //NOSONAR
) : Presenter<PlaylistDetailView>(), //NOSONAR
    PlaylistMenuContract.Presenter by playlistMenuPresenter, //NOSONAR
    SongMenuContract.Presenter by songsMenuPresenter { //NOSONAR

    @AssistedInject.Factory //NOSONAR
    interface Factory { //NOSONAR
        fun create(playlist: Playlist): PlaylistDetailPresenter //NOSONAR
    }

    private var songs: MutableList<Song> = mutableListOf() //NOSONAR

    private var currentSlideShowAlbum: Album? = null //NOSONAR

    override fun bindView(view: PlaylistDetailView) { //NOSONAR
        super.bindView(view) //NOSONAR

        playlistMenuPresenter.bindView(view) //NOSONAR
        songsMenuPresenter.bindView(view) //NOSONAR

        startSlideShow() //NOSONAR
    }

    override fun unbindView(view: PlaylistDetailView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        playlistMenuPresenter.unbindView(view) //NOSONAR
        songsMenuPresenter.unbindView(view) //NOSONAR
    }

    private fun sortSongs(songs: MutableList<Song>) { //NOSONAR
        @SortManager.SongSort val songSort = sortManager.getPlaylistDetailSongsSortOrder(playlist) //NOSONAR

        val songsAscending = sortManager.getPlaylistDetailSongsAscending(playlist) //NOSONAR

        sortManager.sortSongs(songs, songSort) //NOSONAR
        if (!songsAscending) { //NOSONAR
            songs.reverse() //NOSONAR
        }

        if (songSort == SortManager.SongSort.DETAIL_DEFAULT) { //NOSONAR
            when { //NOSONAR
                playlist.type == Playlist.Type.MOST_PLAYED -> songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareInt(b.playCount, a.playCount) }) //NOSONAR
                playlist.type == Playlist.Type.RECENTLY_ADDED -> songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareInt(b.dateAdded, a.dateAdded) }) //NOSONAR
                playlist.type == Playlist.Type.RECENTLY_PLAYED -> songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareLong(b.lastPlayed, a.lastPlayed) }) //NOSONAR
            }
            if (playlist.canEdit) { //NOSONAR
                songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareLong(a.playlistSongPlayOrder, b.playlistSongPlayOrder) }) //NOSONAR
            }
        }
    }

    fun loadData() { //NOSONAR
        PermissionUtils.RequestStoragePermissions { //NOSONAR
            addDisposable( //NOSONAR
                songsRepository.getSongs(playlist) //NOSONAR
                    .map { it.toMutableList() } //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .doOnNext { songs -> sortSongs(songs) } //NOSONAR
                    .subscribe({ songs -> //NOSONAR
                        this.songs = songs //NOSONAR
                        view?.setData(songs) //NOSONAR
                    }, { error -> //NOSONAR
                        LogUtils.logException(TAG, "loadData error", error); //NOSONAR
                    })
            )
        }
    }

    private fun startSlideShow() { //NOSONAR
        val albumsObservable: Observable<List<Album>> = songsRepository.getSongs(playlist) //NOSONAR
            .map { songs -> Operators.songsToAlbums(songs) } //NOSONAR

        val timer: Observable<Long> = io.reactivex.Observable.interval(8, TimeUnit.SECONDS) //NOSONAR
            // Load an image straight away
            .startWith(0L) //NOSONAR
            // If we have a 'current slideshowAlbum' then we're coming back from onResume. Don't load a new one immediately.
            .delay(if (currentSlideShowAlbum == null) 0L else 8L, TimeUnit.SECONDS) //NOSONAR

        addDisposable(Observable //NOSONAR
            .combineLatest(albumsObservable, timer, BiFunction { albums: List<Album>, _: Long -> albums }) //NOSONAR
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

    fun play(song: Song) { //NOSONAR
        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        }
    }

    companion object { //NOSONAR
        const val TAG = "PlaylistDetailPresenter" //NOSONAR
    }
}
