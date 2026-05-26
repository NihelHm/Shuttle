@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.extensions

import com.simplecity.amp_library.data.Repository.SongsRepository
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.ComparisonUtils
import io.reactivex.Single
import java.util.Comparator

fun AlbumArtist.getSongs(songsRepository: SongsRepository): Single<List<Song>> { //NOSONAR
    return songsRepository.getSongs(this) //NOSONAR
        .first(emptyList()) //NOSONAR
        .map { songs -> //NOSONAR
            songs.sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(b.year, a.year) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.track, b.track) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.discNumber, b.discNumber) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumName, b.albumName) }) //NOSONAR
        }
}

fun List<AlbumArtist>.getSongs(songsRepository: SongsRepository): Single<List<Song>> { //NOSONAR
    return Single.concat( //NOSONAR
        map { albumArtist -> albumArtist.getSongsSingle(songsRepository) }) //NOSONAR
        .reduce(emptyList()) { a, b -> a + b } //NOSONAR
}

fun Single<List<AlbumArtist>>.getSongs(songsRepository: SongsRepository): Single<List<Song>> { //NOSONAR
    return flatMap { albumArtist -> albumArtist.getSongs(songsRepository) } //NOSONAR
}
