@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue.menu // NOSONAR

import com.simplecity.amp_library.ui.screens.songs.menu.SongMenuContract // NOSONAR
import com.simplecity.amp_library.utils.menu.queue.QueueMenuCallbacks // NOSONAR

interface QueueMenuContract : SongMenuContract { //NOSONAR

    interface View : SongMenuContract.View { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    interface Presenter : SongMenuContract.Presenter, QueueMenuCallbacks { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR
} // NOSONAR
