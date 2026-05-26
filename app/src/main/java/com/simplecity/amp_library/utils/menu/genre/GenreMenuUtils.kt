@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.genre

import android.support.v7.widget.PopupMenu
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.playback.MediaManager.Defs
import com.simplecity.amp_library.utils.playlists.PlaylistManager

object GenreMenuUtils { //NOSONAR

    fun getGenreClickListener(genre: Genre, callbacks: GenreMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylist(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.play -> { //NOSONAR
                    callbacks.play(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addToQueue(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }
            false //NOSONAR
        }
    }
}
