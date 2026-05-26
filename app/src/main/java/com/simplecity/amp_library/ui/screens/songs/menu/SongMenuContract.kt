@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.songs.menu

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.menu.song.SongsMenuCallbacks

interface SongMenuContract {

    interface View {

        fun presentCreatePlaylistDialog(songs: List<Song>)

        fun presentSongInfoDialog(song: Song)

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int)

        fun onSongsAddedToQueue(numSongs: Int)

        fun presentTagEditorDialog(song: Song)

        fun presentDeleteDialog(songs: List<Song>)

        fun presentRingtonePermissionDialog()

        fun showRingtoneSetMessage()

        fun shareSong(song: Song)
    }

    interface Presenter : SongsMenuCallbacks
}
