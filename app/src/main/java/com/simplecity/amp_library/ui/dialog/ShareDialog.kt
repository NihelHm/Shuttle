@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.extensions.share
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ShareDialog : DialogFragment() { //NOSONAR

    private lateinit var song: Song //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        super.onAttach(context) //NOSONAR

        song = arguments!!.getSerializable(ARG_SONG) as Song //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.share_dialog_title) //NOSONAR
            .items(context!!.getString(R.string.share_option_song_info), context!!.getString(R.string.share_option_audio_file)) //NOSONAR
            .itemsCallback { _, _, i, _ -> //NOSONAR
                when (i) { //NOSONAR
                    0 -> { //NOSONAR
                        val context = context //NOSONAR
                        // Use the compress method on the Bitmap object to write image to the OutputStream
                        Glide.with(context) //NOSONAR
                            .load(song) //NOSONAR
                            .asBitmap() //NOSONAR
                            .priority(Priority.IMMEDIATE) //NOSONAR
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
                            .into(object : SimpleTarget<Bitmap>() { //NOSONAR
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>) { //NOSONAR
                                    val sendIntent = Intent() //NOSONAR
                                    sendIntent.type = "text/plain" //NOSONAR
                                    var fileOutputStream: FileOutputStream? = null //NOSONAR
                                    try { //NOSONAR
                                        val file = File(context!!.filesDir.toString() + "/share_image.jpg") //NOSONAR
                                        fileOutputStream = FileOutputStream(file) //NOSONAR
                                        if (resource != null) { //NOSONAR
                                            resource.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream) //NOSONAR
                                            sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)) //NOSONAR
                                            sendIntent.type = "image/jpeg" //NOSONAR
                                        }
                                    } catch (ignored: FileNotFoundException) { //NOSONAR
                                        // Intentionally left empty.
                                    } finally { //NOSONAR
                                        try { //NOSONAR
                                            fileOutputStream?.close() //NOSONAR
                                        } catch (ignored: IOException) { //NOSONAR
                                            // Intentionally left empty.
                                        }
                                    }

                                    sendIntent.action = Intent.ACTION_SEND //NOSONAR
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "#NowPlaying " + song.artistName + " - " + song.name + "\n\n" + "#Shuttle") //NOSONAR
                                    context!!.startActivity(Intent.createChooser(sendIntent, "Share current song via: ")) //NOSONAR
                                }
                            })
                    }
                    1 -> song.share(context!!) //NOSONAR
                }
            }
            .negativeText(R.string.close) //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "ShareDialog" //NOSONAR

        private const val ARG_SONG = "song" //NOSONAR

        fun newInstance(song: Song): ShareDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_SONG, song) //NOSONAR
            val fragment = ShareDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        }
    }
}
