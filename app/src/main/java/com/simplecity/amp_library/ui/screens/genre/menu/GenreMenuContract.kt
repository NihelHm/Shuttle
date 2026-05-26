@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.menu

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.menu.genre.GenreMenuCallbacks

interface GenreMenuContract { //NOSONAR

    interface View { //NOSONAR

        fun presentCreatePlaylistDialog(songs: List<Song>) //NOSONAR

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) //NOSONAR

        fun onSongsAddedToQueue(numSongs: Int) //NOSONAR

        fun onPlaybackFailed() //NOSONAR
    }

    interface Presenter : GenreMenuCallbacks //NOSONAR

}
