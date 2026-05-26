@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.text.TextUtils
import android.util.Log
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File

internal class MediaPlayerPlayback(context: Context) : LocalPlayback(context), MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener { //NOSONAR

    private var currentMediaPlayer: MediaPlayer? = createMediaPlayer(context) //NOSONAR
    private var nextMediaPlayer: MediaPlayer? = null //NOSONAR

    override var isInitialized: Boolean = false //NOSONAR

    private var isFadingDown: Boolean = false //NOSONAR
    private var isFadingUp: Boolean = false //NOSONAR
    private var fadeAnimator: ValueAnimator? = null //NOSONAR

    override val isPlaying: Boolean //NOSONAR
        get() = synchronized(this) { //NOSONAR
            if (!isInitialized || isFadingDown) { //NOSONAR
                return false //NOSONAR
            } else { //NOSONAR
                return currentMediaPlayer?.isPlaying ?: false || isFadingUp //NOSONAR
            }
        }

    override val duration: Long //NOSONAR
        get() = synchronized(this) { //NOSONAR
            if (isInitialized) { //NOSONAR
                try { //NOSONAR
                    return currentMediaPlayer?.duration?.toLong() ?: 0 //NOSONAR
                } catch (e: IllegalStateException) { //NOSONAR
                    Log.e(TAG, "Error in getDuration() of MediaPlayerPlayback: " + e.localizedMessage) //NOSONAR
                }

            }
            return 0 //NOSONAR
        }

    override val position: Long //NOSONAR
        get() = synchronized(this) { //NOSONAR
            if (isInitialized) { //NOSONAR
                try { //NOSONAR
                    return currentMediaPlayer?.currentPosition?.toLong() ?: 0 //NOSONAR
                } catch (e: IllegalStateException) { //NOSONAR
                    Log.e(TAG, "Error in getPosition() of MediaPlayerPlayback: " + e.localizedMessage) //NOSONAR
                }

            }
            return 0 //NOSONAR
        }

    override val audioSessionId: Int //NOSONAR
        get() = synchronized(this) { //NOSONAR
            var sessionId = 0 //NOSONAR
            if (isInitialized) { //NOSONAR
                try { //NOSONAR
                    sessionId = currentMediaPlayer?.audioSessionId ?: 0 //NOSONAR
                } catch (e: IllegalStateException) { //NOSONAR
                    Log.e(TAG, "Error in getAudioSessionId() of MediaPlayerPlayback: " + e.localizedMessage) //NOSONAR
                }

            }
            return sessionId //NOSONAR
        }

    override fun load(song: Song, playWhenReady: Boolean, seekPosition: Long, completion: ((Boolean) -> Unit)?) { //NOSONAR
        synchronized(this) { //NOSONAR
            fadeAnimator?.cancel() //NOSONAR
            currentMediaPlayer?.let { currentMediaPlayer -> //NOSONAR
                setDataSourceImpl(currentMediaPlayer, song.path) { success -> //NOSONAR
                    isInitialized = success //NOSONAR

                    if (isInitialized) { //NOSONAR
                        // Invalidate any old 'next data source', will be re-set via external call to setNextDataSource().
                        setNextDataSource(null) //NOSONAR

                        if (seekPosition != 0L) { //NOSONAR
                            seekTo(seekPosition) //NOSONAR
                        }

                        if (playWhenReady) { //NOSONAR
                            start() //NOSONAR
                        }
                    }
                    completion?.invoke(isInitialized) //NOSONAR
                }
            }
        }
    }

    private fun setDataSourceImpl(mediaPlayer: MediaPlayer, path: String, completion: (Boolean) -> Unit) { //NOSONAR
        synchronized(this) { //NOSONAR

            if (TextUtils.isEmpty(path)) { //NOSONAR
                completion(false) //NOSONAR
            }
            try { //NOSONAR
                mediaPlayer.reset() //NOSONAR
                if (path.startsWith("content://")) { //NOSONAR
                    val uri = Uri.parse(path) //NOSONAR
                    mediaPlayer.setDataSource(context, uri) //NOSONAR
                } else { //NOSONAR
                    mediaPlayer.setDataSource(Uri.fromFile(File(path)).toString()) //NOSONAR
                }

                mediaPlayer.setAudioAttributes( //NOSONAR
                    AudioAttributes.Builder() //NOSONAR
                        .setUsage(AudioAttributes.USAGE_MEDIA) //NOSONAR
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) //NOSONAR
                        .build() //NOSONAR
                )

                mediaPlayer.setOnPreparedListener { //NOSONAR
                    mediaPlayer.setOnPreparedListener(null) //NOSONAR
                    completion(true) //NOSONAR
                }
                mediaPlayer.prepareAsync() //NOSONAR
            } catch (e: Exception) { //NOSONAR
                LogUtils.logException(TAG, "setDataSourceImpl failed. Path: [$path]", e) //NOSONAR
                completion(false) //NOSONAR
            }

            mediaPlayer.setOnCompletionListener(this) //NOSONAR
            mediaPlayer.setOnErrorListener(this) //NOSONAR
        }
    }

    override fun setNextDataSource(path: String?) { //NOSONAR
        synchronized(this) { //NOSONAR

            try { //NOSONAR
                currentMediaPlayer?.setNextMediaPlayer(null) //NOSONAR
            } catch (ignored: IllegalArgumentException) { //NOSONAR
                // Nothing to do
            }

            releaseNextMediaPlayer() //NOSONAR

            if (TextUtils.isEmpty(path)) { //NOSONAR
                return //NOSONAR
            }

            nextMediaPlayer = createMediaPlayer(context) //NOSONAR
            nextMediaPlayer!!.audioSessionId = audioSessionId //NOSONAR

            setDataSourceImpl(nextMediaPlayer!!, path!!) { success -> //NOSONAR
                if (success) { //NOSONAR
                    try { //NOSONAR
                        currentMediaPlayer?.setNextMediaPlayer(nextMediaPlayer) //NOSONAR
                    } catch (e: Exception) { //NOSONAR
                        LogUtils.logException(TAG, "setNextDataSource failed - failed to call setNextMediaPlayer on currentMediaPlayer", e) //NOSONAR
                        releaseNextMediaPlayer() //NOSONAR
                    }
                } else { //NOSONAR
                    LogUtils.logException(TAG, "setDataSourceImpl failed for path: [$path]. Setting next media player to null", null) //NOSONAR
                    releaseNextMediaPlayer() //NOSONAR
                }
            }
        }
    }

    private fun releaseNextMediaPlayer() { //NOSONAR
        nextMediaPlayer?.release() //NOSONAR
        nextMediaPlayer = null //NOSONAR
    }

    override fun start() { //NOSONAR
        synchronized(this) { //NOSONAR
            super.start() //NOSONAR

            fadeIn() //NOSONAR

            try { //NOSONAR
                currentMediaPlayer?.start() //NOSONAR
            } catch (e: RuntimeException) { //NOSONAR
                LogUtils.logException(TAG, "start() failed", e) //NOSONAR
            }

            callbacks?.onPlayStateChanged(this) //NOSONAR
        }
    }

    override fun stop() { //NOSONAR
        synchronized(this) { //NOSONAR
            super.stop() //NOSONAR
            if (isInitialized) { //NOSONAR
                try { //NOSONAR
                    currentMediaPlayer?.reset() //NOSONAR
                } catch (e: IllegalStateException) { //NOSONAR
                    LogUtils.logException(TAG, "stop() failed", e) //NOSONAR
                }

                isInitialized = false //NOSONAR
            }

            callbacks?.onPlayStateChanged(this) //NOSONAR
        }
    }

    /**
     * You cannot use this player anymore after calling release()
     */
    override fun release() { //NOSONAR
        synchronized(this) { //NOSONAR
            stop() //NOSONAR
            currentMediaPlayer?.release() //NOSONAR
        }
    }

    override fun pause(fade: Boolean) { //NOSONAR
        synchronized(this) { //NOSONAR
            if (fade) { //NOSONAR
                fadeOut() //NOSONAR
            } else { //NOSONAR
                if (isInitialized) { //NOSONAR
                    super.pause(fade) //NOSONAR
                    try { //NOSONAR
                        currentMediaPlayer?.pause() //NOSONAR
                    } catch (e: IllegalStateException) { //NOSONAR
                        Log.e(TAG, "Error pausing MediaPlayerPlayback: " + e.localizedMessage) //NOSONAR
                    }
                    callbacks?.onPlayStateChanged(this) //NOSONAR
                }
            }
        }
    }

    override fun seekTo(position: Long) { //NOSONAR
        synchronized(this) { //NOSONAR
            if (isInitialized) { //NOSONAR
                try { //NOSONAR
                    currentMediaPlayer?.seekTo(position.toInt()) //NOSONAR
                } catch (e: IllegalStateException) { //NOSONAR
                    Log.e(TAG, "Error seeking MediaPlayerPlayback: " + e.localizedMessage) //NOSONAR
                }

            }
        }
    }

    override fun setVolume(volume: Float) { //NOSONAR
        synchronized(this) { //NOSONAR
            if (isInitialized) { //NOSONAR
                try { //NOSONAR
                    currentMediaPlayer?.setVolume(volume, volume) //NOSONAR
                } catch (e: IllegalStateException) { //NOSONAR
                    Log.e(TAG, "Error setting MediaPlayerPlayback volume: " + e.localizedMessage) //NOSONAR
                }

            }
        }
    }

    override val resumeWhenSwitched: Boolean = false //NOSONAR

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean { //NOSONAR
        when (what) { //NOSONAR
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> { //NOSONAR
                isInitialized = false //NOSONAR
                currentMediaPlayer?.release() //NOSONAR
                currentMediaPlayer = createMediaPlayer(context) //NOSONAR
                callbacks?.onError(this, "Server died") //NOSONAR
                return true //NOSONAR
            }
            else -> { //NOSONAR
                // Intentionally left empty.
            }
        }

        callbacks?.onError(this, "Unknown error") //NOSONAR
        return false //NOSONAR
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) { //NOSONAR
        if (mediaPlayer === currentMediaPlayer && nextMediaPlayer != null) { //NOSONAR
            currentMediaPlayer?.release() //NOSONAR
            currentMediaPlayer = nextMediaPlayer //NOSONAR
            nextMediaPlayer = null //NOSONAR
            callbacks?.onTrackEnded(this, true) //NOSONAR
        } else { //NOSONAR
            callbacks?.onTrackEnded(this, false) //NOSONAR
        }
    }

    override fun updateLastKnownStreamPosition() { //NOSONAR
        // Intentionally left empty.
    }

    private fun createMediaPlayer(context: Context): MediaPlayer { //NOSONAR
        val mediaPlayer = MediaPlayer() //NOSONAR
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK) //NOSONAR
        return mediaPlayer //NOSONAR
    }

    private fun fadeIn() { //NOSONAR
        // Animator needs to run on thread with a looper.
        isFadingUp = true //NOSONAR
        Observable.fromCallable { //NOSONAR
            val currentVolume = fadeAnimator?.animatedValue as? Float ?: 0f //NOSONAR
            fadeAnimator?.cancel() //NOSONAR

            setVolume(currentVolume) //NOSONAR

            fadeAnimator = ValueAnimator.ofFloat(currentVolume, 1f) //NOSONAR
            fadeAnimator!!.duration = 250 //NOSONAR
            fadeAnimator!!.interpolator = FadeInterpolator(2) //NOSONAR
            fadeAnimator!!.addUpdateListener { animation -> setVolume(animation.animatedValue as Float) } //NOSONAR
            fadeAnimator!!.addListener(object : AnimatorListenerAdapter() { //NOSONAR
                override fun onAnimationEnd(animation: Animator) { //NOSONAR
                    super.onAnimationEnd(animation) //NOSONAR
                    isFadingUp = false //NOSONAR
                }

                override fun onAnimationCancel(animation: Animator?) { //NOSONAR
                    super.onAnimationCancel(animation) //NOSONAR
                    fadeAnimator!!.removeAllListeners() //NOSONAR
                    isFadingUp = false //NOSONAR
                }
            })
            fadeAnimator!!.start() //NOSONAR
        }
            .subscribeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe() //NOSONAR
    }

    private fun fadeOut() { //NOSONAR
        // Animator needs to run on thread with a looper.
        isFadingDown = true //NOSONAR
        Observable.fromCallable { //NOSONAR

            val currentVolume = fadeAnimator?.animatedValue as? Float ?: 1f //NOSONAR
            fadeAnimator?.cancel() //NOSONAR

            fadeAnimator = ValueAnimator.ofFloat(currentVolume, 0f) //NOSONAR
            fadeAnimator!!.duration = 150 //NOSONAR
            fadeAnimator!!.interpolator = FadeInterpolator(1) //NOSONAR
            fadeAnimator!!.addUpdateListener { animation -> setVolume(animation.animatedValue as Float) } //NOSONAR
            fadeAnimator!!.addListener(object : AnimatorListenerAdapter() { //NOSONAR
                override fun onAnimationEnd(animation: Animator) { //NOSONAR
                    super.onAnimationEnd(animation) //NOSONAR
                    isFadingDown = false //NOSONAR
                    pause(false) //NOSONAR
                }

                override fun onAnimationCancel(animation: Animator?) { //NOSONAR
                    super.onAnimationCancel(animation) //NOSONAR
                    fadeAnimator!!.removeAllListeners() //NOSONAR
                    isFadingDown = false //NOSONAR
                }
            })
            fadeAnimator!!.start() //NOSONAR
        }
            .subscribeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe() //NOSONAR
    }

    /**
     * @param multiplier exaggerates the logarithmic curve.
     * Higher numbers mean the majority of change occurs over the mid-section of the curve.
     * Numbers < 1.0 approximate a 'linear' curve.
     */
    private class FadeInterpolator(private val multiplier: Int) : TimeInterpolator { //NOSONAR
        override fun getInterpolation(input: Float): Float { //NOSONAR
            return (Math.exp((input * multiplier).toDouble()) * input / Math.exp(multiplier.toDouble())).toFloat() //NOSONAR
        }
    }

    companion object { //NOSONAR
        private const val TAG = "MediaPlayerPlayback" //NOSONAR
    }
}
