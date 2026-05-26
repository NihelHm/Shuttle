@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.genre.list

import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuContract

interface GenreListContract {

    interface View : GenreMenuContract.View {

        fun setData(genres: List<Genre>)
    }

    interface Presenter {

        fun loadGenres()
    }
}
