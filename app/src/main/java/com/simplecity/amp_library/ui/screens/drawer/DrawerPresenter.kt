@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.drawer // NOSONAR

import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.ui.common.PurchasePresenter // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuContract // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.menu.PlaylistMenuPresenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.PermissionUtils // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import java.util.concurrent.TimeUnit // NOSONAR
import javax.inject.Inject // NOSONAR

class DrawerPresenter @Inject //NOSONAR
constructor( //NOSONAR
    private val application: ShuttleApplication, //NOSONAR
    private val navigationEventRelay: NavigationEventRelay, //NOSONAR
    private val songsRepository: Repository.SongsRepository, //NOSONAR
    private val playlistsRepository: Repository.PlaylistsRepository, //NOSONAR
    private val settingsManager: SettingsManager, //NOSONAR
    private val playlistMenuPresenter: PlaylistMenuPresenter //NOSONAR
) : PurchasePresenter<DrawerView>(), //NOSONAR
    PlaylistMenuContract.Presenter by playlistMenuPresenter { //NOSONAR

    override fun bindView(view: DrawerView) { //NOSONAR
        super.bindView(view) //NOSONAR

        playlistMenuPresenter.bindView(view) //NOSONAR

        loadData(view) //NOSONAR

        addDisposable(navigationEventRelay.events //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe { drawerEvent -> //NOSONAR
                val drawerView = getView() //NOSONAR
                when (drawerEvent.type) { //NOSONAR
                    NavigationEventRelay.NavigationEvent.Type.LIBRARY_SELECTED -> drawerView?.setDrawerItemSelected(DrawerParent.Type.LIBRARY) //NOSONAR
                    NavigationEventRelay.NavigationEvent.Type.FOLDERS_SELECTED -> if (drawerView != null) { //NOSONAR
                        if (ShuttleUtils.isUpgraded(application, settingsManager)) { //NOSONAR
                            drawerView.setDrawerItemSelected(DrawerParent.Type.FOLDERS) //NOSONAR
                        } else { //NOSONAR
                            upgradeClicked() //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            }) // NOSONAR
    } // NOSONAR

    override fun unbindView(view: DrawerView) { //NOSONAR
        super.unbindView(view) //NOSONAR

        playlistMenuPresenter.unbindView(view) //NOSONAR
    } // NOSONAR

    private fun loadData(drawerView: DrawerView) { //NOSONAR
        PermissionUtils.RequestStoragePermissions { //NOSONAR
            addDisposable(playlistsRepository.getAllPlaylists(songsRepository) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                // Delay the subscription so we're not querying data while the app is launching // NOSONAR
                .delaySubscription(Observable.timer(1500, TimeUnit.MILLISECONDS)) //NOSONAR
                // after all, clear all playlist item // NOSONAR
                // to avoid memory leak in static var DrawerParent.playlistsParent // NOSONAR
                .doFinally { drawerView.setPlaylistItems(emptyList()) } //NOSONAR
                .subscribe( //NOSONAR
                    { drawerView.setPlaylistItems(it) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Error refreshing DrawerFragment adapter items", error) } //NOSONAR
                )) // NOSONAR
        } // NOSONAR
    } // NOSONAR

    internal fun onDrawerItemClicked(drawerParent: DrawerParent) { //NOSONAR
        val drawerView = view //NOSONAR
        if (drawerView != null && drawerParent.isSelectable) { //NOSONAR
            drawerView.setDrawerItemSelected(drawerParent.type) //NOSONAR
        } // NOSONAR

        closeDrawer() //NOSONAR

        if (drawerParent.navigationEvent != null) { //NOSONAR
            navigationEventRelay.sendEvent(drawerParent.navigationEvent!!) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun closeDrawer() { //NOSONAR
        val drawerView = view //NOSONAR
        drawerView?.closeDrawer() //NOSONAR
    } // NOSONAR

    fun onPlaylistClicked(playlist: Playlist) { //NOSONAR
        closeDrawer() //NOSONAR
        navigationEventRelay.sendEvent(NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.PLAYLIST_SELECTED, playlist)) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "DrawerPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
