@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.androidauto

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat.Builder
import android.util.Log
import com.simplecity.amp_library.R.string
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.data.Repository.PlaylistsRepository
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.StringUtils
import com.simplecity.amp_library.utils.extensions.getSongsObservable
import com.simplecity.amp_library.utils.extensions.getSongsSingle
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

sealed class MediaIdWrapper { //NOSONAR

    object RootDirectory : MediaIdWrapper() //NOSONAR

    object ArtistDirectory : MediaIdWrapper() //NOSONAR

    class AlbumDirectory(var artistHash: Int?) : MediaIdWrapper() //NOSONAR

    class SongDirectory(var artistHash: String?, var albumId: Long?) : MediaIdWrapper() //NOSONAR

    object PlaylistDirectory : MediaIdWrapper() //NOSONAR

    object GenreDirectory : MediaIdWrapper() //NOSONAR

    class Song(var artistHash: String?, var albumId: Long?, var songId: Long?) : MediaIdWrapper() //NOSONAR

    class Genre(var genreId: Long) : MediaIdWrapper() //NOSONAR

    class Playlist(var playlistId: Long) : MediaIdWrapper() //NOSONAR
}

class MediaIdHelper( //NOSONAR
    private val application: ShuttleApplication, //NOSONAR
    private val songsRepository: Repository.SongsRepository, //NOSONAR
    private val albumsRepository: Repository.AlbumsRepository, //NOSONAR
    private val albumArtistsRepository: Repository.AlbumArtistsRepository, //NOSONAR
    private val genresRepository: Repository.GenresRepository, //NOSONAR
    private val playlistsRepository: PlaylistsRepository //NOSONAR
) {

    companion object { //NOSONAR
        const val TAG = "MediaIdHelper" //NOSONAR
    }

    @Throws(IllegalStateException::class) //NOSONAR
    private fun parseMediaId(mediaId: String): MediaIdWrapper { //NOSONAR

        val uri = Uri.parse(mediaId) //NOSONAR

        return when (uri.pathSegments.firstOrNull()) { //NOSONAR

            "root" -> { //NOSONAR
                MediaIdWrapper.RootDirectory //NOSONAR
            }
            else -> { //NOSONAR
                val artistHash = uri.pathSegments.getNextSegment("artists") //NOSONAR
                val albumId = uri.pathSegments.getNextSegment("albums")?.toLongOrNull() //NOSONAR
                val songId = uri.pathSegments.getNextSegment("songs")?.toLongOrNull() //NOSONAR
                val playlistId = uri.pathSegments.getNextSegment("playlists")?.toLongOrNull() //NOSONAR
                val genreId = uri.pathSegments.getNextSegment("genres")?.toLongOrNull() //NOSONAR

                if (uri.toString().endsWith('/')) { //NOSONAR
                    when { //NOSONAR
                        uri.pathSegments.contains("songs") -> MediaIdWrapper.SongDirectory(artistHash, albumId) //NOSONAR
                        uri.pathSegments.contains("albums") -> MediaIdWrapper.AlbumDirectory(artistHash?.toInt()) //NOSONAR
                        uri.pathSegments.contains("artists") -> MediaIdWrapper.ArtistDirectory //NOSONAR
                        uri.pathSegments.contains("playlists") -> MediaIdWrapper.PlaylistDirectory //NOSONAR
                        uri.pathSegments.contains("genres") -> MediaIdWrapper.GenreDirectory //NOSONAR
                        else -> { //NOSONAR
                            throw IllegalStateException("Unknown MediaId '$mediaId' path") //NOSONAR
                        }
                    }
                } else { //NOSONAR
                    when { //NOSONAR
                        playlistId != null -> MediaIdWrapper.Playlist(playlistId) //NOSONAR
                        genreId != null -> MediaIdWrapper.Genre(genreId) //NOSONAR
                        else -> MediaIdWrapper.Song(artistHash, albumId, songId) //NOSONAR
                    }
                }
            }
        }
    }

    private fun List<String>.getNextSegment(segmentName: String): String? { //NOSONAR
        val index = indexOf(segmentName) //NOSONAR
        if (index >= 0 && size > index + 1) { //NOSONAR
            return this[index + 1] //NOSONAR
        }
        return null //NOSONAR
    }

    fun getChildren(mediaId: String, result: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) { //NOSONAR

        val mediaIdWrapper: MediaIdWrapper? = try { //NOSONAR
            parseMediaId(mediaId) //NOSONAR
        } catch (e: IllegalStateException) { //NOSONAR
            Log.e(TAG, "Failed to parse media id: ${e.localizedMessage}") //NOSONAR
            null //NOSONAR
        }

        when (mediaIdWrapper) { //NOSONAR
            is MediaIdWrapper.RootDirectory -> result( //NOSONAR
                mutableListOf( //NOSONAR
                    MediaItem( //NOSONAR
                        Builder() //NOSONAR
                            .setTitle(application.getString(string.artists_title)) //NOSONAR
                            .setMediaId("media:/artists/") //NOSONAR
                            .build(), MediaItem.FLAG_BROWSABLE //NOSONAR
                    ),
                    MediaItem( //NOSONAR
                        Builder() //NOSONAR
                            .setTitle(application.getString(string.albums_title)) //NOSONAR
                            .setMediaId("media:/albums/") //NOSONAR
                            .build(), MediaItem.FLAG_BROWSABLE //NOSONAR
                    ), MediaItem( //NOSONAR
                        Builder() //NOSONAR
                            .setTitle(application.getString(string.playlists_title)) //NOSONAR
                            .setMediaId("media:/playlists/") //NOSONAR
                            .build(), MediaItem.FLAG_BROWSABLE //NOSONAR
                    ), MediaItem( //NOSONAR
                        Builder() //NOSONAR
                            .setTitle(application.getString(string.genres_title)) //NOSONAR
                            .setMediaId("media:/genres/") //NOSONAR
                            .build(), MediaItem.FLAG_BROWSABLE //NOSONAR
                    )
                )
            )
            is MediaIdWrapper.GenreDirectory -> listGenres(mediaId, result) //NOSONAR
            is MediaIdWrapper.PlaylistDirectory -> listPlaylists(mediaId, result) //NOSONAR
            is MediaIdWrapper.ArtistDirectory -> listArtists(mediaId, result) //NOSONAR
            is MediaIdWrapper.AlbumDirectory -> listAlbums(mediaId, mediaIdWrapper.artistHash, result) //NOSONAR
            is MediaIdWrapper.SongDirectory -> listSongs(mediaId, mediaIdWrapper.albumId, result) //NOSONAR
            else -> result(mutableListOf()) //NOSONAR
        }
    }

    fun getSongListForMediaId(mediaId: String, completion: (List<Song>, position: Int) -> Unit) { //NOSONAR
        val mediaWrapper = parseMediaId(mediaId) //NOSONAR
        when (mediaWrapper) { //NOSONAR
            is MediaIdWrapper.Song -> { //NOSONAR
                getSongsForPredicate { if (mediaWrapper.albumId == null) true else it.albumId == mediaWrapper.albumId } //NOSONAR
                    .map { songs -> //NOSONAR
                        songs //NOSONAR
                            .sortedBy { song -> song.albumArtistName } //NOSONAR
                            .sortedBy { song -> song.albumName } //NOSONAR
                            .sortedBy { song -> song.track } //NOSONAR
                            .sortedBy { song -> song.discNumber } //NOSONAR
                    }
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                        { songs -> completion(songs, songs.indexOfFirst { it.id == mediaWrapper.songId }.or(0)) }, //NOSONAR
                        { completion(mutableListOf(), 0) } //NOSONAR
                    )
            }
            is MediaIdWrapper.Playlist -> { //NOSONAR
                getSongsForPlaylistId(mediaWrapper.playlistId) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                        { songs -> completion(songs, 0) }, //NOSONAR
                        { completion(mutableListOf(), 0) } //NOSONAR
                    )
            }
            is MediaIdWrapper.Genre -> { //NOSONAR
                getSongsForGenreId(mediaWrapper.genreId) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                        { songs -> completion(songs, 0) }, //NOSONAR
                        { completion(mutableListOf(), 0) } //NOSONAR
                    )
            }
        }
    }

    fun handlePlayFromSearch(query: String, extras: Bundle): Single<Pair<List<Song>, Int>> { //NOSONAR
        val mediaFocus = extras.getString(MediaStore.EXTRA_MEDIA_FOCUS) //NOSONAR
        when (mediaFocus) { //NOSONAR
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> { //NOSONAR
                extras.getString(MediaStore.EXTRA_MEDIA_ARTIST)?.let { artist -> //NOSONAR
                    return getSongsForPredicate { song -> song.artistName.equals(artist, true) } //NOSONAR
                        .map { songs -> //NOSONAR
                            Pair(songs //NOSONAR
                                .sortedBy { song -> song.albumName } //NOSONAR
                                .sortedBy { song -> song.track } //NOSONAR
                                .sortedBy { song -> song.discNumber }, //NOSONAR
                                0 //NOSONAR
                            )
                        }
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> { //NOSONAR
                extras.getString(MediaStore.EXTRA_MEDIA_ALBUM)?.let { album -> //NOSONAR
                    return getSongsForPredicate { song -> song.albumName.equals(album, true) } //NOSONAR
                        .map { songs -> //NOSONAR
                            Pair(songs //NOSONAR
                                .sortedBy { song -> song.track } //NOSONAR
                                .sortedBy { song -> song.discNumber }, //NOSONAR
                                0 //NOSONAR
                            )
                        }
                }
            }
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> { //NOSONAR
                extras.getString(MediaStore.EXTRA_MEDIA_GENRE)?.let { genreName -> //NOSONAR
                    return genresRepository.getGenres() //NOSONAR
                        .first(emptyList()) //NOSONAR
                        .flatMap { genres -> Single.just(genres.first { genre -> genre.name == genreName }) } //NOSONAR
                        .flatMap { genresSingle -> genresSingle.getSongsObservable(application) } //NOSONAR
                        .map { songs -> //NOSONAR
                            Pair(songs.sortedBy { it.playlistSongPlayOrder }.toMutableList(), 0) //NOSONAR
                        }
                }
            }
        }

        return getSongsForPredicate { song -> song.name.contains(query, true) } //NOSONAR
            .flatMap { songs -> //NOSONAR
                if (songs.isEmpty()) { //NOSONAR
                    Single.just(Pair<Song?, List<Song>>(null, emptyList())) //NOSONAR
                } else { //NOSONAR
                    // Take the first song matching our predicate, and retrieve all songs from the same album.
                    val song = songs.first() //NOSONAR
                    song.album.getSongsSingle(songsRepository).map { albumSongs -> Pair(song, albumSongs) } //NOSONAR
                }
            }
            .map { pair -> //NOSONAR
                val songs = pair.second //NOSONAR
                    .sortedBy { song -> song.artistName } //NOSONAR
                    .sortedBy { song -> song.albumName } //NOSONAR
                    .sortedBy { song -> song.track } //NOSONAR
                    .sortedBy { song -> song.discNumber } //NOSONAR
                var index = 0 //NOSONAR
                pair.first?.let { song -> //NOSONAR
                    index = songs.indexOf(song) //NOSONAR
                }
                Pair(songs, index) //NOSONAR
            }
    }

    // DataManager helpers

    @SuppressLint("CheckResult") //NOSONAR
    private fun listArtists(mediaId: String, completion: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) { //NOSONAR
        albumArtistsRepository.getAlbumArtists().first(emptyList()) //NOSONAR
            .map { albumArtists -> //NOSONAR
                albumArtists //NOSONAR
                    .sortedBy { albumArtist -> StringUtils.keyFor(albumArtist.name) } //NOSONAR
                    .map { albumArtist -> albumArtist.toMediaItem(mediaId) } //NOSONAR
                    .toMutableList() //NOSONAR
            }
            .subscribe({ mediaItems -> completion(mediaItems) }, { //NOSONAR
                // Intentionally left empty.
            })
    }

    @SuppressLint("CheckResult") //NOSONAR
    private fun listPlaylists(mediaId: String, completion: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) { //NOSONAR
        playlistsRepository.getPlaylists().first(emptyList()) //NOSONAR
            .map { playlists -> //NOSONAR
                playlists //NOSONAR
                    .sortedBy { playlist -> playlist.type } //NOSONAR
                    .map { playlist -> playlist.toMediaItem(mediaId) } //NOSONAR
                    .toMutableList() //NOSONAR
            }
            .subscribe({ mediaItems -> completion(mediaItems) }, { //NOSONAR
                // Intentionally left empty.
            })
    }

    @SuppressLint("CheckResult") //NOSONAR
    private fun listGenres(mediaId: String, completion: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) { //NOSONAR
        genresRepository.getGenres().first(emptyList()) //NOSONAR
            .map { genres -> //NOSONAR
                genres //NOSONAR
                    .sortedBy { genre -> genre.name } //NOSONAR
                    .map { genre -> genre.toMediaItem(mediaId) } //NOSONAR
                    .toMutableList() //NOSONAR
            }
            .subscribe({ mediaItems -> completion(mediaItems) }, { //NOSONAR
                // Intentionally left empty.
            })
    }

    @SuppressLint("CheckResult") //NOSONAR
    private fun listAlbums(mediaId: String, artistHash: Int?, completion: (MutableList<MediaItem>) -> Unit) { //NOSONAR

        val albumsSingle = if (artistHash != null) { //NOSONAR
            albumArtistsRepository.getAlbumArtists().first(emptyList()) //NOSONAR
                .map { albumArtists -> albumArtists.first { it.hashCode() == artistHash } } //NOSONAR
                .map { albumArtist -> albumArtist.albums } //NOSONAR
        } else { //NOSONAR
            albumsRepository.getAlbums().first(emptyList()) //NOSONAR
        }

        albumsSingle.map { albums -> //NOSONAR
            albums //NOSONAR
                .sortedBy { it.name } //NOSONAR
                .map { album -> album.toMediaItem(mediaId) } //NOSONAR
                .toMutableList() //NOSONAR
        }
            .subscribe({ mediaItems -> completion(mediaItems) }, { //NOSONAR
                // Intentionally left empty.
            })
    }

    @SuppressLint("CheckResult") //NOSONAR
    private fun listSongs(mediaId: String, albumId: Long?, completion: (MutableList<MediaItem>) -> Unit) { //NOSONAR
        getSongsForPredicate { if (albumId == null) true else it.albumId == albumId } //NOSONAR
            .map { songs -> //NOSONAR
                songs //NOSONAR
                    .sortedBy { song -> song.albumArtistName } //NOSONAR
                    .sortedBy { song -> song.albumName } //NOSONAR
                    .sortedBy { song -> song.track } //NOSONAR
                    .sortedBy { song -> song.discNumber } //NOSONAR
                    .map { song -> song.toMediaItem(mediaId) } //NOSONAR
                    .toMutableList() //NOSONAR
            }
            .subscribe({ mediaItems -> completion(mediaItems) }, { //NOSONAR
                // Intentionally left empty.
            })
    }

    private fun getSongsForPredicate(predicate: (Song) -> Boolean): Single<List<Song>> { //NOSONAR
        return songsRepository.getSongs(predicate).first(emptyList()) //NOSONAR
    }

    private fun getSongsForPlaylistId(playlistId: Long?): Single<List<Song>> { //NOSONAR
        return playlistsRepository.getPlaylists() //NOSONAR
            .first(emptyList()) //NOSONAR
            .flatMap { playlists -> Single.just(playlists.first { playlist -> playlist.id == playlistId }) } //NOSONAR
            .flatMap { playlist -> songsRepository.getSongs(playlist).first(emptyList()) } //NOSONAR
            .map { songs -> //NOSONAR
                songs //NOSONAR
                    .sortedBy { it.playlistSongPlayOrder } //NOSONAR
                    .toMutableList() //NOSONAR
            }
    }

    private fun getSongsForGenreId(genreId: Long?): Single<MutableList<Song>> { //NOSONAR
        return genresRepository.getGenres() //NOSONAR
            .first(emptyList()) //NOSONAR
            .flatMap { genres -> Single.just(genres.first { genre -> genre.id == genreId }) } //NOSONAR
            .flatMap { genresSingle -> genresSingle.getSongsObservable(application) } //NOSONAR
            .map { songs -> //NOSONAR
                songs.shuffled().toMutableList() //NOSONAR
            }
    }

    // MediaItem helpers

    private fun Playlist.toMediaItem(parent: String): MediaItem { //NOSONAR
        return MediaItem( //NOSONAR
            Builder() //NOSONAR
                .setTitle(name) //NOSONAR
                .setMediaId("$parent$id/songs") //NOSONAR
                .build(), MediaItem.FLAG_PLAYABLE //NOSONAR
        )
    }

    private fun Genre.toMediaItem(parent: String): MediaItem { //NOSONAR
        return MediaItem( //NOSONAR
            Builder() //NOSONAR
                .setTitle(name) //NOSONAR
                .setMediaId("$parent$id/songs") //NOSONAR
                .build(), MediaItem.FLAG_PLAYABLE //NOSONAR
        )
    }

    private fun AlbumArtist.toMediaItem(parent: String): MediaItem { //NOSONAR
        return MediaItem( //NOSONAR
            Builder() //NOSONAR
                .setTitle(name) //NOSONAR
                .setMediaId("$parent${hashCode()}/albums/") //NOSONAR
                .build(), MediaItem.FLAG_BROWSABLE //NOSONAR
        )
    }

    private fun Album.toMediaItem(parent: String): MediaItem { //NOSONAR
        return MediaItem( //NOSONAR
            Builder() //NOSONAR
                .setTitle(name) //NOSONAR
                .setSubtitle(albumArtistName) //NOSONAR
                .setMediaId("$parent$id/songs/") //NOSONAR
                .build(), MediaItem.FLAG_BROWSABLE //NOSONAR
        )
    }

    private fun Song.toMediaItem(parent: String): MediaItem { //NOSONAR
        return MediaItem( //NOSONAR
            Builder() //NOSONAR
                .setTitle(name) //NOSONAR
                .setSubtitle(albumArtistName) //NOSONAR
                .setMediaUri(Uri.parse(path)) //NOSONAR
                .setMediaId("$parent$id") //NOSONAR
                .build(), MediaItem.FLAG_PLAYABLE //NOSONAR
        )
    }
}
