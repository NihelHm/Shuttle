@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR


package com.simplecity.amp_library.data // NOSONAR

import android.content.Context // NOSONAR
import android.provider.MediaStore // NOSONAR
import android.util.Pair // NOSONAR
import com.jakewharton.rxrelay2.BehaviorRelay // NOSONAR
import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.Genre // NOSONAR
import com.simplecity.amp_library.model.InclExclItem // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Query // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.sql.providers.PlayCountTable // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.StringUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.ObservableTransformer // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.functions.Consumer // NOSONAR
import io.reactivex.functions.Function3 // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import java.util.ArrayList // NOSONAR
import java.util.Arrays // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Singleton // NOSONAR

@Singleton //NOSONAR
open class SongsRepository @Inject constructor( //NOSONAR
    private val context: Context, //NOSONAR
    private val blacklistRepository: Repository.BlacklistRepository, //NOSONAR
    private val whitelistRepository: Repository.WhitelistRepository, //NOSONAR
    private val settingsManager: SettingsManager //NOSONAR
) : SongsRepository { //NOSONAR

    private var songsSubscription: Disposable? = null //NOSONAR
    private val songsRelay = BehaviorRelay.create<List<Song>>() //NOSONAR

    private var allSongsSubscription: Disposable? = null //NOSONAR
    private val allSongsRelay = BehaviorRelay.create<List<Song>>() //NOSONAR

    override fun getAllSongs(): Observable<List<Song>> { //NOSONAR
        if (allSongsSubscription == null || allSongsSubscription?.isDisposed == true) { //NOSONAR
            allSongsSubscription = SqlBriteUtils.createObservableList<Song>(context, { Song(it) }, Song.getQuery()) //NOSONAR
                .subscribe( //NOSONAR
                    allSongsRelay, //NOSONAR
                    Consumer { error -> LogUtils.logException(PlaylistsRepository.TAG, "Failed to get all songs", error) } //NOSONAR
                ) // NOSONAR
        } // NOSONAR

        return allSongsRelay //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR

    override fun getSongs(predicate: ((Song) -> Boolean)?): Observable<List<Song>> { //NOSONAR
        if (songsSubscription == null || songsSubscription?.isDisposed == true) { //NOSONAR
            songsSubscription = getAllSongs() //NOSONAR
                .compose(getInclExclTransformer()) //NOSONAR
                .map { songs -> //NOSONAR
                    songs //NOSONAR
                        .filterNot { song -> song.isPodcast } //NOSONAR
                        .toList() //NOSONAR
                } // NOSONAR
                .subscribe(songsRelay) //NOSONAR
        } // NOSONAR

        return songsRelay //NOSONAR
            .map { songs -> predicate?.let { predicate -> songs.filter(predicate) } ?: songs } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR

    override fun getSongs(album: Album): Observable<List<Song>> { //NOSONAR
        return getSongs { song -> song.albumId == album.id } //NOSONAR
    } // NOSONAR

    override fun getSongs(albumArtist: AlbumArtist): Observable<List<Song>> { //NOSONAR
        return getSongs { song -> //NOSONAR
            albumArtist.albums //NOSONAR
                .map { album -> album.id } //NOSONAR
                .any { albumId -> albumId == song.albumId } //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun getSongs(playlist: Playlist): Observable<List<Song>> { //NOSONAR
        return when (playlist.id) { //NOSONAR
            PlaylistManager.PlaylistIds.RECENTLY_ADDED_PLAYLIST -> { //NOSONAR
                val numWeeks = settingsManager.numWeeks * 3600 * 24 * 7 //NOSONAR
                return getSongs { song -> song.dateAdded > System.currentTimeMillis() / 1000 - numWeeks } //NOSONAR
                    .map { songs -> //NOSONAR
                        songs //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName) }) //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName) }) //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(b.year, a.year) }) //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.track, b.track) }) //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.discNumber, b.discNumber) }) //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumName, b.albumName) }) //NOSONAR
                            .sortedWith(Comparator { a, b -> ComparisonUtils.compareLong(b.dateAdded.toLong(), a.dateAdded.toLong()) }) //NOSONAR
                    } // NOSONAR
            } // NOSONAR

            PlaylistManager.PlaylistIds.PODCASTS_PLAYLIST -> { //NOSONAR
                getAllSongs() //NOSONAR
                    .compose(getInclExclTransformer()) //NOSONAR
                    .map { songs -> songs.filter { song -> song.isPodcast } } //NOSONAR
                    .map { songs -> songs.sortedWith(Comparator { a, b -> ComparisonUtils.compareLong(a.playlistSongPlayOrder, b.playlistSongPlayOrder) }) } //NOSONAR
            } // NOSONAR

            PlaylistManager.PlaylistIds.MOST_PLAYED_PLAYLIST -> { //NOSONAR
                val query = Query.Builder() //NOSONAR
                    .uri(PlayCountTable.URI) //NOSONAR
                    .projection(arrayOf(PlayCountTable.COLUMN_ID, PlayCountTable.COLUMN_PLAY_COUNT)) //NOSONAR
                    .sort(PlayCountTable.COLUMN_PLAY_COUNT + " DESC") //NOSONAR
                    .build() //NOSONAR

                SqlBriteUtils.createObservableList(context, { cursor -> //NOSONAR
                    Pair( //NOSONAR
                        cursor.getLong(cursor.getColumnIndexOrThrow(PlayCountTable.COLUMN_ID)), //NOSONAR
                        cursor.getInt(cursor.getColumnIndexOrThrow(PlayCountTable.COLUMN_PLAY_COUNT)) //NOSONAR
                    ) // NOSONAR
                }, query) //NOSONAR
                    .flatMap { pairs -> //NOSONAR
                        getSongs { song -> //NOSONAR
                            pairs.firstOrNull { pair -> //NOSONAR
                                song.playCount = pair.second //NOSONAR
                                pair.first == song.id && pair.second >= 2 //NOSONAR
                            } != null //NOSONAR
                        }.map { songs -> songs.sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(b.playCount, a.playCount) }) } //NOSONAR
                    } // NOSONAR
            } // NOSONAR

            PlaylistManager.PlaylistIds.RECENTLY_PLAYED_PLAYLIST -> { //NOSONAR
                val query = Query.Builder() //NOSONAR
                    .uri(PlayCountTable.URI) //NOSONAR
                    .projection(arrayOf(PlayCountTable.COLUMN_ID, PlayCountTable.COLUMN_TIME_PLAYED)) //NOSONAR
                    .sort(PlayCountTable.COLUMN_TIME_PLAYED + " DESC") //NOSONAR
                    .build() //NOSONAR

                SqlBriteUtils.createObservableList(context, { cursor -> //NOSONAR
                    Pair( //NOSONAR
                        cursor.getLong(cursor.getColumnIndexOrThrow(PlayCountTable.COLUMN_ID)), //NOSONAR
                        cursor.getLong(cursor.getColumnIndexOrThrow(PlayCountTable.COLUMN_TIME_PLAYED)) //NOSONAR
                    ) // NOSONAR
                }, query) //NOSONAR
                    .flatMap { pairs -> //NOSONAR
                        getSongs { song -> //NOSONAR
                            pairs.filter { pair -> //NOSONAR
                                song.lastPlayed = pair.second //NOSONAR
                                pair.first == song.id //NOSONAR
                            }.firstOrNull() != null //NOSONAR
                        }.map { songs -> songs.sortedWith(Comparator { a, b -> ComparisonUtils.compareLong(b.lastPlayed, a.lastPlayed) }) } //NOSONAR
                    } // NOSONAR
            } // NOSONAR

            else -> { //NOSONAR
                val query = Song.getQuery() //NOSONAR
                query.uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.id) //NOSONAR
                val projection = ArrayList(Arrays.asList(*Song.getProjection())) //NOSONAR
                projection.add(MediaStore.Audio.Playlists.Members._ID) //NOSONAR
                projection.add(MediaStore.Audio.Playlists.Members.AUDIO_ID) //NOSONAR
                projection.add(MediaStore.Audio.Playlists.Members.PLAY_ORDER) //NOSONAR
                query.projection = projection.toTypedArray() //NOSONAR

                SqlBriteUtils.createObservableList<Song>(context, { Playlist.createSongFromPlaylistCursor(it) }, query) //NOSONAR
                    .map { songs -> songs.sortedWith(Comparator { a, b -> ComparisonUtils.compareLong(a.playlistSongPlayOrder, b.playlistSongPlayOrder) }) } //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun getSongs(genre: Genre): Observable<List<Song>> { //NOSONAR
        return getSongs() //NOSONAR
            .map { songs -> //NOSONAR
                songs.sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(b.year, a.year) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.track, b.track) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.discNumber, b.discNumber) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumName, b.albumName) }) //NOSONAR
                    .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName) }) //NOSONAR
            } // NOSONAR
    } // NOSONAR

    private fun getInclExclTransformer(): ObservableTransformer<List<Song>, List<Song>> { //NOSONAR
        return ObservableTransformer { upstream -> //NOSONAR
            Observable.combineLatest<List<Song>, List<InclExclItem>, List<InclExclItem>, List<Song>>( //NOSONAR
                upstream, //NOSONAR
                whitelistRepository.getWhitelistItems(this), //NOSONAR
                blacklistRepository.getBlacklistItems(this), //NOSONAR
                Function3 { songs: List<Song>, inclItems: List<InclExclItem>, exclItems: List<InclExclItem> -> //NOSONAR
                    var result = songs //NOSONAR

                    // Filter out excluded paths // NOSONAR
                    if (!exclItems.isEmpty()) { //NOSONAR
                        result = songs //NOSONAR
                            .filterNot { song -> exclItems.any { exclItem -> StringUtils.containsIgnoreCase(song.path, exclItem.path) } } //NOSONAR
                            .toList() //NOSONAR
                    } // NOSONAR

                    // Filter out non-included paths // NOSONAR
                    if (!inclItems.isEmpty()) { //NOSONAR
                        result = result //NOSONAR
                            .filter { song -> inclItems.any { inclItem -> StringUtils.containsIgnoreCase(song.path, inclItem.path) } } //NOSONAR
                            .toList() //NOSONAR
                    } // NOSONAR

                    result //NOSONAR
                }) // NOSONAR
        } // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "SongsRepository" //NOSONAR
    } // NOSONAR
} // NOSONAR
