@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data // NOSONAR

import com.jakewharton.rxrelay2.BehaviorRelay // NOSONAR
import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import com.simplecity.amp_library.model.Genre // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.functions.Consumer // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Singleton // NOSONAR

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
                ) // NOSONAR
        } // NOSONAR

        return genresRelay.subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "GenresRepository" //NOSONAR
    } // NOSONAR
} // NOSONAR
