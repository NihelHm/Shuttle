@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.list

import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract

interface AlbumArtistListContract { //NOSONAR

    interface View : AlbumArtistMenuContract.View { //NOSONAR

        fun setData(albumArtists: List<AlbumArtist>, scrollToTop: Boolean = false) //NOSONAR

        fun invalidateOptionsMenu() //NOSONAR
    }

    interface Presenter { //NOSONAR

        fun loadAlbumArtists(scrollToTop: Boolean) //NOSONAR

        fun setAlbumArtistsSortOrder(order: Int) //NOSONAR

        fun setAlbumArtistsAscending(ascending: Boolean) //NOSONAR
    }

}
