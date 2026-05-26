@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.queue

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import com.simplecity.amp_library.R
import com.simplecity.amp_library.ui.screens.queue.QueueItem
import com.simplecity.amp_library.ui.screens.queue.toSongs
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper
import io.reactivex.Single

object QueueMenuUtils { //NOSONAR

    fun setupQueueSongMenu(menu: PopupMenu, playlistMenuHelper: PlaylistMenuHelper) { //NOSONAR
        menu.inflate(R.menu.menu_queue_song) //NOSONAR

        // Add playlist menu
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR
    }

    fun getQueueMenuClickListener(queueItems: Single<List<QueueItem>>, callbacks: QueueMenuCallbacks, closeCab: () -> Unit): Toolbar.OnMenuItemClickListener { //NOSONAR
        return Toolbar.OnMenuItemClickListener { item -> //NOSONAR

            if (SongMenuUtils.getSongMenuClickListener(queueItems.map { it.toSongs() }, callbacks).onMenuItemClick(item)) { //NOSONAR
                closeCab() //NOSONAR
            } else { //NOSONAR
                when (item.itemId) { //NOSONAR
                    R.id.queue_remove -> { //NOSONAR
                        callbacks.removeQueueItems(queueItems) //NOSONAR
                        closeCab() //NOSONAR
                        return@OnMenuItemClickListener true //NOSONAR
                    }
                }
            }
            false //NOSONAR
        }
    }

    fun getQueueMenuClickListener(queueItem: QueueItem, callbacks: QueueMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR

            when (item.itemId) { //NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.moveToNext(queueItem) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.remove -> { //NOSONAR
                    callbacks.removeQueueItem(queueItem) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }

            SongMenuUtils.getSongMenuClickListener(queueItem.song, callbacks).onMenuItemClick(item) //NOSONAR
        }
    }
}
