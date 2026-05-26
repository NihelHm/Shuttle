@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data // NOSONAR

import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.Genre // NOSONAR
import com.simplecity.amp_library.model.InclExclItem // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import io.reactivex.Observable // NOSONAR

interface Repository { //NOSONAR

    interface SongsRepository { //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of all [Song]s, no filtering is applied. // NOSONAR
         */ // NOSONAR
        fun getAllSongs(): Observable<List<Song>> //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Song]s, excluding those which are blacklisted, podcasts, or not-whitelisted. // NOSONAR
         */ // NOSONAR
        fun getSongs(predicate: ((Song) -> Boolean)? = null): Observable<List<Song>> //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Song]s belonging to the given [Playlist], excluding those which are blacklisted, podcasts, or not-whitelisted. // NOSONAR
         */ // NOSONAR
        fun getSongs(playlist: Playlist): Observable<List<Song>> //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Song]s belonging to the given [Album], excluding those which are blacklisted, podcasts, or not-whitelisted. // NOSONAR
         */ // NOSONAR
        fun getSongs(album: Album): Observable<List<Song>> //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Song]s belonging to the given [AlbumArtist], excluding those which are blacklisted, podcasts, or not-whitelisted. // NOSONAR
         */ // NOSONAR
        fun getSongs(albumArtist: AlbumArtist): Observable<List<Song>> //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Song]s belonging to the given [Genre], excluding those which are blacklisted, podcasts, or not-whitelisted. // NOSONAR
         */ // NOSONAR
        fun getSongs(genre: Genre): Observable<List<Song>> //NOSONAR
    } // NOSONAR

    interface AlbumsRepository { //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Album]s // NOSONAR
         */ // NOSONAR
        fun getAlbums(): Observable<List<Album>> //NOSONAR
    } // NOSONAR

    interface AlbumArtistsRepository { //NOSONAR

        /** // NOSONAR
         * Returns a continuous list of [AlbumArtist]s // NOSONAR
         */ // NOSONAR
        fun getAlbumArtists(): Observable<List<AlbumArtist>> //NOSONAR
    } // NOSONAR

    interface GenresRepository { //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Genre]s // NOSONAR
         */ // NOSONAR
        fun getGenres(): Observable<List<Genre>> //NOSONAR
    } // NOSONAR

    interface PlaylistsRepository { //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Playlist]s // NOSONAR
         */ // NOSONAR
        fun getPlaylists(): Observable<List<Playlist>> //NOSONAR

        /** // NOSONAR
         * Returns a continuous List of [Playlist]s, including user-created playlists. Empty playlists are no returned. // NOSONAR
         */ // NOSONAR
        fun getAllPlaylists(songsRepository: SongsRepository): Observable<MutableList<Playlist>> //NOSONAR

        fun deletePlaylist(playlist: Playlist) //NOSONAR


        fun getPodcastPlaylist(): Playlist //NOSONAR

        fun getRecentlyAddedPlaylist(): Playlist //NOSONAR

        fun getMostPlayedPlaylist(): Playlist //NOSONAR

        fun getRecentlyPlayedPlaylist(): Playlist //NOSONAR
    } // NOSONAR

    interface InclExclRepository { //NOSONAR

        fun add(inclExclItem: InclExclItem) //NOSONAR

        fun addAll(inclExclItems: List<InclExclItem>) //NOSONAR

        fun addSong(song: Song) //NOSONAR

        fun addAllSongs(songs: List<Song>) //NOSONAR

        fun delete(inclExclItem: InclExclItem) //NOSONAR

        fun deleteAll() //NOSONAR
    } // NOSONAR

    interface BlacklistRepository : InclExclRepository { //NOSONAR

        fun getBlacklistItems(songsRepository: Repository.SongsRepository): Observable<List<InclExclItem>> //NOSONAR
    } // NOSONAR

    interface WhitelistRepository : InclExclRepository { //NOSONAR

        fun getWhitelistItems(songsRepository: Repository.SongsRepository): Observable<List<InclExclItem>> //NOSONAR
    } // NOSONAR
} // NOSONAR
