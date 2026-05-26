@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback // NOSONAR

import android.content.BroadcastReceiver // NOSONAR
import android.content.Context // NOSONAR
import android.content.Intent // NOSONAR
import android.content.IntentFilter // NOSONAR
import android.media.AudioManager // NOSONAR

class HeadsetManager( //NOSONAR
    private val playbackManager: PlaybackManager, //NOSONAR
    private val playbackSettingsManager: PlaybackSettingsManager //NOSONAR
) { // NOSONAR

    private var headsetReceiver: BroadcastReceiver? = null //NOSONAR

    fun registerHeadsetPlugReceiver(context: Context) { //NOSONAR

        val filter = IntentFilter() //NOSONAR
        filter.addAction(AudioManager.ACTION_HEADSET_PLUG) //NOSONAR

        headsetReceiver = object : BroadcastReceiver() { //NOSONAR

            override fun onReceive(context: Context, intent: Intent) { //NOSONAR

                if (isInitialStickyBroadcast) { //NOSONAR
                    return //NOSONAR
                } // NOSONAR

                if (intent.hasExtra("state")) { //NOSONAR
                    if (intent.getIntExtra("state", 0) == 0) { //NOSONAR
                        if (playbackSettingsManager.pauseOnHeadsetDisconnect) { //NOSONAR
                            playbackManager.pause(false) //NOSONAR
                        } // NOSONAR
                    } else if (intent.getIntExtra("state", 0) == 1) { //NOSONAR
                        if (playbackSettingsManager.playOnHeadsetConnect) { //NOSONAR
                            playbackManager.play() //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        context.registerReceiver(headsetReceiver, filter) //NOSONAR
    } // NOSONAR

    fun unregisterHeadsetPlugReceiver(context: Context) { //NOSONAR
        context.unregisterReceiver(headsetReceiver) //NOSONAR
    } // NOSONAR
} // NOSONAR
