@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.menu

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.utils.menu.playlist.PlaylistMenuCallbacks

interface PlaylistMenuContract { //NOSONAR

    interface View { //NOSONAR

        fun onPlaybackFailed() //NOSONAR

        fun onSongsAddedToQueue(numSongs: Int) //NOSONAR

        fun presentEditDialog(playlist: Playlist) //NOSONAR

        fun presentRenameDialog(playlist: Playlist) //NOSONAR

        fun presentM3uDialog(playlist: Playlist) //NOSONAR

        fun presentDeletePlaylistDialog(playlist: Playlist) //NOSONAR
    }

    interface Presenter : PlaylistMenuCallbacks //NOSONAR

}
