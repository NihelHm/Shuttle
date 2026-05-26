@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.playlists

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.crashlytics.android.Crashlytics
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.SongsRepository
import com.simplecity.amp_library.interfaces.FileType
import com.simplecity.amp_library.model.BaseFileObject
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Query
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.sql.SqlUtils
import com.simplecity.amp_library.sql.providers.PlayCountTable
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.ShuttleUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlaylistManager @Inject constructor( //NOSONAR
    private val applicationContext: Context, //NOSONAR
    private val songsRepository: SongsRepository, //NOSONAR
    private val settingsManager: SettingsManager //NOSONAR
) {

    interface PlaylistIds { //NOSONAR
        companion object { //NOSONAR
            const val RECENTLY_ADDED_PLAYLIST: Long = -2 //NOSONAR
            const val MOST_PLAYED_PLAYLIST: Long = -3 //NOSONAR
            const val PODCASTS_PLAYLIST: Long = -4 //NOSONAR
            const val RECENTLY_PLAYED_PLAYLIST: Long = -5 //NOSONAR
        }
    }

    fun clearMostPlayed() { //NOSONAR
        applicationContext.contentResolver.delete(PlayCountTable.URI, null, null) //NOSONAR
    }

    fun addToPlaylist(playlist: Playlist, songs: List<Song>, callback: ((Int) -> Unit)?): Disposable? { //NOSONAR
        if (songs.isEmpty()) { //NOSONAR
            return null //NOSONAR
        }

        val mutableSongList = ArrayList(songs) //NOSONAR

        return songsRepository.getSongs(playlist) //NOSONAR
            .first(emptyList()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { existingSongs -> //NOSONAR
                    if (!settingsManager.ignoreDuplicates()) { //NOSONAR

                        val duplicates = existingSongs //NOSONAR
                            .filter { mutableSongList.contains(it) } //NOSONAR
                            .distinct() //NOSONAR
                            .toMutableList() //NOSONAR

                        if (!duplicates.isEmpty()) { //NOSONAR
                            @SuppressLint("InflateParams") //NOSONAR
                            val customView = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_playlist_duplicates, null) //NOSONAR
                            val messageText = customView.findViewById<TextView>(R.id.textView) //NOSONAR
                            val applyToAll = customView.findViewById<CheckBox>(R.id.applyToAll) //NOSONAR
                            val alwaysAdd = customView.findViewById<CheckBox>(R.id.alwaysAdd) //NOSONAR

                            if (duplicates.size <= 1) { //NOSONAR
                                applyToAll.visibility = View.GONE //NOSONAR
                                applyToAll.isChecked = false //NOSONAR
                            }

                            messageText.text = getPlaylistRemoveString(duplicates[0]) //NOSONAR
                            applyToAll.text = String.format(applicationContext.getString(R.string.dialog_checkbox_playlist_duplicate_apply_all), duplicates.size) //NOSONAR

                            // Fixme: Should not use application context to present dialog.
                            MaterialDialog.Builder(applicationContext) //NOSONAR
                                .title(R.string.dialog_title_playlist_duplicates) //NOSONAR
                                .customView(customView, false) //NOSONAR
                                .positiveText(R.string.dialog_button_playlist_duplicate_add) //NOSONAR
                                .autoDismiss(false) //NOSONAR
                                .onPositive { dialog, which -> //NOSONAR
                                    //If we've only got one item, or we're applying it to all items
                                    if (duplicates.size != 1 && !applyToAll.isChecked) { //NOSONAR
                                        //If we're 'adding' this song, we remove it from the 'duplicates' list
                                        duplicates.removeAt(0) //NOSONAR
                                        messageText.text = getPlaylistRemoveString(duplicates[0]) //NOSONAR
                                        applyToAll.text = String.format(applicationContext.getString(R.string.dialog_checkbox_playlist_duplicate_apply_all), duplicates.size) //NOSONAR
                                    } else { //NOSONAR
                                        //Add all songs to the playlist
                                        insertPlaylistItems(playlist, mutableSongList, existingSongs.size, callback) //NOSONAR
                                        settingsManager.setIgnoreDuplicates(alwaysAdd.isChecked) //NOSONAR
                                        dialog.dismiss() //NOSONAR
                                    }
                                }
                                .negativeText(R.string.dialog_button_playlist_duplicate_skip) //NOSONAR
                                .onNegative { dialog, which -> //NOSONAR
                                    //If we've only got one item, or we're applying it to all items
                                    if (duplicates.size != 1 && !applyToAll.isChecked) { //NOSONAR
                                        //If we're 'skipping' this song, we remove it from the 'duplicates' list,
                                        // and from the ids to be added
                                        mutableSongList.remove(duplicates.removeAt(0)) //NOSONAR
                                        messageText.text = getPlaylistRemoveString(duplicates[0]) //NOSONAR
                                        applyToAll.text = String.format(applicationContext.getString(R.string.dialog_checkbox_playlist_duplicate_apply_all), duplicates.size) //NOSONAR
                                    } else { //NOSONAR
                                        //Remove duplicates from our set of ids
                                        duplicates //NOSONAR
                                            .filter { mutableSongList.contains(it) } //NOSONAR
                                            .forEach { mutableSongList.remove(it) } //NOSONAR
                                        insertPlaylistItems(playlist, mutableSongList, existingSongs.size, callback) //NOSONAR
                                        settingsManager.setIgnoreDuplicates(alwaysAdd.isChecked) //NOSONAR
                                        dialog.dismiss() //NOSONAR
                                    }
                                }
                                .show() //NOSONAR
                        } else { //NOSONAR
                            insertPlaylistItems(playlist, mutableSongList, existingSongs.size, callback) //NOSONAR
                        }
                    } else { //NOSONAR
                        insertPlaylistItems(playlist, mutableSongList, existingSongs.size, callback) //NOSONAR
                    }
                },
                { error -> LogUtils.logException(TAG, "PlaylistManager: Error determining existing songs", error) } //NOSONAR
            )
    }

    private fun insertPlaylistItems(playlist: Playlist, songs: List<Song>, songCount: Int, callback: ((Int) -> Unit)?) { //NOSONAR
        if (songs.isEmpty()) { //NOSONAR
            return //NOSONAR
        }

        val contentValues = arrayOfNulls<ContentValues>(songs.size) //NOSONAR
        var i = 0 //NOSONAR
        val length = songs.size //NOSONAR
        while (i < length) { //NOSONAR
            contentValues[i] = ContentValues() //NOSONAR
            contentValues[i]!!.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, songCount + i) //NOSONAR
            contentValues[i]!!.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songs[i].id) //NOSONAR
            i++ //NOSONAR
        }

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.id) //NOSONAR
        if (uri != null) { //NOSONAR
            applicationContext.contentResolver.bulkInsert(uri, contentValues) //NOSONAR
            callback?.invoke(songs.size) //NOSONAR
        }
    }

    private fun getPlaylistRemoveString(song: Song): SpannableStringBuilder { //NOSONAR
        val spannableString = SpannableStringBuilder(String.format(applicationContext.getString(R.string.dialog_message_playlist_add_duplicate), song.artistName, song.name)) //NOSONAR
        val boldSpan = StyleSpan(android.graphics.Typeface.BOLD) //NOSONAR
        spannableString.setSpan(boldSpan, 0, song.artistName.length + song.name.length + 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE) //NOSONAR
        return spannableString //NOSONAR
    }

    fun clearPlaylist(playlistId: Long) { //NOSONAR
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId) //NOSONAR
        applicationContext.contentResolver.delete(uri, null, null) //NOSONAR
    }

    fun createPlaylist(name: String): Playlist? { //NOSONAR
        var playlist: Playlist? = null //NOSONAR
        var id: Long = -1 //NOSONAR

        if (!TextUtils.isEmpty(name)) { //NOSONAR
            val query = Query.Builder() //NOSONAR
                .uri(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI) //NOSONAR
                .projection(arrayOf(MediaStore.Audio.PlaylistsColumns.NAME)) //NOSONAR
                .selection(MediaStore.Audio.PlaylistsColumns.NAME + " = '" + name + "'") //NOSONAR
                .build() //NOSONAR

            SqlUtils.createQuery(applicationContext, query)?.use { cursor -> //NOSONAR
                val count = cursor.count //NOSONAR

                if (count <= 0) { //NOSONAR
                    val values = ContentValues(1) //NOSONAR
                    values.put(MediaStore.Audio.PlaylistsColumns.NAME, name) //NOSONAR
                    //Catch NPE occurring on Amazon devices.
                    try { //NOSONAR
                        val uri = applicationContext.contentResolver.insert( //NOSONAR
                            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, //NOSONAR
                            values //NOSONAR
                        )
                        if (uri != null) { //NOSONAR
                            id = java.lang.Long.parseLong(uri.lastPathSegment!!) //NOSONAR
                        }
                    } catch (e: NullPointerException) { //NOSONAR
                        Crashlytics.log("Failed to create playlist: " + e.message) //NOSONAR
                    }

                }
            }
        }

        if (id != -1L) { //NOSONAR
            playlist = Playlist(Playlist.Type.USER_CREATED, id, name, true, false, true, true, true) //NOSONAR
        } else { //NOSONAR
            Crashlytics.log(String.format("Failed to create playlist. Name: %s, id: %d", name, id)) //NOSONAR
        }

        return playlist //NOSONAR
    }

    fun removeFromPlaylist(playlist: Playlist, song: Song, callback: Function1<Boolean, Unit>?): Disposable { //NOSONAR
        return Single.fromCallable { //NOSONAR
            var numTracksRemoved = 0 //NOSONAR
            if (playlist.id >= 0) { //NOSONAR
                val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.id) //NOSONAR
                numTracksRemoved = applicationContext.contentResolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID + "=" + song.id, null) //NOSONAR
            }
            numTracksRemoved //NOSONAR
        }
            .delay(150, TimeUnit.MILLISECONDS) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { numTracksRemoved -> callback?.invoke(numTracksRemoved > 0) }, //NOSONAR
                { error -> LogUtils.logException(TAG, "PlaylistManager: Error Removing from favorites", error) } //NOSONAR
            )
    }

    fun addFileObjectsToPlaylist(context: Context, playlist: Playlist, fileObjects: List<BaseFileObject>, callback: Function1<Int, Unit>): Disposable { //NOSONAR
        val progressDialog = ProgressDialog.show(context, "", context.getString(R.string.gathering_songs), false) //NOSONAR

        val folderCount = fileObjects //NOSONAR
            .filter { value -> value.fileType == FileType.FOLDER }.count() //NOSONAR

        if (folderCount > 0) { //NOSONAR
            progressDialog!!.show() //NOSONAR
        }

        return ShuttleUtils.getSongsForFileObjects(songsRepository, fileObjects) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { songs -> //NOSONAR
                    if (progressDialog != null && progressDialog.isShowing) { //NOSONAR
                        progressDialog.dismiss() //NOSONAR
                    }
                    addToPlaylist(playlist, songs, callback) //NOSONAR
                },
                { error -> LogUtils.logException(TAG, "Error getting songs for file object", error) } //NOSONAR
            )
    }

    companion object { //NOSONAR
        private const val TAG = "PlaylistManager" //NOSONAR

        const val ARG_PLAYLIST = "playlist" //NOSONAR
    }
}
