@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.extensions // NOSONAR

import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils // NOSONAR
import io.reactivex.Single // NOSONAR

fun Album.getSongsSingle(songsRepository: Repository.SongsRepository): Single<List<Song>> { //NOSONAR
    return songsRepository.getSongs(this) //NOSONAR
        .first(emptyList()) //NOSONAR
        .map { songs -> //NOSONAR
            songs //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(b.year, a.year) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.track, b.track) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.discNumber, b.discNumber) }) //NOSONAR
        } // NOSONAR
} // NOSONAR

fun List<Album>.getSongs(songsRepository: SongsRepository): Single<List<Song>> { //NOSONAR
    return Single.concat( //NOSONAR
        map { album -> album.getSongsSingle(songsRepository) }) //NOSONAR
        .reduce(emptyList()) { a, b -> a + b } //NOSONAR

} // NOSONAR

fun Single<List<Album>>.getSongsSingle(songsRepository: SongsRepository): Single<List<Song>> { //NOSONAR
    return flatMap { album -> album.getSongs(songsRepository) } //NOSONAR
} // NOSONAR
