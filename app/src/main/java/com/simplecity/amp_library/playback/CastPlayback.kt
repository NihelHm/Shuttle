@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import com.simplecity.amp_library.R
import com.simplecity.amp_library.glide.utils.GlideUtils
import com.simplecity.amp_library.http.HttpServer
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.Playback.Callbacks
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.ShuttleUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import java.io.ByteArrayOutputStream

class CastPlayback(context: Context, castSession: CastSession) : Playback { //NOSONAR

    private val applicationContext = context.applicationContext //NOSONAR

    private val remoteMediaClient: RemoteMediaClient = castSession.remoteMediaClient //NOSONAR
    private val remoteMediaClientCallback: RemoteMediaClient.Callback //NOSONAR

    private var currentPosition = 0L //NOSONAR

    private var currentSong: Song? = null //NOSONAR

    private var playerState: Int? = MediaStatus.PLAYER_STATE_UNKNOWN; //NOSONAR

    // remoteMediaClient.isPlaying() returns true momentarily after it is paused, so we use this to track whether
    // it really is playing, based on calls to play(), pause(), stop() and load()
    private var isMeantToBePlaying = false //NOSONAR

    init { //NOSONAR
        remoteMediaClientCallback = CastMediaClientCallback() //NOSONAR
    }

    override var isInitialized: Boolean = false //NOSONAR

    override val isPlaying: Boolean //NOSONAR
        get() { //NOSONAR
            return remoteMediaClient.isPlaying || isMeantToBePlaying //NOSONAR
        }

    override val position: Long //NOSONAR
        get() { //NOSONAR
            if (remoteMediaClient.approximateStreamPosition == 0L) { //NOSONAR
                return if (currentPosition <= duration) currentPosition else 0L //NOSONAR
            }
            return remoteMediaClient.approximateStreamPosition //NOSONAR
        }

    override val audioSessionId: Int //NOSONAR
        get() = 0 //NOSONAR

    override val duration: Long //NOSONAR
        get() { //NOSONAR
            return remoteMediaClient.streamDuration //NOSONAR
        }

    override var callbacks: Callbacks? = null //NOSONAR

    override fun setVolume(volume: Float) { //NOSONAR
        // Nothing to do
    }

    override fun load(song: Song, playWhenReady: Boolean, seekPosition: Long, completion: ((Boolean) -> Unit)?) { //NOSONAR

        HttpServer.getInstance().start() //NOSONAR
        HttpServer.getInstance().serveAudio(song.path) //NOSONAR
        HttpServer.getInstance().clearImage() //NOSONAR

        val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK) //NOSONAR
        metadata.putString(MediaMetadata.KEY_ALBUM_ARTIST, song.albumArtistName) //NOSONAR
        metadata.putString(MediaMetadata.KEY_ALBUM_TITLE, song.albumName) //NOSONAR
        metadata.putString(MediaMetadata.KEY_TITLE, song.name) //NOSONAR
        metadata.addImage(WebImage(Uri.parse("http://" + ShuttleUtils.getIpAddr(applicationContext) + ":5000" + "/image/" + song.id))) //NOSONAR

        val mediaInfo = MediaInfo.Builder("http://" + ShuttleUtils.getIpAddr(applicationContext) + ":5000" + "/audio/" + song.id) //NOSONAR
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED) //NOSONAR
            .setContentType("audio/*") //NOSONAR
            .setMetadata(metadata) //NOSONAR
            .build() //NOSONAR

        currentSong = song //NOSONAR
        currentPosition = seekPosition //NOSONAR

        fun performLoad() { //NOSONAR
            remoteMediaClient.registerCallback(remoteMediaClientCallback) //NOSONAR

            remoteMediaClient.load( //NOSONAR
                mediaInfo, MediaLoadOptions.Builder() //NOSONAR
                    .setPlayPosition(seekPosition) //NOSONAR
                    .setAutoplay(playWhenReady) //NOSONAR
                    .build() //NOSONAR
            )

            if (playWhenReady) { //NOSONAR
                isMeantToBePlaying = true //NOSONAR
            }

            isInitialized = true //NOSONAR

            completion?.invoke(true) //NOSONAR
        }

        Glide.with(applicationContext).load(song) //NOSONAR
            .asBitmap() //NOSONAR
            .transcode(BitmapBytesTranscoder(), ByteArray::class.java) //NOSONAR
            .placeholder(R.drawable.ic_placeholder_dark_large) //NOSONAR
            .into(object : SimpleTarget<ByteArray>() { //NOSONAR
                override fun onResourceReady(resource: ByteArray, glideAnimation: GlideAnimation<in ByteArray>?) { //NOSONAR
                    HttpServer.getInstance().serveImage(resource) //NOSONAR
                    performLoad() //NOSONAR
                }

                @SuppressLint("CheckResult") //NOSONAR
                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) { //NOSONAR
                    super.onLoadFailed(e, errorDrawable) //NOSONAR

                    Single.fromCallable { //NOSONAR
                        errorDrawable?.let { //NOSONAR
                            val outputStream = ByteArrayOutputStream() //NOSONAR
                            val bitmap = GlideUtils.drawableToBitmap(errorDrawable) //NOSONAR
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) //NOSONAR
                            HttpServer.getInstance().serveImage(outputStream.toByteArray()) //NOSONAR
                        }
                    }
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe({ //NOSONAR
                            performLoad() //NOSONAR
                        }, { error -> LogUtils.logException(TAG, "Failed to load error drawable", error) }) //NOSONAR
                }
            })
    }

    override fun willResumePlayback(): Boolean { //NOSONAR
        return false //NOSONAR
    }

    override fun setNextDataSource(path: String?) { //NOSONAR
        // Nothing to do
    }

    override fun release() { //NOSONAR
        HttpServer.getInstance().stop() //NOSONAR
    }

    override fun seekTo(position: Long) { //NOSONAR
        currentPosition = position //NOSONAR
        try { //NOSONAR
            if (remoteMediaClient.hasMediaSession()) { //NOSONAR
                remoteMediaClient.seek(position) //NOSONAR
            } else { //NOSONAR
                currentSong?.let { currentSong -> //NOSONAR
                    load(currentSong, true, position, null) //NOSONAR
                } ?: Log.e(TAG, "Seek failed, no remote media session") //NOSONAR
            }
        } catch (e: JSONException) { //NOSONAR
            LogUtils.logException(TAG, "Exception pausing cast playback", e) //NOSONAR
            if (callbacks != null) { //NOSONAR
                callbacks?.onError(this, e.message ?: "Unspecified error") //NOSONAR
            }
        }
    }

    override fun pause(fade: Boolean) { //NOSONAR
        isMeantToBePlaying = false //NOSONAR
        try { //NOSONAR
            if (remoteMediaClient.hasMediaSession()) { //NOSONAR
                currentPosition = remoteMediaClient.approximateStreamPosition //NOSONAR
                remoteMediaClient.pause() //NOSONAR
            } else { //NOSONAR
                Log.e(TAG, "Pause failed, no remote media session") //NOSONAR
            }
        } catch (e: JSONException) { //NOSONAR
            LogUtils.logException(TAG, "Exception pausing cast playback", e) //NOSONAR
            callbacks?.onError(this, e.message ?: "Unspecified error") //NOSONAR
        }
    }

    override fun stop() { //NOSONAR
        isMeantToBePlaying = false //NOSONAR

        if (remoteMediaClient.hasMediaSession()) { //NOSONAR
            currentPosition = remoteMediaClient.approximateStreamPosition //NOSONAR
            remoteMediaClient.stop() //NOSONAR
        }

        remoteMediaClient.unregisterCallback(remoteMediaClientCallback) //NOSONAR

        release() //NOSONAR
    }

    override fun start() { //NOSONAR
        isMeantToBePlaying = true //NOSONAR

        if (remoteMediaClient.hasMediaSession() && !remoteMediaClient.isPlaying) { //NOSONAR
            currentPosition = remoteMediaClient.approximateStreamPosition //NOSONAR
            remoteMediaClient.play() //NOSONAR
        } else { //NOSONAR
            Log.e(TAG, "start() failed.. hasMediaSession " + remoteMediaClient.hasMediaSession()) //NOSONAR
        }
    }

    override fun updateLastKnownStreamPosition() { //NOSONAR
        currentPosition = position //NOSONAR
    }

    override val resumeWhenSwitched: Boolean = true //NOSONAR

    private fun updatePlaybackState() { //NOSONAR
        val playerState = remoteMediaClient.playerState //NOSONAR
        if (playerState != this.playerState) { //NOSONAR
            // Convert the remote playback states to media playback states.
            when (playerState) { //NOSONAR
                MediaStatus.PLAYER_STATE_IDLE -> { //NOSONAR
                    val idleReason = remoteMediaClient.idleReason //NOSONAR
                    Log.d(TAG, "onRemoteMediaPlayerStatusUpdated... IDLE, reason: $idleReason") //NOSONAR
                    if (idleReason == MediaStatus.IDLE_REASON_FINISHED) { //NOSONAR
                        currentPosition = 0L //NOSONAR
                        Log.i(TAG, "Calling onTrackEnded") //NOSONAR
                        callbacks?.onTrackEnded(this, false) //NOSONAR
                    }
                }
                MediaStatus.PLAYER_STATE_PLAYING -> { //NOSONAR
                    Log.d(TAG, "onRemoteMediaPlayerStatusUpdated.. PLAYING") //NOSONAR
                    callbacks?.onPlayStateChanged(this) //NOSONAR
                }
                MediaStatus.PLAYER_STATE_PAUSED -> { //NOSONAR
                    Log.d(TAG, "onRemoteMediaPlayerStatusUpdated.. PAUSED") //NOSONAR
                    callbacks?.onPlayStateChanged(this) //NOSONAR
                }
                else -> { //NOSONAR
                    Log.d(TAG, "State default : $playerState") //NOSONAR
                }
            }
        }
        this.playerState = playerState //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "CastPlayback" //NOSONAR
    }

    private inner class CastMediaClientCallback : RemoteMediaClient.Callback() { //NOSONAR

        override fun onMetadataUpdated() { //NOSONAR
            Log.d(TAG, "RemoteMediaClient.onMetadataUpdated") //NOSONAR
        }

        override fun onStatusUpdated() { //NOSONAR
            Log.d(TAG, "RemoteMediaClient.onStatusUpdated") //NOSONAR
            updatePlaybackState() //NOSONAR
        }
    }
}
