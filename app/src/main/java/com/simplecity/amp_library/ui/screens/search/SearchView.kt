@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.search // NOSONAR

import android.view.View // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR

interface SearchView : SongMenuContract.View, AlbumMenuContract.View, AlbumArtistMenuContract.View { //NOSONAR

    fun setLoading(loading: Boolean) //NOSONAR

    fun setData(searchResult: SearchResult) //NOSONAR

    fun setFilterFuzzyChecked(checked: Boolean) //NOSONAR

    fun setFilterArtistsChecked(checked: Boolean) //NOSONAR

    fun setFilterAlbumsChecked(checked: Boolean) //NOSONAR

    fun showPlaybackError() //NOSONAR

    fun goToArtist(albumArtist: AlbumArtist, transitionView: View) //NOSONAR

    fun goToAlbum(album: Album, transitionView: View) //NOSONAR

    fun showUpgradeDialog() //NOSONAR
} // NOSONAR
