@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.utils.menu.playlist

import com.simplecity.amp_library.model.Playlist

interface PlaylistMenuCallbacks {

    fun playNext(playlist: Playlist)

    fun play(playlist: Playlist)

    fun delete(playlist: Playlist)

    fun edit(playlist: Playlist)

    fun rename(playlist: Playlist)

    fun clear(playlist: Playlist)

    fun createM3uPlaylist(playlist: Playlist)
}
