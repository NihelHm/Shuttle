@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback // NOSONAR

import android.content.BroadcastReceiver // NOSONAR
import android.content.Context // NOSONAR
import android.content.Intent // NOSONAR
import android.content.IntentFilter // NOSONAR
import android.media.AudioManager // NOSONAR
import android.support.annotation.CallSuper // NOSONAR
import android.util.Log // NOSONAR
import com.simplecity.amp_library.playback.Playback.Callbacks // NOSONAR
import com.simplecity.amp_library.playback.constants.MediaButtonCommand // NOSONAR
import com.simplecity.amp_library.playback.constants.ServiceCommand // NOSONAR

/** // NOSONAR
 * A base class for local playback engines, which manages requesting/cancelling audio focus, and pausing, resuming or ducking // NOSONAR
 * the audio in response to incoming calls/notifications etc. // NOSONAR
 */ // NOSONAR
abstract class LocalPlayback(context: Context) : Playback { //NOSONAR

    object Volume { //NOSONAR
        /** // NOSONAR
         * The volume we set the media player to when we lose audio focus, but are // NOSONAR
         * allowed to reduce the volume instead of stopping playback. // NOSONAR
         */ // NOSONAR
        const val DUCK = 0.2f //NOSONAR

        /** The volume we set the media player when we have audio focus.  */ // NOSONAR
        const val NORMAL = 1.0f //NOSONAR
    } // NOSONAR

    object AudioFocus { //NOSONAR
        /** We don't have audio focus, and can't duck */ // NOSONAR
        const val NO_FOCUS_NO_DUCK = "no_focus_no_duck" //NOSONAR

        /** We don't have focus, but can duck */ // NOSONAR
        const val NO_FOCUS_CAN_DUCK = "no_focus_can_duck" //NOSONAR

        /** We have full audio focus  */ // NOSONAR
        const val FOCUSED = "focused" //NOSONAR
    } // NOSONAR

    internal var context: Context = context.applicationContext //NOSONAR

    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager //NOSONAR

    private var playOnFocusGain: Boolean = false //NOSONAR

    private var audioNoisyReceiverRegistered: Boolean = false //NOSONAR

    private var currentAudioFocusState = AudioFocus.NO_FOCUS_NO_DUCK //NOSONAR

    private val audioNoisyIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY) //NOSONAR

    override var callbacks: Callbacks? = null //NOSONAR

    private val audioNoisyReceiver = object : BroadcastReceiver() { //NOSONAR
        override fun onReceive(context: Context, intent: Intent) { //NOSONAR
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) { //NOSONAR
                Log.d(TAG, "Headphones disconnected.") //NOSONAR
                if (isPlaying) { //NOSONAR
                    val intent = Intent(context, MusicService::class.java) //NOSONAR
                    intent.action = ServiceCommand.COMMAND //NOSONAR
                    intent.putExtra(MediaButtonCommand.CMD_NAME, ServiceCommand.PAUSE) //NOSONAR
                    context.startService(intent) //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private val onAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange -> //NOSONAR
        Log.d(TAG, String.format("onAudioFocusChange. focusChange: %s", focusChange)) //NOSONAR
        when (focusChange) { //NOSONAR
            AudioManager.AUDIOFOCUS_GAIN -> currentAudioFocusState = AudioFocus.FOCUSED //NOSONAR
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> //NOSONAR
                // Audio focus was lost, but it's possible to duck (i.e.: play quietly) // NOSONAR
                currentAudioFocusState = AudioFocus.NO_FOCUS_CAN_DUCK //NOSONAR
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> { //NOSONAR
                // Lost audio focus, but will gain it back (shortly), so note whether // NOSONAR
                // playback should resume // NOSONAR
                currentAudioFocusState = AudioFocus.NO_FOCUS_NO_DUCK //NOSONAR
                playOnFocusGain = isPlaying //NOSONAR
            } // NOSONAR
            AudioManager.AUDIOFOCUS_LOSS -> //NOSONAR
                // Lost audio focus, probably "permanently" // NOSONAR
                currentAudioFocusState = AudioFocus.NO_FOCUS_NO_DUCK //NOSONAR
        } // NOSONAR

        // Update the player state based on the change // NOSONAR
        configurePlayerState() //NOSONAR
    } // NOSONAR

    override fun willResumePlayback(): Boolean { //NOSONAR
        // Fixme: This returns true even after manually pausing playback. This should not be the case. // NOSONAR
        return playOnFocusGain //NOSONAR
    } // NOSONAR

    @CallSuper //NOSONAR
    override fun pause(fade: Boolean) { //NOSONAR
        unregisterAudioNoisyReceiver() //NOSONAR
    } // NOSONAR

    @CallSuper //NOSONAR
    override fun stop() { //NOSONAR
        playOnFocusGain = false //NOSONAR
        giveUpAudioFocus() //NOSONAR
        unregisterAudioNoisyReceiver() //NOSONAR
    } // NOSONAR

    @CallSuper //NOSONAR
    override fun start() { //NOSONAR
        playOnFocusGain = true //NOSONAR
        tryToGetAudioFocus() //NOSONAR
        registerAudioNoisyReceiver() //NOSONAR
    } // NOSONAR

    private fun tryToGetAudioFocus() { //NOSONAR
        Log.d(TAG, "tryToGetAudioFocus") //NOSONAR
        val result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) //NOSONAR
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { //NOSONAR
            currentAudioFocusState = AudioFocus.FOCUSED //NOSONAR
        } else { //NOSONAR
            currentAudioFocusState = AudioFocus.NO_FOCUS_NO_DUCK //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun giveUpAudioFocus() { //NOSONAR
        Log.d(TAG, "giveUpAudioFocus") //NOSONAR
        if (audioManager.abandonAudioFocus(onAudioFocusChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { //NOSONAR
            currentAudioFocusState = AudioFocus.NO_FOCUS_NO_DUCK //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun registerAudioNoisyReceiver() { //NOSONAR
        if (!audioNoisyReceiverRegistered) { //NOSONAR
            context.registerReceiver(audioNoisyReceiver, audioNoisyIntentFilter) //NOSONAR
            audioNoisyReceiverRegistered = true //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun unregisterAudioNoisyReceiver() { //NOSONAR
        if (audioNoisyReceiverRegistered) { //NOSONAR
            context.unregisterReceiver(audioNoisyReceiver) //NOSONAR
            audioNoisyReceiverRegistered = false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun configurePlayerState() { //NOSONAR
        Log.d(TAG, String.format("configurePlayerState() called. currentAudioFocusState: %s", currentAudioFocusState)) //NOSONAR
        if (currentAudioFocusState == AudioFocus.NO_FOCUS_NO_DUCK) { //NOSONAR
            // We don't have audio focus and can't duck, so we have to pause // NOSONAR
            pause(false) //NOSONAR
        } else { //NOSONAR
            registerAudioNoisyReceiver() //NOSONAR

            if (currentAudioFocusState == AudioFocus.NO_FOCUS_CAN_DUCK) { //NOSONAR
                // We're permitted to play, but only if we 'duck', ie: play softly // NOSONAR
                Log.d(TAG, "Adjusting volume: DUCK") //NOSONAR
                setVolume(Volume.DUCK) //NOSONAR
            } else { //NOSONAR
                Log.d(TAG, "Adjusting volume: Normal") //NOSONAR
                setVolume(Volume.NORMAL) //NOSONAR
            } // NOSONAR

            // If we were playing when we lost focus, we need to resume playing. // NOSONAR
            if (playOnFocusGain) { //NOSONAR
                start() //NOSONAR
                playOnFocusGain = false //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "LocalPlayback" //NOSONAR
    } // NOSONAR
} // NOSONAR
