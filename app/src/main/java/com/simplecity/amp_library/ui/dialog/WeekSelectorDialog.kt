@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.afollestad.materialdialogs.MaterialDialog
import com.simplecity.amp_library.R
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.utils.SettingsManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class WeekSelectorDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        val view = LayoutInflater.from(context).inflate(R.layout.weekpicker, null) //NOSONAR

        val numberPicker: NumberPicker //NOSONAR
        numberPicker = view.findViewById(R.id.weeks) //NOSONAR
        numberPicker.maxValue = 12 //NOSONAR
        numberPicker.minValue = 1 //NOSONAR
        numberPicker.value = settingsManager.numWeeks //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.week_selector) //NOSONAR
            .customView(view, false) //NOSONAR
            .negativeText(R.string.cancel) //NOSONAR
            .positiveText(R.string.button_ok) //NOSONAR
            .onPositive { _, _ -> settingsManager.numWeeks = numberPicker.value } //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        const val TAG = "WeekSelectorDialog" //NOSONAR
    }
}
