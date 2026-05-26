@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.list // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import com.simplecity.amp_library.data.AlbumsRepository // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.ui.screens.album.list.AlbumListContract.View // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import javax.inject.Inject // NOSONAR

class AlbumsPresenter @Inject constructor( //NOSONAR
    private val albumsRepository: AlbumsRepository, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val albumsMenuPresenter: AlbumMenuPresenter //NOSONAR
) : Presenter<View>(), AlbumListContract.Presenter, AlbumMenuContract.Presenter by albumsMenuPresenter { //NOSONAR

    private var albums = mutableListOf<AlbumArtist>() //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR
        albumsMenuPresenter.bindView(view) //NOSONAR
    } // NOSONAR

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR
        albumsMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    override fun loadAlbums(scrollToTop: Boolean) { //NOSONAR
        addDisposable(albumsRepository.getAlbums() //NOSONAR
            .map { albumArtists -> //NOSONAR
                val albumArtists = albumArtists.toMutableList() //NOSONAR

                sortManager.sortAlbums(albumArtists) //NOSONAR

                if (!sortManager.artistsAscending) { //NOSONAR
                    albumArtists.reverse() //NOSONAR
                } // NOSONAR
                albumArtists //NOSONAR
            } // NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { albumArtists -> //NOSONAR
                    this.albums - albumArtists //NOSONAR
                    view?.setData(albumArtists) //NOSONAR
                }, // NOSONAR
                { error -> LogUtils.logException(TAG, "refreshAdapterItems error", error) } //NOSONAR
            )) // NOSONAR
    } // NOSONAR

    override fun setAlbumsSortOrder(order: Int) { //NOSONAR
        sortManager.artistsSortOrder = order //NOSONAR
        loadAlbums(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    override fun setAlbumsAscending(ascending: Boolean) { //NOSONAR
        sortManager.artistsAscending = ascending //NOSONAR
        loadAlbums(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "AlbumPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
