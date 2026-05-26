@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue.menu // NOSONAR

import android.content.Context // NOSONAR
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository // NOSONAR
import com.simplecity.amp_library.data.Repository.AlbumsRepository // NOSONAR
import com.simplecity.amp_library.data.Repository.BlacklistRepository // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItem // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import javax.inject.Inject // NOSONAR

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
    } // NOSONAR

    override fun removeQueueItems(queueItems: List<QueueItem>) { //NOSONAR
        mediaManager.removeFromQueue(queueItems) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "QueueMenuPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
