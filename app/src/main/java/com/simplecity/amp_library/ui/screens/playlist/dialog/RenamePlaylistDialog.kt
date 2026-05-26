@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Playlist

class RenamePlaylistDialog : DialogFragment() { //NOSONAR

    private var playlist: Playlist? = null //NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR

        playlist = arguments!!.getSerializable(ARG_PLAYLIST) as Playlist //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_playlist, null) //NOSONAR
        val editText = customView.findViewById<EditText>(R.id.editText) //NOSONAR
        editText.setText(playlist!!.name) //NOSONAR

        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.create_playlist_create_text_prompt) //NOSONAR
            .customView(customView, false) //NOSONAR
            .positiveText(R.string.save) //NOSONAR
            .onPositive { materialDialog, dialogAction -> //NOSONAR
                val name = editText.text.toString() //NOSONAR
                if (name.isNotEmpty()) { //NOSONAR
                    val resolver = context!!.contentResolver //NOSONAR
                    val values = ContentValues(1) //NOSONAR
                    values.put(MediaStore.Audio.Playlists.NAME, name) //NOSONAR
                    resolver.update( //NOSONAR
                        MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, //NOSONAR
                        values, //NOSONAR
                        MediaStore.Audio.Playlists._ID + "=?", //NOSONAR
                        arrayOf(java.lang.Long.valueOf(playlist!!.id).toString()) //NOSONAR
                    )
                    playlist!!.name = name //NOSONAR
                    Toast.makeText(context, R.string.playlist_renamed_message, Toast.LENGTH_SHORT).show() //NOSONAR
                }
            }
            .negativeText(R.string.cancel) //NOSONAR

        val dialog = builder.build() //NOSONAR

        val textWatcher = object : TextWatcher { //NOSONAR
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { //NOSONAR
                // Intentionally left empty.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { //NOSONAR
                // check if playlist with current name exists already, and warn the user if so.
                setSaveButton(dialog, playlist, editText.text.toString()) //NOSONAR
            }

            override fun afterTextChanged(s: Editable) { //NOSONAR
                // Intentionally left empty.
            }
        }

        editText.addTextChangedListener(textWatcher) //NOSONAR

        return dialog //NOSONAR
    }

    private fun setSaveButton(dialog: MaterialDialog, playlist: Playlist?, typedName: String) { //NOSONAR
        if (typedName.trim { it <= ' ' }.isEmpty()) { //NOSONAR
            val button = dialog.getActionButton(DialogAction.POSITIVE) //NOSONAR
            if (button != null) { //NOSONAR
                button.isEnabled = false //NOSONAR
            }
        } else { //NOSONAR
            val button = dialog.getActionButton(DialogAction.POSITIVE) //NOSONAR
            if (button != null) { //NOSONAR
                button.isEnabled = true //NOSONAR
            }
            if (playlist!!.id >= 0 && playlist.name != typedName) { //NOSONAR
                button?.setText(R.string.create_playlist_overwrite_text) //NOSONAR
            } else { //NOSONAR
                button?.setText(R.string.create_playlist_create_text) //NOSONAR
            }
        }
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        const val TAG = "RenamePlaylistDialog" //NOSONAR

        private const val ARG_PLAYLIST = "playlist" //NOSONAR

        fun newInstance(playlist: Playlist): RenamePlaylistDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_PLAYLIST, playlist) //NOSONAR
            val fragment = RenamePlaylistDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        }
    }
}
