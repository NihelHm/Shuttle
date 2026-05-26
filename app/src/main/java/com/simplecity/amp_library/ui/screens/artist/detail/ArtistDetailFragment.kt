@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.artist.detail // NOSONAR

import android.animation.AnimatorSet // NOSONAR
import android.animation.ObjectAnimator // NOSONAR
import android.annotation.SuppressLint // NOSONAR
import android.content.Context // NOSONAR
import android.content.res.ColorStateList // NOSONAR
import android.os.Bundle // NOSONAR
import android.support.v4.app.Fragment // NOSONAR
import android.support.v4.app.SharedElementCallback // NOSONAR
import android.support.v4.util.Pair // NOSONAR
import android.support.v4.view.ViewCompat // NOSONAR
import android.support.v7.widget.LinearLayoutManager // NOSONAR
import android.support.v7.widget.PopupMenu // NOSONAR
import android.support.v7.widget.Toolbar // NOSONAR
import android.transition.Transition // NOSONAR
import android.transition.TransitionInflater // NOSONAR
import android.view.LayoutInflater // NOSONAR
import android.view.MenuItem // NOSONAR
import android.view.View // NOSONAR
import android.view.ViewGroup // NOSONAR
import android.view.animation.AnimationUtils // NOSONAR
import android.widget.Toast // NOSONAR
import butterknife.ButterKnife // NOSONAR
import butterknife.Unbinder // NOSONAR
import com.afollestad.aesthetic.Aesthetic // NOSONAR
import com.afollestad.aesthetic.Rx.distinctToMainThread // NOSONAR
import com.bumptech.glide.Priority // NOSONAR
import com.bumptech.glide.RequestManager // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy // NOSONAR
import com.google.android.gms.cast.framework.CastButtonFactory // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.cast.CastManager // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.glide.utils.AlwaysCrossFade // NOSONAR
import com.simplecity.amp_library.model.Album // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider // NOSONAR
import com.simplecity.amp_library.model.Playlist // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment // NOSONAR
import com.simplecity.amp_library.ui.common.TransitionListenerAdapter // NOSONAR
import com.simplecity.amp_library.ui.dialog.AlbumBiographyDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.ArtistBiographyDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.DeleteDialog // NOSONAR
import com.simplecity.amp_library.ui.dialog.SongInfoDialog // NOSONAR
import com.simplecity.amp_library.ui.modelviews.AlbumView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.EmptyView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.HorizontalAlbumView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.HorizontalRecyclerView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SongView // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SubheaderView // NOSONAR
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbar // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbarHost // NOSONAR
import com.simplecity.amp_library.utils.ActionBarUtils // NOSONAR
import com.simplecity.amp_library.utils.ArtworkDialog // NOSONAR
import com.simplecity.amp_library.utils.ContextualToolbarHelper // NOSONAR
import com.simplecity.amp_library.utils.Operators // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider // NOSONAR
import com.simplecity.amp_library.utils.ResourceUtils // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils // NOSONAR
import com.simplecity.amp_library.utils.StringUtils // NOSONAR
import com.simplecity.amp_library.utils.TypefaceManager // NOSONAR
import com.simplecity.amp_library.utils.extensions.getSongsSingle // NOSONAR
import com.simplecity.amp_library.utils.extensions.share // NOSONAR
import com.simplecity.amp_library.utils.menu.album.AlbumMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.menu.albumartist.AlbumArtistMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.AlbumSortHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.SongSortHelper // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallbackAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener // NOSONAR
import dagger.android.support.AndroidSupportInjection // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.background // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.fab // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.recyclerView // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.textProtectionScrim // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.textProtectionScrim2 // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.toolbar // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.toolbar_layout // NOSONAR
import java.util.ArrayList // NOSONAR
import javax.inject.Inject // NOSONAR
import kotlinx.android.synthetic.main.fragment_detail.contextualToolbar as ctxToolbar // NOSONAR

@SuppressLint("RestrictedApi") //NOSONAR
class ArtistDetailFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    ArtistDetailView, //NOSONAR
    Toolbar.OnMenuItemClickListener, //NOSONAR
    DrawerLockManager.DrawerLock, //NOSONAR
    ContextualToolbarHost { //NOSONAR

    private lateinit var albumArtist: AlbumArtist //NOSONAR

    private lateinit var adapter: ViewModelAdapter //NOSONAR

    private lateinit var presenter: ArtistDetailPresenter //NOSONAR

    @Inject lateinit var presenterFactory: ArtistDetailPresenter.Factory //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    @Inject lateinit var songsRepository: Repository.SongsRepository //NOSONAR

    @Inject lateinit var sortManager: SortManager //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private var collapsingToolbarTextColor: ColorStateList? = null //NOSONAR
    private var collapsingToolbarSubTextColor: ColorStateList? = null //NOSONAR

    private val emptyView = EmptyView(R.string.empty_songlist) //NOSONAR

    private val horizontalRecyclerView = HorizontalRecyclerView("BaseDetail - horizontal") //NOSONAR

    private var setHorizontalItemsDisposable: Disposable? = null //NOSONAR

    private var setItemsDisposable: Disposable? = null //NOSONAR

    private var contextualToolbarHelper: ContextualToolbarHelper<Single<List<Song>>>? = null //NOSONAR

    private var unbinder: Unbinder? = null //NOSONAR

    private var isFirstLoad = true //NOSONAR

    private val sharedElementEnterTransitionListenerAdapter: TransitionListenerAdapter //NOSONAR
        get() = object : TransitionListenerAdapter() { //NOSONAR
            override fun onTransitionEnd(transition: Transition) { //NOSONAR
                transition.removeListener(this) //NOSONAR
                fadeInUi() //NOSONAR
            } // NOSONAR
        } // NOSONAR

    private val songClickListener = object : SongView.ClickListener { //NOSONAR
        override fun onSongClick(position: Int, songView: SongView) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(songView, Single.just(listOf(songView.song)))) { //NOSONAR
                presenter.songClicked(songView.song) //NOSONAR
            } // NOSONAR
        } // NOSONAR

        override fun onSongLongClick(position: Int, songView: SongView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(songView, Single.just(listOf(songView.song))) //NOSONAR
        } // NOSONAR

        override fun onSongOverflowClick(position: Int, v: View, song: Song) { //NOSONAR
            val popupMenu = PopupMenu(v.context, v) //NOSONAR
            SongMenuUtils.setupSongMenu(popupMenu, false, true, playlistMenuHelper) //NOSONAR
            popupMenu.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener(song, presenter)) //NOSONAR
            popupMenu.show() //NOSONAR
        } // NOSONAR

        override fun onStartDrag(holder: SongView.ViewHolder) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private val albumClickListener = object : AlbumView.ClickListener { //NOSONAR

        override fun onAlbumClick(position: Int, albumView: AlbumView, viewHolder: AlbumView.ViewHolder) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(albumView, albumView.album.getSongsSingle(songsRepository))) { //NOSONAR
                pushDetailFragment(AlbumDetailFragment.newInstance(albumView.album, ViewCompat.getTransitionName(viewHolder.imageOne)!!), viewHolder.imageOne) //NOSONAR
            } // NOSONAR
        } // NOSONAR

        override fun onAlbumLongClick(position: Int, albumView: AlbumView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(albumView, albumView.album.getSongsSingle(songsRepository)) //NOSONAR
        } // NOSONAR

        override fun onAlbumOverflowClicked(v: View, album: Album) { //NOSONAR
            val popupMenu = PopupMenu(v.context, v) //NOSONAR
            AlbumMenuUtils.setupAlbumMenu(popupMenu, playlistMenuHelper, false) //NOSONAR
            popupMenu.setOnMenuItemClickListener(AlbumMenuUtils.getAlbumMenuClickListener(album, presenter)) //NOSONAR
            popupMenu.show() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private val enterSharedElementCallback = object : SharedElementCallback() { //NOSONAR
        override fun onSharedElementStart(sharedElementNames: List<String>?, sharedElements: List<View>?, sharedElementSnapshots: List<View>?) { //NOSONAR
            super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots) //NOSONAR

            fab?.visibility = View.GONE //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        albumArtist = arguments!!.getSerializable(ARG_ALBUM_ARTIST) as AlbumArtist //NOSONAR
    } // NOSONAR

    override fun onCreate(icicle: Bundle?) { //NOSONAR
        super.onCreate(icicle) //NOSONAR

        presenter = presenterFactory.create(albumArtist) //NOSONAR

        adapter = ViewModelAdapter() //NOSONAR

        setHasOptionsMenu(true) //NOSONAR

        setEnterSharedElementCallback(enterSharedElementCallback) //NOSONAR

        isFirstLoad = true //NOSONAR
    } // NOSONAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_detail, container, false) //NOSONAR
    } // NOSONAR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        unbinder = ButterKnife.bind(this, view) //NOSONAR

        toolbar.setNavigationOnClickListener { navigationController.popViewController() } //NOSONAR

        if (ShuttleUtils.canDrawBehindStatusBar()) { //NOSONAR
            toolbar.layoutParams.height = (ActionBarUtils.getActionBarHeight(context!!) + ActionBarUtils.getStatusBarHeight(context!!)).toInt() //NOSONAR
            toolbar.setPadding(toolbar.paddingLeft, (toolbar.paddingTop + ActionBarUtils.getStatusBarHeight(context!!)).toInt(), toolbar.paddingRight, toolbar.paddingBottom) //NOSONAR
        } // NOSONAR

        setupToolbarMenu(toolbar) //NOSONAR

        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR
        recyclerView.adapter = adapter //NOSONAR

        if (isFirstLoad) { //NOSONAR
            recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom) //NOSONAR
        } // NOSONAR

        toolbar_layout.title = albumArtist.name //NOSONAR
        toolbar_layout.setSubtitle(null) //NOSONAR
        toolbar_layout.setExpandedTitleTypeface(TypefaceManager.getInstance().getTypeface(context, TypefaceManager.SANS_SERIF_LIGHT)) //NOSONAR
        toolbar_layout.setCollapsedTitleTypeface(TypefaceManager.getInstance().getTypeface(context, TypefaceManager.SANS_SERIF)) //NOSONAR

        setupContextualToolbar() //NOSONAR

        val transitionName = arguments!!.getString(ARG_TRANSITION_NAME) //NOSONAR
        ViewCompat.setTransitionName(background, transitionName) //NOSONAR

        if (isFirstLoad) { //NOSONAR
            fab!!.visibility = View.GONE //NOSONAR
        } // NOSONAR

        fab.setOnClickListener { //NOSONAR
            presenter.shuffleAll() //NOSONAR
        } // NOSONAR

        if (transitionName == null) { //NOSONAR
            fadeInUi() //NOSONAR
        } // NOSONAR

        loadBackgroundImage() //NOSONAR

        disposables.add(Aesthetic.get(context) //NOSONAR
            .colorPrimary() //NOSONAR
            .compose(distinctToMainThread()) //NOSONAR
            .subscribe { primaryColor -> //NOSONAR
                toolbar_layout.setContentScrimColor(primaryColor!!) //NOSONAR
                toolbar_layout.setBackgroundColor(primaryColor) //NOSONAR
            }) // NOSONAR

        presenter.bindView(this) //NOSONAR
    } // NOSONAR

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadData() //NOSONAR

        DrawerLockManager.getInstance().addDrawerLock(this) //NOSONAR
    } // NOSONAR

    override fun onPause() { //NOSONAR

        DrawerLockManager.getInstance().removeDrawerLock(this) //NOSONAR

        super.onPause() //NOSONAR
    } // NOSONAR

    override fun onDestroyView() { //NOSONAR

        if (setItemsDisposable != null) { //NOSONAR
            setItemsDisposable!!.dispose() //NOSONAR
        } // NOSONAR

        if (setHorizontalItemsDisposable != null) { //NOSONAR
            setHorizontalItemsDisposable!!.dispose() //NOSONAR
        } // NOSONAR

        disposables.clear() //NOSONAR

        presenter.unbindView(this) //NOSONAR

        unbinder!!.unbind() //NOSONAR

        isFirstLoad = false //NOSONAR

        super.onDestroyView() //NOSONAR
    } // NOSONAR

    private fun setupToolbarMenu(toolbar: Toolbar) { //NOSONAR

        toolbar.inflateMenu(R.menu.menu_detail_sort) //NOSONAR

        if (CastManager.isCastAvailable(context!!, settingsManager)) { //NOSONAR
            val menuItem = CastButtonFactory.setUpMediaRouteButton(context, toolbar.menu, R.id.media_route_menu_item) //NOSONAR
            menuItem.isVisible = true //NOSONAR
        } // NOSONAR

        toolbar.setOnMenuItemClickListener(this) //NOSONAR

        // Create playlist menu // NOSONAR
        val sub = toolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(sub).subscribe()) //NOSONAR

        // Inflate sorting menus // NOSONAR
        val item = toolbar.menu.findItem(R.id.sorting) //NOSONAR
        activity!!.menuInflater.inflate(R.menu.menu_detail_sort_albums, item.subMenu) //NOSONAR
        activity!!.menuInflater.inflate(R.menu.menu_detail_sort_songs, item.subMenu) //NOSONAR

        toolbar.menu.findItem(R.id.editTags).isVisible = true //NOSONAR
        toolbar.menu.findItem(R.id.info).isVisible = true //NOSONAR
        toolbar.menu.findItem(R.id.artwork).isVisible = true //NOSONAR

        AlbumSortHelper.updateAlbumSortMenuItems(toolbar.menu, sortManager.artistDetailAlbumsSortOrder, sortManager.artistDetailAlbumsAscending) //NOSONAR
        SongSortHelper.updateSongSortMenuItems(toolbar.menu, sortManager.artistDetailSongsSortOrder, sortManager.artistDetailSongsAscending) //NOSONAR
    } // NOSONAR

    override fun onMenuItemClick(item: MenuItem): Boolean { //NOSONAR
        if (!AlbumArtistMenuUtils.getAlbumArtistClickListener(albumArtist, presenter).onMenuItemClick(item)) { //NOSONAR
            val albumSortOrder = AlbumSortHelper.handleAlbumDetailMenuSortOrderClicks(item) //NOSONAR
            if (albumSortOrder != null) { //NOSONAR
                sortManager.artistDetailAlbumsSortOrder = albumSortOrder //NOSONAR
                presenter.loadData() //NOSONAR
            } // NOSONAR
            val albumsAsc = AlbumSortHelper.handleAlbumDetailMenuSortOrderAscClicks(item) //NOSONAR
            if (albumsAsc != null) { //NOSONAR
                sortManager.artistDetailAlbumsAscending = albumsAsc //NOSONAR
                presenter.loadData() //NOSONAR
            } // NOSONAR
            val songSortOrder = SongSortHelper.handleSongMenuSortOrderClicks(item) //NOSONAR
            if (songSortOrder != null) { //NOSONAR
                sortManager.artistDetailSongsSortOrder = songSortOrder //NOSONAR
                presenter.loadData() //NOSONAR
            } // NOSONAR
            val songsAsc = SongSortHelper.handleSongDetailMenuSortOrderAscClicks(item) //NOSONAR
            if (songsAsc != null) { //NOSONAR
                sortManager.artistDetailSongsAscending = songsAsc //NOSONAR
                presenter.loadData() //NOSONAR
            } // NOSONAR

            AlbumSortHelper.updateAlbumSortMenuItems(toolbar.menu, sortManager.artistDetailAlbumsSortOrder, sortManager.artistDetailAlbumsAscending) //NOSONAR
            SongSortHelper.updateSongSortMenuItems(toolbar.menu, sortManager.artistDetailSongsSortOrder, sortManager.artistDetailSongsAscending) //NOSONAR
        } // NOSONAR

        return super.onOptionsItemSelected(item) //NOSONAR
    } // NOSONAR

    private fun loadBackgroundImage() { //NOSONAR
        val width = ResourceUtils.getScreenSize().width + ResourceUtils.toPixels(60f) //NOSONAR
        val height = resources.getDimensionPixelSize(R.dimen.header_view_height) //NOSONAR

        requestManager.load<ArtworkProvider>(albumArtist as ArtworkProvider?) //NOSONAR
            // Need to override the height/width, as the shared element transition tricks Glide into thinking this ImageView has // NOSONAR
            // the same dimensions as the ImageView that the transition starts with. // NOSONAR
            // So we'll set it to screen width (plus a little extra, which might fix an issue on some devices..) // NOSONAR
            .override(width, height) //NOSONAR
            .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
            .priority(Priority.HIGH) //NOSONAR
            .placeholder(PlaceholderProvider.getInstance(context).getPlaceHolderDrawable(albumArtist.name, true, settingsManager)) //NOSONAR
            .centerCrop() //NOSONAR
            .animate(AlwaysCrossFade(false)) //NOSONAR
            .into(background!!) //NOSONAR
    } // NOSONAR

    override fun setSharedElementEnterTransition(transition: Any?) { //NOSONAR
        super.setSharedElementEnterTransition(transition) //NOSONAR
        (transition as Transition).addListener(sharedElementEnterTransitionListenerAdapter) //NOSONAR
    } // NOSONAR

    private fun fadeInUi() { //NOSONAR

        if (textProtectionScrim == null || textProtectionScrim2 == null || fab == null) { //NOSONAR
            return //NOSONAR
        } // NOSONAR

        //Fade in the text protection scrim // NOSONAR
        textProtectionScrim!!.alpha = 0f //NOSONAR
        textProtectionScrim!!.visibility = View.VISIBLE //NOSONAR
        var fadeAnimator = ObjectAnimator.ofFloat(textProtectionScrim, View.ALPHA, 0f, 1f) //NOSONAR
        fadeAnimator.duration = 600 //NOSONAR
        fadeAnimator.start() //NOSONAR

        textProtectionScrim2!!.alpha = 0f //NOSONAR
        textProtectionScrim2!!.visibility = View.VISIBLE //NOSONAR
        fadeAnimator = ObjectAnimator.ofFloat(textProtectionScrim2, View.ALPHA, 0f, 1f) //NOSONAR
        fadeAnimator.duration = 600 //NOSONAR
        fadeAnimator.start() //NOSONAR

        //Fade & grow the FAB // NOSONAR
        fab!!.alpha = 0f //NOSONAR
        fab!!.visibility = View.VISIBLE //NOSONAR

        fadeAnimator = ObjectAnimator.ofFloat(fab, View.ALPHA, 0.5f, 1f) //NOSONAR
        val scaleXAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_X, 0f, 1f) //NOSONAR
        val scaleYAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_Y, 0f, 1f) //NOSONAR

        val animatorSet = AnimatorSet() //NOSONAR
        animatorSet.playTogether(fadeAnimator, scaleXAnimator, scaleYAnimator) //NOSONAR
        animatorSet.duration = 250 //NOSONAR
        animatorSet.start() //NOSONAR
    } // NOSONAR

    override fun getContextualToolbar(): ContextualToolbar? { //NOSONAR
        return ctxToolbar as ContextualToolbar //NOSONAR
    } // NOSONAR

    private fun setupContextualToolbar() { //NOSONAR

        val contextualToolbar = ContextualToolbar.findContextualToolbar(this) //NOSONAR
        if (contextualToolbar != null) { //NOSONAR

            contextualToolbar.setTransparentBackground(true) //NOSONAR

            contextualToolbar.menu.clear() //NOSONAR
            contextualToolbar.inflateMenu(R.menu.context_menu_general) //NOSONAR
            val subMenu = contextualToolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
            disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(subMenu).subscribe()) //NOSONAR

            contextualToolbar.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener(Single.defer { Operators.reduceSongSingles(contextualToolbarHelper!!.items) }, presenter)) //NOSONAR

            contextualToolbarHelper = object : ContextualToolbarHelper<Single<List<Song>>>(context!!, contextualToolbar, object : ContextualToolbarHelper.Callback { //NOSONAR

                override fun notifyItemChanged(viewModel: SelectableViewModel) { //NOSONAR
                    if (adapter.items.contains(viewModel as ViewModel<*>)) { //NOSONAR
                        val index = adapter.items.indexOf(viewModel as ViewModel<*>) //NOSONAR
                        if (index >= 0) { //NOSONAR
                            adapter.notifyItemChanged(index, 0) //NOSONAR
                        } // NOSONAR
                    } else if (horizontalRecyclerView.viewModelAdapter.items.contains(viewModel as ViewModel<*>)) { //NOSONAR
                        val index = horizontalRecyclerView.viewModelAdapter.items.indexOf(viewModel as ViewModel<*>) //NOSONAR
                        if (index >= 0) { //NOSONAR
                            horizontalRecyclerView.viewModelAdapter.notifyItemChanged(index, 0) //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR

                override fun notifyDatasetChanged() { //NOSONAR
                    adapter.notifyItemRangeChanged(0, adapter.items.size, 0) //NOSONAR
                    horizontalRecyclerView.viewModelAdapter.notifyItemRangeChanged(0, horizontalRecyclerView.viewModelAdapter.items.size, 0) //NOSONAR
                } // NOSONAR
            }) { // NOSONAR
                override fun start() { //NOSONAR
                    super.start() //NOSONAR
                    // Need to hide the collapsed text, as it overlaps the contextual toolbar // NOSONAR
                    collapsingToolbarTextColor = toolbar_layout.collapsedTitleTextColor //NOSONAR
                    collapsingToolbarSubTextColor = toolbar_layout.collapsedSubTextColor //NOSONAR
                    toolbar_layout.setCollapsedTitleTextColor(0x01FFFFFF) //NOSONAR
                    toolbar_layout.setCollapsedSubTextColor(0x01FFFFFF) //NOSONAR

                    toolbar.visibility = View.GONE //NOSONAR
                } // NOSONAR

                override fun finish() { //NOSONAR
                    if (toolbar_layout != null && collapsingToolbarTextColor != null && collapsingToolbarSubTextColor != null) { //NOSONAR
                        toolbar_layout.collapsedTitleTextColor = collapsingToolbarTextColor!! //NOSONAR
                        toolbar_layout.collapsedSubTextColor = collapsingToolbarSubTextColor!! //NOSONAR
                    } // NOSONAR
                    if (toolbar != null) { //NOSONAR
                        toolbar.visibility = View.VISIBLE //NOSONAR
                    } // NOSONAR
                    super.finish() //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    public override fun screenName(): String { //NOSONAR
        return "ArtistDetailFragment" //NOSONAR
    } // NOSONAR

    internal fun pushDetailFragment(fragment: Fragment, transitionView: View?) { //NOSONAR

        val transitions = ArrayList<Pair<View, String>>() //NOSONAR

        if (transitionView != null) { //NOSONAR
            val transitionName = ViewCompat.getTransitionName(transitionView) //NOSONAR
            transitions.add(Pair(transitionView, transitionName)) //NOSONAR
            //            transitions.add(new Pair<>(toolbar, "toolbar")); // NOSONAR

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
                val moveTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_transition) //NOSONAR
                fragment.sharedElementEnterTransition = moveTransition //NOSONAR
                fragment.sharedElementReturnTransition = moveTransition //NOSONAR
            } // NOSONAR
        } // NOSONAR

        navigationController.pushViewController(fragment, "DetailFragment", transitions) //NOSONAR
    } // NOSONAR

    // ArtistDetailView implementation // NOSONAR

    override fun setData(albums: List<Album>, songs: List<Song>) { //NOSONAR
        val viewModels = ArrayList<ViewModel<*>>() //NOSONAR

        if (!albums.isEmpty()) { //NOSONAR

            val items = ArrayList<ViewModel<*>>() //NOSONAR

            if (setHorizontalItemsDisposable != null) { //NOSONAR
                setHorizontalItemsDisposable!!.dispose() //NOSONAR
            } // NOSONAR

            setHorizontalItemsDisposable = horizontalRecyclerView.setItems(albums //NOSONAR
                .map { album -> //NOSONAR
                    val horizontalAlbumView = HorizontalAlbumView(album, requestManager, sortManager, settingsManager) //NOSONAR
                    horizontalAlbumView.setClickListener(albumClickListener) //NOSONAR
                    horizontalAlbumView.showYear(true) //NOSONAR
                    horizontalAlbumView //NOSONAR
                }) // NOSONAR

            items.add(SubheaderView(StringUtils.makeAlbumsLabel(context!!, albums.size))) //NOSONAR
            items.add(horizontalRecyclerView) //NOSONAR

            viewModels.addAll(items) //NOSONAR
        } // NOSONAR

        if (!songs.isEmpty()) { //NOSONAR
            val items = ArrayList<ViewModel<*>>() //NOSONAR

            items.add(SubheaderView(StringUtils.makeSongsAndTimeLabel(context!!, songs.size, songs.map { song -> song.duration / 1000 }.sum()))) //NOSONAR

            items.addAll( //NOSONAR
                songs //NOSONAR
                    .map { song -> //NOSONAR
                        val songView = SongView(song, requestManager, sortManager, settingsManager) //NOSONAR
                        songView.showArtistName(false) //NOSONAR
                        songView.setClickListener(songClickListener) //NOSONAR
                        songView //NOSONAR
                    }.toList() //NOSONAR
            ) // NOSONAR

            viewModels.addAll(items) //NOSONAR
        } // NOSONAR
        if (viewModels.isEmpty()) { //NOSONAR
            viewModels.add(emptyView) //NOSONAR
        } // NOSONAR

        setItemsDisposable = adapter.setItems(viewModels, object : CompletionListUpdateCallbackAdapter() { //NOSONAR
            override fun onComplete() { //NOSONAR
                recyclerView?.scheduleLayoutAnimation() //NOSONAR
            } // NOSONAR
        }) // NOSONAR
    } // NOSONAR

    override fun closeContextualToolbar() { //NOSONAR
        if (contextualToolbarHelper != null) { //NOSONAR
            contextualToolbarHelper!!.finish() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    // AlbumArtistMenuContract.View Implementation // NOSONAR

    override fun presentTagEditorDialog(albumArtist: AlbumArtist) { //NOSONAR
        TaggerDialog.newInstance(albumArtist).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentArtistDeleteDialog(albumArtists: List<AlbumArtist>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListArtistsRef { albumArtists }).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentAlbumArtistInfoDialog(albumArtist: AlbumArtist) { //NOSONAR
        ArtistBiographyDialog.newInstance(albumArtist).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentArtworkEditorDialog(albumArtist: AlbumArtist) { //NOSONAR
        ArtworkDialog.build(context, albumArtist).show() //NOSONAR
    } // NOSONAR

    // AlbumMenuContract.View Implementation // NOSONAR

    override fun presentTagEditorDialog(album: Album) { //NOSONAR
        TaggerDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentDeleteAlbumsDialog(albums: List<Album>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListAlbumsRef { albums }).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentAlbumInfoDialog(album: Album) { //NOSONAR
        AlbumBiographyDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    } // NOSONAR

    override fun presentArtworkEditorDialog(album: Album) { //NOSONAR
        ArtworkDialog.build(context, album).show() //NOSONAR
    } // NOSONAR

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message // NOSONAR
        Toast.makeText(context, R.string.empty_playlist, Toast.LENGTH_SHORT).show() //NOSONAR
    } // NOSONAR

    // SongMenuContract.View Implementation // NOSONAR

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

    companion object { //NOSONAR

        private const val TAG = "ArtistDetailFragment" //NOSONAR

        private const val ARG_TRANSITION_NAME = "transition_name" //NOSONAR

        private const val ARG_ALBUM_ARTIST = "album_artist" //NOSONAR

        fun newInstance(albumArtist: AlbumArtist, transitionName: String?): ArtistDetailFragment { //NOSONAR
            val args = Bundle() //NOSONAR
            val fragment = ArtistDetailFragment() //NOSONAR
            args.putSerializable(ARG_ALBUM_ARTIST, albumArtist) //NOSONAR
            args.putString(ARG_TRANSITION_NAME, transitionName) //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
