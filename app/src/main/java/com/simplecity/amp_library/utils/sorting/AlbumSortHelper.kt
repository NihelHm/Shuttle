@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.sorting // NOSONAR

import android.view.Menu // NOSONAR
import android.view.MenuItem // NOSONAR
import com.simplecity.amp_library.R // NOSONAR

object AlbumSortHelper { //NOSONAR

    @JvmStatic //NOSONAR
    fun updateAlbumSortMenuItems(menu: Menu, albumsSortOrder: Int, albumsAscending: Boolean) { //NOSONAR
        when (albumsSortOrder) { //NOSONAR
            SortManager.AlbumSort.DEFAULT -> menu.findItem(R.id.sort_album_default).isChecked = true //NOSONAR
            SortManager.AlbumSort.NAME -> menu.findItem(R.id.sort_album_name).isChecked = true //NOSONAR
            SortManager.AlbumSort.YEAR -> menu.findItem(R.id.sort_album_year).isChecked = true //NOSONAR
            SortManager.AlbumSort.ARTIST_NAME -> menu.findItem(R.id.sort_album_artist_name).isChecked = true //NOSONAR
        } // NOSONAR

        menu.findItem(R.id.sort_albums_ascending).isChecked = albumsAscending //NOSONAR
    } // NOSONAR

    @JvmStatic //NOSONAR
    @SortManager.AlbumSort //NOSONAR
    fun handleAlbumDetailMenuSortOrderClicks(item: MenuItem): Int? { //NOSONAR
        return when (item.itemId) { //NOSONAR
            R.id.sort_album_default -> SortManager.AlbumSort.DEFAULT //NOSONAR
            R.id.sort_album_name -> SortManager.AlbumSort.NAME //NOSONAR
            R.id.sort_album_year -> SortManager.AlbumSort.YEAR //NOSONAR
            else -> null //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @JvmStatic //NOSONAR
    fun handleAlbumDetailMenuSortOrderAscClicks(item: MenuItem): Boolean? { //NOSONAR
        return when (item.itemId) { //NOSONAR
            R.id.sort_albums_ascending -> !item.isChecked //NOSONAR
            else -> null //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
