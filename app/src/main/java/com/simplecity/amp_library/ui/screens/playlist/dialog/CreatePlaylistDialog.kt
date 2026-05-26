@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.dialog // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import android.app.Dialog // NOSONAR
import android.content.ContentUris // NOSONAR
import android.content.ContentValues // NOSONAR
import android.content.Context // NOSONAR
import android.net.Uri // NOSONAR
import android.os.Bundle // NOSONAR
import android.provider.MediaStore // NOSONAR
import android.support.annotation.WorkerThread // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import android.text.Editable // NOSONAR
import android.text.TextUtils // NOSONAR
import android.text.TextWatcher // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.widget.EditText // NOSONAR
import android.widget.Toast // NOSONAR
import com.afollestad.materialdialogs.DialogAction // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.SongsRepository // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Playlist.Type // NOSONAR
import com.simplecity.amp_library.model.Query // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.sql.SqlUtils // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import java.io.Serializable // NOSONAR
import javax.inject.Inject // NOSONAR

class CreatePlaylistDialog : DialogFragment() { //NOSONAR

    private val disposable = CompositeDisposable() //NOSONAR

    @Inject lateinit var songsRepository: SongsRepository //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var playlistManager: PlaylistManager //NOSONAR

    interface OnSavePlaylistListener { //NOSONAR
        fun onSave(playlist: Playlist) //NOSONAR
    } // NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    } // NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR

        val songsToAdd: List<Song>? = arguments!!.getSerializable(ARG_SONGS) as? List<Song> //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_playlist, null) //NOSONAR
        val editText = customView.findViewById<EditText>(R.id.editText) //NOSONAR

        disposable.add(Observable.fromCallable<String> { makePlaylistName() } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { name -> //NOSONAR
                    editText.setText(name) //NOSONAR
                    if (!TextUtils.isEmpty(name)) { //NOSONAR
                        editText.setSelection(name.length) //NOSONAR
                    } // NOSONAR
                }, // NOSONAR
                { error -> //NOSONAR
                    LogUtils.logException(TAG, "PlaylistManager: Error Setting playlist name", error) //NOSONAR
                } // NOSONAR
            )) // NOSONAR

        val activity = activity //NOSONAR

        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .customView(customView, false) //NOSONAR
            .title(R.string.menu_playlist) //NOSONAR
            .positiveText(R.string.create_playlist_create_text) //NOSONAR
            .onPositive { materialDialog, dialogAction -> //NOSONAR
                val name = editText.text.toString() //NOSONAR
                if (!name.isEmpty()) { //NOSONAR
                    idForPlaylistObservable(name) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                            { id -> //NOSONAR
                                val uri: Uri? //NOSONAR
                                if (id >= 0) { //NOSONAR
                                    uri = ContentUris.withAppendedId(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, id!!.toLong()) //NOSONAR
                                    val uri1 = MediaStore.Audio.Playlists.Members.getContentUri("external", id as Long) //NOSONAR
                                    context!!.contentResolver.delete(uri1, null, null) //NOSONAR
                                } else { //NOSONAR
                                    val values = ContentValues(1) //NOSONAR
                                    values.put(MediaStore.Audio.Playlists.NAME, name) //NOSONAR
                                    uri = try { //NOSONAR
                                        context!!.contentResolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values) //NOSONAR
                                    } catch (e: IllegalArgumentException) { //NOSONAR
                                        if (activity != null) { //NOSONAR
                                            Toast.makeText(activity, R.string.dialog_create_playlist_error, Toast.LENGTH_LONG).show() //NOSONAR
                                        } // NOSONAR
                                        null //NOSONAR
                                    } catch (e: NullPointerException) { //NOSONAR
                                        if (activity != null) { //NOSONAR
                                            Toast.makeText(activity, R.string.dialog_create_playlist_error, Toast.LENGTH_LONG).show() //NOSONAR
                                        } // NOSONAR
                                        null //NOSONAR
                                    } // NOSONAR
                                } // NOSONAR

                                if (uri != null) { //NOSONAR
                                    val playlist = Playlist(Type.USER_CREATED, uri.lastPathSegment!!.toLong(), name, true, false, true, true, true) //NOSONAR

                                    songsToAdd?.let { //NOSONAR
                                        playlistManager.addToPlaylist(playlist, songsToAdd) { numSongs -> //NOSONAR
                                            if (activity != null) { //NOSONAR
                                                Toast.makeText(activity, activity.resources.getQuantityString(R.plurals.NNNtrackstoplaylist, numSongs, numSongs), Toast.LENGTH_LONG).show() //NOSONAR
                                            } // NOSONAR
                                            (parentFragment as? OnSavePlaylistListener)?.onSave(playlist) //NOSONAR
                                        } // NOSONAR
                                    } ?: run { //NOSONAR
                                        if (activity != null) { //NOSONAR
                                            Toast.makeText(activity, activity.resources.getQuantityString(R.plurals.NNNtrackstoplaylist, 0, 0), Toast.LENGTH_LONG).show() //NOSONAR
                                        } // NOSONAR
                                        (parentFragment as? OnSavePlaylistListener)?.onSave(playlist) //NOSONAR
                                    } // NOSONAR
                                } // NOSONAR
                            }, // NOSONAR
                            { error -> //NOSONAR
                                LogUtils.logException( //NOSONAR
                                    TAG, //NOSONAR
                                    "PlaylistManager: Error Saving playlist", //NOSONAR
                                    error //NOSONAR
                                ) // NOSONAR
                            } // NOSONAR
                        ) // NOSONAR
                } // NOSONAR
            } // NOSONAR
            .negativeText(R.string.cancel) //NOSONAR

        val dialog = builder.build() //NOSONAR

        val textWatcher = object : TextWatcher { //NOSONAR
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { //NOSONAR
                // don't care about this one // NOSONAR
            } // NOSONAR

            //Note: It's probably best to just query all playlist names first, and then check against hat list, rather than requerying for each char change. // NOSONAR
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { //NOSONAR
                val newText = editText.text.toString() //NOSONAR
                if (newText.trim { it <= ' ' }.isEmpty()) { //NOSONAR
                    dialog.getActionButton(DialogAction.POSITIVE).isEnabled = false //NOSONAR
                } else { //NOSONAR
                    dialog.getActionButton(DialogAction.POSITIVE).isEnabled = true //NOSONAR
                    // check if playlist with current name exists already, and warn the user if so. // NOSONAR
                    disposable.add(idForPlaylistObservable(newText) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                            { id -> //NOSONAR
                                if (id >= 0) { //NOSONAR
                                    dialog.getActionButton(DialogAction.POSITIVE).setText(R.string.create_playlist_overwrite_text) //NOSONAR
                                } else { //NOSONAR
                                    dialog.getActionButton(DialogAction.POSITIVE).setText(R.string.create_playlist_create_text) //NOSONAR
                                } // NOSONAR
                            }, // NOSONAR
                            { error -> //NOSONAR
                                LogUtils.logException( //NOSONAR
                                    TAG, //NOSONAR
                                    "PlaylistManager: Error handling text change", //NOSONAR
                                    error //NOSONAR
                                ) // NOSONAR
                            } // NOSONAR
                        )) // NOSONAR
                } // NOSONAR
            } // NOSONAR

            override fun afterTextChanged(s: Editable) { //NOSONAR
                // don't care about this one // NOSONAR
            } // NOSONAR
        } // NOSONAR

        editText.addTextChangedListener(textWatcher) //NOSONAR

        return dialog //NOSONAR
    } // NOSONAR

    fun idForPlaylistObservable(name: String): Single<Int> { //NOSONAR
        val query = Query.Builder() //NOSONAR
            .uri(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI) //NOSONAR
            .projection(arrayOf(MediaStore.Audio.Playlists._ID)) //NOSONAR
            .selection(MediaStore.Audio.Playlists.NAME + "='" + name.replace("'".toRegex(), "\''") + "'") //NOSONAR
            .sort(MediaStore.Audio.Playlists.NAME) //NOSONAR
            .build() //NOSONAR

        return SqlBriteUtils.createSingle(context!!, { cursor -> cursor.getInt(0) }, query, -1) //NOSONAR
    } // NOSONAR

    @WorkerThread //NOSONAR
    fun makePlaylistName(): String? { //NOSONAR

        val template = context!!.getString(R.string.new_playlist_name_template) //NOSONAR
        var num = 1 //NOSONAR

        val query = Query.Builder() //NOSONAR
            .uri(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI) //NOSONAR
            .projection(arrayOf(MediaStore.Audio.Playlists.NAME)) //NOSONAR
            .sort(MediaStore.Audio.Playlists.NAME) //NOSONAR
            .build() //NOSONAR

        SqlUtils.createQuery(context, query)?.use { cursor -> //NOSONAR
            var suggestedName = String.format(template, num++) //NOSONAR

            // Need to loop until we've made 1 full pass through without finding a match. // NOSONAR
            // Looping more than once shouldn't happen very often, but will happen // NOSONAR
            // if you have playlists named "New Playlist 1"/10/2/3/4/5/6/7/8/9, where // NOSONAR
            // making only one pass would result in "New Playlist 10" being erroneously // NOSONAR
            // picked for the new name. // NOSONAR
            var done = false //NOSONAR
            while (!done) { //NOSONAR
                done = true //NOSONAR
                cursor.moveToFirst() //NOSONAR
                while (!cursor.isAfterLast) { //NOSONAR
                    val playlistName = cursor.getString(0) //NOSONAR
                    if (playlistName.compareTo(suggestedName, ignoreCase = true) == 0) { //NOSONAR
                        suggestedName = String.format(template, num++) //NOSONAR
                        done = false //NOSONAR
                    } // NOSONAR
                    cursor.moveToNext() //NOSONAR
                } // NOSONAR
            } // NOSONAR
            return suggestedName //NOSONAR
        } // NOSONAR
        return null //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "CreatePlaylistDialog" //NOSONAR

        private const val ARG_SONGS = "songs" //NOSONAR

        fun newInstance(songsToAdd: List<Song>?): CreatePlaylistDialog { //NOSONAR
            val dialogFragment = CreatePlaylistDialog() //NOSONAR
            songsToAdd?.let { //NOSONAR
                val args = Bundle() //NOSONAR
                args.putSerializable(ARG_SONGS, songsToAdd as Serializable) //NOSONAR
                dialogFragment.arguments = args //NOSONAR
            } // NOSONAR
            return dialogFragment //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
