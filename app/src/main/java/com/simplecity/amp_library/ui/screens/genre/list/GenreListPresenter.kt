@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.list // NOSONAR

import com.simplecity.amp_library.data.GenresRepository // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.list.GenreListContract.View // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import javax.inject.Inject // NOSONAR

class GenreListPresenter @Inject constructor( //NOSONAR
    private val genreMenuPresenter: GenreMenuPresenter, //NOSONAR
    private val genresRepository: GenresRepository //NOSONAR
) : Presenter<GenreListContract.View>(), //NOSONAR
    GenreListContract.Presenter, //NOSONAR
    GenreMenuContract.Presenter by genreMenuPresenter { //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR

        genreMenuPresenter.bindView(view) //NOSONAR
    } // NOSONAR

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR

        genreMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    override fun loadGenres() { //NOSONAR
        addDisposable(genresRepository //NOSONAR
            .getGenres() //NOSONAR
            .map { genres -> genres.sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.name, b.name) }) } //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { genres -> view?.setData(genres) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Error refreshing adapter items", error) } //NOSONAR
            )) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "GenreListPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
