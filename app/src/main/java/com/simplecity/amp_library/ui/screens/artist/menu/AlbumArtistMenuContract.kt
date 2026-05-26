@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.menu // NOSONAR

import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.utils.menu.albumartist.AlbumArtistMenuCallbacks // NOSONAR

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
    } // NOSONAR

    interface Presenter : AlbumArtistMenuCallbacks //NOSONAR

} // NOSONAR
