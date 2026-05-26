@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue.menu

import android.content.Context
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository
import com.simplecity.amp_library.data.Repository.AlbumsRepository
import com.simplecity.amp_library.data.Repository.BlacklistRepository
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay
import com.simplecity.amp_library.ui.screens.queue.QueueItem
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter
import com.simplecity.amp_library.utils.RingtoneManager
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import javax.inject.Inject

class QueueMenuPresenter @Inject constructor( //NOSONAR
    context: Context, //NOSONAR
    private val mediaManager: MediaManager, //NOSONAR
    playlistManager: PlaylistManager, //NOSONAR
    blacklistRepository: BlacklistRepository, //NOSONAR
    ringtoneManager: RingtoneManager, //NOSONAR
    albumArtistsRepository: AlbumArtistsRepository, //NOSONAR
    albumsRepository: AlbumsRepository, //NOSONAR
    navigationEventRelay: NavigationEventRelay //NOSONAR
) : SongMenuPresenter( //NOSONAR
    context, //NOSONAR
    mediaManager, //NOSONAR
    playlistManager, //NOSONAR
    blacklistRepository, //NOSONAR
    ringtoneManager, //NOSONAR
    albumArtistsRepository, //NOSONAR
    albumsRepository, //NOSONAR
    navigationEventRelay //NOSONAR
), QueueMenuContract.Presenter { //NOSONAR

    override fun moveToNext(queueItem: QueueItem) { //NOSONAR
        mediaManager.moveToNext(queueItem) //NOSONAR
    }

    override fun removeQueueItems(queueItems: List<QueueItem>) { //NOSONAR
        mediaManager.removeFromQueue(queueItems) //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "QueueMenuPresenter" //NOSONAR
    }
}
