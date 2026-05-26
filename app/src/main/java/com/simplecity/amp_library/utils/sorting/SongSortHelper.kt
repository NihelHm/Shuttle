@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.sorting

import android.view.Menu
import android.view.MenuItem
import com.simplecity.amp_library.R

object SongSortHelper { //NOSONAR

    @JvmStatic //NOSONAR
    fun updateSongSortMenuItems(menu: Menu, songsSortOrder: Int, songsAscending: Boolean) { //NOSONAR
        when (songsSortOrder) { //NOSONAR
            SortManager.SongSort.DETAIL_DEFAULT -> menu.findItem(R.id.sort_song_detail_default).isChecked = true //NOSONAR
            SortManager.SongSort.DEFAULT -> menu.findItem(R.id.sort_song_default).isChecked = true //NOSONAR
            SortManager.SongSort.NAME -> menu.findItem(R.id.sort_song_name).isChecked = true //NOSONAR
            SortManager.SongSort.TRACK_NUMBER -> menu.findItem(R.id.sort_song_track_number).isChecked = true //NOSONAR
            SortManager.SongSort.DURATION -> menu.findItem(R.id.sort_song_duration).isChecked = true //NOSONAR
            SortManager.SongSort.DATE -> menu.findItem(R.id.sort_song_date).isChecked = true //NOSONAR
            SortManager.SongSort.YEAR -> menu.findItem(R.id.sort_song_year).isChecked = true //NOSONAR
            SortManager.SongSort.ALBUM_NAME -> menu.findItem(R.id.sort_song_album_name).isChecked = true //NOSONAR
            SortManager.SongSort.ARTIST_NAME -> menu.findItem(R.id.sort_song_artist_name).isChecked = true //NOSONAR
        }

        menu.findItem(R.id.sort_song_ascending).isChecked = songsAscending //NOSONAR
    }

    @JvmStatic //NOSONAR
    @SortManager.SongSort //NOSONAR
    fun handleSongMenuSortOrderClicks(item: MenuItem): Int? { //NOSONAR
        return when (item.itemId) { //NOSONAR
            R.id.sort_song_detail_default -> SortManager.SongSort.DETAIL_DEFAULT //NOSONAR
            R.id.sort_song_default -> SortManager.SongSort.DEFAULT //NOSONAR
            R.id.sort_song_name -> SortManager.SongSort.NAME //NOSONAR
            R.id.sort_song_track_number -> SortManager.SongSort.TRACK_NUMBER //NOSONAR
            R.id.sort_song_duration -> SortManager.SongSort.DURATION //NOSONAR
            R.id.sort_song_year -> SortManager.SongSort.YEAR //NOSONAR
            R.id.sort_song_date -> SortManager.SongSort.DATE //NOSONAR
            R.id.sort_song_album_name -> SortManager.SongSort.ALBUM_NAME //NOSONAR
            R.id.sort_song_artist_name -> SortManager.SongSort.ARTIST_NAME //NOSONAR
            else -> null //NOSONAR
        }
    }

    @JvmStatic //NOSONAR
    fun handleSongDetailMenuSortOrderAscClicks(item: MenuItem): Boolean? { //NOSONAR
        return when (item.itemId) { //NOSONAR
            R.id.sort_song_ascending -> !item.isChecked //NOSONAR
            else -> null //NOSONAR
        }
    }
}
