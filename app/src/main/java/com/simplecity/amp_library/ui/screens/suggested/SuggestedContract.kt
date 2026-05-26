@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.suggested // NOSONAR

import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.suggested.SuggestedPresenter.SuggestedData // NOSONAR

interface SuggestedContract { //NOSONAR

    interface Presenter { //NOSONAR

        fun loadData() //NOSONAR

    } // NOSONAR

    interface View : AlbumMenuContract.View, SongMenuContract.View { //NOSONAR

        fun setData(suggestedData: SuggestedData) //NOSONAR
    } // NOSONAR

} // NOSONAR
