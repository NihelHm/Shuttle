@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.R.id.album
import com.simplecity.amp_library.R.id.artist
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SongInfoDialog : DialogFragment() { //NOSONAR

    private lateinit var song: Song //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        super.onAttach(context) //NOSONAR

        song = arguments!!.getSerializable(ARG_SONG) as Song //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        @SuppressLint("InflateParams") //NOSONAR
        val view = (context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.dialog_song_info, null) //NOSONAR

        val titleView = view.findViewById<View>(R.id.title) //NOSONAR
        val titleKey = titleView.findViewById<TextView>(R.id.key) //NOSONAR
        titleKey.setText(R.string.song_title) //NOSONAR
        val titleValue = titleView.findViewById<TextView>(R.id.value) //NOSONAR
        titleValue.text = song.name //NOSONAR

        val trackNumberView = view.findViewById<View>(R.id.track_number) //NOSONAR
        val trackNumberKey = trackNumberView.findViewById<TextView>(R.id.key) //NOSONAR
        trackNumberKey.setText(R.string.track_number) //NOSONAR
        val trackNumberValue = trackNumberView.findViewById<TextView>(R.id.value) //NOSONAR
        trackNumberValue.text = song.trackNumberLabel.toString() //NOSONAR

        val artistView = view.findViewById<View>(artist) //NOSONAR
        val artistKey = artistView.findViewById<TextView>(R.id.key) //NOSONAR
        artistKey.setText(R.string.artist_title) //NOSONAR
        val artistValue = artistView.findViewById<TextView>(R.id.value) //NOSONAR
        artistValue.text = song.artistName //NOSONAR

        val albumView = view.findViewById<View>(album) //NOSONAR
        val albumKey = albumView.findViewById<TextView>(R.id.key) //NOSONAR
        albumKey.setText(R.string.album_title) //NOSONAR
        val albumValue = albumView.findViewById<TextView>(R.id.value) //NOSONAR
        albumValue.text = song.albumName //NOSONAR

        val genreView = view.findViewById<View>(R.id.genre) //NOSONAR
        val genreKey = genreView.findViewById<TextView>(R.id.key) //NOSONAR
        genreKey.setText(R.string.genre_title) //NOSONAR
        val genreValue = genreView.findViewById<TextView>(R.id.value) //NOSONAR
        song.getGenre(context!!.applicationContext as ShuttleApplication) //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe({ genre -> genreValue.text = genre.name }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Error getting genre", error) }) //NOSONAR

        val albumArtistView = view.findViewById<View>(R.id.album_artist) //NOSONAR
        val albumArtistKey = albumArtistView.findViewById<TextView>(R.id.key) //NOSONAR
        albumArtistKey.setText(R.string.album_artist_title) //NOSONAR
        val albumArtistValue = albumArtistView.findViewById<TextView>(R.id.value) //NOSONAR
        albumArtistValue.text = song.albumArtistName //NOSONAR

        val durationView = view.findViewById<View>(R.id.duration) //NOSONAR
        val durationKey = durationView.findViewById<TextView>(R.id.key) //NOSONAR
        durationKey.setText(R.string.sort_song_duration) //NOSONAR
        val durationValue = durationView.findViewById<TextView>(R.id.value) //NOSONAR
        durationValue.text = song.getDurationLabel(context) //NOSONAR

        val pathView = view.findViewById<View>(R.id.path) //NOSONAR
        val pathKey = pathView.findViewById<TextView>(R.id.key) //NOSONAR
        pathKey.setText(R.string.song_info_path) //NOSONAR
        val pathValue = pathView.findViewById<TextView>(R.id.value) //NOSONAR
        pathValue.text = song.path //NOSONAR

        val discNumberView = view.findViewById<View>(R.id.disc_number) //NOSONAR
        val discNumberKey = discNumberView.findViewById<TextView>(R.id.key) //NOSONAR
        discNumberKey.setText(R.string.disc_number) //NOSONAR
        val discNumberValue = discNumberView.findViewById<TextView>(R.id.value) //NOSONAR
        discNumberValue.text = song.discNumberLabel.toString() //NOSONAR

        val fileSizeView = view.findViewById<View>(R.id.file_size) //NOSONAR
        val fileSizeKey = fileSizeView.findViewById<TextView>(R.id.key) //NOSONAR
        fileSizeKey.setText(R.string.song_info_file_size) //NOSONAR
        val fileSizeValue = fileSizeView.findViewById<TextView>(R.id.value) //NOSONAR
        fileSizeValue.text = song.fileSizeLabel //NOSONAR

        val formatView = view.findViewById<View>(R.id.format) //NOSONAR
        val formatKey = formatView.findViewById<TextView>(R.id.key) //NOSONAR
        formatKey.setText(R.string.song_info_format) //NOSONAR
        val formatValue = formatView.findViewById<TextView>(R.id.value) //NOSONAR
        formatValue.text = song.formatLabel //NOSONAR

        val bitrateView = view.findViewById<View>(R.id.bitrate) //NOSONAR
        val bitrateKey = bitrateView.findViewById<TextView>(R.id.key) //NOSONAR
        bitrateKey.setText(R.string.song_info_bitrate) //NOSONAR
        val bitrateValue = bitrateView.findViewById<TextView>(R.id.value) //NOSONAR
        bitrateValue.text = song.getBitrateLabel(context) //NOSONAR

        val samplingRateView = view.findViewById<View>(R.id.sample_rate) //NOSONAR
        val samplingRateKey = samplingRateView.findViewById<TextView>(R.id.key) //NOSONAR
        samplingRateKey.setText(R.string.song_info_sample_Rate) //NOSONAR
        val samplingRateValue = samplingRateView.findViewById<TextView>(R.id.value) //NOSONAR
        samplingRateValue.text = song.getSampleRateLabel(context) //NOSONAR

        val playCountView = view.findViewById<View>(R.id.play_count) //NOSONAR
        val playCountKey = playCountView.findViewById<TextView>(R.id.key) //NOSONAR
        playCountKey.setText(R.string.song_info_play_count) //NOSONAR
        val playCountValue = playCountView.findViewById<TextView>(R.id.value) //NOSONAR

        Observable.fromCallable { song.getPlayCount(context) } //NOSONAR
            .subscribeOn(Schedulers.io()) //NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe({ playCount -> playCountValue.text = playCount.toString() }, //NOSONAR
                { error -> LogUtils.logException(TAG, "Error getting play count", error) }) //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(context!!.getString(R.string.dialog_song_info_title)) //NOSONAR
            .customView(view, false) //NOSONAR
            .negativeText(R.string.close) //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "SongInfoDialog" //NOSONAR

        private const val ARG_SONG = "song" //NOSONAR

        fun newInstance(song: Song): SongInfoDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putSerializable(ARG_SONG, song) //NOSONAR
            val fragment = SongInfoDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        }
    }
}
