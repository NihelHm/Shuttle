@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.playlist.detail

import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

interface PlaylistDetailView :
    PlaylistMenuContract.View,
    SongMenuContract.View {

    fun setData(data: MutableList<Song>)

    fun closeContextualToolbar()

    fun fadeInSlideShowAlbum(previousAlbum: Album?, newAlbum: Album)
}
