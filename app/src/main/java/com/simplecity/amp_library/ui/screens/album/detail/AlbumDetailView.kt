@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.detail

import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

interface AlbumDetailView : //NOSONAR
    SongMenuContract.View, //NOSONAR
    AlbumMenuContract.View { //NOSONAR
    fun setData(data: MutableList<Song>) //NOSONAR

    fun closeContextualToolbar() //NOSONAR
}
