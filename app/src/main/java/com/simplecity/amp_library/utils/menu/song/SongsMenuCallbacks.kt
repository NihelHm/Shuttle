@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.song

import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import io.reactivex.Single

interface SongsMenuCallbacks { //NOSONAR

    fun createPlaylist(songs: List<Song>) //NOSONAR

    fun addToPlaylist(playlist: Playlist, songs: List<Song>) //NOSONAR

    fun addToQueue(songs: List<Song>) //NOSONAR

    fun playNext(songs: List<Song>) //NOSONAR

    fun blacklist(songs: List<Song>) //NOSONAR

    fun delete(songs: List<Song>) //NOSONAR

    fun songInfo(song: Song) //NOSONAR

    fun setRingtone(song: Song) //NOSONAR

    fun share(song: Song) //NOSONAR

    fun editTags(song: Song) //NOSONAR

    fun goToArtist(song: Song) //NOSONAR

    fun goToAlbum(song: Song) //NOSONAR

    fun goToGenre(song: Song) //NOSONAR

    fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) //NOSONAR
}


fun SongsMenuCallbacks.createPlaylist(song: Song) { //NOSONAR
    createPlaylist(listOf(song)) //NOSONAR
}

fun SongsMenuCallbacks.addToPlaylist(playlist: Playlist, song: Song) { //NOSONAR
    addToPlaylist(playlist, listOf(song)) //NOSONAR
}

fun SongsMenuCallbacks.addToQueue(song: Song) { //NOSONAR
    addToQueue(listOf(song)) //NOSONAR
}

fun SongsMenuCallbacks.playNext(song: Song) { //NOSONAR
    playNext(listOf(song)) //NOSONAR
}

fun SongsMenuCallbacks.blacklist(song: Song) { //NOSONAR
    blacklist(listOf(song)) //NOSONAR
}

fun SongsMenuCallbacks.delete(song: Song) { //NOSONAR
    delete(listOf(song)) //NOSONAR
}


fun SongsMenuCallbacks.createPlaylist(songs: Single<List<Song>>) { //NOSONAR
    transform(songs) { createPlaylist(songs) } //NOSONAR
}

fun SongsMenuCallbacks.addToPlaylist(playlist: Playlist, songs: Single<List<Song>>) { //NOSONAR
    transform(songs) { songs -> addToPlaylist(playlist, songs) } //NOSONAR
}

fun SongsMenuCallbacks.addToQueue(songs: Single<List<Song>>) { //NOSONAR
    transform(songs) { songs -> addToQueue(songs) } //NOSONAR
}

fun SongsMenuCallbacks.playNext(songs: Single<List<Song>>) { //NOSONAR
    transform(songs) { songs -> playNext(songs) } //NOSONAR
}

fun SongsMenuCallbacks.blacklist(songs: Single<List<Song>>) { //NOSONAR
    transform(songs) { songs -> blacklist(songs) } //NOSONAR
}

fun SongsMenuCallbacks.delete(songs: Single<List<Song>>) { //NOSONAR
    transform(songs) { songs -> delete(songs) } //NOSONAR
}
