@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.genre // NOSONAR

import android.support.v7.widget.PopupMenu // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Genre // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.playback.MediaManager.Defs // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR

object GenreMenuUtils { //NOSONAR

    fun getGenreClickListener(genre: Genre, callbacks: GenreMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylist(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.play -> { //NOSONAR
                    callbacks.play(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addToQueue(genre) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
