@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.albumartist

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.playback.MediaManager.Defs
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import io.reactivex.Single

object AlbumArtistMenuUtils { //NOSONAR

    fun getAlbumArtistMenuClickListener(selectedAlbumArtists: Single<List<AlbumArtist>>, callbacks: AlbumArtistMenuCallbacks): Toolbar.OnMenuItemClickListener { //NOSONAR
        return Toolbar.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createArtistsPlaylist(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addArtistsToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.playNext -> { //NOSONAR
                    callbacks.playArtistsNext(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addArtistsToQueue(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.delete -> { //NOSONAR
                    callbacks.deleteArtists(selectedAlbumArtists) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }
            false //NOSONAR
        }
    }

    fun getAlbumArtistClickListener(albumArtist: AlbumArtist, callbacks: AlbumArtistMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.play -> { //NOSONAR
                    callbacks.play(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.playNext -> { //NOSONAR
                    callbacks.playArtistsNext(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createArtistsPlaylist(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addArtistsToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addArtistsToQueue(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.editTags -> { //NOSONAR
                    callbacks.editTags(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.info -> { //NOSONAR
                    callbacks.albumArtistInfo(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.artwork -> { //NOSONAR
                    callbacks.editArtwork(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklistArtists(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.delete -> { //NOSONAR
                    callbacks.deleteArtists(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.albumShuffle -> { //NOSONAR
                    callbacks.albumShuffle(albumArtist) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }
            false //NOSONAR
        }
    }
}
