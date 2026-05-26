@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.nowplaying

import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.QueueManager
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

interface PlayerView : SongMenuContract.View {

    fun setSeekProgress(progress: Int)

    fun currentTimeVisibilityChanged(visible: Boolean)

    fun currentTimeChanged(seconds: Long)

    fun totalTimeChanged(seconds: Long)

    fun queueChanged(queuePosition: Int, queueLength: Int)

    fun playbackChanged(isPlaying: Boolean)

    fun shuffleChanged(@QueueManager.ShuffleMode shuffleMode: Int)

    fun repeatChanged(@QueueManager.RepeatMode repeatMode: Int)

    fun favoriteChanged(isFavorite: Boolean)

    fun trackInfoChanged(song: Song?)

    fun showLyricsDialog()

    fun showUpgradeDialog()
}
