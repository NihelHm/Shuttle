@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback

import android.content.SharedPreferences
import com.simplecity.amp_library.utils.BaseSettingsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //NOSONAR
class PlaybackSettingsManager @Inject constructor(sharedPreferences: SharedPreferences) : BaseSettingsManager(sharedPreferences) { //NOSONAR

    private val KEY_SEEK_POSITION = "seek_position" //NOSONAR
    var seekPosition: Long //NOSONAR
        get() = getLong(KEY_SEEK_POSITION, 0) //NOSONAR
        set(seekPosition) = setLong(KEY_SEEK_POSITION, seekPosition) //NOSONAR

    private val KEY_QUEUE_POSITION = "queue_position" //NOSONAR
    var queuePosition: Int //NOSONAR
        get() = getInt(KEY_QUEUE_POSITION, 0) //NOSONAR
        set(queuePosition) = setInt(KEY_QUEUE_POSITION, queuePosition) //NOSONAR

    private val KEY_REPEAT_MODE = "repeat_mode" //NOSONAR
    @QueueManager.RepeatMode() //NOSONAR
    var repeatMode: Int //NOSONAR
        get() = getInt(KEY_REPEAT_MODE, QueueManager.RepeatMode.OFF) //NOSONAR
        set(repeatMode) = setInt(KEY_REPEAT_MODE, repeatMode) //NOSONAR

    private val KEY_SHUFFLE_MODE = "shuffle_mode" //NOSONAR
    @QueueManager.ShuffleMode() //NOSONAR
    var shuffleMode: Int //NOSONAR
        get() = getInt(KEY_SHUFFLE_MODE, QueueManager.ShuffleMode.OFF) //NOSONAR
        set(shuffleMode) = setInt(KEY_SHUFFLE_MODE, shuffleMode) //NOSONAR

    private val KEY_QUEUE_LIST = "queue_list" //NOSONAR
    var queueList: String? //NOSONAR
        get() = getString(KEY_QUEUE_LIST, null) //NOSONAR
        set(queueList) = setString(KEY_QUEUE_LIST, queueList) //NOSONAR

    private val KEY_SHUFFLE_LIST = "shuffle_list" //NOSONAR
    var shuffleList: String? //NOSONAR
        get() = getString(KEY_SHUFFLE_LIST, null) //NOSONAR
        set(shuffleList) = setString(KEY_SHUFFLE_LIST, shuffleList) //NOSONAR

    private val KEY_HEADSET_DISCONNECT = "pref_headset_disconnect" //NOSONAR
    var pauseOnHeadsetDisconnect: Boolean //NOSONAR
        get() = getBool(KEY_HEADSET_DISCONNECT, true) //NOSONAR
        set(headsetDisconnect) = setBool(KEY_HEADSET_DISCONNECT, headsetDisconnect) //NOSONAR

    private val KEY_HEADSET_CONNECT = "pref_headset_connect" //NOSONAR
    var playOnHeadsetConnect: Boolean //NOSONAR
        get() = getBool(KEY_HEADSET_CONNECT, false) //NOSONAR
        set(headsetConnect) = setBool(KEY_HEADSET_CONNECT, headsetConnect) //NOSONAR

    private val KEY_LAST_FM_SCROBBLING = "pref_simple_lastfm_scrobbler" //NOSONAR
    var enableLastFmScrobbling: Boolean //NOSONAR
        get() = getBool(KEY_LAST_FM_SCROBBLING, false) //NOSONAR
        set(enableLastFmScrobbling) = setBool(KEY_LAST_FM_SCROBBLING, enableLastFmScrobbling) //NOSONAR

}
