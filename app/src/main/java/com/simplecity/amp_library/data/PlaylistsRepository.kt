@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Playlist.Type
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //NOSONAR
class PlaylistsRepository @Inject constructor( //NOSONAR
    private val context: Context //NOSONAR
) : Repository.PlaylistsRepository { //NOSONAR

    private var playlistsSubscription: Disposable? = null //NOSONAR
    private val playlistsRelay = BehaviorRelay.create<List<Playlist>>() //NOSONAR

    override fun getPlaylists(): Observable<List<Playlist>> { //NOSONAR
        if (playlistsSubscription == null || playlistsSubscription?.isDisposed == true) { //NOSONAR
            playlistsSubscription = SqlBriteUtils.createObservableList( //NOSONAR
                context, //NOSONAR
                { cursor -> Playlist(context, cursor) }, //NOSONAR
                Playlist.getQuery() //NOSONAR
            )
                .subscribe( //NOSONAR
                    playlistsRelay, //NOSONAR
                    Consumer { error -> LogUtils.logException(TAG, "Failed to get playlists", error) } //NOSONAR
                )
        }
        return playlistsRelay.subscribeOn(Schedulers.io()) //NOSONAR
    }

    override fun getAllPlaylists(songsRepository: SongsRepository): Observable<MutableList<Playlist>> { //NOSONAR
        val defaultPlaylistsObservable = Observable.fromCallable<List<Playlist>> { //NOSONAR
            val playlists = mutableListOf<Playlist>() //NOSONAR

            // To do later: Hide Podcasts if there are no songs
            playlists.add(getPodcastPlaylist()) //NOSONAR
            playlists.add(getRecentlyAddedPlaylist()) //NOSONAR
            playlists.add(getMostPlayedPlaylist()) //NOSONAR

            playlists //NOSONAR
        }.subscribeOn(Schedulers.io()) //NOSONAR

        val playlistsObservable = getPlaylists() //NOSONAR

        return Observable.combineLatest<List<Playlist>, List<Playlist>, MutableList<Playlist>>( //NOSONAR
            defaultPlaylistsObservable, playlistsObservable, BiFunction { defaultPlaylists: List<Playlist>, playlists1: List<Playlist> -> //NOSONAR
                val list = mutableListOf<Playlist>() //NOSONAR
                list.addAll(defaultPlaylists) //NOSONAR
                list.addAll(playlists1) //NOSONAR
                list //NOSONAR
            })
            .concatMap { playlists -> //NOSONAR
                Observable.fromIterable<Playlist?>(playlists) //NOSONAR
                    .concatMap<Playlist> { playlist -> //NOSONAR
                        songsRepository.getSongs(playlist) //NOSONAR
                            .first(emptyList()) //NOSONAR
                            .flatMapObservable { songs -> //NOSONAR
                                if (playlist.type != Type.USER_CREATED && playlist.type != Type.FAVORITES && songs.isEmpty() //NOSONAR
                                ) {
                                    Observable.empty() //NOSONAR
                                } else { //NOSONAR
                                    Observable.just(playlist) //NOSONAR
                                }
                            }
                    }
                    .toList() //NOSONAR
                    .toObservable() //NOSONAR
            }

    }

    override fun deletePlaylist(playlist: Playlist) { //NOSONAR
        if (!playlist.canDelete) { //NOSONAR
            Log.e(TAG, "Playlist cannot be deleted") //NOSONAR
            return //NOSONAR
        }

        ContentUris.withAppendedId(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, playlist.id)?.let { uri -> //NOSONAR
            context.contentResolver.delete(uri, null, null) //NOSONAR
        }
    }

    override fun getPodcastPlaylist(): Playlist { //NOSONAR
        return Playlist( //NOSONAR
            Type.PODCAST, //NOSONAR
            PlaylistManager.PlaylistIds.PODCASTS_PLAYLIST, //NOSONAR
            context.getString(R.string.podcasts_title), //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false //NOSONAR
        )
    }

    override fun getRecentlyAddedPlaylist(): Playlist { //NOSONAR
        return Playlist( //NOSONAR
            Type.RECENTLY_ADDED, //NOSONAR
            PlaylistManager.PlaylistIds.RECENTLY_ADDED_PLAYLIST, //NOSONAR
            context.getString(R.string.recentlyadded), //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false //NOSONAR
        )
    }

    override fun getMostPlayedPlaylist(): Playlist { //NOSONAR
        return Playlist( //NOSONAR
            Type.MOST_PLAYED, //NOSONAR
            PlaylistManager.PlaylistIds.MOST_PLAYED_PLAYLIST, //NOSONAR
            context.getString(R.string.mostplayed), //NOSONAR
            false, //NOSONAR
            true, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false //NOSONAR
        )
    }

    override fun getRecentlyPlayedPlaylist(): Playlist { //NOSONAR
        return Playlist( //NOSONAR
            Type.RECENTLY_PLAYED, //NOSONAR
            PlaylistManager.PlaylistIds.RECENTLY_PLAYED_PLAYLIST, //NOSONAR
            context.getString(R.string.suggested_recent_title), //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false, //NOSONAR
            false //NOSONAR
        )
    }

    companion object { //NOSONAR
        const val TAG = "PlaylistsRepository" //NOSONAR
    }

}
