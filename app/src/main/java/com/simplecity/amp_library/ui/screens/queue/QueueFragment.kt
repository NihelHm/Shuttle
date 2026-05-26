@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.queue // NOSONAR

import android.content.Context // NOSONAR
import android.graphics.Color // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v7.widget.LinearLayoutManager // NOSONAR
import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.RecyclerView // NOSONAR
import android.support.v7.widget.Toolbar // NOSONAR
import android.support.v7.widget.helper.ItemTouchHelper // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.View // NOSONAR
import android.view.ViewGroup // NOSONAR
import android.widget.Toast // NOSONAR
import com.afollestad.aesthetic.Aesthetic // NOSONAR
import com.afollestad.aesthetic.Util // NOSONAR
import com.bumptech.glide.RequestManager // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import com.simplecity.amp_library.billing.BillingManager // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.playback.MediaManager.Defs // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment // NOSONAR
import com.simplecity.amp_library.ui.dialog.DeleteDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.SongInfoDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeDialog // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SubheaderView // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerPresenter // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbar // NOSONAR
import com.simplecity.amp_library.ui.views.LockActionBarView // NOSONAR
import com.simplecity.amp_library.ui.views.PlayerViewAdapter // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetSlideEventRelay // NOSONAR
import com.simplecity.amp_library.utils.* // NOSONAR
import com.simplecity.amp_library.utils.ContextualToolbarHelper.Callback // NOSONAR
import com.simplecity.amp_library.utils.extensions.share // NOSONAR
import com.simplecity.amp_library.utils.menu.queue.QueueMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.menu.queue.removeQueueItem // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView.Sheet // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallbackAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import kotlinx.android.synthetic.main.fragment_queue.* // NOSONAR
import java.util.* // NOSONAR
import javax.inject.Inject // NOSONAR

class QueueFragment : //NOSONAR
        BaseFragment(), //NOSONAR
        QueueContract.View { //NOSONAR

    private var loadDataDisposable: Disposable? = null //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private lateinit var adapter: ViewModelAdapter //NOSONAR

    @Inject //NOSONAR
    lateinit var application: ShuttleApplication //NOSONAR

    @Inject //NOSONAR
    lateinit var requestManager: RequestManager //NOSONAR

    @Inject //NOSONAR
    lateinit var multiSheetSlideEventRelay: MultiSheetSlideEventRelay //NOSONAR

    @Inject //NOSONAR
    lateinit var playerPresenter: PlayerPresenter //NOSONAR

    @Inject //NOSONAR
    lateinit var queuePresenter: QueuePresenter //NOSONAR

    @Inject //NOSONAR
    lateinit var billingManager: BillingManager //NOSONAR

    @Inject //NOSONAR
    lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject //NOSONAR
    lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    @Inject //NOSONAR
    lateinit var sortManager: SortManager //NOSONAR

    private lateinit var itemTouchHelper: ItemTouchHelper //NOSONAR

    private lateinit var itemTouchHelperCallback: com.simplecity.amp_library.ui.views.recyclerview.ItemTouchHelperCallback //NOSONAR

    private lateinit var cabToolbar: ContextualToolbar //NOSONAR

    private lateinit var cabHelper: ContextualToolbarHelper<QueueItem> //NOSONAR


    // Lifecycle // NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    } // NOSONAR

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        setHasOptionsMenu(true) //NOSONAR

        adapter = ViewModelAdapter() //NOSONAR
    } // NOSONAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_queue, container, false) //NOSONAR
    } // NOSONAR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        toolbar.setNavigationOnClickListener { v -> activity?.onBackPressed() } //NOSONAR
        toolbar.inflateMenu(R.menu.menu_fragment_queue) //NOSONAR

        val subMenu = toolbar.menu.addSubMenu(0, Defs.ADD_TO_PLAYLIST, 1, R.string.save_as_playlist) //NOSONAR
        disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(subMenu).subscribe()) //NOSONAR

        toolbar.setOnMenuItemClickListener(toolbarListener) //NOSONAR

        cabToolbar = contextualToolbar as ContextualToolbar //NOSONAR

        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR
        recyclerView.adapter = adapter //NOSONAR

        itemTouchHelperCallback = ItemTouchHelperCallback() //NOSONAR
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback) //NOSONAR
        itemTouchHelper.attachToRecyclerView(recyclerView) //NOSONAR

        val lockMenuItem = toolbar.menu.findItem(R.id.menu_lock) //NOSONAR
        val menuActionView = lockMenuItem.actionView as LockActionBarView //NOSONAR
        val swipeLocked = settingsManager.queueSwipeLocked() //NOSONAR

        setQueueSwipeLocked(swipeLocked) //NOSONAR
        menuActionView.setLocked(swipeLocked, false) //NOSONAR

        menuActionView.setOnClickListener { lockActionBarView -> //NOSONAR
            (lockActionBarView as LockActionBarView).toggle() //NOSONAR
            queuePresenter.setQueueSwipeLocked(lockActionBarView.isLocked) //NOSONAR
        } // NOSONAR

        disposables.add(Aesthetic.get(context) //NOSONAR
                .colorPrimary() //NOSONAR
                .subscribe { color -> //NOSONAR
                    val isLight = Util.isColorLight(color!!) //NOSONAR
                    line1.setTextColor(if (isLight) Color.BLACK else Color.WHITE) //NOSONAR
                    line2.setTextColor(if (isLight) Color.BLACK else Color.WHITE) //NOSONAR
                }) // NOSONAR

        // In landscape, we need to adjust the status bar's translation depending on the slide offset of the sheet // NOSONAR
        if (ShuttleUtils.isLandscape(application)) { //NOSONAR
            statusBarView.translationY = ResourceUtils.toPixels(16f).toFloat() //NOSONAR

            disposables.add(multiSheetSlideEventRelay.events //NOSONAR
                    .filter { multiSheetEvent -> multiSheetEvent.sheet == MultiSheetView.Sheet.SECOND } //NOSONAR
                    .filter { multiSheetEvent -> multiSheetEvent.slideOffset >= 0 } //NOSONAR
                    .subscribe { multiSheetEvent -> statusBarView.translationY = (1 - multiSheetEvent.slideOffset) * ResourceUtils.toPixels(16f) }) //NOSONAR
        } // NOSONAR

        setupContextualToolbar() //NOSONAR
    } // NOSONAR

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR
        adapter.notifyItemRangeChanged(0, adapter.itemCount, 0) //NOSONAR

        playerPresenter.bindView(playerViewAdapter) //NOSONAR
        queuePresenter.bindView(this) //NOSONAR
    } // NOSONAR

    override fun onPause() { //NOSONAR
        super.onPause() //NOSONAR
        loadDataDisposable?.dispose() //NOSONAR
        playerPresenter.unbindView(playerViewAdapter) //NOSONAR
        queuePresenter.unbindView(this) //NOSONAR
    } // NOSONAR

    override fun onDestroyView() { //NOSONAR
        disposables.clear() //NOSONAR
        super.onDestroyView() //NOSONAR
    } // NOSONAR

    private val songClickListener = object : QueueViewBinder.ClickListener { //NOSONAR

        override fun onQueueItemClick(position: Int, queueViewBinder: QueueViewBinder) { //NOSONAR
            if (!cabHelper.handleClick(queueViewBinder, queueViewBinder.queueItem)) { //NOSONAR
                queuePresenter.play(queueViewBinder.queueItem) //NOSONAR
            } // NOSONAR
        } // NOSONAR

        override fun onQueueItemLongClick(position: Int, queueViewBinder: QueueViewBinder): Boolean { //NOSONAR
            return cabHelper.handleLongClick(queueViewBinder, queueViewBinder.queueItem) //NOSONAR
        } // NOSONAR

        override fun onQueueItemOverflowClick(position: Int, view: View, queueViewBinder: QueueViewBinder) { //NOSONAR
            val menu = PopupMenu(view.context, view) //NOSONAR
            QueueMenuUtils.setupQueueSongMenu(menu, playlistMenuHelper) //NOSONAR
            menu.setOnMenuItemClickListener(QueueMenuUtils.getQueueMenuClickListener(queueViewBinder.queueItem, queuePresenter)) //NOSONAR
            menu.show() //NOSONAR
        } // NOSONAR

        override fun onStartDrag(holder: QueueViewBinder.ViewHolder) { //NOSONAR
            itemTouchHelper.startDrag(holder) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private val playerViewAdapter = object : PlayerViewAdapter() { //NOSONAR
        override fun trackInfoChanged(song: Song?) { //NOSONAR
            if (song != null) { //NOSONAR
                line1.text = song.name //NOSONAR
                if (song.albumArtistName != null && song.albumName != null) { //NOSONAR
                    line2.text = String.format("%s • %s", song.albumArtistName, song.albumName) //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        override fun showUpgradeDialog() { //NOSONAR
            UpgradeDialog().show(childFragmentManager) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private var toolbarListener = Toolbar.OnMenuItemClickListener { item -> //NOSONAR
        when (item.itemId) { //NOSONAR
            R.id.menu_clear -> { //NOSONAR
                queuePresenter.clearQueue() //NOSONAR
                return@OnMenuItemClickListener true //NOSONAR
            } // NOSONAR
            Defs.NEW_PLAYLIST -> { //NOSONAR
                queuePresenter.saveQueue(context!!) //NOSONAR
                return@OnMenuItemClickListener true //NOSONAR
            } // NOSONAR
            Defs.PLAYLIST_SELECTED -> { //NOSONAR
                queuePresenter.saveQueue(context!!, item) //NOSONAR
                return@OnMenuItemClickListener true //NOSONAR
            } // NOSONAR
        } // NOSONAR
        false //NOSONAR
    } // NOSONAR

    private fun setupContextualToolbar() { //NOSONAR
        cabToolbar.menu.clear() //NOSONAR
        cabToolbar.inflateMenu(R.menu.context_menu_queue) //NOSONAR

        val sub = cabToolbar.menu.findItem(R.id.queue_add_to_playlist).subMenu //NOSONAR
        disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(sub).subscribe()) //NOSONAR
        cabToolbar.setOnMenuItemClickListener(QueueMenuUtils.getQueueMenuClickListener(Single.fromCallable { cabHelper.items }, queuePresenter) { cabHelper.finish() }) //NOSONAR

        cabHelper = ContextualToolbarHelper(context, cabToolbar, object : Callback { //NOSONAR
            override fun notifyItemChanged(viewModel: SelectableViewModel) { //NOSONAR

                val index = adapter.items.indexOf(viewModel as ViewModel<*>) //NOSONAR
                if (index >= 0) { //NOSONAR
                    adapter.notifyItemChanged(index, 0) //NOSONAR
                } // NOSONAR
            } // NOSONAR

            override fun notifyDatasetChanged() { //NOSONAR
                adapter.notifyItemRangeChanged(0, adapter.items.size, 0) //NOSONAR
            } // NOSONAR
        }) // NOSONAR
    } // NOSONAR

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    } // NOSONAR


    // QueueView implementation // NOSONAR

    override fun setData(queueItems: List<QueueItem>, position: Int) { //NOSONAR

        PermissionUtils.RequestStoragePermissions { //NOSONAR
            if (activity != null && isAdded) { //NOSONAR
                loadDataDisposable?.dispose() //NOSONAR

                val queueHeaderView = QueueHeaderView( //NOSONAR
                        StringUtils.makeSongsAndTimeLabel( //NOSONAR
                                application, //NOSONAR
                                queueItems.size, //NOSONAR
                                queueItems.map { queueItem -> queueItem.song.duration / 1000 }.sum() //NOSONAR
                        ) // NOSONAR
                ) // NOSONAR

                val viewModels = ArrayList<ViewModel<*>>() //NOSONAR
                viewModels.add(queueHeaderView) //NOSONAR

                viewModels.addAll(queueItems.map { song -> //NOSONAR
                    val queueView = QueueViewBinder(song, requestManager, sortManager, settingsManager) //NOSONAR
                    queueView.setClickListener(songClickListener) //NOSONAR
                    queueView.showAlbumArt(true) //NOSONAR

                    queueView as ViewModel<*> //NOSONAR
                }.toList()) //NOSONAR

                loadDataDisposable = adapter.setItems(viewModels, object : CompletionListUpdateCallbackAdapter() { //NOSONAR
                    override fun onComplete() { //NOSONAR
                        updateQueuePosition(position) //NOSONAR
                    } // NOSONAR
                }) // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun showToast(message: String, duration: Int) { //NOSONAR
        Toast.makeText(context, message, duration).show() //NOSONAR
    } // NOSONAR

    override fun updateQueuePosition(queuePosition: Int) { //NOSONAR

        val queueViewBinders = adapter.items.filterIsInstance<QueueViewBinder>() //NOSONAR

        if (queueViewBinders.isEmpty() || queuePosition >= queueViewBinders.size || queuePosition < 0) { //NOSONAR
            return //NOSONAR
        } // NOSONAR

        MultiSheetView.getParentMultiSheetView(view)?.let { multiSheetView -> //NOSONAR

            // If we're not currently displaying the queue, then scroll to keep the position up to date // NOSONAR
            if (multiSheetView.currentSheet != Sheet.SECOND) { //NOSONAR
                if (!queueViewBinders.isEmpty() && queuePosition < queueViewBinders.size) { //NOSONAR
                    val index = adapter.items.indexOf(queueViewBinders[queuePosition]) //NOSONAR
                    if (index >= 0) { //NOSONAR
                        recyclerView.scrollToPosition(index) //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        // Deselect previous 'current track' // NOSONAR
        queueViewBinders //NOSONAR
                .firstOrNull { queueViewBinder -> queueViewBinder.isCurrentTrack } //NOSONAR
                ?.let { previouslySelectedQueueViewBinder -> //NOSONAR
                    previouslySelectedQueueViewBinder.isCurrentTrack = false //NOSONAR
                    adapter.notifyItemChanged(adapter.items.indexOf(previouslySelectedQueueViewBinder), 1) //NOSONAR
                } // NOSONAR

        // Select the new 'current track' // NOSONAR
        queueViewBinders[queuePosition].isCurrentTrack = true //NOSONAR
        val index = adapter.items.indexOf(queueViewBinders[queuePosition]) //NOSONAR
        adapter.notifyItemChanged(index, 1) //NOSONAR
    } // NOSONAR

    override fun showTaggerDialog(taggerDialog: TaggerDialog) { //NOSONAR
        taggerDialog.show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun showDeleteDialog(deleteDialog: DeleteDialog) { //NOSONAR
        deleteDialog.show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun onRemovedFromQueue(queueItem: QueueItem) { //NOSONAR
        adapter.items.first { viewBinder -> viewBinder is QueueViewBinder && viewBinder.queueItem == queueItem }?.let { //NOSONAR
            adapter.removeItem(it) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onRemovedFromQueue(queueItems: List<QueueItem>) { //NOSONAR
        adapter.items.filter { viewBinder -> viewBinder is QueueViewBinder && queueItems.contains(viewBinder.queueItem) }.forEach { //NOSONAR
            adapter.removeItem(it) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun showUpgradeDialog() { //NOSONAR
        UpgradeDialog().show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun setQueueSwipeLocked(locked: Boolean) { //NOSONAR
        itemTouchHelperCallback.setEnabled(!locked) //NOSONAR
    } // NOSONAR

    override fun showCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        CreatePlaylistDialog.newInstance(mediaManager.queue.toSongs()).show(childFragmentManager, "CreatePlaylistDialog") //NOSONAR
    } // NOSONAR

    inner class ItemTouchHelperCallback : com.simplecity.amp_library.ui.views.recyclerview.ItemTouchHelperCallback( //NOSONAR
            { fromPosition, toPosition -> adapter.moveItem(fromPosition, toPosition) }, //NOSONAR
            { from, to -> //NOSONAR
                val numBeforeFrom = (0..from) //NOSONAR
                        .map { value -> adapter.items[value] } //NOSONAR
                        .filter { value -> value !is QueueViewBinder } //NOSONAR
                        .count() //NOSONAR

                val numBeforeTo = (0..to) //NOSONAR
                        .map { value -> adapter.items[value] } //NOSONAR
                        .filter { value -> value !is QueueViewBinder } //NOSONAR
                        .count() //NOSONAR

                queuePresenter.moveQueueItem(from - numBeforeFrom, to - numBeforeTo) //NOSONAR
            }, // NOSONAR
            { // NOSONAR
                // Nothing to do // NOSONAR
            }, // NOSONAR
            { pos -> //NOSONAR
                queuePresenter.removeQueueItem((adapter.items[pos] as QueueViewBinder).queueItem) //NOSONAR
            }) { // NOSONAR
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { //NOSONAR
            return if (viewHolder.itemViewType == target.itemViewType) { //NOSONAR
                super.onMove(recyclerView, viewHolder, target) //NOSONAR
            } else false //NOSONAR
        } // NOSONAR
    } // NOSONAR

    class QueueHeaderView(title: String) : SubheaderView(title) { //NOSONAR

        override fun equals(other: Any?): Boolean { //NOSONAR
            if (this === other) return true //NOSONAR
            if (javaClass != other?.javaClass) return false //NOSONAR
            return true //NOSONAR
        } // NOSONAR

        override fun hashCode(): Int { //NOSONAR
            return javaClass.hashCode() //NOSONAR
        } // NOSONAR

        override fun areContentsEqual(other: Any?): Boolean { //NOSONAR
            return (other as? QueueHeaderView)?.title == title //NOSONAR
        } // NOSONAR
    } // NOSONAR


    // QueueMenuContract.View Implementation // NOSONAR

    override fun presentCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        CreatePlaylistDialog.newInstance(songs).show(childFragmentManager, "CreatePlaylistDialog") //NOSONAR
    } // NOSONAR

    override fun presentSongInfoDialog(song: Song) { //NOSONAR
        SongInfoDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoplaylist, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    override fun presentTagEditorDialog(song: Song) { //NOSONAR
        TaggerDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentDeleteDialog(songs: List<Song>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListSongsRef { songs }).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun shareSong(song: Song) { //NOSONAR
        song.share(context!!) //NOSONAR
    } // NOSONAR

    override fun presentRingtonePermissionDialog() { //NOSONAR
        RingtoneManager.getDialog(context!!).show() //NOSONAR
    } // NOSONAR

    override fun showRingtoneSetMessage() { //NOSONAR
        Toast.makeText(context, R.string.ringtone_set_new, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR


    // Static // NOSONAR

    companion object { //NOSONAR

        private const val TAG = "QueueFragment" //NOSONAR

        fun newInstance() = QueueFragment() //NOSONAR
    } // NOSONAR
} // NOSONAR
