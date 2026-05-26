@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.songs.list

import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

interface SongListContract {

    interface View: SongMenuContract.View {

        fun setData(songs: List<Song>, scrollToTop: Boolean = false)

        fun invalidateOptionsMenu()

        fun showPlaybackError()
    }

    interface Presenter {

        fun loadSongs(scrollToTop: Boolean = false)

        fun setSongsSortOrder(order: Int)

        fun setSongsAscending(ascending: Boolean)

        fun setShowArtwork(show: Boolean)

        fun play(song: Song)

        fun shuffleAll()
    }

}
