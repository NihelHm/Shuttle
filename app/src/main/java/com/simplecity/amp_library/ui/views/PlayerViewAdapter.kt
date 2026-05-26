package com.simplecity.amp_library.ui.views

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerView

abstract class PlayerViewAdapter : PlayerView {

    override fun setSeekProgress(progress: Int) {
        // Intentionally left empty.
    }

    override fun currentTimeVisibilityChanged(visible: Boolean) {
        // Intentionally left empty.
    }

    override fun currentTimeChanged(seconds: Long) {
        // Intentionally left empty.
    }

    override fun totalTimeChanged(seconds: Long) {
        // Intentionally left empty.
    }

    override fun queueChanged(queuePosition: Int, queueLength: Int) {
        // Intentionally left empty.
    }

    override fun playbackChanged(isPlaying: Boolean) {
        // Intentionally left empty.
    }

    override fun shuffleChanged(shuffleMode: Int) {
        // Intentionally left empty.
    }

    override fun repeatChanged(repeatMode: Int) {
        // Intentionally left empty.
    }

    override fun favoriteChanged(isFavorite: Boolean) {
        // Intentionally left empty.
    }

    override fun trackInfoChanged(song: Song?) {
        // Intentionally left empty.
    }

    override fun showLyricsDialog() {
        // Intentionally left empty.
    }

    override fun showUpgradeDialog() {
        // Intentionally left empty.
    }

    override fun presentCreatePlaylistDialog(songs: List<Song>) {
        // Intentionally left empty.
    }

    override fun presentSongInfoDialog(song: Song) {
        // Intentionally left empty.
    }

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) {
        // Intentionally left empty.
    }

    override fun onSongsAddedToQueue(numSongs: Int) {
        // Intentionally left empty.
    }

    override fun presentTagEditorDialog(song: Song) {
        // Intentionally left empty.
    }

    override fun presentDeleteDialog(songs: List<Song>) {
        // Intentionally left empty.
    }

    override fun shareSong(song: Song) {
        // Intentionally left empty.
    }

    override fun presentRingtonePermissionDialog() {
        // Intentionally left empty.
    }

    override fun showRingtoneSetMessage() {
        // Intentionally left empty.
    }
}
