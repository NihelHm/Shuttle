@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data

import com.jakewharton.rxrelay2.BehaviorRelay
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository
import com.simplecity.amp_library.data.Repository.AlbumsRepository
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.Operators
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

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
                )
        }
        return albumArtistsRelay.subscribeOn(Schedulers.io()) //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "AlbumArtistsRepository" //NOSONAR
    }

}
