@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.detail

import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

interface ArtistDetailView : //NOSONAR
    SongMenuContract.View, //NOSONAR
    AlbumMenuContract.View, //NOSONAR
    AlbumArtistMenuContract.View { //NOSONAR

    fun setData(albums: List<Album>, songs: List<Song>) //NOSONAR

    fun closeContextualToolbar() //NOSONAR
}
