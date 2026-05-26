@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data

import com.jakewharton.rxrelay2.BehaviorRelay
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //NOSONAR
class GenresRepository @Inject constructor(private val application: ShuttleApplication) : Repository.GenresRepository { //NOSONAR

    private var genresSubscription: Disposable? = null //NOSONAR
    private val genresRelay = BehaviorRelay.create<List<Genre>>() //NOSONAR

    override fun getGenres(): Observable<List<Genre>> { //NOSONAR
        if (genresSubscription == null || genresSubscription?.isDisposed == true) { //NOSONAR
            genresSubscription = SqlBriteUtils.createObservableList<Genre>(application, { Genre(it) }, Genre.getQuery()) //NOSONAR
                .subscribe( //NOSONAR
                    genresRelay, //NOSONAR
                    Consumer { error -> LogUtils.logException(PlaylistsRepository.TAG, "Failed to get genres", error) } //NOSONAR
                )
        }

        return genresRelay.subscribeOn(Schedulers.io()) //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "GenresRepository" //NOSONAR
    }
}
