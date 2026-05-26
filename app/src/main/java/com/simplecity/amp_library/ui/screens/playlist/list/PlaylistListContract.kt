@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.playlist.list

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract

interface PlaylistListContract {

    interface View : PlaylistMenuContract.View {

        fun setData(playlists: List<Playlist>)
    }

    interface Presenter {

        fun loadData()
    }

}
