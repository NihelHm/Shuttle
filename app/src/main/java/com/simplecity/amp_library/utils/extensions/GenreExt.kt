@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.extensions

import android.content.Context
import android.provider.MediaStore
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils
import com.simplecity.amp_library.utils.ComparisonUtils
import io.reactivex.Single
import java.util.Comparator

fun Genre.getSongsObservable(context: Context): Single<List<Song>> { //NOSONAR
    val query = Song.getQuery() //NOSONAR
    query.uri = MediaStore.Audio.Genres.Members.getContentUri("external", id) //NOSONAR

    return SqlBriteUtils.createSingleList(context, { Song(it) }, query) //NOSONAR
}

fun Genre.getSongs(context: Context): Single<List<Song>> { //NOSONAR
    return getSongsObservable(context) //NOSONAR
        .map { songs -> //NOSONAR
            songs //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(b.year, a.year) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.track, b.track) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compareInt(a.discNumber, b.discNumber) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumName, b.albumName) }) //NOSONAR
                .sortedWith(Comparator { a, b -> ComparisonUtils.compare(a.albumArtistName, b.albumArtistName) }) //NOSONAR
        }
}
