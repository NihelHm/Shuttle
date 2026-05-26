@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback // NOSONAR

import android.bluetooth.BluetoothA2dp // NOSONAR
import android.bluetooth.BluetoothHeadset // NOSONAR
import android.content.BroadcastReceiver // NOSONAR
import android.content.Context // NOSONAR
import android.content.Intent // NOSONAR
import android.content.IntentFilter // NOSONAR
import android.os.Bundle // NOSONAR
import com.simplecity.amp_library.playback.constants.ExternalIntents // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR

class BluetoothManager( //NOSONAR
    private val playbackManager: PlaybackManager, //NOSONAR
    private val analyticsManager: AnalyticsManager, //NOSONAR
    private val musicServiceCallbacks: MusicService.Callbacks, //NOSONAR
    private val settingsManager: SettingsManager //NOSONAR
) { // NOSONAR

    private var bluetoothReceiver: BroadcastReceiver? = null //NOSONAR

    private var a2dpReceiver: BroadcastReceiver? = null //NOSONAR

    fun registerBluetoothReceiver(context: Context) { //NOSONAR

        val filter = IntentFilter() //NOSONAR
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED) //NOSONAR
        filter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED) //NOSONAR

        bluetoothReceiver = object : BroadcastReceiver() { //NOSONAR
            override fun onReceive(context: Context, intent: Intent) { //NOSONAR

                val action = intent.action //NOSONAR
                if (action != null) { //NOSONAR
                    val extras = intent.extras //NOSONAR
                    if (settingsManager.bluetoothPauseDisconnect) { //NOSONAR
                        when (action) { //NOSONAR
                            BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> if (extras != null) { //NOSONAR
                                val state = extras.getInt(BluetoothA2dp.EXTRA_STATE) //NOSONAR
                                val previousState = extras.getInt(BluetoothA2dp.EXTRA_PREVIOUS_STATE) //NOSONAR
                                if ((state == BluetoothA2dp.STATE_DISCONNECTED || state == BluetoothA2dp.STATE_DISCONNECTING) && previousState == BluetoothA2dp.STATE_CONNECTED) { //NOSONAR
                                    analyticsManager.dropBreadcrumb(TAG, "ACTION_AUDIO_STATE_CHANGED.. pausing. State: $state") //NOSONAR
                                    playbackManager.pause(false) //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                            BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED -> if (extras != null) { //NOSONAR
                                val state = extras.getInt(BluetoothHeadset.EXTRA_STATE) //NOSONAR
                                val previousState = extras.getInt(BluetoothHeadset.EXTRA_PREVIOUS_STATE) //NOSONAR
                                if (state == BluetoothHeadset.STATE_AUDIO_DISCONNECTED && previousState == BluetoothHeadset.STATE_AUDIO_CONNECTED) { //NOSONAR
                                    analyticsManager.dropBreadcrumb(TAG, "ACTION_AUDIO_STATE_CHANGED.. pausing. State: $state") //NOSONAR
                                    playbackManager.pause(false) //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } // NOSONAR

                    if (settingsManager.bluetoothResumeConnect) { //NOSONAR
                        when (action) { //NOSONAR
                            BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> if (extras != null) { //NOSONAR
                                val state = extras.getInt(BluetoothA2dp.EXTRA_STATE) //NOSONAR
                                if (state == BluetoothA2dp.STATE_CONNECTED) { //NOSONAR
                                    playbackManager.play() //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                            BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED -> if (extras != null) { //NOSONAR
                                val state = extras.getInt(BluetoothHeadset.EXTRA_STATE) //NOSONAR
                                if (state == BluetoothHeadset.STATE_AUDIO_CONNECTED) { //NOSONAR
                                    playbackManager.play() //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        context.registerReceiver(bluetoothReceiver, filter) //NOSONAR
    } // NOSONAR

    fun unregisterBluetoothReceiver(context: Context) { //NOSONAR
        context.unregisterReceiver(bluetoothReceiver) //NOSONAR
    } // NOSONAR

    fun registerA2dpServiceListener(context: Context) { //NOSONAR
        a2dpReceiver = object : BroadcastReceiver() { //NOSONAR
            override fun onReceive(context: Context, intent: Intent) { //NOSONAR
                val action = intent.action //NOSONAR
                if (action != null && action == ExternalIntents.PLAY_STATUS_REQUEST) { //NOSONAR
                    musicServiceCallbacks.notifyChange(ExternalIntents.PLAY_STATUS_RESPONSE) //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        val intentFilter = IntentFilter() //NOSONAR
        intentFilter.addAction(ExternalIntents.PLAY_STATUS_REQUEST) //NOSONAR
        context.registerReceiver(a2dpReceiver, intentFilter) //NOSONAR
    } // NOSONAR

    fun unregisterA2dpServiceListener(context: Context) { //NOSONAR
        context.unregisterReceiver(a2dpReceiver) //NOSONAR
    } // NOSONAR

    fun sendPlayStateChangedIntent(context: Context, extras: Bundle) { //NOSONAR
        val intent = Intent(ExternalIntents.AVRCP_PLAY_STATE_CHANGED) //NOSONAR
        intent.putExtras(extras) //NOSONAR
        context.sendBroadcast(intent) //NOSONAR
    } // NOSONAR

    fun sendMetaChangedIntent(context: Context, extras: Bundle) { //NOSONAR
        val intent = Intent(ExternalIntents.AVRCP_META_CHANGED) //NOSONAR
        intent.putExtras(extras) //NOSONAR
        context.sendBroadcast(intent) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "BluetoothManager" //NOSONAR
    } // NOSONAR
} // NOSONAR
