@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.lyrics // NOSONAR

import com.simplecity.amp_library.model.Song // NOSONAR

interface LyricsView { //NOSONAR

    fun updateLyrics(lyrics: String?) //NOSONAR

    fun showNoLyricsView(show: Boolean) //NOSONAR

    fun showQuickLyricInfoButton(show: Boolean) //NOSONAR

    fun showQuickLyricInfoDialog() //NOSONAR

    fun downloadQuickLyric() //NOSONAR

    fun launchQuickLyric(song: Song) //NOSONAR
} // NOSONAR
