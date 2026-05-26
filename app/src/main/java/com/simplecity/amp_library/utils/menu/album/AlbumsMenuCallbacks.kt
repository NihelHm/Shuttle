@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.album

import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.Playlist
import io.reactivex.Single

interface AlbumsMenuCallbacks { //NOSONAR

    fun createPlaylistFromAlbums(albums: List<Album>) //NOSONAR

    fun addAlbumsToPlaylist(playlist: Playlist, albums: List<Album>) //NOSONAR

    fun addAlbumsToQueue(albums: List<Album>) //NOSONAR

    fun playAlbumsNext(albums: List<Album>) //NOSONAR

    fun play(album: Album) //NOSONAR

    fun editTags(album: Album) //NOSONAR

    fun albumInfo(album: Album) //NOSONAR

    fun editArtwork(album: Album) //NOSONAR

    fun blacklistAlbums(albums: List<Album>) //NOSONAR

    fun deleteAlbums(albums: List<Album>) //NOSONAR

    fun goToArtist(album: Album) //NOSONAR

    fun <T> transform(src: Single<List<T>>, dst: (List<T>) -> Unit) //NOSONAR
}

fun AlbumsMenuCallbacks.createPlaylistFromAlbums(albums: Single<List<Album>>) { //NOSONAR
    transform(albums) { albums -> createPlaylistFromAlbums(albums) } //NOSONAR
}

fun AlbumsMenuCallbacks.addAlbumsToPlaylist(playlist: Playlist, albums: Single<List<Album>>) { //NOSONAR
    transform(albums) { albums -> addAlbumsToPlaylist(playlist, albums) } //NOSONAR
}

fun AlbumsMenuCallbacks.playAlbumsNext(albums: Single<List<Album>>) { //NOSONAR
    transform(albums) { albums -> playAlbumsNext(albums) } //NOSONAR
}

fun AlbumsMenuCallbacks.addAlbumsToQueue(albums: Single<List<Album>>) { //NOSONAR
    transform(albums) { albums -> addAlbumsToQueue(albums) } //NOSONAR
}

fun AlbumsMenuCallbacks.deleteAlbums(albums: Single<List<Album>>) { //NOSONAR
    transform(albums) { albums -> deleteAlbums(albums) } //NOSONAR
}

fun AlbumsMenuCallbacks.playAlbumsNext(album: Album) { //NOSONAR
    playAlbumsNext(listOf(album)) //NOSONAR
}

fun AlbumsMenuCallbacks.createPlaylistFromAlbums(album: Album) { //NOSONAR
    createPlaylistFromAlbums(listOf(album)) //NOSONAR
}

fun AlbumsMenuCallbacks.addAlbumsToPlaylist(playlist: Playlist, album: Album) { //NOSONAR
    addAlbumsToPlaylist(playlist, listOf(album)) //NOSONAR
}

fun AlbumsMenuCallbacks.addAlbumsToQueue(album: Album) { //NOSONAR
    addAlbumsToQueue(listOf(album)) //NOSONAR
}

fun AlbumsMenuCallbacks.blacklistAlbums(album: Album) { //NOSONAR
    blacklistAlbums(listOf(album)) //NOSONAR
}

fun AlbumsMenuCallbacks.deleteAlbums(album: Album) { //NOSONAR
    deleteAlbums(listOf(album)) //NOSONAR
}
