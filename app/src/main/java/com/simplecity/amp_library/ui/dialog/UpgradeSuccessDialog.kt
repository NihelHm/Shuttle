@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.ui.screens.main.MainActivity

class UpgradeSuccessDialog : DialogFragment() { //NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(context!!.resources.getString(R.string.upgraded_title)) //NOSONAR
            .content(context!!.resources.getString(R.string.upgraded_message)) //NOSONAR
            .positiveText(R.string.restart_button) //NOSONAR
            .onPositive { _, _ -> //NOSONAR
                val intent = Intent(context, MainActivity::class.java) //NOSONAR
                val componentName = intent.component //NOSONAR
                val mainIntent = Intent.makeRestartActivityTask(componentName) //NOSONAR
                startActivity(mainIntent) //NOSONAR
            }
            .build() //NOSONAR
    }
}
