@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.shortcut

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.simplecity.amp_library.playback.MusicService
import com.simplecity.amp_library.playback.constants.ShortcutCommands
import com.simplecity.amp_library.ui.screens.main.MainActivity
import com.simplecity.amp_library.utils.AnalyticsManager
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.ResumingServiceManager
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShortcutTrampolineActivity : AppCompatActivity() { //NOSONAR

    @Inject lateinit var favoritesPlaylistManager: FavoritesPlaylistManager //NOSONAR

    @Inject lateinit var analyticsManager: AnalyticsManager //NOSONAR

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        AndroidInjection.inject(this) //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        val action = intent.action //NOSONAR
        when (action) { //NOSONAR
            ShortcutCommands.PLAY, ShortcutCommands.SHUFFLE_ALL -> { //NOSONAR
                val intent = Intent(this, MusicService::class.java) //NOSONAR
                intent.action = action //NOSONAR
                ResumingServiceManager(lifecycle, analyticsManager).startService(this, intent, null) //NOSONAR
                finish() //NOSONAR
            }
            ShortcutCommands.FOLDERS -> { //NOSONAR
                intent = Intent(this, MainActivity::class.java) //NOSONAR
                intent.action = action //NOSONAR
                startActivity(intent) //NOSONAR
                finish() //NOSONAR
            }
            ShortcutCommands.PLAYLIST -> { //NOSONAR
                intent = Intent(this, MainActivity::class.java) //NOSONAR
                intent.action = action //NOSONAR
                favoritesPlaylistManager.getFavoritesPlaylist() //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                                { playlist -> //NOSONAR
                                    intent.putExtra(PlaylistManager.ARG_PLAYLIST, playlist) //NOSONAR
                                    startActivity(intent) //NOSONAR
                                    finish() //NOSONAR
                                },
                                { error -> LogUtils.logException(TAG, "Error starting activity", error) } //NOSONAR
                        )
            }
        }
    }

    companion object { //NOSONAR
        private const val TAG = "ShortcutTrampolineActiv" //NOSONAR
    }
}
