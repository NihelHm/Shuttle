@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.utils.SettingsManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UpgradeNagDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        settingsManager.setNagMessageRead() //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .title(context!!.resources.getString(R.string.get_pro_title)) //NOSONAR
            .content(context!!.resources.getString(R.string.get_pro_message)) //NOSONAR
            .positiveText(R.string.btn_upgrade) //NOSONAR
            .onPositive { dialog, which -> //NOSONAR
                // To do later: Show IAP or open Play Store
            }
            .negativeText(R.string.get_pro_button_no) //NOSONAR

        return builder.build() //NOSONAR
    }
}
