@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.album // NOSONAR

import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.Toolbar // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.playback.MediaManager.Defs // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import io.reactivex.Single // NOSONAR

object AlbumMenuUtils { //NOSONAR

    const val TAG = "AlbumMenuUtils" //NOSONAR

    fun setupAlbumMenu(menu: PopupMenu, playlistMenuHelper: PlaylistMenuHelper, showGoToArtist: Boolean = true) { //NOSONAR
        menu.inflate(R.menu.menu_album) //NOSONAR

        if (!showGoToArtist) { //NOSONAR
            menu.menu.findItem(R.id.go_to).isVisible = false //NOSONAR
        } // NOSONAR

        // Add playlist menu // NOSONAR
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR
    } // NOSONAR

    fun getAlbumMenuClickListener(selectedAlbums: Single<List<Album>>, callbacks: AlbumsMenuCallbacks): Toolbar.OnMenuItemClickListener { //NOSONAR
        return Toolbar.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylistFromAlbums(selectedAlbums) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addAlbumsToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, selectedAlbums) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playAlbumsNext(selectedAlbums) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addAlbumsToQueue(selectedAlbums) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    callbacks.deleteAlbums(selectedAlbums) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun getAlbumMenuClickListener(album: Album, callbacks: AlbumsMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.play -> { //NOSONAR
                    callbacks.play(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playAlbumsNext(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylistFromAlbums(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addAlbumsToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addAlbumsToQueue(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.editTags -> { //NOSONAR
                    callbacks.editTags(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.info -> { //NOSONAR
                    callbacks.albumInfo(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.artwork -> { //NOSONAR
                    callbacks.editArtwork(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklistAlbums(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    callbacks.deleteAlbums(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.goToArtist -> { //NOSONAR
                    callbacks.goToArtist(album) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
