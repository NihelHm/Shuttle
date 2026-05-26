@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.list

import android.annotation.SuppressLint
import com.simplecity.amp_library.data.Repository.AlbumArtistsRepository
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuContract
import com.simplecity.amp_library.ui.screens.album.menu.AlbumArtistMenuPresenter
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListContract.View
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.sorting.SortManager
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AlbumArtistListPresenter @Inject constructor( //NOSONAR
    private val artistsRepository: AlbumArtistsRepository, //NOSONAR
    private val sortManager: SortManager, //NOSONAR
    private val albumArtistsMenuPresenter: AlbumArtistMenuPresenter //NOSONAR
) :
    Presenter<View>(), //NOSONAR
    AlbumArtistListContract.Presenter, //NOSONAR
    AlbumArtistMenuContract.Presenter by albumArtistsMenuPresenter { //NOSONAR

    private var albumArtists = mutableListOf<AlbumArtist>() //NOSONAR

    override fun bindView(view: View) { //NOSONAR
        super.bindView(view) //NOSONAR
        albumArtistsMenuPresenter.bindView(view) //NOSONAR
    }

    override fun unbindView(view: View) { //NOSONAR
        super.unbindView(view) //NOSONAR
        albumArtistsMenuPresenter.unbindView(view) //NOSONAR
    }

    @SuppressLint("CheckResult") //NOSONAR
    override fun loadAlbumArtists(scrollToTop: Boolean) { //NOSONAR
        addDisposable(artistsRepository.getAlbumArtists() //NOSONAR
            .map { albumArtists -> //NOSONAR
                val albumArtists = albumArtists.toMutableList() //NOSONAR

                sortManager.sortAlbumArtists(albumArtists) //NOSONAR

                if (!sortManager.artistsAscending) { //NOSONAR
                    albumArtists.reverse() //NOSONAR
                }
                albumArtists //NOSONAR
            }
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { albumArtists -> //NOSONAR
                    this.albumArtists - albumArtists //NOSONAR
                    view?.setData(albumArtists) //NOSONAR
                },
                { error -> LogUtils.logException(TAG, "refreshAdapterItems error", error) } //NOSONAR
            ))
    }

    override fun setAlbumArtistsSortOrder(order: Int) { //NOSONAR
        sortManager.artistsSortOrder = order //NOSONAR
        loadAlbumArtists(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    override fun setAlbumArtistsAscending(ascending: Boolean) { //NOSONAR
        sortManager.artistsAscending = ascending //NOSONAR
        loadAlbumArtists(true) //NOSONAR
        view?.invalidateOptionsMenu() //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "AlbumArtistListPresenter" //NOSONAR
    }
}
