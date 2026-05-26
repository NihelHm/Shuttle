@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import android.text.Html // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.View // NOSONAR
import android.widget.ProgressBar // NOSONAR
import android.widget.TextView // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.R.string // NOSONAR
import com.simplecity.amp_library.http.HttpClient // NOSONAR
import com.simplecity.amp_library.http.lastfm.LastFmAlbum // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils // NOSONAR
import retrofit2.Call // NOSONAR
import retrofit2.Callback // NOSONAR
import retrofit2.Response // NOSONAR

class AlbumBiographyDialog : DialogFragment() { //NOSONAR

    private lateinit var album: Album //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        super.onAttach(context) //NOSONAR

        album = arguments!!.getSerializable(ARG_ALBUM) as Album //NOSONAR
    } // NOSONAR

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
                        } // NOSONAR
                    } else { //NOSONAR
                        message.setText(string.no_album_info) //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR

            override fun onFailure(call: Call<LastFmAlbum>, t: Throwable) { //NOSONAR
                progressBar.visibility = View.GONE //NOSONAR
                message.setText(string.no_album_info) //NOSONAR
            } // NOSONAR
        }) // NOSONAR

        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.info) //NOSONAR
            .customView(customView, false) //NOSONAR
            .negativeText(R.string.close) //NOSONAR

        return builder.build() //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "AlbumBiographyDialog" //NOSONAR

        private const val ARG_ALBUM = "album" //NOSONAR

        fun newInstance(album: Album): AlbumBiographyDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_ALBUM, album) //NOSONAR
            val fragment = AlbumBiographyDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
