@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog // NOSONAR

import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.view.View // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import javax.inject.Inject // NOSONAR

class UpgradeNagDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    } // NOSONAR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        settingsManager.setNagMessageRead() //NOSONAR
    } // NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .title(context!!.resources.getString(R.string.get_pro_title)) //NOSONAR
            .content(context!!.resources.getString(R.string.get_pro_message)) //NOSONAR
            .positiveText(R.string.btn_upgrade) //NOSONAR
            .onPositive { dialog, which -> //NOSONAR
                // To do later: Show IAP or open Play Store // NOSONAR
            } // NOSONAR
            .negativeText(R.string.get_pro_button_no) //NOSONAR

        return builder.build() //NOSONAR
    } // NOSONAR
} // NOSONAR
