@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.dialog // NOSONAR

import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.Fragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.Repository.PlaylistsRepository // NOSONAR
import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule // NOSONAR
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import dagger.Binds // NOSONAR
import dagger.Module // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Named // NOSONAR

class DeletePlaylistConfirmationDialog : DialogFragment() { //NOSONAR

    private lateinit var playlist: Playlist //NOSONAR

    @Inject lateinit var playlistsRepository: PlaylistsRepository //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        playlist = arguments!!.getSerializable(ARG_PLAYLIST) as Playlist //NOSONAR
    } // NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.dialog_title_playlist_delete) //NOSONAR
            .content(R.string.dialog_message_playlist_delete, playlist.name) //NOSONAR
            .positiveText(R.string.dialog_button_delete) //NOSONAR
            .onPositive { dialog, which -> playlistsRepository.deletePlaylist(playlist) } //NOSONAR
            .negativeText(R.string.cancel) //NOSONAR
            .build() //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        const val TAG = "DeletePlaylistConfirmationDialog" //NOSONAR

        const val ARG_PLAYLIST = "playlist" //NOSONAR

        fun newInstance(playlist: Playlist): DeletePlaylistConfirmationDialog { //NOSONAR
            val bundle = Bundle() //NOSONAR
            bundle.putSerializable(ARG_PLAYLIST, playlist) //NOSONAR
            val fragment = DeletePlaylistConfirmationDialog() //NOSONAR
            fragment.arguments = bundle //NOSONAR
            return fragment //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR

@Module(includes = [FragmentModule::class]) //NOSONAR
abstract class DeletePlaylistConfirmationDialogFragmentModule { //NOSONAR

    @Binds //NOSONAR
    @Named(FragmentModule.FRAGMENT) //NOSONAR
    @FragmentScope //NOSONAR
    internal abstract fun fragment(deletePlaylistConfirmationDialog: DeletePlaylistConfirmationDialog): Fragment //NOSONAR
} // NOSONAR
