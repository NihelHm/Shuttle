@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.playlists // NOSONAR

import android.content.Intent // NOSONAR
import android.view.SubMenu // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.PlaylistsRepository // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import io.reactivex.Completable // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR

class PlaylistMenuHelper @Inject constructor( //NOSONAR
    private val playlistsRepository: PlaylistsRepository //NOSONAR
) { // NOSONAR

    fun createPlaylistMenu(subMenu: SubMenu): Disposable { //NOSONAR
        return createPlaylistMenu(subMenu, false) //NOSONAR
            .subscribe( //NOSONAR
                { // NOSONAR
                    // Intentionally left empty. // NOSONAR
                }, // NOSONAR
                { throwable -> LogUtils.logException(TAG, "createPlaylistMenu error", throwable) } //NOSONAR
            ) // NOSONAR
    } // NOSONAR

    fun createUpdatingPlaylistMenu(subMenu: SubMenu): Completable { //NOSONAR
        return createPlaylistMenu(subMenu, true) //NOSONAR
    } // NOSONAR

    fun createPlaylistMenu(subMenu: SubMenu, autoUpdate: Boolean): Completable { //NOSONAR
        return playlistsRepository.getPlaylists() //NOSONAR
            .take(if (autoUpdate) java.lang.Long.MAX_VALUE else 1) //NOSONAR
            .doOnNext { playlists -> //NOSONAR
                subMenu.clear() //NOSONAR
                subMenu.add(0, MediaManager.Defs.NEW_PLAYLIST, 0, R.string.new_playlist) //NOSONAR
                for (playlist in playlists) { //NOSONAR
                    val intent = Intent() //NOSONAR
                    intent.putExtra(PlaylistManager.ARG_PLAYLIST, playlist) //NOSONAR
                    subMenu.add(0, MediaManager.Defs.PLAYLIST_SELECTED, 0, playlist.name).intent = intent //NOSONAR
                } // NOSONAR
            } // NOSONAR
            .ignoreElements() //NOSONAR
            .doOnError { throwable -> LogUtils.logException(TAG, "createUpdatingPlaylistMenu failed", throwable) } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "PlaylistMenuHelper" //NOSONAR
    } // NOSONAR
} // NOSONAR
