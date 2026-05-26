@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.album.menu

import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.menu.album.AlbumsMenuCallbacks

interface AlbumMenuContract {

    interface View {

        fun presentCreatePlaylistDialog(songs: List<Song>)

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int)

        fun onSongsAddedToQueue(numSongs: Int)

        fun onPlaybackFailed()

        fun presentTagEditorDialog(album: Album)

        fun presentDeleteAlbumsDialog(albums: List<Album>)

        fun presentAlbumInfoDialog(album: Album)

        fun presentArtworkEditorDialog(album: Album)
    }

    interface Presenter : AlbumsMenuCallbacks

}
