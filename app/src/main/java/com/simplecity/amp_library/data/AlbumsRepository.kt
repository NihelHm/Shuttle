@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data // NOSONAR

import com.jakewharton.rxrelay2.BehaviorRelay // NOSONAR
import com.simplecity.amp_library.data.Repository.AlbumsRepository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.Operators // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.functions.Consumer // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Singleton // NOSONAR

@Singleton //NOSONAR
class AlbumsRepository @Inject constructor(private val songsRepository: Repository.SongsRepository) : AlbumsRepository { //NOSONAR

    private var albumsSubscription: Disposable? = null //NOSONAR
    private val albumsRelay = BehaviorRelay.create<List<Album>>() //NOSONAR

    override fun getAlbums(): Observable<List<Album>> { //NOSONAR
        if (albumsSubscription == null || albumsSubscription?.isDisposed == true) { //NOSONAR
            albumsSubscription = songsRepository.getSongs() //NOSONAR
                .flatMap { songs -> Observable.just(Operators.songsToAlbums(songs)) } //NOSONAR
                .subscribe( //NOSONAR
                    albumsRelay, //NOSONAR
                    Consumer { error -> LogUtils.logException(PlaylistsRepository.TAG, "Failed to get albums", error) } //NOSONAR
                ) // NOSONAR
        } // NOSONAR
        return albumsRelay.subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "AlbumsRepository" //NOSONAR
    } // NOSONAR
} // NOSONAR
