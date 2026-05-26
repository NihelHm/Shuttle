@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.suggested

import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.ui.screens.suggested.SuggestedPresenter.SuggestedData

interface SuggestedContract { //NOSONAR

    interface Presenter { //NOSONAR

        fun loadData() //NOSONAR

    }

    interface View : AlbumMenuContract.View, SongMenuContract.View { //NOSONAR

        fun setData(suggestedData: SuggestedData) //NOSONAR
    }

}
