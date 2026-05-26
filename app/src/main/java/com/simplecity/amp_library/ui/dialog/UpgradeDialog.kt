@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.android.billingclient.api.BillingClient
import com.simplecity.amp_library.R
import com.simplecity.amp_library.billing.BillingManager
import com.simplecity.amp_library.constants.Config
import com.simplecity.amp_library.utils.ShuttleUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UpgradeDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var billingManager: BillingManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(context!!.resources.getString(R.string.get_pro_title)) //NOSONAR
            .content(context!!.resources.getString(R.string.upgrade_dialog_message)) //NOSONAR
            .positiveText(R.string.btn_upgrade) //NOSONAR
            .onPositive { _, _ -> //NOSONAR
                if (ShuttleUtils.isAmazonBuild()) { //NOSONAR
                    val storeIntent = ShuttleUtils.getShuttleStoreIntent("com.simplecity.amp_pro") //NOSONAR
                    if (storeIntent.resolveActivity(context!!.packageManager) != null) { //NOSONAR
                        context!!.startActivity(storeIntent) //NOSONAR
                    } else { //NOSONAR
                        context!!.startActivity(ShuttleUtils.getShuttleWebIntent("com.simplecity.amp_pro")) //NOSONAR
                    }
                } else { //NOSONAR
                    billingManager.initiatePurchaseFlow(Config.SKU_PREMIUM, BillingClient.SkuType.INAPP) //NOSONAR
                }
            }
            .negativeText(R.string.get_pro_button_no) //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR
        const val TAG = "UpgradeDialog" //NOSONAR

        fun newInstance() = UpgradeDialog() //NOSONAR
    }
}
