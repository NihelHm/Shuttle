@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils.menu.folder // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import android.content.Context // NOSONAR
import android.support.annotation.StringRes // NOSONAR
import android.support.v4.app.Fragment // NOSONAR
import android.support.v7.widget.PopupMenu // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.widget.EditText // NOSONAR
import android.widget.Toast // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.Repository.SongsRepository // NOSONAR
import com.simplecity.amp_library.interfaces.FileType // NOSONAR
import com.simplecity.amp_library.model.BaseFileObject // NOSONAR
import com.simplecity.amp_library.model.FileObject // NOSONAR
import com.simplecity.amp_library.model.FolderObject // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager // NOSONAR
import com.simplecity.amp_library.playback.MediaManager.Defs // NOSONAR
import com.simplecity.amp_library.ui.modelviews.FolderView // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog // NOSONAR
import com.simplecity.amp_library.utils.CustomMediaScanner // NOSONAR
import com.simplecity.amp_library.utils.FileHelper // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.menu.MenuUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import java.io.File // NOSONAR

object FolderMenuUtils { //NOSONAR

    private val TAG = "FolderMenuUtils" //NOSONAR

    interface Callbacks { //NOSONAR

        fun showToast(message: String) //NOSONAR

        fun showToast(@StringRes messageResId: Int) //NOSONAR

        fun onSongsAddedToQueue(numSongs: Int) //NOSONAR

        fun onPlaybackFailed() //NOSONAR

        fun shareSong(song: Song) //NOSONAR

        fun setRingtone(song: Song) //NOSONAR

        fun showSongInfo(song: Song) //NOSONAR

        fun onPlaylistItemsInserted() //NOSONAR

        fun showTagEditor(song: Song) //NOSONAR

        fun onFileNameChanged(folderView: FolderView) //NOSONAR

        fun onFileDeleted(folderView: FolderView) //NOSONAR

        fun playNext(songsSingle: Single<List<Song>>) //NOSONAR

        fun whitelist(songsSingle: Single<List<Song>>) //NOSONAR

        fun blacklist(songsSingle: Single<List<Song>>) //NOSONAR

        fun whitelist(song: Song) //NOSONAR

        fun blacklist(song: Song) //NOSONAR
    } // NOSONAR

    private fun getSongForFile(songsRepository: SongsRepository, fileObject: FileObject): Single<Song> { //NOSONAR
        return FileHelper.getSong(songsRepository, File(fileObject.path)) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
    } // NOSONAR

    private fun getSongsForFolderObject(songsRepository: SongsRepository, folderObject: FolderObject): Single<List<Song>> { //NOSONAR
        return FileHelper.getSongList(songsRepository, File(folderObject.path), true, false) //NOSONAR
    } // NOSONAR

    private fun scanFile(context: Context, fileObject: FileObject, callbacks: Callbacks) { //NOSONAR
        CustomMediaScanner.scanFile(context, fileObject.path, { callbacks.showToast(it) }) //NOSONAR
    } // NOSONAR

    // To do later: Remove context requirement. // NOSONAR
    private fun scanFolder(context: Context, folderObject: FolderObject) { //NOSONAR
        CustomMediaScanner.scanFile(context, folderObject) //NOSONAR
    } // NOSONAR

    // To do later: Remove context requirement. // NOSONAR
    private fun renameFile(context: Context, folderView: FolderView, fileObject: BaseFileObject, callbacks: Callbacks) { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_rename, null) //NOSONAR

        val editText = customView.findViewById<EditText>(R.id.editText) //NOSONAR
        editText.setText(fileObject.name) //NOSONAR

        val builder = MaterialDialog.Builder(context) //NOSONAR
        if (fileObject.fileType == FileType.FILE) { //NOSONAR
            builder.title(R.string.rename_file) //NOSONAR
        } else { //NOSONAR
            builder.title(R.string.rename_folder) //NOSONAR
        } // NOSONAR

        builder.customView(customView, false) //NOSONAR
        builder.positiveText(R.string.save) //NOSONAR
            .onPositive { materialDialog, dialogAction -> //NOSONAR
                if (editText.text != null) { //NOSONAR
                    if (FileHelper.renameFile(context, fileObject, editText.text.toString())) { //NOSONAR
                        callbacks.onFileNameChanged(folderView) //NOSONAR
                    } else { //NOSONAR
                        callbacks.showToast(if (fileObject.fileType == FileType.FOLDER) R.string.rename_folder_failed else R.string.rename_file_failed) //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        builder.negativeText(R.string.cancel) //NOSONAR
            .show() //NOSONAR
    } // NOSONAR

    // To do later: Remove context requirement. // NOSONAR
    private fun deleteFile(context: Context, folderView: FolderView, fileObject: BaseFileObject, callbacks: Callbacks) { //NOSONAR
        val builder = MaterialDialog.Builder(context) //NOSONAR
            .title(R.string.delete_item) //NOSONAR
            .iconRes(R.drawable.ic_warning_24dp) //NOSONAR
        if (fileObject.fileType == FileType.FILE) { //NOSONAR
            builder.content( //NOSONAR
                String.format( //NOSONAR
                    context.resources.getString( //NOSONAR
                        R.string.delete_file_confirmation_dialog //NOSONAR
                    ), fileObject.name //NOSONAR
                ) // NOSONAR
            ) // NOSONAR
        } else { //NOSONAR
            builder.content( //NOSONAR
                String.format( //NOSONAR
                    context.resources.getString( //NOSONAR
                        R.string.delete_folder_confirmation_dialog //NOSONAR
                    ), fileObject.path //NOSONAR
                ) // NOSONAR
            ) // NOSONAR
        } // NOSONAR
        builder.positiveText(R.string.button_ok) //NOSONAR
            .onPositive { materialDialog, dialogAction -> //NOSONAR
                if (FileHelper.deleteFile(File(fileObject.path))) { //NOSONAR
                    callbacks.onFileDeleted(folderView) //NOSONAR
                    CustomMediaScanner.scanFiles(context, listOf(fileObject.path), null) //NOSONAR
                } else { //NOSONAR
                    Toast.makeText( //NOSONAR
                        context, //NOSONAR
                        if (fileObject.fileType == FileType.FOLDER) R.string.delete_folder_failed else R.string.delete_file_failed, //NOSONAR
                        Toast.LENGTH_LONG //NOSONAR
                    ).show() //NOSONAR
                } // NOSONAR
            } // NOSONAR
        builder.negativeText(R.string.cancel) //NOSONAR
            .show() //NOSONAR
    } // NOSONAR

    fun setupFolderMenu(menu: PopupMenu, fileObject: BaseFileObject, playlistMenuHelper: PlaylistMenuHelper) { //NOSONAR

        menu.inflate(R.menu.menu_file) //NOSONAR

        // Add playlist menu // NOSONAR
        val subMenu = menu.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        playlistMenuHelper.createPlaylistMenu(subMenu) //NOSONAR

        if (!fileObject.canReadWrite()) { //NOSONAR
            menu.menu.findItem(R.id.rename).isVisible = false //NOSONAR
        } // NOSONAR

        when (fileObject.fileType) { //NOSONAR
            FileType.FILE -> menu.menu.findItem(R.id.play).isVisible = false //NOSONAR
            FileType.FOLDER -> { //NOSONAR
                menu.menu.findItem(R.id.songInfo).isVisible = false //NOSONAR
                menu.menu.findItem(R.id.ringtone).isVisible = false //NOSONAR
                menu.menu.findItem(R.id.share).isVisible = false //NOSONAR
                menu.menu.findItem(R.id.editTags).isVisible = false //NOSONAR
            } // NOSONAR
            FileType.PARENT -> { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun getFolderMenuClickListener( //NOSONAR
        fragment: Fragment, //NOSONAR
        mediaManager: MediaManager, //NOSONAR
        songsRepository: SongsRepository, //NOSONAR
        folderView: FolderView, //NOSONAR
        playlistManager: PlaylistManager, //NOSONAR
        callbacks: Callbacks //NOSONAR
    ): PopupMenu.OnMenuItemClickListener? { //NOSONAR
        when (folderView.baseFileObject.fileType) { //NOSONAR
            FileType.FILE -> return getFileMenuClickListener(fragment, mediaManager, songsRepository, folderView, folderView.baseFileObject as FileObject, playlistManager, callbacks) //NOSONAR
            FileType.FOLDER -> return getFolderMenuClickListener(fragment, mediaManager, songsRepository, folderView, folderView.baseFileObject as FolderObject, playlistManager, callbacks) //NOSONAR
        } // NOSONAR
        return null //NOSONAR
    } // NOSONAR

    private fun getFolderMenuClickListener( //NOSONAR
        fragment: Fragment, //NOSONAR
        mediaManager: MediaManager, //NOSONAR
        songsRepository: SongsRepository, //NOSONAR
        folderView: FolderView, //NOSONAR
        folderObject: FolderObject, //NOSONAR
        playlistManager: PlaylistManager, //NOSONAR
        callbacks: Callbacks //NOSONAR
    ): PopupMenu.OnMenuItemClickListener { //NOSONAR

        return PopupMenu.OnMenuItemClickListener { menuItem -> //NOSONAR
            when (menuItem.itemId) { //NOSONAR
                R.id.play -> { //NOSONAR
                    MenuUtils.play(mediaManager, getSongsForFolderObject(songsRepository, folderObject)) { callbacks.onPlaybackFailed() } //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.playNext -> { //NOSONAR
                    callbacks.playNext(getSongsForFolderObject(songsRepository, folderObject)) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    MenuUtils.newPlaylist( //NOSONAR
                        fragment, //NOSONAR
                        getSongsForFolderObject(songsRepository, folderObject) //NOSONAR
                    ) // NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    getSongsForFolderObject(songsRepository, folderObject).subscribe { songs -> //NOSONAR
                        MenuUtils.addToPlaylist( //NOSONAR
                            playlistManager, //NOSONAR
                            menuItem.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, //NOSONAR
                            songs //NOSONAR
                        ) { callbacks.onPlaylistItemsInserted() } //NOSONAR
                    } // NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    MenuUtils.addToQueue(mediaManager, getSongsForFolderObject(songsRepository, folderObject)) { callbacks.onSongsAddedToQueue(it) } //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.scan -> { //NOSONAR
                    scanFolder(fragment.context!!, folderObject) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.whitelist -> { //NOSONAR
                    callbacks.whitelist(getSongsForFolderObject(songsRepository, folderObject)) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.blacklist -> { //NOSONAR
                    callbacks.blacklist(getSongsForFolderObject(songsRepository, folderObject)) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.rename -> { //NOSONAR
                    renameFile(fragment.context!!, folderView, folderObject, callbacks) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    deleteFile(fragment.context!!, folderView, folderObject, callbacks) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private fun getFileMenuClickListener( //NOSONAR
        fragment: Fragment, //NOSONAR
        mediaManager: MediaManager, //NOSONAR
        songsRepository: SongsRepository, //NOSONAR
        folderView: FolderView, //NOSONAR
        fileObject: FileObject, //NOSONAR
        playlistManager: PlaylistManager, //NOSONAR
        callbacks: Callbacks //NOSONAR
    ): PopupMenu.OnMenuItemClickListener { //NOSONAR
        return PopupMenu.OnMenuItemClickListener { menuItem -> //NOSONAR

            val errorHandler: (Throwable) -> Unit = { e -> LogUtils.logException(TAG, "getFileMenuClickListener threw error", e) } //NOSONAR

            when (menuItem.itemId) { //NOSONAR
                R.id.playNext -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                            { song -> mediaManager.playNext(listOf(song)) { callbacks.onSongsAddedToQueue(it) } }, //NOSONAR
                            errorHandler //NOSONAR
                        ) // NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.NEW_PLAYLIST -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe( //NOSONAR
                        { song -> //NOSONAR
                            CreatePlaylistDialog.newInstance(listOf(song)).show(fragment.childFragmentManager, "CreatePlaylistDialog") //NOSONAR
                        }, // NOSONAR
                        errorHandler //NOSONAR
                    ) // NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                Defs.PLAYLIST_SELECTED -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ song -> //NOSONAR
                        MenuUtils.addToPlaylist( //NOSONAR
                            playlistManager, //NOSONAR
                            menuItem.intent.getSerializableExtra(PlaylistManager.ARG_PLAYLIST) as Playlist, //NOSONAR
                            listOf(song) //NOSONAR
                        ) { callbacks.onPlaylistItemsInserted() } //NOSONAR
                    }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.addToQueue -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ song -> MenuUtils.addToQueue(mediaManager, listOf(song), { callbacks.onSongsAddedToQueue(it) }) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.scan -> { //NOSONAR
                    scanFile(fragment.context!!, fileObject, callbacks) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.editTags -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ callbacks.showTagEditor(it) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.share -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ callbacks.shareSong(it) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.ringtone -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ callbacks.setRingtone(it) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.songInfo -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ callbacks.showSongInfo(it) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.blacklist -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ callbacks.blacklist(it) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.whitelist -> { //NOSONAR
                    getSongForFile(songsRepository, fileObject).subscribe({ callbacks.whitelist(it) }, errorHandler) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.rename -> { //NOSONAR
                    renameFile(fragment.context!!, folderView, fileObject, callbacks) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
                R.id.delete -> { //NOSONAR
                    deleteFile(fragment.context!!, folderView, fileObject, callbacks) //NOSONAR
                    return@OnMenuItemClickListener true //NOSONAR
                } // NOSONAR
            } // NOSONAR
            false //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
