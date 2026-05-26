@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue // NOSONAR

import android.content.Context // NOSONAR
import android.view.MenuItem // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.DeleteDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.menu.QueueMenuContract // NOSONAR

interface QueueContract { //NOSONAR

    interface View : QueueMenuContract.View { //NOSONAR

        fun setData(queueItems: List<QueueItem>, position: Int) //NOSONAR

        fun updateQueuePosition(queuePosition: Int) //NOSONAR

        fun showToast(message: String, duration: Int) //NOSONAR

        fun showTaggerDialog(taggerDialog: TaggerDialog) //NOSONAR

        fun showDeleteDialog(deleteDialog: DeleteDialog) //NOSONAR

        fun onRemovedFromQueue(queueItem: QueueItem) //NOSONAR

        fun onRemovedFromQueue(queueItems: List<QueueItem>) //NOSONAR

        fun showUpgradeDialog() //NOSONAR

        fun setQueueSwipeLocked(locked: Boolean) //NOSONAR

        fun showCreatePlaylistDialog(songs: List<Song>) //NOSONAR
    } // NOSONAR

    interface Presenter { //NOSONAR

        fun saveQueue(context: Context) //NOSONAR

        fun saveQueue(context: Context, item: MenuItem) //NOSONAR

        fun clearQueue() //NOSONAR

        fun moveQueueItem(from: Int, to: Int) //NOSONAR

        fun loadData() //NOSONAR

        fun play(queueItem: QueueItem) //NOSONAR

        fun setQueueSwipeLocked(locked: Boolean) //NOSONAR
    } // NOSONAR
} // NOSONAR
