@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.songs.list

import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

interface SongListContract { //NOSONAR

    interface View: SongMenuContract.View { //NOSONAR

        fun setData(songs: List<Song>, scrollToTop: Boolean = false) //NOSONAR

        fun invalidateOptionsMenu() //NOSONAR

        fun showPlaybackError() //NOSONAR
    }

    interface Presenter { //NOSONAR

        fun loadSongs(scrollToTop: Boolean = false) //NOSONAR

        fun setSongsSortOrder(order: Int) //NOSONAR

        fun setSongsAscending(ascending: Boolean) //NOSONAR

        fun setShowArtwork(show: Boolean) //NOSONAR

        fun play(song: Song) //NOSONAR

        fun shuffleAll() //NOSONAR
    }

}
