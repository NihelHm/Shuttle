@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckBox
import android.widget.ProgressBar
import com.afollestad.aesthetic.Aesthetic
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.ViewUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ChangelogDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        @SuppressLint("InflateParams") //NOSONAR
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_changelog, null) //NOSONAR

        val webView = customView.findViewById<WebView>(R.id.webView) //NOSONAR
        webView.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent)) //NOSONAR

        val checkBox = customView.findViewById<CheckBox>(R.id.checkbox) //NOSONAR
        checkBox.isChecked = settingsManager.showChangelogOnLaunch //NOSONAR
        checkBox.setOnCheckedChangeListener { buttonView, isChecked -> settingsManager.showChangelogOnLaunch = isChecked } //NOSONAR

        val progressBar = customView.findViewById<ProgressBar>(R.id.progress) //NOSONAR

        webView.webViewClient = object : WebViewClient() { //NOSONAR
            override fun onPageFinished(view: WebView, url: String) { //NOSONAR
                super.onPageFinished(view, url) //NOSONAR

                ViewUtils.fadeOut(progressBar) { ViewUtils.fadeIn(webView, null) } //NOSONAR
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean { //NOSONAR
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)) //NOSONAR
                if (intent.resolveActivity(context!!.packageManager) != null) { //NOSONAR
                    context?.startActivity(intent) //NOSONAR
                    return true //NOSONAR
                }
                return false //NOSONAR
            }
        }

        Aesthetic.get(context) //NOSONAR
            .isDark //NOSONAR
            .take(1) //NOSONAR
            .subscribe { isDark -> webView.loadUrl(if (isDark) "file:///android_asset/web/info_dark.html" else "file:///android_asset/web/info.html") } //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.pref_title_changelog) //NOSONAR
            .customView(customView, false) //NOSONAR
            .negativeText(R.string.close) //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR
        private const val TAG = "ChangelogDialog" //NOSONAR

        fun newInstance() = ChangelogDialog() //NOSONAR
    }
}
