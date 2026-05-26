@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data // NOSONAR

import android.content.ContentUris // NOSONAR
import android.content.Context // NOSONAR
import android.provider.MediaStore // NOSONAR
import android.util.Log // NOSONAR
import com.jakewharton.rxrelay2.BehaviorRelay // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Playlist.Type // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.functions.BiFunction // NOSONAR
import io.reactivex.functions.Consumer // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Singleton // NOSONAR

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
            ) // NOSONAR
                .subscribe( //NOSONAR
                    playlistsRelay, //NOSONAR
                    Consumer { error -> LogUtils.logException(TAG, "Failed to get playlists", error) } //NOSONAR
                ) // NOSONAR
        } // NOSONAR
        return playlistsRelay.subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR

    override fun getAllPlaylists(songsRepository: SongsRepository): Observable<MutableList<Playlist>> { //NOSONAR
        val defaultPlaylistsObservable = Observable.fromCallable<List<Playlist>> { //NOSONAR
            val playlists = mutableListOf<Playlist>() //NOSONAR

            // To do later: Hide Podcasts if there are no songs // NOSONAR
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
            }) // NOSONAR
            .concatMap { playlists -> //NOSONAR
                Observable.fromIterable<Playlist?>(playlists) //NOSONAR
                    .concatMap<Playlist> { playlist -> //NOSONAR
                        songsRepository.getSongs(playlist) //NOSONAR
                            .first(emptyList()) //NOSONAR
                            .flatMapObservable { songs -> //NOSONAR
                                if (playlist.type != Type.USER_CREATED && playlist.type != Type.FAVORITES && songs.isEmpty() //NOSONAR
                                ) { // NOSONAR
                                    Observable.empty() //NOSONAR
                                } else { //NOSONAR
                                    Observable.just(playlist) //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                    } // NOSONAR
                    .toList() //NOSONAR
                    .toObservable() //NOSONAR
            } // NOSONAR

    } // NOSONAR

    override fun deletePlaylist(playlist: Playlist) { //NOSONAR
        if (!playlist.canDelete) { //NOSONAR
            Log.e(TAG, "Playlist cannot be deleted") //NOSONAR
            return //NOSONAR
        } // NOSONAR

        ContentUris.withAppendedId(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, playlist.id)?.let { uri -> //NOSONAR
            context.contentResolver.delete(uri, null, null) //NOSONAR
        } // NOSONAR
    } // NOSONAR

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
        ) // NOSONAR
    } // NOSONAR

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
        ) // NOSONAR
    } // NOSONAR

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
        ) // NOSONAR
    } // NOSONAR

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
        ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "PlaylistsRepository" //NOSONAR
    } // NOSONAR

} // NOSONAR
