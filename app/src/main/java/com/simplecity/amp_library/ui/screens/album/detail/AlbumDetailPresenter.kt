@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.detail // NOSONAR

import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.extensions.getSongsSingle // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.squareup.inject.assisted.Assisted // NOSONAR
import com.squareup.inject.assisted.AssistedInject // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR

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
    } // NOSONAR

    private var songs: MutableList<Song> = mutableListOf() //NOSONAR

    override fun bindView(view: AlbumDetailView) { //NOSONAR
        super.bindView(view) //NOSONAR

        songsMenuPresenter.bindView(view) //NOSONAR
        albumsMenuPresenter.bindView(view) //NOSONAR
    } // NOSONAR

    override fun unbindView(view: AlbumDetailView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        songsMenuPresenter.unbindView(view) //NOSONAR
        albumsMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    fun loadData() { //NOSONAR
        addDisposable( //NOSONAR
            album.getSongsSingle(songsRepository) //NOSONAR
                .map { it.toMutableList() } //NOSONAR
                .doOnSuccess { sortSongs(it) } //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe { songs -> //NOSONAR
                    this.songs = songs //NOSONAR
                    view?.setData(songs) //NOSONAR
                } // NOSONAR
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

    private fun sortSongs(songs: MutableList<Song>) { //NOSONAR
        @SortManager.SongSort val songSort = sortManager.albumDetailSongsSortOrder //NOSONAR

        val songsAscending = sortManager.albumDetailSongsAscending //NOSONAR

        sortManager.sortSongs(songs, songSort) //NOSONAR
        if (!songsAscending) { //NOSONAR
            songs.reverse() //NOSONAR
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
} // NOSONAR
