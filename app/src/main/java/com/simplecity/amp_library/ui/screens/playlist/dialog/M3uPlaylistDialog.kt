@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.playlist.dialog

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.widget.Toast
import com.simplecity.amp_library.R
import com.simplecity.amp_library.data.SongsRepository
import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.LogUtils
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class M3uPlaylistDialog : DialogFragment() { //NOSONAR

    private lateinit var playlist: Playlist //NOSONAR

    @Inject lateinit var songsRepository: SongsRepository //NOSONAR

    private var disposable: Disposable? = null //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR

        playlist = arguments!!.getSerializable(ARG_PLAYLIST) as Playlist //NOSONAR

        val progressDialog = ProgressDialog(context) //NOSONAR
        progressDialog.isIndeterminate = true //NOSONAR
        progressDialog.setTitle(R.string.saving_playlist) //NOSONAR

        disposable = songsRepository.getSongs(playlist) //NOSONAR
            .first(emptyList()) //NOSONAR
            .map(Function<List<Song>, File> { songs -> //NOSONAR
                if (!songs.isEmpty()) { //NOSONAR

                    var playlistFile: File? = null //NOSONAR

                    if (Environment.getExternalStorageDirectory().canWrite()) { //NOSONAR
                        val root = File(Environment.getExternalStorageDirectory(), "Playlists/Export/") //NOSONAR
                        if (!root.exists()) { //NOSONAR
                            root.mkdirs() //NOSONAR
                        }

                        val noMedia = File(root, ".nomedia") //NOSONAR
                        if (!noMedia.exists()) { //NOSONAR
                            try { //NOSONAR
                                noMedia.createNewFile() //NOSONAR
                            } catch (e: IOException) { //NOSONAR
                                e.printStackTrace() //NOSONAR
                            }
                        }

                        val name = playlist.name.replace("[^a-zA-Z0-9.-]".toRegex(), "_") //NOSONAR

                        playlistFile = File(root, "$name.m3u") //NOSONAR

                        var i = 0 //NOSONAR
                        while (playlistFile!!.exists()) { //NOSONAR
                            i++ //NOSONAR
                            playlistFile = File(root, "$name$i.m3u") //NOSONAR
                        }

                        try { //NOSONAR
                            val fileWriter = FileWriter(playlistFile) //NOSONAR
                            val body = StringBuilder() //NOSONAR
                            body.append("#EXTM3U\n") //NOSONAR

                            for (song in songs) { //NOSONAR
                                body.append("#EXTINF:") //NOSONAR
                                    .append(song.duration / 1000) //NOSONAR
                                    .append(",") //NOSONAR
                                    .append(song.name) //NOSONAR
                                    .append(" - ") //NOSONAR
                                    .append(song.artistName) //NOSONAR
                                    .append("\n") //NOSONAR
                                    //To do later: Use relative paths instead of absolute
                                    .append(song.path) //NOSONAR
                                    .append("\n") //NOSONAR
                            }
                            fileWriter.append(body) //NOSONAR
                            fileWriter.flush() //NOSONAR
                            fileWriter.close() //NOSONAR
                        } catch (e: IOException) { //NOSONAR
                            Log.e(TAG, "Failed to write file: $e") //NOSONAR
                        }

                    }
                    return@Function playlistFile //NOSONAR
                }
                null //NOSONAR
            })
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { file -> //NOSONAR
                    progressDialog.dismiss() //NOSONAR
                    if (file != null) { //NOSONAR
                        Toast.makeText(context, String.format(context!!.getString(R.string.playlist_saved), file.path), Toast.LENGTH_LONG).show() //NOSONAR
                    } else { //NOSONAR
                        Toast.makeText(context, R.string.playlist_save_failed, Toast.LENGTH_SHORT).show() //NOSONAR
                    }
                },
                { error -> LogUtils.logException(TAG, "Error saving m3u playlist", error) } //NOSONAR
            )

        return progressDialog //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        disposable!!.dispose() //NOSONAR
        super.onDestroyView() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "M3uPlaylistDialog" //NOSONAR

        private const val ARG_PLAYLIST = "playlist" //NOSONAR

        fun newInstance(playlist: Playlist): M3uPlaylistDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_PLAYLIST, playlist) //NOSONAR
            val fragment = M3uPlaylistDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        }
    }
}

@Module(includes = arrayOf(FragmentModule::class)) //NOSONAR
abstract class M3uDialogFragmentModule { //NOSONAR

    @Binds //NOSONAR
    @Named(FragmentModule.FRAGMENT) //NOSONAR
    @FragmentScope //NOSONAR
    internal abstract fun fragment(m3uPlaylistDialog: M3uPlaylistDialog): Fragment //NOSONAR
}
