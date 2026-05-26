@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.lyrics // NOSONAR

import android.content.Context // NOSONAR
import android.content.IntentFilter // NOSONAR
import android.net.Uri // NOSONAR
import android.provider.MediaStore // NOSONAR
import android.text.TextUtils // NOSONAR
import com.cantrowitz.rxbroadcast.RxBroadcast // NOSONAR
import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import com.simplecity.amp_library.model.Query // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents // NOSONAR
import com.simplecity.amp_library.sql.SqlUtils // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import io.reactivex.BackpressureStrategy // NOSONAR
import io.reactivex.Observable // NOSONAR
import org.jaudiotagger.audio.AudioFileIO // NOSONAR
import org.jaudiotagger.audio.exceptions.CannotReadException // NOSONAR
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException // NOSONAR
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException // NOSONAR
import org.jaudiotagger.tag.FieldKey // NOSONAR
import org.jaudiotagger.tag.TagException // NOSONAR
import java.io.File // NOSONAR
import java.io.IOException // NOSONAR
import java.util.concurrent.Callable // NOSONAR
import javax.inject.Inject // NOSONAR

class LyricsPresenter @Inject //NOSONAR
constructor( //NOSONAR
    private val application: ShuttleApplication, //NOSONAR
    private val mediaManager: MediaManager //NOSONAR
) : Presenter<LyricsView>() { //NOSONAR

    override fun bindView(view: LyricsView) { //NOSONAR
        super.bindView(view) //NOSONAR

        updateLyrics(application) //NOSONAR

        addDisposable( //NOSONAR
            RxBroadcast.fromBroadcast(application, IntentFilter(InternalIntents.META_CHANGED)) //NOSONAR
                .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                .subscribe( //NOSONAR
                    { _ -> updateLyrics(application) }, //NOSONAR
                    { error -> LogUtils.logException(TAG, "Error receiving meta changed", error) } //NOSONAR
                ) // NOSONAR
        ) // NOSONAR
    } // NOSONAR

    fun downloadOrLaunchQuickLyric() { //NOSONAR
        val lyricsView = view //NOSONAR
        if (lyricsView != null) { //NOSONAR
            if (QuickLyricUtils.isQLInstalled(application)) { //NOSONAR
                val song = mediaManager.song //NOSONAR
                if (song != null) { //NOSONAR
                    lyricsView.launchQuickLyric(song) //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                lyricsView.downloadQuickLyric() //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun showQuickLyricInfoDialog() { //NOSONAR
        val lyricsView = view //NOSONAR
        lyricsView?.showQuickLyricInfoDialog() //NOSONAR
    } // NOSONAR

    private fun updateLyrics(context: Context) { //NOSONAR
        addDisposable( //NOSONAR
            Observable.fromCallable(Callable { //NOSONAR
                var lyrics = "" //NOSONAR
                var path = mediaManager.filePath //NOSONAR

                if (TextUtils.isEmpty(path)) { //NOSONAR
                    return@Callable lyrics //NOSONAR
                } // NOSONAR

                if (path!!.startsWith("content://")) { //NOSONAR
                    val query = Query.Builder() //NOSONAR
                        .uri(Uri.parse(path)) //NOSONAR
                        .projection(arrayOf(MediaStore.Audio.Media.DATA)) //NOSONAR
                        .build() //NOSONAR

                    val cursor = SqlUtils.createQuery(application, query) //NOSONAR
                    if (cursor != null) { //NOSONAR
                        try { //NOSONAR
                            val colIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA) //NOSONAR
                            if (cursor.moveToFirst()) { //NOSONAR
                                path = cursor.getString(colIndex) //NOSONAR
                            } // NOSONAR
                        } finally { //NOSONAR
                            cursor.close() //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR

                val file = File(path) //NOSONAR
                if (file.exists()) { //NOSONAR
                    try { //NOSONAR
                        val audioFile = AudioFileIO.read(file) //NOSONAR
                        if (audioFile != null) { //NOSONAR
                            val tag = audioFile.tag //NOSONAR
                            if (tag != null) { //NOSONAR
                                val tagLyrics = tag.getFirst(FieldKey.LYRICS) //NOSONAR
                                if (tagLyrics != null && tagLyrics.isNotEmpty()) { //NOSONAR
                                    lyrics = tagLyrics.replace("\r", "\n") //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } catch (ignored: CannotReadException) { //NOSONAR
                        // Intentionally left empty. // NOSONAR
                    } catch (ignored: IOException) { //NOSONAR
                        // Intentionally left empty. // NOSONAR
                    } catch (ignored: TagException) { //NOSONAR
                        // Intentionally left empty. // NOSONAR
                    } catch (ignored: ReadOnlyFileException) { //NOSONAR
                        // Intentionally left empty. // NOSONAR
                    } catch (ignored: InvalidAudioFrameException) { //NOSONAR
                        // Intentionally left empty. // NOSONAR
                    } catch (ignored: UnsupportedOperationException) { //NOSONAR
                        // Intentionally left empty. // NOSONAR
                    } // NOSONAR
                } // NOSONAR
                lyrics //NOSONAR
            }) // NOSONAR
                .subscribe( //NOSONAR
                    { lyrics -> //NOSONAR
                        val lyricsView = view //NOSONAR
                        if (lyricsView != null) { //NOSONAR
                            lyricsView.updateLyrics(lyrics) //NOSONAR
                            lyricsView.showNoLyricsView(TextUtils.isEmpty(lyrics)) //NOSONAR
                            lyricsView.showQuickLyricInfoButton(!QuickLyricUtils.isQLInstalled(context)) //NOSONAR
                        } // NOSONAR
                    }, // NOSONAR
                    { error -> LogUtils.logException(TAG, "Error getting lyrics", error) }) //NOSONAR
        ) // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "LyricsPresenter" //NOSONAR
    } // NOSONAR
} // NOSONAR
