@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.detail // NOSONAR

import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.menu.GenreMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR

interface GenreDetailView : //NOSONAR
    GenreMenuContract.View, //NOSONAR
    SongMenuContract.View, //NOSONAR
    AlbumMenuContract.View { //NOSONAR

    fun setData(albums: List<Album>, songs: List<Song>) //NOSONAR

    fun closeContextualToolbar() //NOSONAR

    fun fadeInSlideShowAlbum(previousAlbum: Album?, newAlbum: Album) //NOSONAR
} // NOSONAR
