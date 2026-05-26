@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.queue // NOSONAR

import com.simplecity.amp_library.ui.screens.queue.QueueItem // NOSONAR
import com.simplecity.amp_library.utils.menu.song.SongsMenuCallbacks // NOSONAR
import io.reactivex.Single // NOSONAR

interface QueueMenuCallbacks : SongsMenuCallbacks { //NOSONAR

    fun moveToNext(queueItem: QueueItem) //NOSONAR

    fun removeQueueItems(queueItems: List<QueueItem>) //NOSONAR
} // NOSONAR


fun QueueMenuCallbacks.removeQueueItem(queueItem: QueueItem) { //NOSONAR
    removeQueueItems(listOf(queueItem)) //NOSONAR
} // NOSONAR


fun QueueMenuCallbacks.removeQueueItems(queueItems: Single<List<QueueItem>>) { //NOSONAR
    transform(queueItems) { queueItems -> removeQueueItems(queueItems) } //NOSONAR
} // NOSONAR
