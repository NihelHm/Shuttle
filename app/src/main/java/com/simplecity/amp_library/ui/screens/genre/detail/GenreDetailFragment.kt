@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.genre.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.afollestad.aesthetic.Aesthetic
import com.afollestad.aesthetic.Rx.distinctToMainThread
import com.annimon.stream.Stream
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.cast.framework.CastButtonFactory
import com.simplecity.amp_library.R
import com.simplecity.amp_library.cast.CastManager
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.glide.utils.AlwaysCrossFade
import com.simplecity.amp_library.model.Album
import com.simplecity.amp_library.model.ArtworkProvider
import com.simplecity.amp_library.model.Genre
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.common.TransitionListenerAdapter
import com.simplecity.amp_library.ui.dialog.AlbumBiographyDialog
import com.simplecity.amp_library.ui.dialog.DeleteDialog
import com.simplecity.amp_library.ui.dialog.SongInfoDialog
import com.simplecity.amp_library.ui.modelviews.AlbumView
import com.simplecity.amp_library.ui.modelviews.EmptyView
import com.simplecity.amp_library.ui.modelviews.HorizontalAlbumView
import com.simplecity.amp_library.ui.modelviews.HorizontalRecyclerView
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel
import com.simplecity.amp_library.ui.modelviews.SongView
import com.simplecity.amp_library.ui.modelviews.SubheaderView
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog
import com.simplecity.amp_library.ui.views.ContextualToolbar
import com.simplecity.amp_library.ui.views.ContextualToolbarHost
import com.simplecity.amp_library.utils.ActionBarUtils
import com.simplecity.amp_library.utils.ArtworkDialog
import com.simplecity.amp_library.utils.ContextualToolbarHelper
import com.simplecity.amp_library.utils.Operators
import com.simplecity.amp_library.utils.PlaceholderProvider
import com.simplecity.amp_library.utils.ResourceUtils
import com.simplecity.amp_library.utils.RingtoneManager
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.ShuttleUtils
import com.simplecity.amp_library.utils.StringUtils
import com.simplecity.amp_library.utils.TypefaceManager
import com.simplecity.amp_library.utils.extensions.getSongsSingle
import com.simplecity.amp_library.utils.extensions.share
import com.simplecity.amp_library.utils.menu.album.AlbumMenuUtils
import com.simplecity.amp_library.utils.menu.genre.GenreMenuUtils
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper
import com.simplecity.amp_library.utils.sorting.AlbumSortHelper
import com.simplecity.amp_library.utils.sorting.SongSortHelper
import com.simplecity.amp_library.utils.sorting.SortManager
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallbackAdapter
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter
import com.simplecityapps.recycler_adapter.model.ViewModel
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_detail.background
import kotlinx.android.synthetic.main.fragment_detail.fab
import kotlinx.android.synthetic.main.fragment_detail.recyclerView
import kotlinx.android.synthetic.main.fragment_detail.textProtectionScrim
import kotlinx.android.synthetic.main.fragment_detail.textProtectionScrim2
import kotlinx.android.synthetic.main.fragment_detail.toolbar
import kotlinx.android.synthetic.main.fragment_detail.toolbar_layout
import java.util.ArrayList
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_detail.contextualToolbar as ctxToolbar

@SuppressLint("RestrictedApi") //NOSONAR
class GenreDetailFragment : //NOSONAR
    BaseFragment(), //NOSONAR
    GenreDetailView, //NOSONAR
    Toolbar.OnMenuItemClickListener, //NOSONAR
    DrawerLockManager.DrawerLock, //NOSONAR
    ContextualToolbarHost { //NOSONAR

    private lateinit var genre: Genre //NOSONAR

    private lateinit var adapter: ViewModelAdapter //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private var collapsingToolbarTextColor: ColorStateList? = null //NOSONAR
    private var collapsingToolbarSubTextColor: ColorStateList? = null //NOSONAR

    private val emptyView = EmptyView(R.string.empty_songlist) //NOSONAR

    private val horizontalRecyclerView = HorizontalRecyclerView("BaseDetail - horizontal") //NOSONAR

    private var setHorizontalItemsDisposable: Disposable? = null //NOSONAR

    private var setItemsDisposable: Disposable? = null //NOSONAR

    private var contextualToolbarHelper: ContextualToolbarHelper<Single<List<Song>>>? = null //NOSONAR

    lateinit var presenter: GenreDetailPresenter //NOSONAR

    @Inject lateinit var presenterFactory: GenreDetailPresenter.Factory //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    @Inject lateinit var sortManager: SortManager //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var songsRepository: Repository.SongsRepository //NOSONAR

    @Inject lateinit var playlistManager: PlaylistManager //NOSONAR

    @Inject lateinit var playlistMenuHelper: PlaylistMenuHelper //NOSONAR

    private var isFirstLoad = true //NOSONAR

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR

        genre = arguments!!.getSerializable(ARG_GENRE) as Genre //NOSONAR

        presenter = presenterFactory.create(genre) //NOSONAR
    }

    override fun onCreate(icicle: Bundle?) { //NOSONAR
        super.onCreate(icicle) //NOSONAR

        requestManager = Glide.with(this) //NOSONAR

        setHasOptionsMenu(true) //NOSONAR

        setEnterSharedElementCallback(enterSharedElementCallback) //NOSONAR

        isFirstLoad = true //NOSONAR

        adapter = ViewModelAdapter() //NOSONAR
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_detail, container, false) //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        toolbar.setNavigationOnClickListener { navigationController.popViewController() } //NOSONAR

        if (ShuttleUtils.canDrawBehindStatusBar()) { //NOSONAR
            toolbar.layoutParams.height = (ActionBarUtils.getActionBarHeight(context!!) + ActionBarUtils.getStatusBarHeight(context!!)).toInt() //NOSONAR
            toolbar.setPadding(toolbar.paddingLeft, (toolbar.paddingTop + ActionBarUtils.getStatusBarHeight(context!!)).toInt(), toolbar.paddingRight, toolbar.paddingBottom) //NOSONAR
        }

        setupToolbarMenu(toolbar!!) //NOSONAR

        recyclerView.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView.setRecyclerListener(RecyclerListener()) //NOSONAR
        recyclerView.adapter = adapter //NOSONAR

        if (isFirstLoad) { //NOSONAR
            recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom) //NOSONAR
        }

        toolbar_layout.title = genre.name //NOSONAR
        toolbar_layout.setSubtitle(null) //NOSONAR
        toolbar_layout.setExpandedTitleTypeface(TypefaceManager.getInstance().getTypeface(context, TypefaceManager.SANS_SERIF_LIGHT)) //NOSONAR
        toolbar_layout.setCollapsedTitleTypeface(TypefaceManager.getInstance().getTypeface(context, TypefaceManager.SANS_SERIF)) //NOSONAR

        setupContextualToolbar() //NOSONAR

        val transitionName = arguments!!.getString(ARG_TRANSITION_NAME) //NOSONAR
        ViewCompat.setTransitionName(background!!, transitionName) //NOSONAR

        if (isFirstLoad) { //NOSONAR
            fab.visibility = View.GONE //NOSONAR
        }

        fab.setOnClickListener { //NOSONAR
            presenter.shuffleAll() //NOSONAR
        }

        if (transitionName == null) { //NOSONAR
            fadeInUi() //NOSONAR
        }

        loadBackgroundImage() //NOSONAR

        disposables.add(Aesthetic.get(context) //NOSONAR
            .colorPrimary() //NOSONAR
            .compose(distinctToMainThread()) //NOSONAR
            .subscribe { primaryColor -> //NOSONAR
                toolbar_layout!!.setContentScrimColor(primaryColor!!) //NOSONAR
                toolbar_layout!!.setBackgroundColor(primaryColor) //NOSONAR
            })

        presenter.bindView(this) //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        presenter.loadData() //NOSONAR

        DrawerLockManager.getInstance().addDrawerLock(this) //NOSONAR
    }

    override fun onPause() { //NOSONAR

        DrawerLockManager.getInstance().removeDrawerLock(this) //NOSONAR

        super.onPause() //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        setItemsDisposable?.dispose() //NOSONAR

        setHorizontalItemsDisposable?.dispose() //NOSONAR

        disposables.clear() //NOSONAR

        presenter.unbindView(this) //NOSONAR

        isFirstLoad = false //NOSONAR

        super.onDestroyView() //NOSONAR
    }

    private fun setupToolbarMenu(toolbar: Toolbar) { //NOSONAR
        toolbar.inflateMenu(R.menu.menu_detail_sort) //NOSONAR

        if (CastManager.isCastAvailable(context!!, settingsManager)) { //NOSONAR
            val menuItem = CastButtonFactory.setUpMediaRouteButton(context, toolbar.menu, R.id.media_route_menu_item) //NOSONAR
            menuItem.isVisible = true //NOSONAR
        }

        toolbar.setOnMenuItemClickListener(this) //NOSONAR

        // Create playlist menu
        val sub = toolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
        disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(sub).subscribe()) //NOSONAR

        // Inflate sorting menus
        val item = toolbar.menu.findItem(R.id.sorting) //NOSONAR

        activity!!.menuInflater.inflate(R.menu.menu_detail_sort_albums, item.subMenu) //NOSONAR
        activity!!.menuInflater.inflate(R.menu.menu_detail_sort_songs, item.subMenu) //NOSONAR

        AlbumSortHelper.updateAlbumSortMenuItems(toolbar.menu, sortManager.genreDetailAlbumsSortOrder, sortManager.genreDetailAlbumsAscending) //NOSONAR
        SongSortHelper.updateSongSortMenuItems(toolbar.menu, sortManager.genreDetailSongsSortOrder, sortManager.genreDetailSongsAscending) //NOSONAR
    }

    override fun onMenuItemClick(item: MenuItem): Boolean { //NOSONAR
        if (!GenreMenuUtils.getGenreClickListener(genre, presenter).onMenuItemClick(item)) { //NOSONAR
            val albumSortOrder = AlbumSortHelper.handleAlbumDetailMenuSortOrderClicks(item) //NOSONAR
            if (albumSortOrder != null) { //NOSONAR
                sortManager.genreDetailAlbumsSortOrder = albumSortOrder //NOSONAR
                presenter.loadData() //NOSONAR
            }
            val albumsAsc = AlbumSortHelper.handleAlbumDetailMenuSortOrderAscClicks(item) //NOSONAR
            if (albumsAsc != null) { //NOSONAR
                sortManager.genreDetailAlbumsAscending = albumsAsc //NOSONAR
                presenter.loadData() //NOSONAR
            }
            val songSortOrder = SongSortHelper.handleSongMenuSortOrderClicks(item) //NOSONAR
            if (songSortOrder != null) { //NOSONAR
                sortManager.genreDetailSongsSortOrder = songSortOrder //NOSONAR
                presenter.loadData() //NOSONAR
            }
            val songsAsc = SongSortHelper.handleSongDetailMenuSortOrderAscClicks(item) //NOSONAR
            if (songsAsc != null) { //NOSONAR
                sortManager.genreDetailSongsAscending = songsAsc //NOSONAR
                presenter.loadData() //NOSONAR
            }

            AlbumSortHelper.updateAlbumSortMenuItems(toolbar!!.menu, sortManager.genreDetailAlbumsSortOrder, sortManager.genreDetailAlbumsAscending) //NOSONAR
            SongSortHelper.updateSongSortMenuItems(toolbar!!.menu, sortManager.genreDetailSongsSortOrder, sortManager.genreDetailSongsAscending) //NOSONAR
        }

        return super.onOptionsItemSelected(item) //NOSONAR
    }

    private fun loadBackgroundImage() { //NOSONAR

        val width = ResourceUtils.getScreenSize().width + ResourceUtils.toPixels(60f) //NOSONAR
        val height = resources.getDimensionPixelSize(R.dimen.header_view_height) //NOSONAR

        requestManager.load<ArtworkProvider>(null as ArtworkProvider?) //NOSONAR
            // Need to override the height/width, as the shared element transition tricks Glide into thinking this ImageView has
            // the same dimensions as the ImageView that the transition starts with.
            // So we'll set it to screen width (plus a little extra, which might fix an issue on some devices..)
            .override(width, height) //NOSONAR
            .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
            .priority(Priority.HIGH) //NOSONAR
            .placeholder(PlaceholderProvider.getInstance(context).getPlaceHolderDrawable(genre.name, true, settingsManager)) //NOSONAR
            .centerCrop() //NOSONAR
            .animate(AlwaysCrossFade(false)) //NOSONAR
            .into(background) //NOSONAR
    }

    override fun setSharedElementEnterTransition(transition: Any?) { //NOSONAR
        super.setSharedElementEnterTransition(transition) //NOSONAR
        (transition as Transition).addListener(sharedElementEnterTransitionListenerAdapter) //NOSONAR
    }

    internal fun fadeInUi() { //NOSONAR

        if (textProtectionScrim == null || textProtectionScrim2 == null || fab == null) { //NOSONAR
            return //NOSONAR
        }

        //Fade in the text protection scrim
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

        //Fade & grow the FAB
        fab!!.alpha = 0f //NOSONAR
        fab!!.visibility = View.VISIBLE //NOSONAR

        fadeAnimator = ObjectAnimator.ofFloat(fab, View.ALPHA, 0.5f, 1f) //NOSONAR
        val scaleXAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_X, 0f, 1f) //NOSONAR
        val scaleYAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_Y, 0f, 1f) //NOSONAR

        val animatorSet = AnimatorSet() //NOSONAR
        animatorSet.playTogether(fadeAnimator, scaleXAnimator, scaleYAnimator) //NOSONAR
        animatorSet.duration = 250 //NOSONAR
        animatorSet.start() //NOSONAR
    }

    override fun getContextualToolbar(): ContextualToolbar? { //NOSONAR
        return ctxToolbar as ContextualToolbar //NOSONAR
    }

    private fun setupContextualToolbar() { //NOSONAR

        val contextualToolbar = ContextualToolbar.findContextualToolbar(this) //NOSONAR
        if (contextualToolbar != null) { //NOSONAR

            contextualToolbar.setTransparentBackground(true) //NOSONAR

            contextualToolbar.menu.clear() //NOSONAR
            contextualToolbar.inflateMenu(R.menu.context_menu_general) //NOSONAR
            val sub = contextualToolbar.menu.findItem(R.id.addToPlaylist).subMenu //NOSONAR
            disposables.add(playlistMenuHelper.createUpdatingPlaylistMenu(sub).subscribe()) //NOSONAR

            contextualToolbar.setOnMenuItemClickListener( //NOSONAR
                SongMenuUtils.getSongMenuClickListener(Single.defer { Operators.reduceSongSingles(contextualToolbarHelper!!.items) }, presenter) //NOSONAR
            )

            contextualToolbarHelper = object : ContextualToolbarHelper<Single<List<Song>>>(context!!, contextualToolbar, object : ContextualToolbarHelper.Callback { //NOSONAR

                override fun notifyItemChanged(viewModel: SelectableViewModel) { //NOSONAR
                    if (adapter.items.contains(viewModel as ViewModel<*>)) { //NOSONAR
                        val index = adapter.items.indexOf(viewModel) //NOSONAR
                        if (index >= 0) { //NOSONAR
                            adapter.notifyItemChanged(index, 0) //NOSONAR
                        }
                    } else if (horizontalRecyclerView.viewModelAdapter.items.contains(viewModel)) { //NOSONAR
                        val index = horizontalRecyclerView.viewModelAdapter.items.indexOf(viewModel) //NOSONAR
                        if (index >= 0) { //NOSONAR
                            horizontalRecyclerView.viewModelAdapter.notifyItemChanged(index, 0) //NOSONAR
                        }
                    }
                }

                override fun notifyDatasetChanged() { //NOSONAR
                    adapter.notifyItemRangeChanged(0, adapter.items.size, 0) //NOSONAR
                    horizontalRecyclerView.viewModelAdapter.notifyItemRangeChanged(0, horizontalRecyclerView.viewModelAdapter.items.size, 0) //NOSONAR
                }
            }) {
                override fun start() { //NOSONAR
                    super.start() //NOSONAR
                    // Need to hide the collapsed text, as it overlaps the contextual toolbar
                    collapsingToolbarTextColor = toolbar_layout!!.collapsedTitleTextColor //NOSONAR
                    collapsingToolbarSubTextColor = toolbar_layout!!.collapsedSubTextColor //NOSONAR
                    toolbar_layout!!.setCollapsedTitleTextColor(0x01FFFFFF) //NOSONAR
                    toolbar_layout!!.setCollapsedSubTextColor(0x01FFFFFF) //NOSONAR

                    toolbar!!.visibility = View.GONE //NOSONAR
                }

                override fun finish() { //NOSONAR
                    if (toolbar_layout != null && collapsingToolbarTextColor != null && collapsingToolbarSubTextColor != null) { //NOSONAR
                        toolbar_layout!!.collapsedTitleTextColor = collapsingToolbarTextColor!! //NOSONAR
                        toolbar_layout!!.collapsedSubTextColor = collapsingToolbarSubTextColor!! //NOSONAR
                    }
                    if (toolbar != null) { //NOSONAR
                        toolbar!!.visibility = View.VISIBLE //NOSONAR
                    }
                    super.finish() //NOSONAR
                }
            }
        }
    }

    private val sharedElementEnterTransitionListenerAdapter: TransitionListenerAdapter //NOSONAR
        get() = object : TransitionListenerAdapter() { //NOSONAR
            override fun onTransitionEnd(transition: Transition) { //NOSONAR
                transition.removeListener(this) //NOSONAR
                fadeInUi() //NOSONAR
            }
        }

    private val enterSharedElementCallback = object : SharedElementCallback() { //NOSONAR
        override fun onSharedElementStart(sharedElementNames: List<String>?, sharedElements: List<View>?, sharedElementSnapshots: List<View>?) { //NOSONAR
            super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots) //NOSONAR

            fab?.visibility = View.GONE //NOSONAR
        }
    }

    private var songClickListener: SongView.ClickListener = object : SongView.ClickListener { //NOSONAR
        override fun onSongClick(position: Int, songView: SongView) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(songView, Single.just(listOf(songView.song)))) { //NOSONAR
                presenter.songClicked(songView.song) //NOSONAR
            }
        }

        override fun onSongLongClick(position: Int, songView: SongView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(songView, Single.just(listOf(songView.song))) //NOSONAR
        }

        override fun onSongOverflowClick(position: Int, v: View, song: Song) { //NOSONAR
            val popupMenu = PopupMenu(v.context, v) //NOSONAR
            SongMenuUtils.setupSongMenu(popupMenu, false, true, playlistMenuHelper) //NOSONAR
            popupMenu.setOnMenuItemClickListener(SongMenuUtils.getSongMenuClickListener(song, presenter)) //NOSONAR
            popupMenu.show() //NOSONAR
        }

        override fun onStartDrag(holder: SongView.ViewHolder) { //NOSONAR
            // Intentionally left empty.
        }
    }

    private var albumClickListener: AlbumView.ClickListener = object : AlbumView.ClickListener { //NOSONAR

        override fun onAlbumClick(position: Int, albumView: AlbumView, viewHolder: AlbumView.ViewHolder) { //NOSONAR
            if (!contextualToolbarHelper!!.handleClick(albumView, albumView.album.getSongsSingle(songsRepository))) { //NOSONAR
                pushDetailFragment(AlbumDetailFragment.newInstance(albumView.album, ViewCompat.getTransitionName(viewHolder.imageOne)!!), viewHolder.imageOne) //NOSONAR
            }
        }

        override fun onAlbumLongClick(position: Int, albumView: AlbumView): Boolean { //NOSONAR
            return contextualToolbarHelper!!.handleLongClick(albumView, albumView.album.getSongsSingle(songsRepository)) //NOSONAR
        }

        override fun onAlbumOverflowClicked(v: View, album: Album) { //NOSONAR
            val popupMenu = PopupMenu(v.context, v) //NOSONAR
            AlbumMenuUtils.setupAlbumMenu(popupMenu, playlistMenuHelper, true) //NOSONAR
            popupMenu.setOnMenuItemClickListener(AlbumMenuUtils.getAlbumMenuClickListener(album, presenter)) //NOSONAR
            popupMenu.show() //NOSONAR
        }
    }

    private fun pushDetailFragment(fragment: Fragment, transitionView: View?) { //NOSONAR

        val transitions = ArrayList<Pair<View, String>>() //NOSONAR

        if (transitionView != null) { //NOSONAR
            val transitionName = ViewCompat.getTransitionName(transitionView) //NOSONAR
            transitions.add(Pair(transitionView, transitionName)) //NOSONAR
            //            transitions.add(new Pair<>(toolbar, "toolbar"));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
                val moveTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_transition) //NOSONAR
                fragment.sharedElementEnterTransition = moveTransition //NOSONAR
                fragment.sharedElementReturnTransition = moveTransition //NOSONAR
            }
        }

        navigationController.pushViewController(fragment, "DetailFragment", transitions) //NOSONAR
    }


    // GenreDetailView implementation

    override fun closeContextualToolbar() { //NOSONAR
        contextualToolbarHelper?.finish() //NOSONAR
    }

    override fun setData(albums: List<Album>, songs: List<Song>) { //NOSONAR
        val viewModels = ArrayList<ViewModel<*>>() //NOSONAR

        if (!albums.isEmpty()) { //NOSONAR

            val items = ArrayList<ViewModel<*>>() //NOSONAR

            setHorizontalItemsDisposable?.dispose() //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, "horizontalRecyclerView.setItems()") //NOSONAR
            setHorizontalItemsDisposable = horizontalRecyclerView.setItems(albums //NOSONAR
                .map { album -> //NOSONAR
                    val horizontalAlbumView = HorizontalAlbumView(album, requestManager, sortManager, settingsManager) //NOSONAR
                    horizontalAlbumView.setClickListener(albumClickListener) //NOSONAR
                    horizontalAlbumView.showYear(true) //NOSONAR
                    horizontalAlbumView //NOSONAR
                })
            items.add(SubheaderView(StringUtils.makeAlbumsLabel(context!!, albums.size))) //NOSONAR
            items.add(horizontalRecyclerView) //NOSONAR

            viewModels.addAll(items) //NOSONAR
        }

        if (!songs.isEmpty()) { //NOSONAR
            val items = ArrayList<ViewModel<*>>() //NOSONAR

            items.add(SubheaderView(StringUtils.makeSongsAndTimeLabel(context!!, songs.size, Stream.of(songs).mapToLong { song -> song.duration / 1000 }.sum()))) //NOSONAR

            items.addAll( //NOSONAR
                songs //NOSONAR
                    .map { song -> //NOSONAR
                        val songView = SongView(song, requestManager, sortManager, settingsManager) //NOSONAR
                        songView.setClickListener(songClickListener) //NOSONAR
                        songView //NOSONAR
                    }.toList() //NOSONAR
            )

            viewModels.addAll(items) //NOSONAR
        }
        if (viewModels.isEmpty()) { //NOSONAR
            viewModels.add(emptyView) //NOSONAR
        }

        setItemsDisposable = adapter.setItems(viewModels, object : CompletionListUpdateCallbackAdapter() { //NOSONAR
            override fun onComplete() { //NOSONAR
                recyclerView?.scheduleLayoutAnimation() //NOSONAR
            }
        })
    }

    override fun fadeInSlideShowAlbum(previousAlbum: Album?, newAlbum: Album) { //NOSONAR
        //This crazy business is what's required to have a smooth Glide crossfade with no 'white flicker'
        requestManager.load(newAlbum) //NOSONAR
            .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
            .priority(Priority.HIGH) //NOSONAR
            .error(PlaceholderProvider.getInstance(context).getPlaceHolderDrawable(newAlbum.name, true, settingsManager)) //NOSONAR
            .centerCrop() //NOSONAR
            .thumbnail( //NOSONAR
                Glide //NOSONAR
                    .with(this) //NOSONAR
                    .load<Album>(previousAlbum) //NOSONAR
                    .centerCrop() //NOSONAR
            )
            .crossFade(600) //NOSONAR
            .into(background) //NOSONAR
    }


    // AlbumMenuContract.View Implementation

    override fun presentTagEditorDialog(album: Album) { //NOSONAR
        TaggerDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    }

    override fun presentDeleteAlbumsDialog(albums: List<Album>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListAlbumsRef { albums }).show(childFragmentManager) //NOSONAR
    }

    override fun presentAlbumInfoDialog(album: Album) { //NOSONAR
        AlbumBiographyDialog.newInstance(album).show(childFragmentManager) //NOSONAR
    }

    override fun presentArtworkEditorDialog(album: Album) { //NOSONAR
        ArtworkDialog.build(context, album).show() //NOSONAR
    }

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message
        Toast.makeText(context, R.string.empty_playlist, Toast.LENGTH_SHORT).show() //NOSONAR
    }

    // SongMenuContract.View Implementation

    override fun presentCreatePlaylistDialog(songs: List<Song>) { //NOSONAR
        CreatePlaylistDialog.newInstance(songs).show(childFragmentManager, "CreatePlaylistDialog") //NOSONAR
    }

    override fun presentSongInfoDialog(song: Song) { //NOSONAR
        SongInfoDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    }

    override fun onSongsAddedToPlaylist(playlist: Playlist, numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoplaylist, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun presentTagEditorDialog(song: Song) { //NOSONAR
        TaggerDialog.newInstance(song).show(childFragmentManager) //NOSONAR
    }

    override fun presentDeleteDialog(songs: List<Song>) { //NOSONAR
        DeleteDialog.newInstance(DeleteDialog.ListSongsRef { songs }).show(childFragmentManager) //NOSONAR
    }

    override fun shareSong(song: Song) { //NOSONAR
        song.share(context!!) //NOSONAR
    }

    override fun presentRingtonePermissionDialog() { //NOSONAR
        RingtoneManager.getDialog(context!!).show() //NOSONAR
    }

    override fun showRingtoneSetMessage() { //NOSONAR
        Toast.makeText(context, R.string.ringtone_set_new, Toast.LENGTH_SHORT).show() //NOSONAR
    }


    // BaseFragment Implementation

    public override fun screenName(): String { //NOSONAR
        return "GenreDetailFragment" //NOSONAR
    }


    // Static

    companion object { //NOSONAR

        private const val TAG = "BaseDetailFragment" //NOSONAR

        private const val ARG_TRANSITION_NAME = "transition_name" //NOSONAR

        var ARG_GENRE = "genre" //NOSONAR

        fun newInstance(genre: Genre): GenreDetailFragment { //NOSONAR
            val args = Bundle() //NOSONAR
            val fragment = GenreDetailFragment() //NOSONAR
            args.putSerializable(ARG_GENRE, genre) //NOSONAR
            fragment.arguments = args //NOSONAR
            return fragment //NOSONAR
        }
    }
}



