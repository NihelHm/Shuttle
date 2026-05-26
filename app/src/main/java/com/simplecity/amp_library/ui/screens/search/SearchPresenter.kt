@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.search

import android.text.TextUtils
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.modelviews.AlbumArtistView
import com.simplecity.amp_library.ui.modelviews.AlbumView
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuPresenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.StringUtils
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOperator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class SearchPresenter @Inject //NOSONAR
constructor( //NOSONAR
        private val mediaManager: MediaManager, //NOSONAR
        private val songsRepository: Repository.SongsRepository, //NOSONAR
        private val albumsRepository: Repository.AlbumsRepository, //NOSONAR
        private val albumArtistsRepository: Repository.AlbumArtistsRepository, //NOSONAR
        private val settingsManager: SettingsManager, //NOSONAR
        private val songMenuPresenter: SongMenuPresenter, //NOSONAR
        private val albumMenuPresenter: AlbumMenuPresenter, //NOSONAR
        private val albumArtistsMenuPresenter: AlbumArtistMenuPresenter //NOSONAR

) : Presenter<SearchView>(), //NOSONAR
        SongMenuContract.Presenter by songMenuPresenter, //NOSONAR
        AlbumMenuContract.Presenter by albumMenuPresenter, //NOSONAR
        AlbumArtistMenuContract.Presenter by albumArtistsMenuPresenter { //NOSONAR

    private var performSearchSubscription: Disposable? = null //NOSONAR

    private var query: String? = null //NOSONAR

    override fun bindView(view: SearchView) { //NOSONAR
        super.bindView(view) //NOSONAR

        songMenuPresenter.bindView(view) //NOSONAR
        albumMenuPresenter.bindView(view) //NOSONAR
        albumArtistsMenuPresenter.bindView(view) //NOSONAR

        view.setFilterFuzzyChecked(settingsManager.searchFuzzy) //NOSONAR
        view.setFilterArtistsChecked(settingsManager.searchArtists) //NOSONAR
        view.setFilterAlbumsChecked(settingsManager.searchAlbums) //NOSONAR
    }

    override fun unbindView(view: SearchView) { //NOSONAR
        super.unbindView(view) //NOSONAR
        songMenuPresenter.unbindView(view) //NOSONAR
        albumMenuPresenter.unbindView(view) //NOSONAR
        albumArtistsMenuPresenter.unbindView(view) //NOSONAR
    }

    fun queryChanged(query: String?) { //NOSONAR
        var query = query //NOSONAR

        if (TextUtils.isEmpty(query)) { //NOSONAR
            query = "" //NOSONAR
        }

        if (query == this.query) { //NOSONAR
            return //NOSONAR
        }

        loadData(query!!) //NOSONAR

        this.query = query //NOSONAR
    }

    private fun loadData(query: String) { //NOSONAR

        val searchView = view //NOSONAR

        if (searchView != null) { //NOSONAR

            searchView.setLoading(true) //NOSONAR

            //We've received a new refresh call. Unsubscribe the in-flight subscription if it exists.
            if (performSearchSubscription != null) { //NOSONAR
                performSearchSubscription!!.dispose() //NOSONAR
            }

            val albumArtistsObservable = if (settingsManager.searchArtists) //NOSONAR
                albumArtistsRepository.getAlbumArtists() //NOSONAR
                        .first(emptyList()) //NOSONAR
                        .lift(AlbumArtistFilterOperator(query)) //NOSONAR
            else //NOSONAR
                Single.just(emptyList()) //NOSONAR

            val albumsObservable = if (settingsManager.searchAlbums) //NOSONAR
                albumsRepository.getAlbums() //NOSONAR
                        .first(emptyList()) //NOSONAR
                        .lift(AlbumFilterOperator(query)) //NOSONAR
            else //NOSONAR
                Single.just(emptyList()) //NOSONAR

            val songsObservable = songsRepository.getSongs(null as Function1<Song, Boolean>?) //NOSONAR
                    .first(emptyList()) //NOSONAR
                    .lift(SongFilterOperator(query)) //NOSONAR

            performSearchSubscription = Single.zip<List<AlbumArtist>, List<Album>, List<Song>, SearchResult>(albumArtistsObservable, albumsObservable, songsObservable, Function3 { albumArtists: List<AlbumArtist>, albums: List<Album>, songs: List<Song> -> SearchResult(albumArtists, albums, songs) }) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            { searchView.setData(it) }, //NOSONAR
                            { error -> LogUtils.logException(TAG, "Error refreshing adapter", error) } //NOSONAR
                    )

            addDisposable(performSearchSubscription!!) //NOSONAR
        }
    }

    fun setSearchFuzzy(searchFuzzy: Boolean) { //NOSONAR
        settingsManager.searchFuzzy = searchFuzzy //NOSONAR
        loadData(query!!) //NOSONAR
    }

    fun setSearchArtists(searchArtists: Boolean) { //NOSONAR
        settingsManager.searchArtists = searchArtists //NOSONAR
        loadData(query!!) //NOSONAR
    }

    fun setSearchAlbums(searchAlbums: Boolean) { //NOSONAR
        settingsManager.searchAlbums = searchAlbums //NOSONAR
        loadData(query!!) //NOSONAR
    }

    fun onSongClick(songs: List<Song>, song: Song) { //NOSONAR
        val view = view //NOSONAR

        mediaManager.playAll(songs, songs.indexOf(song), true) { //NOSONAR
            view?.showPlaybackError() //NOSONAR
            Unit //NOSONAR
        }
    }

    fun onArtistClicked(albumArtistView: AlbumArtistView, viewholder: AlbumArtistView.ViewHolder) { //NOSONAR
        val view = view //NOSONAR
        view?.goToArtist(albumArtistView.albumArtist, viewholder.imageOne) //NOSONAR
    }

    fun onAlbumClick(albumView: AlbumView, viewHolder: AlbumView.ViewHolder) { //NOSONAR
        val view = view //NOSONAR
        view?.goToAlbum(albumView.album, viewHolder.imageOne) //NOSONAR
    }

    private inner class SongFilterOperator internal constructor(internal var filterString: String) : SingleOperator<List<Song>, List<Song>> { //NOSONAR

        override fun apply(observer: SingleObserver<in List<Song>>): SingleObserver<in List<Song>> { //NOSONAR
            return object : SingleObserver<List<Song>> { //NOSONAR
                override fun onSubscribe(d: Disposable) { //NOSONAR
                    observer.onSubscribe(d) //NOSONAR
                }

                override fun onSuccess(songs: List<Song>) { //NOSONAR
                    var songs = songs //NOSONAR
                    val songList = songs.filter { song -> song.name != null } //NOSONAR
                    songs = (if (settingsManager.searchFuzzy) applyJaroWinklerFilter(songList) else applySongFilter(songList)).toList() //NOSONAR
                    observer.onSuccess(songs) //NOSONAR
                }

                override fun onError(e: Throwable) { //NOSONAR
                    observer.onError(e) //NOSONAR
                }
            }
        }

        internal fun applyJaroWinklerFilter(songList: List<Song>): List<Song> { //NOSONAR
            return songList.map { song -> JaroWinklerObject(song, filterString, song.name) } //NOSONAR
                    .filter { jaroWinklerObject -> jaroWinklerObject.score > SCORE_THRESHOLD || TextUtils.isEmpty(filterString) } //NOSONAR
                    .sortedWith(Comparator { a, b -> a.`object`.compareTo(b.`object`) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> java.lang.Double.compare(b.score, a.score) }) //NOSONAR
                    .map { jaroWinklerObject -> jaroWinklerObject.`object` } //NOSONAR
        }

        internal fun applySongFilter(songStream: List<Song>): List<Song> { //NOSONAR
            return songStream.filter { song -> StringUtils.containsIgnoreCase(song.name, filterString) } //NOSONAR
        }
    }

    private inner class AlbumFilterOperator internal constructor(internal var filterString: String) : SingleOperator<List<Album>, List<Album>> { //NOSONAR

        override fun apply(observer: SingleObserver<in List<Album>>): SingleObserver<in List<Album>> { //NOSONAR
            return object : SingleObserver<List<Album>> { //NOSONAR
                override fun onSubscribe(d: Disposable) { //NOSONAR
                    observer.onSubscribe(d) //NOSONAR
                }

                override fun onSuccess(albums: List<Album>) { //NOSONAR
                    albums.sortedWith(Comparator { a, b -> a.compareTo(b) }) //NOSONAR
                    val albumStream = albums.filter { album -> album.name != null } //NOSONAR
                    val filteredStream = if (settingsManager.searchFuzzy) applyJaroWinklerAlbumFilter(albumStream) else applyAlbumFilter(albumStream) //NOSONAR
                    observer.onSuccess(filteredStream.toList()) //NOSONAR
                }

                override fun onError(e: Throwable) { //NOSONAR
                    observer.onError(e) //NOSONAR
                }
            }
        }

        internal fun applyJaroWinklerAlbumFilter(albums: List<Album>): List<Album> { //NOSONAR
            return albums.map { album -> JaroWinklerObject(album, filterString, album.name) } //NOSONAR
                    .filter { jaroWinklerObject -> jaroWinklerObject.score > SCORE_THRESHOLD || TextUtils.isEmpty(filterString) } //NOSONAR
                    .sortedWith(Comparator { a, b -> a.`object`.compareTo(b.`object`) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> java.lang.Double.compare(b.score, a.score) }) //NOSONAR
                    .map { jaroWinklerObject -> jaroWinklerObject.`object` } //NOSONAR
        }

        internal fun applyAlbumFilter(stream: List<Album>): List<Album> { //NOSONAR
            return stream.filter { album -> StringUtils.containsIgnoreCase(album.name, filterString) } //NOSONAR
        }
    }

    private inner class AlbumArtistFilterOperator internal constructor(internal var filterString: String) : SingleOperator<List<AlbumArtist>, List<AlbumArtist>> { //NOSONAR

        override fun apply(observer: SingleObserver<in List<AlbumArtist>>): SingleObserver<in List<AlbumArtist>> { //NOSONAR
            return object : SingleObserver<List<AlbumArtist>> { //NOSONAR
                override fun onSubscribe(d: Disposable) { //NOSONAR
                    observer.onSubscribe(d) //NOSONAR
                }

                override fun onSuccess(albumArtists: List<AlbumArtist>) { //NOSONAR
                    Collections.sort(albumArtists) { obj, albumArtist -> obj.compareTo(albumArtist) } //NOSONAR
                    val albumArtistList = albumArtists.filter { albumArtist -> albumArtist.name != null } //NOSONAR
                    val filteredList = if (settingsManager.searchFuzzy) applyJaroWinklerAlbumArtistFilter(albumArtistList) else applyAlbumArtistFilter(albumArtistList) //NOSONAR
                    observer.onSuccess(filteredList.toList()) //NOSONAR
                }

                override fun onError(e: Throwable) { //NOSONAR
                    observer.onError(e) //NOSONAR
                }
            }
        }

        internal fun applyJaroWinklerAlbumArtistFilter(stream: List<AlbumArtist>): List<AlbumArtist> { //NOSONAR
            return stream.map { albumArtist -> JaroWinklerObject(albumArtist, filterString, albumArtist.name) } //NOSONAR
                    .filter { jaroWinklerObject -> jaroWinklerObject.score > SCORE_THRESHOLD || TextUtils.isEmpty(filterString) } //NOSONAR
                    .sortedWith(Comparator { a, b -> a.`object`.compareTo(b.`object`) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> java.lang.Double.compare(b.score, a.score) }) //NOSONAR
                    .map { jaroWinklerObject -> jaroWinklerObject.`object` } //NOSONAR
        }

        internal fun applyAlbumArtistFilter(stream: List<AlbumArtist>): List<AlbumArtist> { //NOSONAR
            return stream.filter { albumArtist -> StringUtils.containsIgnoreCase(albumArtist.name, filterString) } //NOSONAR
        }
    }

    override fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) { //NOSONAR
        addDisposable( //NOSONAR
                src //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .subscribe( //NOSONAR
                                { items -> dst(items) }, //NOSONAR
                                { error -> LogUtils.logException(SearchPresenter.TAG, "Failed to transform src single", error) } //NOSONAR
                        )
        )
    }

    companion object { //NOSONAR

        private const val TAG = "SearchPresenter" //NOSONAR

        private const val SCORE_THRESHOLD = 0.80 //NOSONAR
    }
}
