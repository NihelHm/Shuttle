@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog // NOSONAR

import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.android.billingclient.api.BillingClient // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.billing.BillingManager // NOSONAR
import com.simplecity.amp_library.constants.Config // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import javax.inject.Inject // NOSONAR

class UpgradeDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var billingManager: BillingManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    } // NOSONAR

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
                    } // NOSONAR
                } else { //NOSONAR
                    billingManager.initiatePurchaseFlow(Config.SKU_PREMIUM, BillingClient.SkuType.INAPP) //NOSONAR
                } // NOSONAR
            } // NOSONAR
            .negativeText(R.string.get_pro_button_no) //NOSONAR
            .build() //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val TAG = "UpgradeDialog" //NOSONAR

        fun newInstance() = UpgradeDialog() //NOSONAR
    } // NOSONAR
} // NOSONAR
