@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.nowplaying

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import com.cantrowitz.rxbroadcast.RxBroadcast
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.playback.PlaybackMonitor
import com.simplecity.amp_library.playback.constants.InternalIntents
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.ShuttleUtils
import com.simplecity.amp_library.utils.menu.song.SongsMenuCallbacks
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlayerPresenter @Inject constructor( //NOSONAR
    private val context: Context, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val playbackMonitor: PlaybackMonitor, //NOSONAR
    private val settingsManager: SettingsManager, //NOSONAR
    private val favoritesPlaylistManager: FavoritesPlaylistManager, //NOSONAR
    private val songMenuPresenter: SongMenuPresenter //NOSONAR
) : Presenter<PlayerView>(), //NOSONAR
    SongsMenuCallbacks by songMenuPresenter { //NOSONAR

    private var startSeekPos: Long = 0 //NOSONAR
    private var lastSeekEventTime: Long = 0 //NOSONAR

    private var currentPlaybackTime: Long = 0 //NOSONAR
    private var currentPlaybackTimeVisible: Boolean = false //NOSONAR

    private var isFavoriteDisposable: Disposable? = null //NOSONAR

    override fun bindView(view: PlayerView) { //NOSONAR
        super.bindView(view) //NOSONAR

        songMenuPresenter.bindView(view) //NOSONAR

        updateTrackInfo() //NOSONAR
        updateShuffleMode() //NOSONAR
        updatePlaystate() //NOSONAR
        updateRepeatMode() //NOSONAR

        addDisposable( //NOSONAR
            playbackMonitor.progressObservable //NOSONAR
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    { progress -> view.setSeekProgress((progress!! * 1000).toInt()) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "PlayerPresenter: Error updating seek progress", error) }) //NOSONAR
        )

        addDisposable( //NOSONAR
            playbackMonitor.currentTimeObservable //NOSONAR
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    { pos -> refreshTimeText(pos!! / 1000) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "PlayerPresenter: Error refreshing time text", error) }) //NOSONAR
        )

        addDisposable( //NOSONAR
            Flowable.interval(500, TimeUnit.MILLISECONDS) //NOSONAR
                .onBackpressureDrop() //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    { setCurrentTimeVisibility(mediaManager.isPlaying || !currentPlaybackTimeVisible) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "PlayerPresenter: Error emitting current time", error) }) //NOSONAR
        )

        val filter = IntentFilter() //NOSONAR
        filter.addAction(InternalIntents.META_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.QUEUE_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.PLAY_STATE_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.SHUFFLE_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.REPEAT_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.SERVICE_CONNECTED) //NOSONAR

        addDisposable( //NOSONAR
            RxBroadcast.fromBroadcast(context, filter) //NOSONAR
                .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                    { intent -> //NOSONAR
                        when (intent.action) { //NOSONAR
                            InternalIntents.META_CHANGED -> updateTrackInfo() //NOSONAR
                            InternalIntents.QUEUE_CHANGED -> updateTrackInfo() //NOSONAR
                            InternalIntents.PLAY_STATE_CHANGED -> { //NOSONAR
                                updateTrackInfo() //NOSONAR
                                updatePlaystate() //NOSONAR
                            }
                            InternalIntents.SHUFFLE_CHANGED -> { //NOSONAR
                                updateTrackInfo() //NOSONAR
                                updateShuffleMode() //NOSONAR
                            }
                            InternalIntents.REPEAT_CHANGED -> updateRepeatMode() //NOSONAR
                            InternalIntents.SERVICE_CONNECTED -> { //NOSONAR
                                updateTrackInfo() //NOSONAR
                                updatePlaystate() //NOSONAR
                                updateShuffleMode() //NOSONAR
                                updateRepeatMode() //NOSONAR
                            }
                        }
                    },
                    { error -> LogUtils.logException(TAG, "PlayerPresenter: Error sending broadcast", error) } //NOSONAR
                )
        )
    }

    override fun unbindView(view: PlayerView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        songMenuPresenter.unbindView(view) //NOSONAR
    }

    private fun refreshTimeText(playbackTime: Long) { //NOSONAR
        if (playbackTime != currentPlaybackTime) { //NOSONAR
            view?.currentTimeChanged(playbackTime) //NOSONAR
            if (settingsManager.displayRemainingTime()) { //NOSONAR
                view?.totalTimeChanged(-(mediaManager.duration / 1000 - playbackTime)) //NOSONAR
            }
        }
        currentPlaybackTime = playbackTime //NOSONAR
    }

    private fun setCurrentTimeVisibility(visible: Boolean) { //NOSONAR
        if (visible != currentPlaybackTimeVisible) { //NOSONAR
            view?.currentTimeVisibilityChanged(visible) //NOSONAR
        }
        currentPlaybackTimeVisible = visible //NOSONAR
    }

    private fun updateFavorite(isFavorite: Boolean) { //NOSONAR
        view?.favoriteChanged(isFavorite) //NOSONAR
    }

    fun updateTrackInfo() { //NOSONAR
        view?.trackInfoChanged(mediaManager.song) //NOSONAR
        view?.queueChanged(mediaManager.queuePosition + 1, mediaManager.queue.size) //NOSONAR
        view?.currentTimeChanged(mediaManager.position / 1000) //NOSONAR
        updateRemainingTime() //NOSONAR

        isFavoriteDisposable?.dispose() //NOSONAR
        isFavoriteDisposable = favoritesPlaylistManager.isFavorite(mediaManager.song) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { isFavorite -> updateFavorite(isFavorite) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "updateTrackInfo error", error) } //NOSONAR
            )

        addDisposable(isFavoriteDisposable!!) //NOSONAR
    }

    private fun updatePlaystate() { //NOSONAR
        view?.playbackChanged(mediaManager.isPlaying) //NOSONAR
    }

    private fun updateShuffleMode() { //NOSONAR
        view?.repeatChanged(mediaManager.repeatMode) //NOSONAR
        view?.shuffleChanged(mediaManager.shuffleMode) //NOSONAR
    }

    private fun updateRepeatMode() { //NOSONAR
        view?.repeatChanged(mediaManager.repeatMode) //NOSONAR
    }

    fun togglePlayback() { //NOSONAR
        mediaManager.togglePlayback() //NOSONAR
    }

    fun toggleFavorite() { //NOSONAR
        mediaManager.toggleFavorite() //NOSONAR
    }

    fun skip() { //NOSONAR
        mediaManager.next() //NOSONAR
    }

    fun prev(force: Boolean) { //NOSONAR
        mediaManager.previous(force) //NOSONAR
    }

    fun toggleShuffle() { //NOSONAR
        mediaManager.toggleShuffleMode() //NOSONAR
        updateShuffleMode() //NOSONAR
    }

    fun toggleRepeat() { //NOSONAR
        mediaManager.cycleRepeat() //NOSONAR
        updateRepeatMode() //NOSONAR
    }

    fun seekTo(progress: Int) { //NOSONAR
        mediaManager.seekTo(mediaManager.duration * progress / 1000) //NOSONAR
    }

    fun scanForward(repeatCount: Int, delta: Long) { //NOSONAR
        var delta = delta //NOSONAR
        if (repeatCount == 0) { //NOSONAR
            startSeekPos = mediaManager.position //NOSONAR
            lastSeekEventTime = 0 //NOSONAR
        } else { //NOSONAR
            if (delta < 5000) { //NOSONAR
                // seek at 10x speed for the first 5 seconds
                delta *= 10 //NOSONAR
            } else { //NOSONAR
                // seek at 40x after that
                delta = 50000 + (delta - 5000) * 40 //NOSONAR
            }
            var newpos = startSeekPos + delta //NOSONAR
            val duration = mediaManager.duration //NOSONAR
            if (newpos >= duration) { //NOSONAR
                // move to next track
                mediaManager.next() //NOSONAR
                startSeekPos -= duration // is OK to go negative //NOSONAR
                newpos -= duration //NOSONAR
            }
            if (delta - lastSeekEventTime > 250 || repeatCount < 0) { //NOSONAR
                mediaManager.seekTo(newpos) //NOSONAR
                lastSeekEventTime = delta //NOSONAR
            }
        }
    }

    fun scanBackward(repeatCount: Int, delta: Long) { //NOSONAR
        var delta = delta //NOSONAR
        if (repeatCount == 0) { //NOSONAR
            startSeekPos = mediaManager.position //NOSONAR
            lastSeekEventTime = 0 //NOSONAR
        } else { //NOSONAR
            if (delta < 5000) { //NOSONAR
                // seek at 10x speed for the first 5 seconds
                delta *= 10 //NOSONAR
            } else { //NOSONAR
                // seek at 40x after that
                delta = 50000 + (delta - 5000) * 40 //NOSONAR
            }
            var newpos = startSeekPos - delta //NOSONAR
            if (newpos < 0) { //NOSONAR
                // move to previous track
                mediaManager.previous(true) //NOSONAR
                val duration = mediaManager.duration //NOSONAR
                startSeekPos += duration //NOSONAR
                newpos += duration //NOSONAR
            }
            if (delta - lastSeekEventTime > 250 || repeatCount < 0) { //NOSONAR
                mediaManager.seekTo(newpos) //NOSONAR
                lastSeekEventTime = delta //NOSONAR
            }
        }
    }

    fun showLyrics() { //NOSONAR
        view?.showLyricsDialog() //NOSONAR
    }

    fun editTagsClicked(activity: Activity) { //NOSONAR
        if (!ShuttleUtils.isUpgraded(activity.applicationContext as ShuttleApplication, settingsManager)) { //NOSONAR
            view?.showUpgradeDialog() //NOSONAR
        } else { //NOSONAR
            view?.presentTagEditorDialog(mediaManager.song!!) //NOSONAR
        }
    }

    fun songInfoClicked() { //NOSONAR
        val song = mediaManager.song //NOSONAR
        if (song != null) { //NOSONAR
            view?.presentSongInfoDialog(song) //NOSONAR
        }
    }

    fun updateRemainingTime() { //NOSONAR
        if (settingsManager.displayRemainingTime()) { //NOSONAR
            view?.totalTimeChanged(-((mediaManager.duration - mediaManager.position) / 1000)) //NOSONAR
        } else { //NOSONAR
            view?.totalTimeChanged(mediaManager.duration / 1000) //NOSONAR
        }
    }

    fun shareClicked() { //NOSONAR
        view?.shareSong(mediaManager.song!!) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "PlayerPresenter" //NOSONAR
    }
}
