@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.list

import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract

interface AlbumListContract { //NOSONAR

    interface View : AlbumMenuContract.View { //NOSONAR

        fun setData(albums: List<Album>, scrollToTop: Boolean = false) //NOSONAR

        fun invalidateOptionsMenu() //NOSONAR
    }

    interface Presenter { //NOSONAR

        fun loadAlbums(scrollToTop: Boolean) //NOSONAR

        fun setAlbumsSortOrder(order: Int) //NOSONAR

        fun setAlbumsAscending(ascending: Boolean) //NOSONAR
    }
}
