@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback // NOSONAR

import com.simplecity.amp_library.model.Song // NOSONAR

interface Playback { //NOSONAR

    var isInitialized: Boolean //NOSONAR

    val isPlaying: Boolean //NOSONAR

    val position: Long //NOSONAR

    val audioSessionId: Int //NOSONAR

    val duration: Long //NOSONAR

    var callbacks: Callbacks? //NOSONAR

    fun setVolume(volume: Float) //NOSONAR

    fun load(song: Song, playWhenReady: Boolean, seekPosition: Long, completion: ((Boolean) -> Unit)?) //NOSONAR

    fun willResumePlayback(): Boolean //NOSONAR

    fun setNextDataSource(path: String?) //NOSONAR

    fun release() //NOSONAR

    fun seekTo(position: Long) //NOSONAR

    fun pause(fade: Boolean) //NOSONAR

    fun stop() //NOSONAR

    fun start() //NOSONAR

    fun updateLastKnownStreamPosition() //NOSONAR

    val resumeWhenSwitched: Boolean //NOSONAR

    interface Callbacks { //NOSONAR

        /** // NOSONAR
         * @param trackDidChange true if the underlying [Playback] already handled the transition to next track. // NOSONAR
         */ // NOSONAR
        fun onTrackEnded(playback: Playback, trackDidChange: Boolean) //NOSONAR

        fun onPlayStateChanged(playback: Playback) //NOSONAR

        fun onError(playback: Playback, message: String) //NOSONAR
    } // NOSONAR
} // NOSONAR
