@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.playlist.menu

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.utils.menu.playlist.PlaylistMenuCallbacks

interface PlaylistMenuContract {

    interface View {

        fun onPlaybackFailed()

        fun onSongsAddedToQueue(numSongs: Int)

        fun presentEditDialog(playlist: Playlist)

        fun presentRenameDialog(playlist: Playlist)

        fun presentM3uDialog(playlist: Playlist)

        fun presentDeletePlaylistDialog(playlist: Playlist)
    }

    interface Presenter : PlaylistMenuCallbacks

}
