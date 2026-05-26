@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.View // NOSONAR
import android.widget.TextView // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.R.id.album // NOSONAR
import com.simplecity.amp_library.R.id.artist // NOSONAR
import com.simplecity.amp_library.model.FileObject // NOSONAR
import com.simplecity.amp_library.utils.FileHelper // NOSONAR

class FileInfoDialog : DialogFragment() { //NOSONAR

    private var fileObject: FileObject? = null //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        super.onAttach(context) //NOSONAR

        fileObject = arguments!!.getSerializable(ARG_FILE_OBJECT) as FileObject //NOSONAR
    } // NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        val view = (context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.dialog_song_info, null) //NOSONAR

        val titleView = view.findViewById<View>(R.id.title) //NOSONAR
        val titleKey = titleView.findViewById<TextView>(R.id.key) //NOSONAR
        titleKey.setText(R.string.song_title) //NOSONAR
        val titleValue = titleView.findViewById<TextView>(R.id.value) //NOSONAR
        titleValue.text = fileObject!!.tagInfo.trackName //NOSONAR

        val trackNumberView = view.findViewById<View>(R.id.track_number) //NOSONAR
        val trackNumberKey = trackNumberView.findViewById<TextView>(R.id.key) //NOSONAR
        trackNumberKey.setText(R.string.track_number) //NOSONAR
        val trackNumberValue = trackNumberView.findViewById<TextView>(R.id.value) //NOSONAR
        if (fileObject!!.tagInfo.trackTotal != 0) { //NOSONAR
            trackNumberValue.text = String.format(context!!.getString(R.string.track_count), fileObject!!.tagInfo.trackNumber.toString(), fileObject!!.tagInfo.trackTotal.toString()) //NOSONAR
        } else { //NOSONAR
            trackNumberValue.text = fileObject!!.tagInfo.trackNumber.toString() //NOSONAR
        } // NOSONAR

        val artistView = view.findViewById<View>(artist) //NOSONAR
        val artistKey = artistView.findViewById<TextView>(R.id.key) //NOSONAR
        artistKey.setText(R.string.artist_title) //NOSONAR
        val artistValue = artistView.findViewById<TextView>(R.id.value) //NOSONAR
        artistValue.text = fileObject!!.tagInfo.artistName //NOSONAR

        val albumView = view.findViewById<View>(album) //NOSONAR
        val albumKey = albumView.findViewById<TextView>(R.id.key) //NOSONAR
        albumKey.setText(R.string.album_title) //NOSONAR
        val albumValue = albumView.findViewById<TextView>(R.id.value) //NOSONAR
        albumValue.text = fileObject!!.tagInfo.albumName //NOSONAR

        val genreView = view.findViewById<View>(R.id.genre) //NOSONAR
        val genreKey = genreView.findViewById<TextView>(R.id.key) //NOSONAR
        genreKey.setText(R.string.genre_title) //NOSONAR
        val genreValue = genreView.findViewById<TextView>(R.id.value) //NOSONAR
        genreValue.text = fileObject!!.tagInfo.genre //NOSONAR

        val albumArtistView = view.findViewById<View>(R.id.album_artist) //NOSONAR
        val albumArtistKey = albumArtistView.findViewById<TextView>(R.id.key) //NOSONAR
        albumArtistKey.setText(R.string.album_artist_title) //NOSONAR
        val albumArtistValue = albumArtistView.findViewById<TextView>(R.id.value) //NOSONAR
        albumArtistValue.text = fileObject!!.tagInfo.albumArtistName //NOSONAR

        val durationView = view.findViewById<View>(R.id.duration) //NOSONAR
        val durationKey = durationView.findViewById<TextView>(R.id.key) //NOSONAR
        durationKey.setText(R.string.sort_song_duration) //NOSONAR
        val durationValue = durationView.findViewById<TextView>(R.id.value) //NOSONAR
        durationValue.text = fileObject!!.getTimeString(context) //NOSONAR

        val pathView = view.findViewById<View>(R.id.path) //NOSONAR
        val pathKey = pathView.findViewById<TextView>(R.id.key) //NOSONAR
        pathKey.setText(R.string.song_info_path) //NOSONAR
        val pathValue = pathView.findViewById<TextView>(R.id.value) //NOSONAR
        pathValue.text = fileObject!!.path + "/" + fileObject!!.name + "." + fileObject!!.extension //NOSONAR

        val discNumberView = view.findViewById<View>(R.id.disc_number) //NOSONAR
        val discNumberKey = discNumberView.findViewById<TextView>(R.id.key) //NOSONAR
        discNumberKey.setText(R.string.disc_number) //NOSONAR
        val discNumberValue = discNumberView.findViewById<TextView>(R.id.value) //NOSONAR
        if (fileObject!!.tagInfo.discTotal != 0) { //NOSONAR
            discNumberValue.text = String.format(context!!.getString(R.string.track_count), fileObject!!.tagInfo.discNumber.toString(), fileObject!!.tagInfo.discTotal.toString()) //NOSONAR
        } else { //NOSONAR
            discNumberValue.text = fileObject!!.tagInfo.discNumber.toString() //NOSONAR
        } // NOSONAR

        val fileSizeView = view.findViewById<View>(R.id.file_size) //NOSONAR
        val fileSizeKey = fileSizeView.findViewById<TextView>(R.id.key) //NOSONAR
        fileSizeKey.setText(R.string.song_info_file_size) //NOSONAR
        val fileSizeValue = fileSizeView.findViewById<TextView>(R.id.value) //NOSONAR
        fileSizeValue.text = FileHelper.getHumanReadableSize(fileObject!!.size) //NOSONAR

        val formatView = view.findViewById<View>(R.id.format) //NOSONAR
        val formatKey = formatView.findViewById<TextView>(R.id.key) //NOSONAR
        formatKey.setText(R.string.song_info_format) //NOSONAR
        val formatValue = formatView.findViewById<TextView>(R.id.value) //NOSONAR
        formatValue.text = fileObject!!.tagInfo.format //NOSONAR

        val bitrateView = view.findViewById<View>(R.id.bitrate) //NOSONAR
        val bitrateKey = bitrateView.findViewById<TextView>(R.id.key) //NOSONAR
        bitrateKey.setText(R.string.song_info_bitrate) //NOSONAR
        val bitrateValue = bitrateView.findViewById<TextView>(R.id.value) //NOSONAR
        bitrateValue.text = fileObject!!.tagInfo.bitrate + context!!.getString(R.string.song_info_bitrate_suffix) //NOSONAR

        val samplingRateView = view.findViewById<View>(R.id.sample_rate) //NOSONAR
        val samplingRateKey = samplingRateView.findViewById<TextView>(R.id.key) //NOSONAR
        samplingRateKey.setText(R.string.song_info_sample_Rate) //NOSONAR
        val samplingRateValue = samplingRateView.findViewById<TextView>(R.id.value) //NOSONAR
        samplingRateValue.text = (fileObject!!.tagInfo.sampleRate / 1000).toString() + context!!.getString(R.string.song_info_sample_rate_suffix) //NOSONAR

        val playCountView = view.findViewById<View>(R.id.play_count) //NOSONAR
        playCountView.visibility = View.GONE //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(context!!.getString(R.string.dialog_song_info_title)) //NOSONAR
            .customView(view, false) //NOSONAR
            .negativeText(R.string.close) //NOSONAR
            .show() //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "FileInfoDialog" //NOSONAR

        private const val ARG_FILE_OBJECT = "file" //NOSONAR

        fun newInstance(fileObject: FileObject): FileInfoDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_FILE_OBJECT, fileObject) //NOSONAR
            val fragment = FileInfoDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
