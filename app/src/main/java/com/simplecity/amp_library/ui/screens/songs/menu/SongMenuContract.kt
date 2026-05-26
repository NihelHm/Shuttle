@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.menu

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.menu.song.SongsMenuCallbacks

interface SongMenuContract { //NOSONAR

    interface View { //NOSONAR

        fun presentCreatePlaylistDialog(songs: List<Song>) //NOSONAR

        fun presentSongInfoDialog(song: Song) //NOSONAR

        fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) //NOSONAR

        fun onSongsAddedToQueue(numSongs: Int) //NOSONAR

        fun presentTagEditorDialog(song: Song) //NOSONAR

        fun presentDeleteDialog(songs: List<Song>) //NOSONAR

        fun presentRingtonePermissionDialog() //NOSONAR

        fun showRingtoneSetMessage() //NOSONAR

        fun shareSong(song: Song) //NOSONAR
    }

    interface Presenter : SongsMenuCallbacks //NOSONAR
}
