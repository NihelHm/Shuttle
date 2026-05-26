@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.list // NOSONAR

import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract // NOSONAR

interface PlaylistListContract { //NOSONAR

    interface View : PlaylistMenuContract.View { //NOSONAR

        fun setData(playlists: List<Playlist>) //NOSONAR
    } // NOSONAR

    interface Presenter { //NOSONAR

        fun loadData() //NOSONAR
    } // NOSONAR

} // NOSONAR
