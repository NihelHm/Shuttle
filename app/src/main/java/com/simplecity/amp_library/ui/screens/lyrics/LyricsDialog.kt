@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.lyrics

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.utils.ViewUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LyricsDialog : DialogFragment(), LyricsView { //NOSONAR

    @Inject lateinit var lyricsPresenter: LyricsPresenter //NOSONAR

    private var lyricsTextView: TextView? = null //NOSONAR

    private var noLyricsView: View? = null //NOSONAR

    private var quickLyricInfo: View? = null //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        @SuppressLint("InflateParams") //NOSONAR
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_lyrics, null) //NOSONAR

        lyricsTextView = customView.findViewById(R.id.text1) //NOSONAR

        noLyricsView = customView.findViewById(R.id.noLyricsView) //NOSONAR

        val quickLyricButton = customView.findViewById<Button>(R.id.quickLyricButton) //NOSONAR
        quickLyricButton.text = QuickLyricUtils.getSpannedString(context!!) //NOSONAR
        quickLyricButton.setOnClickListener { lyricsPresenter.downloadOrLaunchQuickLyric() } //NOSONAR

        quickLyricInfo = customView.findViewById(R.id.quickLyricInfo) //NOSONAR
        quickLyricInfo!!.setOnClickListener { lyricsPresenter.showQuickLyricInfoDialog() } //NOSONAR

        val quickLyricsLayout = customView.findViewById<View>(R.id.quickLyricLayout) //NOSONAR
        if (!QuickLyricUtils.canDownloadQuickLyric(context)) { //NOSONAR
            quickLyricsLayout.visibility = View.GONE //NOSONAR
        }

        lyricsPresenter.bindView(this) //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .customView(customView, false) //NOSONAR
            .title(R.string.lyrics) //NOSONAR
            .negativeText(R.string.close) //NOSONAR
            .build() //NOSONAR
    }

    override fun updateLyrics(lyrics: String?) { //NOSONAR
        lyricsTextView!!.text = lyrics //NOSONAR
    }

    override fun showNoLyricsView(show: Boolean) { //NOSONAR
        if (show) { //NOSONAR
            ViewUtils.fadeOut(lyricsTextView) { //NOSONAR
                if (noLyricsView!!.visibility == View.GONE) { //NOSONAR
                    ViewUtils.fadeIn(noLyricsView!!, null) //NOSONAR
                }
            }
        } else { //NOSONAR
            ViewUtils.fadeOut(noLyricsView) { //NOSONAR
                if (lyricsTextView!!.visibility == View.GONE) { //NOSONAR
                    ViewUtils.fadeIn(lyricsTextView!!, null) //NOSONAR
                }
            }
        }
    }

    override fun showQuickLyricInfoButton(show: Boolean) { //NOSONAR
        quickLyricInfo!!.visibility = if (show) View.VISIBLE else View.GONE //NOSONAR
    }

    override fun launchQuickLyric(song: Song) { //NOSONAR
        QuickLyricUtils.getLyricsFor(context!!, song) //NOSONAR
    }

    override fun downloadQuickLyric() { //NOSONAR
        try { //NOSONAR
            startActivity(QuickLyricUtils.getQuickLyricIntent()) //NOSONAR
        } catch (ignored: ActivityNotFoundException) { //NOSONAR
            // If the user doesn't have the play store on their device
        }
    }

    override fun showQuickLyricInfoDialog() { //NOSONAR
        MaterialDialog.Builder(context!!) //NOSONAR
            .iconRes(R.drawable.quicklyric) //NOSONAR
            .title(R.string.quicklyric) //NOSONAR
            .content(context!!.getString(R.string.quicklyric_info)) //NOSONAR
            .positiveText(R.string.download) //NOSONAR
            .onPositive { _, _ -> lyricsPresenter.downloadOrLaunchQuickLyric() } //NOSONAR
            .negativeText(R.string.close) //NOSONAR
            .show() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "LyricsDialog" //NOSONAR

        fun newInstance() = LyricsDialog() //NOSONAR
    }
}
