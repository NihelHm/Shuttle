@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.content.Intent // NOSONAR
import android.net.Uri // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import android.support.v4.content.ContextCompat // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.webkit.WebView // NOSONAR
import android.webkit.WebViewClient // NOSONAR
import android.widget.CheckBox // NOSONAR
import android.widget.ProgressBar // NOSONAR
import com.afollestad.aesthetic.Aesthetic // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.ViewUtils // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import javax.inject.Inject // NOSONAR

class ChangelogDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    } // NOSONAR

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
            } // NOSONAR

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean { //NOSONAR
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)) //NOSONAR
                if (intent.resolveActivity(context!!.packageManager) != null) { //NOSONAR
                    context?.startActivity(intent) //NOSONAR
                    return true //NOSONAR
                } // NOSONAR
                return false //NOSONAR
            } // NOSONAR
        } // NOSONAR

        Aesthetic.get(context) //NOSONAR
            .isDark //NOSONAR
            .take(1) //NOSONAR
            .subscribe { isDark -> webView.loadUrl(if (isDark) "file:///android_asset/web/info_dark.html" else "file:///android_asset/web/info.html") } //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.pref_title_changelog) //NOSONAR
            .customView(customView, false) //NOSONAR
            .negativeText(R.string.close) //NOSONAR
            .build() //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        private const val TAG = "ChangelogDialog" //NOSONAR

        fun newInstance() = ChangelogDialog() //NOSONAR
    } // NOSONAR
} // NOSONAR
