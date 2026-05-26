@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.drawer

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract
import com.simplecity.amp_library.ui.views.PurchaseView

interface DrawerView : //NOSONAR
    PurchaseView, //NOSONAR
    PlaylistMenuContract.View { //NOSONAR

    fun setPlaylistItems(playlists: List<Playlist>) //NOSONAR

    fun closeDrawer() //NOSONAR

    fun setDrawerItemSelected(@DrawerParent.Type type: Int) //NOSONAR
}
