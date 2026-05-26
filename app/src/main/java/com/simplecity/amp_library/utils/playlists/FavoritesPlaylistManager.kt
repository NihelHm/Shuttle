@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.playlists // NOSONAR

import android.content.ContentValues // NOSONAR
import android.content.Context // NOSONAR
import android.provider.MediaStore // NOSONAR
import android.support.v4.util.Pair // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.PlaylistsRepository // NOSONAR
import com.simplecity.amp_library.data.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Playlist.Type // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import io.reactivex.Completable // NOSONAR
import io.reactivex.Maybe // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.functions.BiFunction // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import java.util.Collections // NOSONAR
import java.util.concurrent.TimeUnit // NOSONAR
import javax.inject.Inject // NOSONAR

class FavoritesPlaylistManager @Inject constructor( //NOSONAR
    private val applicationContext: Context, //NOSONAR
    private val playlistManager: PlaylistManager, //NOSONAR
    private val playlistsRepository: PlaylistsRepository, //NOSONAR
    private val songsRepository: SongsRepository //NOSONAR
) { // NOSONAR

    fun getFavoritesPlaylist(): Single<Playlist?> { //NOSONAR
        return playlistsRepository.getPlaylists() //NOSONAR
            .first(Collections.emptyList()) //NOSONAR
            .flatMapObservable { playlists -> Observable.fromIterable(playlists) } //NOSONAR
            .filter { playlist -> playlist.type == Type.FAVORITES } //NOSONAR
            .switchIfEmpty(Maybe.fromCallable { createFavoritePlaylist() }.toObservable()) //NOSONAR
            .firstOrError() //NOSONAR
            .doOnError { throwable -> LogUtils.logException(TAG, "getFavoritesPlaylist failed", throwable) } //NOSONAR
    } // NOSONAR

    fun isFavorite(song: Song?): Observable<Boolean> { //NOSONAR
        return if (song == null) { //NOSONAR
            Observable.just(false) //NOSONAR
        } else getFavoritesPlaylist().flatMapObservable { playlist -> songsRepository.getSongs(playlist) } //NOSONAR
            .map { songs -> songs.contains(song) } //NOSONAR

    } // NOSONAR

    fun createFavoritePlaylist(): Playlist? { //NOSONAR
        val playlist = playlistManager.createPlaylist(applicationContext.getString(R.string.fav_title)) //NOSONAR
        if (playlist != null) { //NOSONAR
            playlist.canDelete = false //NOSONAR
            playlist.canRename = false //NOSONAR
            playlist.type = Playlist.Type.FAVORITES //NOSONAR
        } // NOSONAR
        return playlist //NOSONAR
    } // NOSONAR

    fun clearFavorites(): Disposable { //NOSONAR
        return getFavoritesPlaylist() //NOSONAR
            .flatMapCompletable { playlist -> //NOSONAR
                Completable.fromAction { //NOSONAR
                    val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.id) //NOSONAR
                    applicationContext.contentResolver.delete(uri, null, null) //NOSONAR
                } // NOSONAR
            } // NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .subscribe( //NOSONAR
                { // NOSONAR
                    // Intentionally left empty. // NOSONAR
                }, // NOSONAR
                { throwable -> LogUtils.logException(TAG, "clearFavorites error", throwable) } //NOSONAR
            ) // NOSONAR
    } // NOSONAR

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
                            } // NOSONAR
                        } // NOSONAR
                    } else { //NOSONAR
                        removeFromFavorites(song) { success -> //NOSONAR
                            if (success) { //NOSONAR
                                isFavorite.invoke(false) //NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                }, // NOSONAR
                { error -> LogUtils.logException(TAG, "PlaylistManager: Error toggling favorites", error) } //NOSONAR
            ) // NOSONAR
    } // NOSONAR

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
            } // NOSONAR
            .delay(150, TimeUnit.MILLISECONDS) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { success.invoke(it) }, //NOSONAR
                { throwable -> LogUtils.logException(TAG, "Error adding to playlist", throwable) } //NOSONAR
            ) // NOSONAR
    } // NOSONAR

    fun removeFromFavorites(song: Song, callback: (Boolean) -> Unit): Disposable { //NOSONAR
        return getFavoritesPlaylist() //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { playlist -> playlist?.let { playlistManager.removeFromPlaylist(it, song, callback) } }, //NOSONAR
                { error -> LogUtils.logException(TAG, "PlaylistManager: Error Removing from favorites", error) } //NOSONAR
            ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private val TAG = "FavoritesPlaylistManage" //NOSONAR
    } // NOSONAR
} // NOSONAR
