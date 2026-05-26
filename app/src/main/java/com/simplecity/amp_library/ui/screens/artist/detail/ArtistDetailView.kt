@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.detail // NOSONAR

import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR

interface ArtistDetailView : //NOSONAR
    SongMenuContract.View, //NOSONAR
    AlbumMenuContract.View, //NOSONAR
    AlbumArtistMenuContract.View { //NOSONAR

    fun setData(albums: List<Album>, songs: List<Song>) //NOSONAR

    fun closeContextualToolbar() //NOSONAR
} // NOSONAR
