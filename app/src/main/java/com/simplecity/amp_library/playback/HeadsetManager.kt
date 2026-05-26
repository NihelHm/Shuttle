@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.playback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

class HeadsetManager(
    private val playbackManager: PlaybackManager,
    private val playbackSettingsManager: PlaybackSettingsManager
) {

    private var headsetReceiver: BroadcastReceiver? = null

    fun registerHeadsetPlugReceiver(context: Context) {

        val filter = IntentFilter()
        filter.addAction(AudioManager.ACTION_HEADSET_PLUG)

        headsetReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {

                if (isInitialStickyBroadcast) {
                    return
                }

                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        if (playbackSettingsManager.pauseOnHeadsetDisconnect) {
                            playbackManager.pause(false)
                        }
                    } else if (intent.getIntExtra("state", 0) == 1) {
                        if (playbackSettingsManager.playOnHeadsetConnect) {
                            playbackManager.play()
                        }
                    }
                }
            }
        }

        context.registerReceiver(headsetReceiver, filter)
    }

    fun unregisterHeadsetPlugReceiver(context: Context) {
        context.unregisterReceiver(headsetReceiver)
    }
}
