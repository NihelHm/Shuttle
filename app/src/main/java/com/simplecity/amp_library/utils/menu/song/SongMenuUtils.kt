@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.song // NOSONAR

import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.Toolbar // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager.Defs // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import io.reactivex.Single // NOSONAR

object SongMenuUtils { //NOSONAR

    const val TAG = "SongMenuUtils" //NOSONAR

    fun setupSongMenu( //NOSONAR
        menu: PopupMenu, //NOSONAR
        showGoToAlbum: Boolean = true, //NOSONAR
        showGoToArtist: Boolean = true, //NOSONAR
        playlistMenuHelper: PlaylistMenuHelper //NOSONAR
    ) { // NOSONAR
        menu.inflate(R.menu.menu_song) //NOSONAR

        if (!showGoToAlbum) { //NOSONAR
            menu.menu.findItem(R.id.goToAlbum).isVisible = false //NOSONAR
        } // NOSONAR

        if (!showGoToArtist) { //NOSONAR
            menu.menu.findItem(R.id.goToArtist).isVisible = false //NOSONAR
        } // NOSONAR

        // Add playlist menu // NOSONAR
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR
    } // NOSONAR

    fun getSongMenuClickListener(songs: Single<List<Song>>, callbacks: SongsMenuCallbacks): Toolbar.OnMenuItemClickListener { //NOSONAR
        return Toolbar.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylist(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addToQueue(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklist(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    callbacks.delete(songs) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun getSongMenuClickListener(song: Song, callbacks: SongsMenuCallbacks): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { item -> //NOSONAR
            when (item.itemId) { //NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    callbacks.createPlaylist(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    callbacks.addToPlaylist(item.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    callbacks.addToQueue(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.editTags -> { //NOSONAR
                    callbacks.editTags(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.share -> { //NOSONAR
                    callbacks.share(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.ringtone -> { //NOSONAR
                    callbacks.setRingtone(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.songInfo -> { //NOSONAR
                    callbacks.songInfo(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklist(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    callbacks.delete(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.goToAlbum -> { //NOSONAR
                    callbacks.goToAlbum(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.goToArtist -> { //NOSONAR
                    callbacks.goToArtist(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.goToGenre -> { //NOSONAR
                    callbacks.goToGenre(song) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
