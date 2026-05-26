@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.simplecity.amp_library.model.Song

class QueueItem(var song: Song, var occurrence: Int) { //NOSONAR

    override fun equals(other: Any?): Boolean { //NOSONAR
        if (this === other) return true //NOSONAR
        if (javaClass != other?.javaClass) return false //NOSONAR

        other as QueueItem //NOSONAR

        if (song != other.song) return false //NOSONAR
        if (occurrence != other.occurrence) return false //NOSONAR

        return true //NOSONAR
    }

    override fun hashCode(): Int { //NOSONAR
        var result = song.hashCode() //NOSONAR
        result = 31 * result + occurrence //NOSONAR
        return result //NOSONAR
    }
}

fun List<Song>.toQueueItems(): List<QueueItem> { //NOSONAR
    val queueItems = map { song -> QueueItem(song, 1) } //NOSONAR
    queueItems.updateOccurrence() //NOSONAR
    return queueItems //NOSONAR
}

fun List<QueueItem>.updateOccurrence() { //NOSONAR
    groupBy { queueItem -> queueItem.song } //NOSONAR
        .values //NOSONAR
        .forEach { //NOSONAR
            it.forEachIndexed { index, queueItem -> //NOSONAR
                queueItem.occurrence = index + 1 //NOSONAR
            }
        }
}

fun List<QueueItem>.toSongs(): List<Song> { //NOSONAR
    return map { queueItem -> queueItem.song } //NOSONAR
}

fun QueueItem.toMediaSessionQueueItem(): MediaSessionCompat.QueueItem { //NOSONAR
    val mediaDescription = MediaDescriptionCompat.Builder() //NOSONAR
        .setMediaId(song.id.toString()) //NOSONAR
        .setTitle(song.name) //NOSONAR
        .setSubtitle(song.artistName) //NOSONAR
        .build() //NOSONAR
    return MediaSessionCompat.QueueItem(mediaDescription, hashCode().toLong()) //NOSONAR
}

fun List<QueueItem>.toMediaSessionQueueItems(): List<MediaSessionCompat.QueueItem> { //NOSONAR
    return map { queueItem -> queueItem.toMediaSessionQueueItem() } //NOSONAR
}
