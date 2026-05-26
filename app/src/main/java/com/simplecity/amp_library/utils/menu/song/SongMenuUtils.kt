@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.song

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.playback.MediaManager.Defs
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper
import io.reactivex.Single

object SongMenuUtils { //NOSONAR

    const val TAG = "SongMenuUtils" //NOSONAR

    fun setupSongMenu( //NOSONAR
        menu: PopupMenu, //NOSONAR
        showGoToAlbum: Boolean = true, //NOSONAR
        showGoToArtist: Boolean = true, //NOSONAR
        playlistMenuHelper: PlaylistMenuHelper //NOSONAR
    ) {
        menu.inflate(R.menu.menu_song) //NOSONAR

        if (!showGoToAlbum) { //NOSONAR
            menu.menu.findItem(R.id.goToAlbum).isVisible = false //NOSONAR
        }

        if (!showGoToArtist) { //NOSONAR
            menu.menu.findItem(R.id.goToArtist).isVisible = false //NOSONAR
        }

        // Add playlist menu
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR
    }

    fun getSongMenuClickListener(songs: Single<List<Song>>, callbacks: SongsMenuCallbacks): Toolbar.OnMenuItemClickListener { //NOSONAR
        return Toolbar.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylist(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addToQueue(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklist(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.delete -> { //NOSONAR
                    callbacks.delete(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }
            false //NOSONAR
        }
    }

    fun getSongMenuClickListener(song: Song, callbacks: SongsMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylist(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addToQueue(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.editTags -> { //NOSONAR
                    callbacks.editTags(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.share -> { //NOSONAR
                    callbacks.share(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.ringtone -> { //NOSONAR
                    callbacks.setRingtone(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.songInfo -> { //NOSONAR
                    callbacks.songInfo(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklist(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.delete -> { //NOSONAR
                    callbacks.delete(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.goToAlbum -> { //NOSONAR
                    callbacks.goToAlbum(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.goToArtist -> { //NOSONAR
                    callbacks.goToArtist(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
                R.id.goToGenre -> { //NOSONAR
                    callbacks.goToGenre(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                }
            }
            false //NOSONAR
        }
    }
}
