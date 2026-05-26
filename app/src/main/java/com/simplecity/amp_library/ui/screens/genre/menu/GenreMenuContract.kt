@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.genre.menu

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.menu.genre.GenreMenuCallbacks

interface GenreMenuContract {

    interface View {

        fun presentCreatePlaylistDialog(songs: List<Song>)

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int)

        fun onSongsAddedToQueue(numSongs: Int)

        fun onPlaybackFailed()
    }

    interface Presenter : GenreMenuCallbacks

}
