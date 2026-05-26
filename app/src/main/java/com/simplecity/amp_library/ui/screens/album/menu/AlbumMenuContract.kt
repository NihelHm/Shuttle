@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.menu // NOSONAR

import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.utils.menu.album.AlbumsMenuCallbacks // NOSONAR

interface AlbumMenuContract { //NOSONAR

    interface View { //NOSONAR

        fun presentCreatePlaylistDialog(songs: List<Song>) //NOSONAR

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) //NOSONAR

        fun onSongsAddedToQueue(numSongs: Int) //NOSONAR

        fun onPlaybackFailed() //NOSONAR

        fun presentTagEditorDialog(album: Album) //NOSONAR

        fun presentDeleteAlbumsDialog(albums: List<Album>) //NOSONAR

        fun presentAlbumInfoDialog(album: Album) //NOSONAR

        fun presentArtworkEditorDialog(album: Album) //NOSONAR
    } // NOSONAR

    interface Presenter : AlbumsMenuCallbacks //NOSONAR

} // NOSONAR
