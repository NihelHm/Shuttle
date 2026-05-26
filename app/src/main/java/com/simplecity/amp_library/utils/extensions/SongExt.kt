@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.extensions

import android.content.Context
import android.content.Intent
import android.support.v4.content.FileProvider
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.LogUtils
import java.io.File

const val TAG = "SongExtensions" //NOSONAR

fun Song.share(context: Context) { //NOSONAR
    try { //NOSONAR
        val intent = Intent(Intent.ACTION_SEND).setType("audio/*") //NOSONAR
        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", File(path)) //NOSONAR
        intent.putExtra(Intent.EXTRA_STREAM, uri) //NOSONAR
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_via))) //NOSONAR
    } catch (e: IllegalArgumentException) { //NOSONAR
        LogUtils.logException(TAG, "Failed to share track", e) //NOSONAR
    }
}

fun Song.delete(): Boolean { //NOSONAR

    if (path == null) return false //NOSONAR

    var success = false //NOSONAR

    val file = File(path) //NOSONAR
    if (file.exists()) { //NOSONAR
        success = file.delete() //NOSONAR
    }

    return success //NOSONAR
}

