@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.list

import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuContract

interface GenreListContract { //NOSONAR

    interface View : GenreMenuContract.View { //NOSONAR

        fun setData(genres: List<Genre>) //NOSONAR
    }

    interface Presenter { //NOSONAR

        fun loadGenres() //NOSONAR
    }
}
