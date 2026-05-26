@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.search

import android.view.View
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract

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
}
