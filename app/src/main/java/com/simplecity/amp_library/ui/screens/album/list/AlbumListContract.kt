@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.list // NOSONAR

import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR

interface AlbumListContract { //NOSONAR

    interface View : AlbumMenuContract.View { //NOSONAR

        fun setData(albums: List<Album>, scrollToTop: Boolean = false) //NOSONAR

        fun invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    interface Presenter { //NOSONAR

        fun loadAlbums(scrollToTop: Boolean) //NOSONAR

        fun setAlbumsSortOrder(order: Int) //NOSONAR

        fun setAlbumsAscending(ascending: Boolean) //NOSONAR
    } // NOSONAR
} // NOSONAR
