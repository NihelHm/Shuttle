@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.detail // NOSONAR

import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuPresenter // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.Operators // NOSONAR
import com.simplecity.amp_library.utils.PermissionUtils // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.squareup.inject.assisted.Assisted // NOSONAR
import com.squareup.inject.assisted.AssistedInject // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.functions.BiFunction // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import java.util.Random // NOSONAR
import java.util.concurrent.TimeUnit // NOSONAR

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
    } // NOSONAR

    private var songs: MutableList<Song> = mutableListOf() //NOSONAR

    private var currentSlideShowAlbum: Album? = null //NOSONAR

    override fun bindView(view: PlaylistDetailView) { //NOSONAR
        super.bindView(view) //NOSONAR

        playlistMenuPresenter.bindView(view) //NOSONAR
        songsMenuPresenter.bindView(view) //NOSONAR

        startSlideShow() //NOSONAR
    } // NOSONAR

    override fun unbindView(view: PlaylistDetailView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        playlistMenuPresenter.unbindView(view) //NOSONAR
        songsMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    private fun sortSongs(songs: MutableList<Song>) { //NOSONAR
        @SortManager.SongSort val songSort = sortManager.getPlaylistDetailSongsSortOrder(playlist) //NOSONAR

        val songsAscending = sortManager.getPlaylistDetailSongsAscending(playlist) //NOSONAR

        sortManager.sortSongs(songs, songSort) //NOSONAR
        if (!songsAscending) { //NOSONAR
            songs.reverse() //NOSONAR
        } // NOSONAR

        if (songSort == SortManager.SongSort.DETAIL_DEFAULT) { //NOSONAR
            when { //NOSONAR
                playlist.type == Playlist.Type.MOST_PLAYED -> songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareInt(b.playCount, a.playCount) }) //NOSONAR
                playlist.type == Playlist.Type.RECENTLY_ADDED -> songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareInt(b.dateAdded, a.dateAdded) }) //NOSONAR
                playlist.type == Playlist.Type.RECENTLY_PLAYED -> songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareLong(b.lastPlayed, a.lastPlayed) }) //NOSONAR
            } // NOSONAR
            if (playlist.canEdit) { //NOSONAR
                songs.sortWith(kotlin.Comparator { a, b -> ComparisonUtils.compareLong(a.playlistSongPlayOrder, b.playlistSongPlayOrder) }) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

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
                    }) // NOSONAR
            ) // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun startSlideShow() { //NOSONAR
        val albumsObservable: Observable<List<Album>> = songsRepository.getSongs(playlist) //NOSONAR
            .map { songs -> Operators.songsToAlbums(songs) } //NOSONAR

        val timer: Observable<Long> = io.reactivex.Observable.interval(8, TimeUnit.SECONDS) //NOSONAR
            // Load an image straight away // NOSONAR
            .startWith(0L) //NOSONAR
            // If we have a 'current slideshowAlbum' then we're coming back from onResume. Don't load a new one immediately. // NOSONAR
            .delay(if (currentSlideShowAlbum == null) 0L else 8L, TimeUnit.SECONDS) //NOSONAR

        addDisposable(Observable //NOSONAR
            .combineLatest(albumsObservable, timer, BiFunction { albums: List<Album>, _: Long -> albums }) //NOSONAR
            .map { albums -> //NOSONAR
                if (albums.isEmpty()) { //NOSONAR
                    currentSlideShowAlbum //NOSONAR
                } else { //NOSONAR
                    albums[(Random().nextInt(albums.size))] //NOSONAR
                } // NOSONAR
            } // NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe({ newAlbum -> //NOSONAR
                newAlbum?.let { //NOSONAR
                    view?.fadeInSlideShowAlbum(currentSlideShowAlbum, newAlbum) //NOSONAR
                    currentSlideShowAlbum = newAlbum //NOSONAR
                } // NOSONAR
            }, { error -> //NOSONAR
                LogUtils.logException(TAG, "startSlideShow threw error", error) //NOSONAR
            }) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    fun closeContextualToolbar() { //NOSONAR
        view?.closeContextualToolbar() //NOSONAR
    } // NOSONAR

    fun shuffleAll() { //NOSONAR
        mediaManager.shuffleAll(songs) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun play(song: Song) { //NOSONAR
        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "PlaylistDetailPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
