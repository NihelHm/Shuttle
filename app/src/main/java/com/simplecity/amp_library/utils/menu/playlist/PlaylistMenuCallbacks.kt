@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.playlist // NOSONAR

import com.simplecity.amp_library.model.Playlist // NOSONAR

interface PlaylistMenuCallbacks { //NOSONAR

    fun playNext(playlist: Playlist) //NOSONAR

    fun play(playlist: Playlist) //NOSONAR

    fun delete(playlist: Playlist) //NOSONAR

    fun edit(playlist: Playlist) //NOSONAR

    fun rename(playlist: Playlist) //NOSONAR

    fun clear(playlist: Playlist) //NOSONAR

    fun createM3uPlaylist(playlist: Playlist) //NOSONAR
} // NOSONAR
