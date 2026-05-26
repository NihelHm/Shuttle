@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data

import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.InclExclItem
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import io.reactivex.Observable

interface Repository { //NOSONAR

    interface SongsRepository { //NOSONAR

        /**
         * Returns a continuous List of all [Song]s, no filtering is applied.
         */
        fun getAllSongs(): Observable<List<Song>> //NOSONAR

        /**
         * Returns a continuous List of [Song]s, excluding those which are blacklisted, podcasts, or not-whitelisted.
         */
        fun getSongs(predicate: ((Song) -> Boolean)? = null): Observable<List<Song>> //NOSONAR

        /**
         * Returns a continuous List of [Song]s belonging to the given [Playlist], excluding those which are blacklisted, podcasts, or not-whitelisted.
         */
        fun getSongs(playlist: Playlist): Observable<List<Song>> //NOSONAR

        /**
         * Returns a continuous List of [Song]s belonging to the given [Album], excluding those which are blacklisted, podcasts, or not-whitelisted.
         */
        fun getSongs(album: Album): Observable<List<Song>> //NOSONAR

        /**
         * Returns a continuous List of [Song]s belonging to the given [AlbumArtist], excluding those which are blacklisted, podcasts, or not-whitelisted.
         */
        fun getSongs(albumArtist: AlbumArtist): Observable<List<Song>> //NOSONAR

        /**
         * Returns a continuous List of [Song]s belonging to the given [Genre], excluding those which are blacklisted, podcasts, or not-whitelisted.
         */
        fun getSongs(genre: Genre): Observable<List<Song>> //NOSONAR
    }

    interface AlbumsRepository { //NOSONAR

        /**
         * Returns a continuous List of [Album]s
         */
        fun getAlbums(): Observable<List<Album>> //NOSONAR
    }

    interface AlbumArtistsRepository { //NOSONAR

        /**
         * Returns a continuous list of [AlbumArtist]s
         */
        fun getAlbumArtists(): Observable<List<AlbumArtist>> //NOSONAR
    }

    interface GenresRepository { //NOSONAR

        /**
         * Returns a continuous List of [Genre]s
         */
        fun getGenres(): Observable<List<Genre>> //NOSONAR
    }

    interface PlaylistsRepository { //NOSONAR

        /**
         * Returns a continuous List of [Playlist]s
         */
        fun getPlaylists(): Observable<List<Playlist>> //NOSONAR

        /**
         * Returns a continuous List of [Playlist]s, including user-created playlists. Empty playlists are no returned.
         */
        fun getAllPlaylists(songsRepository: SongsRepository): Observable<MutableList<Playlist>> //NOSONAR

        fun deletePlaylist(playlist: Playlist) //NOSONAR


        fun getPodcastPlaylist(): Playlist //NOSONAR

        fun getRecentlyAddedPlaylist(): Playlist //NOSONAR

        fun getMostPlayedPlaylist(): Playlist //NOSONAR

        fun getRecentlyPlayedPlaylist(): Playlist //NOSONAR
    }

    interface InclExclRepository { //NOSONAR

        fun add(inclExclItem: InclExclItem) //NOSONAR

        fun addAll(inclExclItems: List<InclExclItem>) //NOSONAR

        fun addSong(song: Song) //NOSONAR

        fun addAllSongs(songs: List<Song>) //NOSONAR

        fun delete(inclExclItem: InclExclItem) //NOSONAR

        fun deleteAll() //NOSONAR
    }

    interface BlacklistRepository : InclExclRepository { //NOSONAR

        fun getBlacklistItems(songsRepository: Repository.SongsRepository): Observable<List<InclExclItem>> //NOSONAR
    }

    interface WhitelistRepository : InclExclRepository { //NOSONAR

        fun getWhitelistItems(songsRepository: Repository.SongsRepository): Observable<List<InclExclItem>> //NOSONAR
    }
}
