@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.nowplaying // NOSONAR

import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.QueueManager // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR

interface PlayerView : SongMenuContract.View { //NOSONAR

    fun setSeekProgress(progress: Int) //NOSONAR

    fun currentTimeVisibilityChanged(visible: Boolean) //NOSONAR

    fun currentTimeChanged(seconds: Long) //NOSONAR

    fun totalTimeChanged(seconds: Long) //NOSONAR

    fun queueChanged(queuePosition: Int, queueLength: Int) //NOSONAR

    fun playbackChanged(isPlaying: Boolean) //NOSONAR

    fun shuffleChanged(@QueueManager.ShuffleMode shuffleMode: Int) //NOSONAR

    fun repeatChanged(@QueueManager.RepeatMode repeatMode: Int) //NOSONAR

    fun favoriteChanged(isFavorite: Boolean) //NOSONAR

    fun trackInfoChanged(song: Song?) //NOSONAR

    fun showLyricsDialog() //NOSONAR

    fun showUpgradeDialog() //NOSONAR
} // NOSONAR
