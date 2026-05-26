@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback

import android.content.Context
import android.content.Intent
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.constants.ExternalIntents

class ScrobbleManager(private val playbackSettingsManager: PlaybackSettingsManager) { //NOSONAR

    enum class ScrobbleStatus(val value: Int) { //NOSONAR
        START(0), //NOSONAR
        RESUME(1), //NOSONAR
        PAUSE(2), //NOSONAR
        COMPLETE(3) //NOSONAR
    }

    fun scrobbleBroadcast(context: Context, state: ScrobbleStatus, song: Song) { //NOSONAR
        if (playbackSettingsManager.enableLastFmScrobbling) { //NOSONAR
            val intent = Intent(ExternalIntents.SCROBBLER) //NOSONAR
            intent.putExtra("state", state.value) //NOSONAR
            intent.putExtra("app-name", context.getString(R.string.app_name)) //NOSONAR
            intent.putExtra("app-package", context.packageName) //NOSONAR
            intent.putExtra("artist", song.artistName) //NOSONAR
            intent.putExtra("album", song.albumName) //NOSONAR
            intent.putExtra("track", song.name) //NOSONAR
            intent.putExtra("duration", song.duration / 1000) //NOSONAR
            context.sendBroadcast(intent) //NOSONAR
        }
    }
}
