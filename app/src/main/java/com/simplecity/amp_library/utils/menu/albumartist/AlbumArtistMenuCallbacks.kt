@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.albumartist

import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Playlist
import io.reactivex.Single

interface AlbumArtistMenuCallbacks { //NOSONAR

    fun createArtistsPlaylist(albumArtists: List<AlbumArtist>) //NOSONAR

    fun addArtistsToPlaylist(playlist: Playlist, albumArtists: List<AlbumArtist>) //NOSONAR

    fun addArtistsToQueue(albumArtists: List<AlbumArtist>) //NOSONAR

    fun playArtistsNext(albumArtists: List<AlbumArtist>) //NOSONAR

    fun play(albumArtist: AlbumArtist) //NOSONAR

    fun editTags(albumArtist: AlbumArtist) //NOSONAR

    fun albumArtistInfo(albumArtist: AlbumArtist) //NOSONAR

    fun editArtwork(albumArtist: AlbumArtist) //NOSONAR

    fun blacklistArtists(albumArtists: List<AlbumArtist>) //NOSONAR

    fun deleteArtists(albumArtists: List<AlbumArtist>) //NOSONAR

    fun goToArtist(albumArtist: AlbumArtist) //NOSONAR

    fun albumShuffle(albumArtist: AlbumArtist) //NOSONAR

    fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) //NOSONAR
}

fun AlbumArtistMenuCallbacks.createArtistsPlaylist(albumArtists: Single<List<AlbumArtist>>) { //NOSONAR
    transform(albumArtists) { albumArtists -> createArtistsPlaylist(albumArtists) } //NOSONAR
}

fun AlbumArtistMenuCallbacks.addArtistsToPlaylist(playlist: Playlist, albumArtists: Single<List<AlbumArtist>>) { //NOSONAR
    transform(albumArtists) { albumArtists -> addArtistsToPlaylist(playlist, albumArtists) } //NOSONAR
}

fun AlbumArtistMenuCallbacks.playArtistsNext(albumArtists: Single<List<AlbumArtist>>) { //NOSONAR
    transform(albumArtists) { albumArtists -> playArtistsNext(albumArtists) } //NOSONAR
}

fun AlbumArtistMenuCallbacks.addArtistsToQueue(albumArtists: Single<List<AlbumArtist>>) { //NOSONAR
    transform(albumArtists) { albumArtists -> addArtistsToQueue(albumArtists) } //NOSONAR
}

fun AlbumArtistMenuCallbacks.deleteArtists(albumArtists: Single<List<AlbumArtist>>) { //NOSONAR
    transform(albumArtists) { albumArtists -> deleteArtists(albumArtists) } //NOSONAR
}

fun AlbumArtistMenuCallbacks.playArtistsNext(albumArtist: AlbumArtist) { //NOSONAR
    playArtistsNext(listOf(albumArtist)) //NOSONAR
}

fun AlbumArtistMenuCallbacks.createArtistsPlaylist(albumArtist: AlbumArtist) { //NOSONAR
    createArtistsPlaylist(listOf(albumArtist)) //NOSONAR
}

fun AlbumArtistMenuCallbacks.addArtistsToPlaylist(playlist: Playlist, albumArtist: AlbumArtist) { //NOSONAR
    addArtistsToPlaylist(playlist, listOf(albumArtist)) //NOSONAR
}

fun AlbumArtistMenuCallbacks.addArtistsToQueue(albumArtist: AlbumArtist) { //NOSONAR
    addArtistsToQueue(listOf(albumArtist)) //NOSONAR
}

fun AlbumArtistMenuCallbacks.blacklistArtists(albumArtist: AlbumArtist) { //NOSONAR
    blacklistArtists(listOf(albumArtist)) //NOSONAR
}

fun AlbumArtistMenuCallbacks.deleteArtists(albumArtist: AlbumArtist) { //NOSONAR
    deleteArtists(listOf(albumArtist)) //NOSONAR
}

fun AlbumArtistMenuCallbacks.albumShuffle(albumArtist: AlbumArtist) { //NOSONAR
    albumShuffle(albumArtist) //NOSONAR
}
