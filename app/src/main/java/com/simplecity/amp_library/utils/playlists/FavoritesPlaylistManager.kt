@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.playlists

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.support.v4.util.Pair
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.PlaylistsRepository
import com.simplecity.amp_library.data.SongsRepository
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Playlist.Type
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FavoritesPlaylistManager @Inject constructor( //NOSONAR
    private val applicationContext: Context, //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val playlistsRepository: PlaylistsRepository, //NOSONAR
    private val songsRepository: SongsRepository //NOSONAR
) {

    fun getFavoritesPlaylist(): Single<Playlist?> { //NOSONAR
        return playlistsRepository.getPlaylists() //NOSONAR
            .first(Collections.emptyList()) //NOSONAR
            .flatMapObservable { playlists -> Observable.fromIterable(playlists) } //NOSONAR
            .filter { playlist -> playlist.type == Type.FAVORITES } //NOSONAR
            .switchIfEmpty(Maybe.fromCallable { createFavoritePlaylist() }.toObservable()) //NOSONAR
            .firstOrError() //NOSONAR
            .doOnError { throwable -> LogUtils.logException(TAG, "getFavoritesPlaylist failed", throwable) } //NOSONAR
    }

    fun isFavorite(song: Song?): Observable<Boolean> { //NOSONAR
        return if (song == null) { //NOSONAR
            Observable.just(false) //NOSONAR
        } else getFavoritesPlaylist().flatMapObservable { playlist -> songsRepository.getSongs(playlist) } //NOSONAR
            .map { songs -> songs.contains(song) } //NOSONAR

    }

    fun createFavoritePlaylist(): Playlist? { //NOSONAR
        val playlist = playlistManager.createPlaylist(applicationContext.getString(R.string.fav_title)) //NOSONAR
        if (playlist != null) { //NOSONAR
            playlist.canDelete = false //NOSONAR
            playlist.canRename = false //NOSONAR
            playlist.type = Playlist.Type.FAVORITES //NOSONAR
        }
        return playlist //NOSONAR
    }

    fun clearFavorites(): Disposable { //NOSONAR
        return getFavoritesPlaylist() //NOSONAR
            .flatMapCompletable { playlist -> //NOSONAR
                Completable.fromAction { //NOSONAR
                    val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.id) //NOSONAR
                    applicationContext.contentResolver.delete(uri, null, null) //NOSONAR
                }
            }
            .subscribeOn(Schedulers.io()) //NOSONAR
            .subscribe( //NOSONAR
                {
                    // Intentionally left empty.
                },
                { throwable -> LogUtils.logException(TAG, "clearFavorites error", throwable) } //NOSONAR
            )
    }

    fun toggleFavorite(song: Song, isFavorite: (Boolean) -> Unit): Disposable { //NOSONAR
        return isFavorite(song) //NOSONAR
            .first(false) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .subscribe( //NOSONAR
                { favorite -> //NOSONAR
                    if (!favorite) { //NOSONAR
                        addToFavorites(song) { success -> //NOSONAR
                            if (success) { //NOSONAR
                                isFavorite.invoke(true) //NOSONAR
                            }
                        }
                    } else { //NOSONAR
                        removeFromFavorites(song) { success -> //NOSONAR
                            if (success) { //NOSONAR
                                isFavorite.invoke(false) //NOSONAR
                            }
                        }
                    }
                },
                { error -> LogUtils.logException(TAG, "PlaylistManager: Error toggling favorites", error) } //NOSONAR
            )
    }

    fun addToFavorites(song: Song, success: (Boolean) -> Unit): Disposable { //NOSONAR
        return Single.zip<Playlist, Int, Pair<Playlist, Int>>( //NOSONAR
            getFavoritesPlaylist(), //NOSONAR
            getFavoritesPlaylist().flatMapObservable<List<Song>> { songsRepository.getSongs(it) } //NOSONAR
                .first(emptyList()) //NOSONAR
                .map { it.size }, //NOSONAR
            BiFunction { first, second -> Pair(first, second) }) //NOSONAR
            .map { pair -> //NOSONAR
                val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", pair.first!!.id) //NOSONAR
                val values = ContentValues() //NOSONAR
                values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song.id) //NOSONAR
                values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, pair.second!! + 1) //NOSONAR
                val newUri = applicationContext.contentResolver.insert(uri, values) //NOSONAR
                applicationContext.contentResolver.notifyChange(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, null) //NOSONAR
                newUri != null //NOSONAR
            }
            .delay(150, TimeUnit.MILLISECONDS) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { success.invoke(it) }, //NOSONAR
                { throwable -> LogUtils.logException(TAG, "Error adding to playlist", throwable) } //NOSONAR
            )
    }

    fun removeFromFavorites(song: Song, callback: (Boolean) -> Unit): Disposable { //NOSONAR
        return getFavoritesPlaylist() //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { playlist -> playlist?.let { playlistManager.removeFromPlaylist(it, song, callback) } }, //NOSONAR
                { error -> LogUtils.logException(TAG, "PlaylistManager: Error Removing from favorites", error) } //NOSONAR
            )
    }

    companion object { //NOSONAR

        private val TAG = "FavoritesPlaylistManage" //NOSONAR
    }
}
