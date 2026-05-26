@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.menu

import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.menu.albumartist.AlbumArtistMenuCallbacks

interface AlbumArtistMenuContract { //NOSONAR

    interface View { //NOSONAR

        fun presentCreatePlaylistDialog(songs: List<Song>) //NOSONAR

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) //NOSONAR

        fun onSongsAddedToQueue(numSongs: Int) //NOSONAR

        fun onPlaybackFailed() //NOSONAR

        fun presentTagEditorDialog(albumArtist: AlbumArtist) //NOSONAR

        fun presentArtistDeleteDialog(albumArtists: List<AlbumArtist>) //NOSONAR

        fun presentAlbumArtistInfoDialog(albumArtist: AlbumArtist) //NOSONAR

        fun presentArtworkEditorDialog(albumArtist: AlbumArtist) //NOSONAR
    }

    interface Presenter : AlbumArtistMenuCallbacks //NOSONAR

}
