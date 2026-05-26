@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils // NOSONAR

import android.app.AlertDialog // NOSONAR
import android.content.ContentUris // NOSONAR
import android.content.ContentValues // NOSONAR
import android.content.Context // NOSONAR
import android.content.Intent // NOSONAR
import android.net.Uri // NOSONAR
import android.os.Build // NOSONAR
import android.provider.BaseColumns // NOSONAR
import android.provider.MediaStore // NOSONAR
import android.provider.Settings // NOSONAR
import android.util.Log // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.model.Query // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.sql.SqlUtils // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import java.util.concurrent.Callable // NOSONAR
import javax.inject.Inject // NOSONAR

class RingtoneManager @Inject constructor(val applicationContext: Context) { //NOSONAR

    fun setRingtone(song: Song, onSuccess: () -> Unit): Disposable? { //NOSONAR

        return Observable.fromCallable(Callable { //NOSONAR
            var success = false //NOSONAR

            val resolver = applicationContext.contentResolver //NOSONAR
            // Set the flag in the database to mark this as a ringtone // NOSONAR
            val ringUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id) //NOSONAR
            try { //NOSONAR
                val values = ContentValues(2) //NOSONAR
                values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, "1") //NOSONAR
                values.put(MediaStore.Audio.AudioColumns.IS_ALARM, "1") //NOSONAR
                if (ringUri != null) { //NOSONAR
                    resolver.update(ringUri, values, null, null) //NOSONAR
                } // NOSONAR
            } catch (ex: UnsupportedOperationException) { //NOSONAR
                // most likely the card just got unmounted // NOSONAR
                Log.e(TAG, "couldn't set ringtone flag for song $song") //NOSONAR
                return@Callable false //NOSONAR
            } // NOSONAR

            val query = Query.Builder() //NOSONAR
                .uri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) //NOSONAR
                .projection(arrayOf(BaseColumns._ID, MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.TITLE)) //NOSONAR
                .selection(BaseColumns._ID + "=" + song.id) //NOSONAR
                .build() //NOSONAR

            SqlUtils.createQuery(applicationContext, query)?.use { cursor -> //NOSONAR
                if (cursor.count == 1) { //NOSONAR
                    // Set the system setting to make this the current ringtone // NOSONAR
                    cursor.moveToFirst() //NOSONAR
                    if (ringUri != null) { //NOSONAR
                        Settings.System.putString(resolver, Settings.System.RINGTONE, ringUri.toString()) //NOSONAR
                    } // NOSONAR
                    success = true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            success //NOSONAR
        }) // NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { onSuccess() }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Error setting ringtone", error) } //NOSONAR
            ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        private const val TAG = "RingtoneManager" //NOSONAR

        fun requiresDialog(context: Context): Boolean { //NOSONAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //NOSONAR
                if (!Settings.System.canWrite(context)) { //NOSONAR
                    return true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            return false //NOSONAR
        } // NOSONAR

        fun getDialog(context: Context): AlertDialog { //NOSONAR
            return AlertDialog.Builder(context) //NOSONAR
                .setTitle(R.string.dialog_title_set_ringtone) //NOSONAR
                .setMessage(R.string.dialog_message_set_ringtone) //NOSONAR
                .setPositiveButton(R.string.button_ok) { dialog, which -> //NOSONAR
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS) //NOSONAR
                    intent.data = Uri.parse("package:" + context.applicationContext.packageName) //NOSONAR
                    context.startActivity(intent) //NOSONAR
                }.setNegativeButton(R.string.cancel, null) //NOSONAR
                .show() //NOSONAR
        } // NOSONAR

    } // NOSONAR
} // NOSONAR
