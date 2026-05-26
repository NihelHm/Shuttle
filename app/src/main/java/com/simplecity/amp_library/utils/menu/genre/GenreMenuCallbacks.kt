@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.genre // NOSONAR

import com.simplecity.amp_library.model.Genre // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR

interface GenreMenuCallbacks { //NOSONAR

    fun createPlaylist(genre: Genre) //NOSONAR

    fun addToPlaylist(playlist: Playlist, genre: Genre) //NOSONAR

    fun addToQueue(genre: Genre) //NOSONAR

    fun play(genre: Genre) //NOSONAR

    fun playNext(genre: Genre) //NOSONAR
} // NOSONAR
