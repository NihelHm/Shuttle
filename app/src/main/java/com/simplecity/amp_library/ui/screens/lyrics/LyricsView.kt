@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.lyrics

import com.simplecity.amp_library.model.Song

interface LyricsView {

    fun updateLyrics(lyrics: String?)

    fun showNoLyricsView(show: Boolean)

    fun showQuickLyricInfoButton(show: Boolean)

    fun showQuickLyricInfoDialog()

    fun downloadQuickLyric()

    fun launchQuickLyric(song: Song)
}
