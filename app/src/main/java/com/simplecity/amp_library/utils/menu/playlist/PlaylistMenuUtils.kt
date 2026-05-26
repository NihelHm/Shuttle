@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.playlist // NOSONAR

import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.Toolbar // NOSONAR
import android.view.Menu // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR

object PlaylistMenuUtils { //NOSONAR

    fun setupPlaylistMenu(menu: PopupMenu, playlist: Playlist) { //NOSONAR
        menu.inflate(R.menu.menu_playlist) //NOSONAR
        updateMenuItemVisibility(menu.menu, playlist) //NOSONAR
    } // NOSONAR

    fun setupPlaylistMenu(toolbar: Toolbar, playlist: Playlist) { //NOSONAR
        toolbar.inflateMenu(R.menu.menu_playlist) //NOSONAR
        updateMenuItemVisibility(toolbar.menu, playlist) //NOSONAR
    } // NOSONAR

    fun updateMenuItemVisibility(menu: Menu, playlist: Playlist) { //NOSONAR
        if (!playlist.canDelete) { //NOSONAR
            menu.findItem(R.id.deletePlaylist).isVisible = false //NOSONAR
        } // NOSONAR

        if (!playlist.canClear) { //NOSONAR
            menu.findItem(R.id.clearPlaylist).isVisible = false //NOSONAR
        } // NOSONAR

        if (playlist.id != PlaylistManager.PlaylistIds.RECENTLY_ADDED_PLAYLIST) { //NOSONAR
            menu.findItem(R.id.editPlaylist).isVisible = false //NOSONAR
        } // NOSONAR

        if (!playlist.canRename) { //NOSONAR
            menu.findItem(R.id.renamePlaylist).isVisible = false //NOSONAR
        } // NOSONAR

        if (playlist.id == PlaylistManager.PlaylistIds.MOST_PLAYED_PLAYLIST) { //NOSONAR
            menu.findItem(R.id.exportPlaylist).isVisible = false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun getPlaylistPopupMenuClickListener(playlist: Playlist, callbacks: PlaylistMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.playPlaylist -> { //NOSONAR
                    callbacks.play(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.deletePlaylist -> { //NOSONAR
                    callbacks.delete(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.editPlaylist -> { //NOSONAR
                    callbacks.edit(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.renamePlaylist -> { //NOSONAR
                    callbacks.rename(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.exportPlaylist -> { //NOSONAR
                    callbacks.createM3uPlaylist(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.clearPlaylist -> { //NOSONAR
                    callbacks.clear(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
