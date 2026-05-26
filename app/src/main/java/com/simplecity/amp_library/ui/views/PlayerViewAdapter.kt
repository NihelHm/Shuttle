@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.views // NOSONAR

import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerView // NOSONAR

abstract class PlayerViewAdapter : PlayerView { //NOSONAR

    override fun setSeekProgress(progress: Int) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun currentTimeVisibilityChanged(visible: Boolean) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun currentTimeChanged(seconds: Long) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun totalTimeChanged(seconds: Long) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun queueChanged(queuePosition: Int, queueLength: Int) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun playbackChanged(isPlaying: Boolean) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun shuffleChanged(shuffleMode: Int) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun repeatChanged(repeatMode: Int) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun favoriteChanged(isFavorite: Boolean) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun trackInfoChanged(song: Song?) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun showLyricsDialog() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun showUpgradeDialog() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun presentCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun presentSongInfoDialog(song: Song) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun presentTagEditorDialog(song: Song) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun presentDeleteDialog(songs: List<Song>) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun shareSong(song: Song) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun presentRingtonePermissionDialog() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    override fun showRingtoneSetMessage() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR
} // NOSONAR
