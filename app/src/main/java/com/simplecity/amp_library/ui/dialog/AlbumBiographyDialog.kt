@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.R.string
import com.simplecity.amp_library.http.HttpClient
import com.simplecity.amp_library.http.lastfm.LastFmAlbum
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.utils.ShuttleUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumBiographyDialog : DialogFragment() { //NOSONAR

    private lateinit var album: Album //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        super.onAttach(context) //NOSONAR

        album = arguments!!.getSerializable(ARG_ALBUM) as Album //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_biography, null, false) //NOSONAR

        val progressBar = customView.findViewById<ProgressBar>(R.id.progress) //NOSONAR
        val message = customView.findViewById<TextView>(R.id.message) //NOSONAR

        HttpClient.getInstance().lastFmService.getLastFmAlbumResult(album.albumArtistName, album.name).enqueue(object : Callback<LastFmAlbum> { //NOSONAR
            override fun onResponse(call: Call<LastFmAlbum>, response: Response<LastFmAlbum>) { //NOSONAR
                progressBar.visibility = View.GONE //NOSONAR
                if (response.isSuccessful) { //NOSONAR
                    if (response.body() != null && response.body()!!.album != null && response.body()!!.album.wiki != null) { //NOSONAR
                        val summary = response.body()!!.album.wiki.summary //NOSONAR
                        if (ShuttleUtils.hasNougat()) { //NOSONAR
                            message.text = Html.fromHtml(summary, Html.FROM_HTML_MODE_COMPACT) //NOSONAR
                        } else { //NOSONAR
                            message.text = Html.fromHtml(summary) //NOSONAR
                        }
                    } else { //NOSONAR
                        message.setText(string.no_album_info) //NOSONAR
                    }
                }
            }

            override fun onFailure(call: Call<LastFmAlbum>, t: Throwable) { //NOSONAR
                progressBar.visibility = View.GONE //NOSONAR
                message.setText(string.no_album_info) //NOSONAR
            }
        })

        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.info) //NOSONAR
            .customView(customView, false) //NOSONAR
            .negativeText(R.string.close) //NOSONAR

        return builder.build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "AlbumBiographyDialog" //NOSONAR

        private const val ARG_ALBUM = "album" //NOSONAR

        fun newInstance(album: Album): AlbumBiographyDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_ALBUM, album) //NOSONAR
            val fragment = AlbumBiographyDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        }
    }
}
