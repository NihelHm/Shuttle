@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.detail

import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.extensions.getSongsSingle
import com.simplecity.amp_library.utils.sorting.SortManager
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AlbumDetailPresenter @AssistedInject constructor( //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val albumsMenuPresenter: AlbumMenuPresenter, //NOSONAR
    private val songsMenuPresenter: SongMenuPresenter, //NOSONAR
    @Assisted private val album: Album //NOSONAR
) : Presenter<AlbumDetailView>(), //NOSONAR
    AlbumMenuContract.Presenter by albumsMenuPresenter, //NOSONAR
    SongMenuContract.Presenter by songsMenuPresenter { //NOSONAR

    @AssistedInject.Factory //NOSONAR
    interface Factory { //NOSONAR
        fun create(album: Album): AlbumDetailPresenter //NOSONAR
    }

    private var songs: MutableList<Song> = mutableListOf() //NOSONAR

    override fun bindView(view: AlbumDetailView) { //NOSONAR
        super.bindView(view) //NOSONAR

        songsMenuPresenter.bindView(view) //NOSONAR
        albumsMenuPresenter.bindView(view) //NOSONAR
    }

    override fun unbindView(view: AlbumDetailView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        songsMenuPresenter.unbindView(view) //NOSONAR
        albumsMenuPresenter.unbindView(view) //NOSONAR
    }

    fun loadData() { //NOSONAR
        addDisposable( //NOSONAR
            album.getSongsSingle(songsRepository) //NOSONAR
                .map { it.toMutableList() } //NOSONAR
                .doOnSuccess { sortSongs(it) } //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe { songs -> //NOSONAR
                    this.songs = songs //NOSONAR
                    view?.setData(songs) //NOSONAR
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

    fun play(song: Song) { //NOSONAR
        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.onPlaybackFailed() //NOSONAR
        }
    }

    private fun sortSongs(songs: MutableList<Song>) { //NOSONAR
        @SortManager.SongSort val songSort = sortManager.albumDetailSongsSortOrder //NOSONAR

        val songsAscending = sortManager.albumDetailSongsAscending //NOSONAR

        sortManager.sortSongs(songs, songSort) //NOSONAR
        if (!songsAscending) { //NOSONAR
            songs.reverse() //NOSONAR
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
