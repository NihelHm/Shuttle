@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.ui.screens.queue.menu

import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract
import com.simplecity.amp_library.utils.menu.queue.QueueMenuCallbacks

interface QueueMenuContract : SongMenuContract {

    interface View : SongMenuContract.View {
        // Intentionally left empty.
    }

    interface Presenter : SongMenuContract.Presenter, QueueMenuCallbacks {
        // Intentionally left empty.
    }
}
