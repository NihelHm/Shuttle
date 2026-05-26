@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.lyrics

import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import com.cantrowitz.rxbroadcast.RxBroadcast
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.model.Query
import com.simplecity.amp_library.playback.MediaManager
import com.simplecity.amp_library.playback.constants.InternalIntents
import com.simplecity.amp_library.sql.SqlUtils
import com.simplecity.amp_library.ui.common.Presenter
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.TagException
import java.io.File
import java.io.IOException
import java.util.concurrent.Callable
import javax.inject.Inject

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
                )
        )
    }

    fun downloadOrLaunchQuickLyric() { //NOSONAR
        val lyricsView = view //NOSONAR
        if (lyricsView != null) { //NOSONAR
            if (QuickLyricUtils.isQLInstalled(application)) { //NOSONAR
                val song = mediaManager.song //NOSONAR
                if (song != null) { //NOSONAR
                    lyricsView.launchQuickLyric(song) //NOSONAR
                }
            } else { //NOSONAR
                lyricsView.downloadQuickLyric() //NOSONAR
            }
        }
    }

    fun showQuickLyricInfoDialog() { //NOSONAR
        val lyricsView = view //NOSONAR
        lyricsView?.showQuickLyricInfoDialog() //NOSONAR
    }

    private fun updateLyrics(context: Context) { //NOSONAR
        addDisposable( //NOSONAR
            Observable.fromCallable(Callable { //NOSONAR
                var lyrics = "" //NOSONAR
                var path = mediaManager.filePath //NOSONAR

                if (TextUtils.isEmpty(path)) { //NOSONAR
                    return@Callable lyrics //NOSONAR
                }

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
                            }
                        } finally { //NOSONAR
                            cursor.close() //NOSONAR
                        }
                    }
                }

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
                                }
                            }
                        }
                    } catch (ignored: CannotReadException) { //NOSONAR
                        // Intentionally left empty.
                    } catch (ignored: IOException) { //NOSONAR
                        // Intentionally left empty.
                    } catch (ignored: TagException) { //NOSONAR
                        // Intentionally left empty.
                    } catch (ignored: ReadOnlyFileException) { //NOSONAR
                        // Intentionally left empty.
                    } catch (ignored: InvalidAudioFrameException) { //NOSONAR
                        // Intentionally left empty.
                    } catch (ignored: UnsupportedOperationException) { //NOSONAR
                        // Intentionally left empty.
                    }
                }
                lyrics //NOSONAR
            })
                .subscribe( //NOSONAR
                    { lyrics -> //NOSONAR
                        val lyricsView = view //NOSONAR
                        if (lyricsView != null) { //NOSONAR
                            lyricsView.updateLyrics(lyrics) //NOSONAR
                            lyricsView.showNoLyricsView(TextUtils.isEmpty(lyrics)) //NOSONAR
                            lyricsView.showQuickLyricInfoButton(!QuickLyricUtils.isQLInstalled(context)) //NOSONAR
                        }
                    },
                    { error -> LogUtils.logException(TAG, "Error getting lyrics", error) }) //NOSONAR
        )
    }

    companion object { //NOSONAR

        private const val TAG = "LyricsPresenter" //NOSONAR
    }
}
