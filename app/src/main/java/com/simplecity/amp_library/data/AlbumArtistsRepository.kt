@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data // NOSONAR

import com.jakewharton.rxrelay2.BehaviorRelay // NOSONAR
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository // NOSONAR
import com.simplecity.amp_library.data.Repository.AlbumsRepository // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.Operators // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.functions.Consumer // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Singleton // NOSONAR

@Singleton //NOSONAR
class AlbumArtistsRepository @Inject constructor(private val albumsRepository: AlbumsRepository) : AlbumArtistsRepository { //NOSONAR

    private var albumArtistsSubscription: Disposable? = null //NOSONAR
    private val albumArtistsRelay = BehaviorRelay.create<List<AlbumArtist>>() //NOSONAR

    override fun getAlbumArtists(): Observable<List<AlbumArtist>> { //NOSONAR
        if (albumArtistsSubscription == null || albumArtistsSubscription?.isDisposed == true) { //NOSONAR
            albumArtistsSubscription = albumsRepository.getAlbums() //NOSONAR
                .flatMap { albums -> Observable.just(Operators.albumsToAlbumArtists(albums)) } //NOSONAR
                .subscribe( //NOSONAR
                    albumArtistsRelay, //NOSONAR
                    Consumer { error -> LogUtils.logException(PlaylistsRepository.TAG, "Failed to get album artists", error) } //NOSONAR
                ) // NOSONAR
        } // NOSONAR
        return albumArtistsRelay.subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "AlbumArtistsRepository" //NOSONAR
    } // NOSONAR

} // NOSONAR
