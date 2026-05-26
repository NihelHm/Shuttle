@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.views

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerView

abstract class PlayerViewAdapter : PlayerView { //NOSONAR

    override fun setSeekProgress(progress: Int) { //NOSONAR
        // Intentionally left empty.
    }

    override fun currentTimeVisibilityChanged(visible: Boolean) { //NOSONAR
        // Intentionally left empty.
    }

    override fun currentTimeChanged(seconds: Long) { //NOSONAR
        // Intentionally left empty.
    }

    override fun totalTimeChanged(seconds: Long) { //NOSONAR
        // Intentionally left empty.
    }

    override fun queueChanged(queuePosition: Int, queueLength: Int) { //NOSONAR
        // Intentionally left empty.
    }

    override fun playbackChanged(isPlaying: Boolean) { //NOSONAR
        // Intentionally left empty.
    }

    override fun shuffleChanged(shuffleMode: Int) { //NOSONAR
        // Intentionally left empty.
    }

    override fun repeatChanged(repeatMode: Int) { //NOSONAR
        // Intentionally left empty.
    }

    override fun favoriteChanged(isFavorite: Boolean) { //NOSONAR
        // Intentionally left empty.
    }

    override fun trackInfoChanged(song: Song?) { //NOSONAR
        // Intentionally left empty.
    }

    override fun showLyricsDialog() { //NOSONAR
        // Intentionally left empty.
    }

    override fun showUpgradeDialog() { //NOSONAR
        // Intentionally left empty.
    }

    override fun presentCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        // Intentionally left empty.
    }

    override fun presentSongInfoDialog(song: Song) { //NOSONAR
        // Intentionally left empty.
    }

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) { //NOSONAR
        // Intentionally left empty.
    }

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        // Intentionally left empty.
    }

    override fun presentTagEditorDialog(song: Song) { //NOSONAR
        // Intentionally left empty.
    }

    override fun presentDeleteDialog(songs: List<Song>) { //NOSONAR
        // Intentionally left empty.
    }

    override fun shareSong(song: Song) { //NOSONAR
        // Intentionally left empty.
    }

    override fun presentRingtonePermissionDialog() { //NOSONAR
        // Intentionally left empty.
    }

    override fun showRingtoneSetMessage() { //NOSONAR
        // Intentionally left empty.
    }
}
