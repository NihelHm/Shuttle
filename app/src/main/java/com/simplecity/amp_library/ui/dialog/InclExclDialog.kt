@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.dialog // NOSONAR

import android.annotation.SuppressLint // NOSONAR
import android.app.Dialog // NOSONAR
import android.content.Context // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.DialogFragment // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import android.support.v7.widget.LinearLayoutManager // NOSONAR
import android.support.v7.widget.RecyclerView // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.widget.Toast // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.model.InclExclItem // NOSONAR
import com.simplecity.amp_library.ui.modelviews.EmptyView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.InclExclView // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import javax.inject.Inject // NOSONAR

class InclExclDialog : DialogFragment() { //NOSONAR

    @Inject lateinit var songsRepository: Repository.SongsRepository //NOSONAR
    @Inject lateinit var blacklistRepository: Repository.BlacklistRepository //NOSONAR
    @Inject lateinit var whitelistRepository: Repository.WhitelistRepository //NOSONAR
    @Inject lateinit var analyticsManager: AnalyticsManager //NOSONAR

    @InclExclItem.Type //NOSONAR
    var type: Int = 0 //NOSONAR

    private var disposable: Disposable? = null //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        type = arguments!!.getInt(ARG_TYPE) //NOSONAR
    } // NOSONAR

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { //NOSONAR
        @SuppressLint("InflateParams") //NOSONAR
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_incl_excl, null) //NOSONAR

        val builder = MaterialDialog.Builder(context!!) //NOSONAR
            .title( //NOSONAR
                when (type) { //NOSONAR
                    InclExclItem.Type.INCLUDE -> R.string.whitelist_title //NOSONAR
                    else -> R.string.blacklist_title //NOSONAR
                } // NOSONAR
            ) // NOSONAR
            .customView(view, false) //NOSONAR
            .positiveText(R.string.close) //NOSONAR
            .negativeText(R.string.pref_title_clear_whitelist) //NOSONAR
            .onNegative { _, _ -> //NOSONAR
                when (type) { //NOSONAR
                    InclExclItem.Type.INCLUDE -> { //NOSONAR
                        whitelistRepository.deleteAll() //NOSONAR
                        blacklistRepository.deleteAll() //NOSONAR
                    } // NOSONAR
                    InclExclItem.Type.EXCLUDE -> blacklistRepository.deleteAll() //NOSONAR
                } // NOSONAR
                Toast.makeText( //NOSONAR
                    context, when (type) { //NOSONAR
                        InclExclItem.Type.INCLUDE -> R.string.whitelist_deleted //NOSONAR
                        else -> R.string.blacklist_deleted //NOSONAR
                    }, Toast.LENGTH_SHORT //NOSONAR
                ).show() //NOSONAR
            } // NOSONAR

        val dialog = builder.build() //NOSONAR

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView) //NOSONAR
        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR

        val inclExclAdapter = ViewModelAdapter() //NOSONAR

        recyclerView.adapter = inclExclAdapter //NOSONAR

        val listener = InclExclView.ClickListener { inclExclView -> //NOSONAR
            when (type) { //NOSONAR
                InclExclItem.Type.INCLUDE -> { //NOSONAR
                    whitelistRepository.delete(inclExclView.inclExclItem) //NOSONAR
                    blacklistRepository.delete(inclExclView.inclExclItem) //NOSONAR
                } // NOSONAR
                InclExclItem.Type.EXCLUDE -> blacklistRepository.delete(inclExclView.inclExclItem) //NOSONAR
            } // NOSONAR
            if (inclExclAdapter.items.size == 0) { //NOSONAR
                dialog.dismiss() //NOSONAR
            } // NOSONAR
        } // NOSONAR

        val items = when (type) { //NOSONAR
            InclExclItem.Type.INCLUDE -> whitelistRepository.getWhitelistItems(songsRepository) //NOSONAR
            else -> blacklistRepository.getBlacklistItems(songsRepository) //NOSONAR
        } // NOSONAR

        disposable = items.map<List<ViewModel<*>>> { inclExclItems -> //NOSONAR
            inclExclItems //NOSONAR
                .map { inclExclItem -> //NOSONAR
                    val inclExclView = InclExclView(inclExclItem) //NOSONAR
                    inclExclView.setClickListener(listener) //NOSONAR
                    inclExclView as ViewModel<*> //NOSONAR
                } // NOSONAR
                .toList() //NOSONAR
        } // NOSONAR
            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
            .subscribe( //NOSONAR
                { inclExclViews -> //NOSONAR
                    when { //NOSONAR
                        inclExclViews.isEmpty() -> { //NOSONAR
                            analyticsManager.dropBreadcrumb(TAG, "getDialog setData (empty)") //NOSONAR
                            inclExclAdapter.setItems(listOf<ViewModel<*>>(EmptyView(if (type == InclExclItem.Type.INCLUDE) R.string.whitelist_empty else R.string.blacklist_empty))) //NOSONAR
                        } // NOSONAR
                        else -> { //NOSONAR
                            analyticsManager.dropBreadcrumb(TAG, "getDialog setData") //NOSONAR
                            inclExclAdapter.setItems(inclExclViews) //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                }, // NOSONAR
                { error -> LogUtils.logException(TAG, "Error setting incl/excl items", error) }) //NOSONAR

        return dialog //NOSONAR
    } // NOSONAR

    override fun onDestroyView() { //NOSONAR
        super.onDestroyView() //NOSONAR

        disposable?.dispose() //NOSONAR
    } // NOSONAR

    fun show(fragmentManager: FragmentManager) { //NOSONAR
        show(fragmentManager, TAG) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "InclExclDialog" //NOSONAR

        private const val ARG_TYPE = "type" //NOSONAR

        fun newInstance(@InclExclItem.Type type: Int): InclExclDialog { //NOSONAR
            val args = Bundle() //NOSONAR
            args.putInt(ARG_TYPE, type) //NOSONAR
            val fragment = InclExclDialog() //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
