@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.settings

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.annimon.stream.Stream
import com.simplecity.amp_library.R
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.model.CategoryItem
import com.simplecity.amp_library.ui.dialog.UpgradeDialog
import com.simplecity.amp_library.ui.modelviews.TabViewModel
import com.simplecity.amp_library.ui.screens.main.LibraryController
import com.simplecity.amp_library.ui.views.recyclerview.ItemTouchHelperCallback
import com.simplecity.amp_library.utils.AnalyticsManager
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.ShuttleUtils
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter
import com.simplecityapps.recycler_adapter.model.ViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TabChooserDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var analyticsManager: AnalyticsManager //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) //NOSONAR

        val adapter = ViewModelAdapter() //NOSONAR

        val itemTouchHelper = ItemTouchHelper( //NOSONAR
            ItemTouchHelperCallback( //NOSONAR
                ItemTouchHelperCallback.OnItemMoveListener { fromPosition, toPosition -> adapter.moveItem(fromPosition, toPosition) }, //NOSONAR
                ItemTouchHelperCallback.OnDropListener { _, _ -> }, //NOSONAR
                ItemTouchHelperCallback.OnClearListener { //NOSONAR
                    // Intentionally left empty.
                },
                ItemTouchHelperCallback.OnSwipeListener { //NOSONAR
                    // Intentionally left empty.
                }
            ))

        val listener = object : TabViewModel.Listener { //NOSONAR
            override fun onStartDrag(holder: TabViewModel.ViewHolder) { //NOSONAR
                itemTouchHelper.startDrag(holder) //NOSONAR
            }

            override fun onFolderChecked(tabViewModel: TabViewModel, viewHolder: TabViewModel.ViewHolder) { //NOSONAR
                if (!ShuttleUtils.isUpgraded(context!!.applicationContext as ShuttleApplication, settingsManager)) { //NOSONAR
                    viewHolder.checkBox.isChecked = false //NOSONAR
                    tabViewModel.categoryItem.isChecked = false //NOSONAR
                    UpgradeDialog().show(fragmentManager!!) //NOSONAR
                }
            }
        }

        val items = CategoryItem.getCategoryItems(sharedPreferences) //NOSONAR
            .map { categoryItem -> //NOSONAR
                val tabViewModel = TabViewModel(categoryItem, settingsManager) //NOSONAR
                tabViewModel.setListener(listener) //NOSONAR
                tabViewModel //NOSONAR
            }

        analyticsManager.dropBreadcrumb(TAG, "setItems()") //NOSONAR
        adapter.setItems(items) //NOSONAR

        val recyclerView = RecyclerView(context!!) //NOSONAR
        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView.adapter = adapter //NOSONAR

        itemTouchHelper.attachToRecyclerView(recyclerView) //NOSONAR

        return MaterialDialog.Builder(context!!) //NOSONAR
            .title(R.string.pref_title_choose_tabs) //NOSONAR
            .customView(recyclerView, false) //NOSONAR
            .positiveText(R.string.button_done) //NOSONAR
            .onPositive { dialog, which -> //NOSONAR
                val editor = sharedPreferences.edit() //NOSONAR
                Stream.of<ViewModel<*>>(adapter.items) //NOSONAR
                    .indexed() //NOSONAR
                    .forEach { viewModelIntPair -> //NOSONAR
                        (viewModelIntPair.second as TabViewModel).categoryItem.sortOrder = viewModelIntPair.first //NOSONAR
                        (viewModelIntPair.second as TabViewModel).categoryItem.savePrefs(editor) //NOSONAR
                    }
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(Intent(LibraryController.EVENT_TABS_CHANGED)) //NOSONAR
            }
            .negativeText(R.string.close) //NOSONAR
            .build() //NOSONAR
    }

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    }

    companion object { //NOSONAR

        private const val TAG = "TabChooserDialog" //NOSONAR
    }
}
