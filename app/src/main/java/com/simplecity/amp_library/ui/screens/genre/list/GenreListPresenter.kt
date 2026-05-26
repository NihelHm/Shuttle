@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.list

import com.simplecity.amp_library.data.GenresRepository
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.genre.list.GenreListContract.View
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuContract
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuPresenter
import com.simplecity.amp_library.utils.ComparisonUtils
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class GenreListPresenter @Inject constructor( //NOSONAR
    private val genreMenuPresenter: GenreMenuPresenter, //NOSONAR
    private val genresRepository: GenresRepository //NOSONAR
) : Presenter<GenreListContract.View>(), //NOSONAR
    GenreListContract.Presenter, //NOSONAR
    GenreMenuContract.Presenter by genreMenuPresenter { //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR

        genreMenuPresenter.bindView(view) //NOSONAR
    }

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR

        genreMenuPresenter.unbindView(view) //NOSONAR
    }

    override fun loadGenres() { //NOSONAR
        addDisposable(genresRepository //NOSONAR
            .getGenres() //NOSONAR
            .map { genres -> genres.sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.name, b.name) }) } //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { genres -> view?.setData(genres) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Error refreshing adapter items", error) } //NOSONAR
            ))
    }

    companion object { //NOSONAR
        const val TAG = "GenreListPresenter" //NOSONAR
    }
}
