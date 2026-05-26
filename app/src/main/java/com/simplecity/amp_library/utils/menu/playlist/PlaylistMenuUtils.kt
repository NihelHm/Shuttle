@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.playlist

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.utils.playlists.PlaylistManager

object PlaylistMenuUtils { //NOSONAR

    fun setupPlaylistMenu(menu: PopupMenu, playlist: Playlist) { //NOSONAR
        menu.inflate(R.menu.menu_playlist) //NOSONAR
        updateMenuItemVisibility(menu.menu, playlist) //NOSONAR
    }

    fun setupPlaylistMenu(toolbar: Toolbar, playlist: Playlist) { //NOSONAR
        toolbar.inflateMenu(R.menu.menu_playlist) //NOSONAR
        updateMenuItemVisibility(toolbar.menu, playlist) //NOSONAR
    }

    fun updateMenuItemVisibility(menu: Menu, playlist: Playlist) { //NOSONAR
        if (!playlist.canDelete) { //NOSONAR
            menu.findItem(R.id.deletePlaylist).isVisible = false //NOSONAR
        }

        if (!playlist.canClear) { //NOSONAR
            menu.findItem(R.id.clearPlaylist).isVisible = false //NOSONAR
        }

        if (playlist.id != PlaylistManager.PlaylistIds.RECENTLY_ADDED_PLAYLIST) { //NOSONAR
            menu.findItem(R.id.editPlaylist).isVisible = false //NOSONAR
        }

        if (!playlist.canRename) { //NOSONAR
            menu.findItem(R.id.renamePlaylist).isVisible = false //NOSONAR
        }

        if (playlist.id == PlaylistManager.PlaylistIds.MOST_PLAYED_PLAYLIST) { //NOSONAR
            menu.findItem(R.id.exportPlaylist).isVisible = false //NOSONAR
        }
    }

    fun getPlaylistPopupMenuClickListener(playlist: Playlist, callbacks: PlaylistMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.playPlaylist -> { //NOSONAR
                    callbacks.play(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.deletePlaylist -> { //NOSONAR
                    callbacks.delete(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.editPlaylist -> { //NOSONAR
                    callbacks.edit(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.renamePlaylist -> { //NOSONAR
                    callbacks.rename(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.exportPlaylist -> { //NOSONAR
                    callbacks.createM3uPlaylist(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.clearPlaylist -> { //NOSONAR
                    callbacks.clear(playlist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }
            false //NOSONAR
        }
    }
}
