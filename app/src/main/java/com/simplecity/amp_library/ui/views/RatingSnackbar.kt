@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.views // NOSONAR

import android.graphics.Color // NOSONAR
import android.support.design.widget.BaseTransientBottomBar // NOSONAR
import android.support.design.widget.Snackbar // NOSONAR
import android.view.View // NOSONAR
import android.widget.TextView // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR

class RatingSnackbar( //NOSONAR
    private val settingsManager: SettingsManager, //NOSONAR
    private val analyticsManager: AnalyticsManager //NOSONAR
) { // NOSONAR

    fun show(view: View, onClicked: () -> Unit) { //NOSONAR
        //If the user hasn't dismissed the snackbar in the past, and we haven't already shown it for this session // NOSONAR
        if (!settingsManager.hasRated && !settingsManager.hasSeenRateSnackbar) { //NOSONAR
            //If this is the tenth launch, or a multiple of 50 // NOSONAR
            if (settingsManager.launchCount == 10 || settingsManager.launchCount != 0 && settingsManager.launchCount % 50 == 0) { //NOSONAR
                val snackbar = Snackbar.make(view, R.string.snackbar_rate_text, Snackbar.LENGTH_INDEFINITE) //NOSONAR
                    .setDuration(15000) //NOSONAR
                    .setAction(R.string.snackbar_rate_action) { v -> //NOSONAR
                        onClicked.invoke() //NOSONAR
                        analyticsManager.logRateClicked() //NOSONAR
                    } // NOSONAR
                    .addCallback(object : Snackbar.Callback() { //NOSONAR
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) { //NOSONAR
                            super.onDismissed(transientBottomBar, event) //NOSONAR

                            if (event != BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT) { //NOSONAR
                                // We don't really care whether the user has rated or not. The snackbar was // NOSONAR
                                // dismissed. Never show it again. // NOSONAR
                                settingsManager.setHasRated() //NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    }) // NOSONAR
                snackbar.show() //NOSONAR

                val snackbarText = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text) //NOSONAR
                snackbarText?.setTextColor(Color.WHITE) //NOSONAR

                analyticsManager.logRateShown() //NOSONAR
            } // NOSONAR

            settingsManager.hasSeenRateSnackbar = true //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
