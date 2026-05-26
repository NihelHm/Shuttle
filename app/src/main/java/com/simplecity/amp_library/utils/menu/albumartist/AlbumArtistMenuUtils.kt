@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.albumartist // NOSONAR

import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.Toolbar // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.playback.MediaManager.Defs // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import io.reactivex.Single // NOSONAR

object AlbumArtistMenuUtils { //NOSONAR

    fun getAlbumArtistMenuClickListener(selectedAlbumArtists: Single<List<AlbumArtist>>, callbacks: AlbumArtistMenuCallbacks): Toolbar.OnMenuItemClickListener { //NOSONAR
        return Toolbar.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createArtistsPlaylist(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addArtistsToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playArtistsNext(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addArtistsToQueue(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    callbacks.deleteArtists(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun getAlbumArtistClickListener(albumArtist: AlbumArtist, callbacks: AlbumArtistMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.play -> { //NOSONAR
                    callbacks.play(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playArtistsNext(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createArtistsPlaylist(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addArtistsToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addArtistsToQueue(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.editTags -> { //NOSONAR
                    callbacks.editTags(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.info -> { //NOSONAR
                    callbacks.albumArtistInfo(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.artwork -> { //NOSONAR
                    callbacks.editArtwork(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklistArtists(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    callbacks.deleteArtists(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.albumShuffle -> { //NOSONAR
                    callbacks.albumShuffle(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
