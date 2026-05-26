@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.album.list

import android.annotation.SuppressLint
import com.simplecity.amp_library.data.AlbumsRepository
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.list.AlbumListContract.View
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumMenuPresenter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.sorting.SortManager
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AlbumsPresenter @Inject constructor( //NOSONAR
    private val albumsRepository: AlbumsRepository, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val albumsMenuPresenter: AlbumMenuPresenter //NOSONAR
) : Presenter<View>(), AlbumListContract.Presenter, AlbumMenuContract.Presenter by albumsMenuPresenter { //NOSONAR

    private var albums = mutableListOf<AlbumArtist>() //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR
        albumsMenuPresenter.bindView(view) //NOSONAR
    }

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR
        albumsMenuPresenter.unbindView(view) //NOSONAR
    }

    @SuppressLint("CheckResult") //NOSONAR
    override fun loadAlbums(scrollToTop: Boolean) { //NOSONAR
        addDisposable(albumsRepository.getAlbums() //NOSONAR
            .map { albumArtists -> //NOSONAR
                val albumArtists = albumArtists.toMutableList() //NOSONAR

                sortManager.sortAlbums(albumArtists) //NOSONAR

                if (!sortManager.artistsAscending) { //NOSONAR
                    albumArtists.reverse() //NOSONAR
                }
                albumArtists //NOSONAR
            }
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { albumArtists -> //NOSONAR
                    this.albums - albumArtists //NOSONAR
                    view?.setData(albumArtists) //NOSONAR
                },
                { error -> LogUtils.logException(TAG, "refreshAdapterItems error", error) } //NOSONAR
            ))
    }

    override fun setAlbumsSortOrder(order: Int) { //NOSONAR
        sortManager.artistsSortOrder = order //NOSONAR
        loadAlbums(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    override fun setAlbumsAscending(ascending: Boolean) { //NOSONAR
        sortManager.artistsAscending = ascending //NOSONAR
        loadAlbums(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "AlbumPresenter" //NOSONAR
    }
}
