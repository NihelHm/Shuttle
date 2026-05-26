@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.playlists

import android.content.Intent
import android.view.SubMenu
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.PlaylistsRepository
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlaylistMenuHelper @Inject constructor( //NOSONAR
    private val playlistsRepository: PlaylistsRepository //NOSONAR
) {

    fun createPlaylistMenu(subMenu: SubMenu): Disposable { //NOSONAR
        return createPlaylistMenu(subMenu, false) //NOSONAR
            .subscribe( //NOSONAR
                {
                    // Intentionally left empty.
                },
                { throwable -> LogUtils.logException(TAG, "createPlaylistMenu error", throwable) } //NOSONAR
            )
    }

    fun createUpdatingPlaylistMenu(subMenu: SubMenu): Completable { //NOSONAR
        return createPlaylistMenu(subMenu, true) //NOSONAR
    }

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
                }
            }
            .ignoreElements() //NOSONAR
            .doOnError { throwable -> LogUtils.logException(TAG, "createUpdatingPlaylistMenu failed", throwable) } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "PlaylistMenuHelper" //NOSONAR
    }
}
