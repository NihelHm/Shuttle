@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.Repository.PlaylistsRepository
import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope
import com.simplecity.amp_library.model.Playlist
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import javax.inject.Named

class DeletePlaylistConfirmationDialog : DialogFragment() { //NOSONAR

    private lateinit var playlist: Playlist //NOSONAR

    @Inject lateinit var playlistsRepository: PlaylistsRepository //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        playlist = arguments!!.getSerializable(ARG_PLAYLIST) as Playlist //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.dialog_title_playlist_delete) //NOSONAR
            .content(R.string.dialog_message_playlist_delete, playlist.name) //NOSONAR
            .positiveText(R.string.dialog_button_delete) //NOSONAR
            .onPositive { dialog, which -> playlistsRepository.deletePlaylist(playlist) } //NOSONAR
            .negativeText(R.string.cancel) //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        const val TAG = "DeletePlaylistConfirmationDialog" //NOSONAR

        const val ARG_PLAYLIST = "playlist" //NOSONAR

        fun newInstance(playlist: Playlist): DeletePlaylistConfirmationDialog { //NOSONAR
            val bundle = Bundle() //NOSONAR
            bundle.putSerializable(ARG_PLAYLIST, playlist) //NOSONAR
            val fragment = DeletePlaylistConfirmationDialog() //NOSONAR
            fragment.arguments = bundle //NOSONAR
            return fragment //NOSONAR
        }
    }
}

@Module(includes = [FragmentModule::class]) //NOSONAR
abstract class DeletePlaylistConfirmationDialogFragmentModule { //NOSONAR

    @Binds //NOSONAR
    @Named(FragmentModule.FRAGMENT) //NOSONAR
    @FragmentScope //NOSONAR
    internal abstract fun fragment(deletePlaylistConfirmationDialog: DeletePlaylistConfirmationDialog): Fragment //NOSONAR
}
