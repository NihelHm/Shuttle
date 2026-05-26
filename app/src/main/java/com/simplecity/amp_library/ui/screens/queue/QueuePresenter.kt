@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.MenuItem
import com.cantrowitz.rxbroadcast.RxBroadcast
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.playback.constants.InternalIntents
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.queue.QueueContract.View
import com.simplecity.amp_library.ui.screens.queue.menu.QueueMenuContract
import com.simplecity.amp_library.ui.screens.queue.menu.QueueMenuPresenter
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class QueuePresenter @Inject constructor( //NOSONAR
    private val application: ShuttleApplication, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    private val settingsManager: SettingsManager, //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val queueMenuPresenter: QueueMenuPresenter //NOSONAR
) : Presenter<View>(), //NOSONAR
    QueueContract.Presenter, //NOSONAR
    QueueMenuContract.Presenter by queueMenuPresenter { //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR

        queueMenuPresenter.bindView(view) //NOSONAR

        var filter = IntentFilter() //NOSONAR
        filter.addAction(InternalIntents.META_CHANGED) //NOSONAR
        addDisposable(RxBroadcast.fromBroadcast(application, filter) //NOSONAR
            .startWith(Intent(InternalIntents.QUEUE_CHANGED)) //NOSONAR
            .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
            .debounce(150, TimeUnit.MILLISECONDS) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe { intent -> //NOSONAR
                val queueView = getView() //NOSONAR
                queueView?.updateQueuePosition(mediaManager.queuePosition) //NOSONAR
            })

        filter = IntentFilter() //NOSONAR
        filter.addAction(InternalIntents.REPEAT_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.SHUFFLE_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.QUEUE_CHANGED) //NOSONAR
        filter.addAction(InternalIntents.SERVICE_CONNECTED) //NOSONAR
        addDisposable(RxBroadcast.fromBroadcast(application, filter) //NOSONAR
            .startWith(Intent(InternalIntents.QUEUE_CHANGED)) //NOSONAR
            .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
            .debounce(150, TimeUnit.MILLISECONDS) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe { intent -> //NOSONAR
                loadData() //NOSONAR
            })
    }

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR

        queueMenuPresenter.unbindView(view) //NOSONAR
    }

    override fun loadData() { //NOSONAR
        view?.setData(mediaManager.queue, mediaManager.queuePosition) //NOSONAR
    }

    override fun play(queueItem: QueueItem) { //NOSONAR
        val index = mediaManager.queue.indexOf(queueItem) //NOSONAR
        if (index >= 0) { //NOSONAR
            mediaManager.queuePosition = index //NOSONAR
            view?.updateQueuePosition(index) //NOSONAR
        }
    }

    override fun saveQueue(context: Context) { //NOSONAR
        view?.showCreatePlaylistDialog(mediaManager.queue.toSongs()) //NOSONAR
    }

    override fun saveQueue(context: Context, item: MenuItem) { //NOSONAR
        val playlist = item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist //NOSONAR
        playlistManager.addToPlaylist(playlist, mediaManager.queue.toSongs(), null) //NOSONAR
    }

    override fun clearQueue() { //NOSONAR
        mediaManager.clearQueue() //NOSONAR
    }

    override fun moveQueueItem(from: Int, to: Int) { //NOSONAR
        mediaManager.moveQueueItem(from, to) //NOSONAR
    }

    override fun setQueueSwipeLocked(locked: Boolean) { //NOSONAR
        settingsManager.setQueueSwipeLocked(locked) //NOSONAR
        view?.setQueueSwipeLocked(locked) //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "QueuePresenter" //NOSONAR
    }

}
