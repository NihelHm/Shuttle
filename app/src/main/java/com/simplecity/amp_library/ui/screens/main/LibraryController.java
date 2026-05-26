package com.simplecity.amp_library.ui.screens.main; // NOSONAR

import android.content.IntentFilter; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.design.widget.AppBarLayout; // NOSONAR
import android.support.design.widget.TabLayout; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.support.v4.view.ViewCompat; // NOSONAR
import android.support.v4.view.ViewPager; // NOSONAR
import android.support.v7.app.AppCompatActivity; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.transition.Transition; // NOSONAR
import android.transition.TransitionInflater; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.Menu; // NOSONAR
import android.view.MenuInflater; // NOSONAR
import android.view.MenuItem; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.ViewBackgroundAction; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.cantrowitz.rxbroadcast.RxBroadcast; // NOSONAR
import com.google.android.gms.cast.framework.CastButtonFactory; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.cast.CastManager; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist; // NOSONAR
import com.simplecity.amp_library.model.CategoryItem; // NOSONAR
import com.simplecity.amp_library.model.Genre; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR
import com.simplecity.amp_library.ui.adapters.PagerAdapter; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment; // NOSONAR
import com.simplecity.amp_library.ui.common.ToolbarListener; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.list.AlbumListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.detail.GenreDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.list.GenreListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.search.SearchFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.suggested.SuggestedFragment; // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbar; // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbarHost; // NOSONAR
import com.simplecity.amp_library.ui.views.RatingSnackbar; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetEventRelay; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR
import test.com.androidnavigation.fragment.FragmentInfo; // NOSONAR

import static com.afollestad.aesthetic.Rx.distinctToMainThread; // NOSONAR
import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LibraryController extends BaseFragment implements //NOSONAR
        AlbumArtistListFragment.AlbumArtistClickListener, //NOSONAR
        AlbumListFragment.AlbumClickListener, //NOSONAR
        SuggestedFragment.SuggestedClickListener, //NOSONAR
        PlaylistListFragment.PlaylistClickListener, //NOSONAR
        GenreListFragment.GenreClickListener, //NOSONAR
        ContextualToolbarHost { //NOSONAR

    private static final String TAG = "LibraryController"; //NOSONAR

    public static final String EVENT_TABS_CHANGED = "tabs_changed"; //NOSONAR

    @BindView(R.id.tabs) //NOSONAR
    TabLayout slidingTabLayout; //NOSONAR

    @BindView(R.id.pager) //NOSONAR
    ViewPager pager; //NOSONAR

    @BindView(R.id.toolbar) //NOSONAR
    Toolbar toolbar; //NOSONAR

    @BindView(R.id.contextualToolbar) //NOSONAR
    ContextualToolbar contextualToolbar; //NOSONAR

    @BindView(R.id.app_bar) //NOSONAR
    AppBarLayout appBarLayout; //NOSONAR

    @Inject //NOSONAR
    NavigationEventRelay navigationEventRelay; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    MultiSheetEventRelay multiSheetEventRelay; //NOSONAR

    @Inject //NOSONAR
    AnalyticsManager analyticsManager; //NOSONAR

    private CompositeDisposable compositeDisposable = new CompositeDisposable(); //NOSONAR

    private Disposable tabChangedDisposable; //NOSONAR

    private Unbinder unbinder; //NOSONAR

    private boolean refreshPagerAdapter = false; //NOSONAR

    private PagerAdapter pagerAdapter; //NOSONAR

    public static FragmentInfo fragmentInfo() { //NOSONAR
        return new FragmentInfo(LibraryController.class, null, "LibraryController"); //NOSONAR
    } // NOSONAR

    public LibraryController() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(@Nullable Bundle savedInstanceState) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        setHasOptionsMenu(true); //NOSONAR

        tabChangedDisposable = RxBroadcast.fromLocalBroadcast(getContext(), new IntentFilter(EVENT_TABS_CHANGED)).subscribe(onNext -> refreshPagerAdapter = true); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) { //NOSONAR
        final View rootView = inflater.inflate(R.layout.fragment_library, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); //NOSONAR

        setupViewPager(); //NOSONAR

        compositeDisposable.add(Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .compose(distinctToMainThread()) //NOSONAR
                .subscribe(color -> ViewBackgroundAction.create(appBarLayout) //NOSONAR
                        .accept(color), onErrorLogAndRethrow())); //NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR

        if (getActivity() instanceof ToolbarListener) { //NOSONAR
            ((ToolbarListener) getActivity()).toolbarAttached(view.findViewById(R.id.toolbar)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (!mediaManager.getQueue().isEmpty()) { //NOSONAR
            multiSheetEventRelay.sendEvent(new MultiSheetEventRelay.MultiSheetEvent(MultiSheetEventRelay.MultiSheetEvent.Action.SHOW_IF_HIDDEN, MultiSheetView.Sheet.NONE)); //NOSONAR
        } // NOSONAR

        navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.LIBRARY_SELECTED, null, false)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        pager.setAdapter(null); //NOSONAR
        compositeDisposable.clear(); //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR
        tabChangedDisposable.dispose(); //NOSONAR
        super.onDestroy(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { //NOSONAR
        super.onCreateOptionsMenu(menu, inflater); //NOSONAR

        inflater.inflate(R.menu.menu_library, menu); //NOSONAR

        if (CastManager.isCastAvailable(getContext(), settingsManager)) { //NOSONAR
            MenuItem menuItem = CastButtonFactory.setUpMediaRouteButton(getContext(), menu, R.id.media_route_menu_item); //NOSONAR
            menuItem.setVisible(true); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onOptionsItemSelected(MenuItem item) { //NOSONAR
        switch (item.getItemId()) { //NOSONAR
            case R.id.action_search: //NOSONAR
                openSearch(); //NOSONAR
                return true; //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    private void setupViewPager() { //NOSONAR
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext()); //NOSONAR
        CategoryItem.getCategoryItems(sharedPreferences); //NOSONAR

        if (pagerAdapter != null && refreshPagerAdapter) { //NOSONAR
            pagerAdapter.removeAllChildFragments(); //NOSONAR
            refreshPagerAdapter = false; //NOSONAR
            pager.setAdapter(null); //NOSONAR
        } // NOSONAR

        int defaultPage = 1; //NOSONAR

        pagerAdapter = new PagerAdapter(getChildFragmentManager()); //NOSONAR
        List<CategoryItem> categoryItems = Stream.of(CategoryItem.getCategoryItems(sharedPreferences)) //NOSONAR
                .filter(categoryItem -> categoryItem.isChecked) //NOSONAR
                .toList(); //NOSONAR

        int defaultPageType = settingsManager.getDefaultPageType(); //NOSONAR
        for (int i = 0; i < categoryItems.size(); i++) { //NOSONAR
            CategoryItem categoryItem = categoryItems.get(i); //NOSONAR
            pagerAdapter.addFragment(categoryItem.getFragment(getContext())); //NOSONAR
            if (categoryItem.type == defaultPageType) { //NOSONAR
                defaultPage = i; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        int currentPage = Math.min(defaultPage, pagerAdapter.getCount()); //NOSONAR
        pager.setAdapter(pagerAdapter); //NOSONAR
        pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1); //NOSONAR
        pager.setCurrentItem(currentPage); //NOSONAR

        slidingTabLayout.setupWithViewPager(pager); //NOSONAR

        pager.postDelayed(() -> { //NOSONAR
            if (pager != null) { //NOSONAR
                new RatingSnackbar(settingsManager, analyticsManager).show(pager, () -> { //NOSONAR
                    ShuttleUtils.openShuttleLink(getActivity(), getActivity().getPackageName(), getActivity().getPackageManager()); //NOSONAR
                    return Unit.INSTANCE; //NOSONAR
                }); // NOSONAR
            } // NOSONAR
        }, 1000); //NOSONAR
    } // NOSONAR

    private void openSearch() { //NOSONAR
        getNavigationController().pushViewController(SearchFragment.Companion.newInstance(null), "SearchFragment"); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAlbumArtistClicked(AlbumArtist albumArtist, View transitionView) { //NOSONAR
        String transitionName = ViewCompat.getTransitionName(transitionView); //NOSONAR
        ArtistDetailFragment detailFragment = ArtistDetailFragment.Companion.newInstance(albumArtist, transitionName); //NOSONAR
        pushDetailFragment(detailFragment, transitionView); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAlbumClicked(Album album, View transitionView) { //NOSONAR
        String transitionName = ViewCompat.getTransitionName(transitionView); //NOSONAR
        AlbumDetailFragment detailFragment = AlbumDetailFragment.Companion.newInstance(album, transitionName); //NOSONAR
        pushDetailFragment(detailFragment, transitionView); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onGenreClicked(Genre genre) { //NOSONAR
        pushDetailFragment(GenreDetailFragment.Companion.newInstance(genre), null); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPlaylistClicked(Playlist playlist) { //NOSONAR
        pushDetailFragment(PlaylistDetailFragment.Companion.newInstance(playlist), null); //NOSONAR
    } // NOSONAR

    void pushDetailFragment(Fragment fragment, @Nullable View transitionView) { //NOSONAR

        List<Pair<View, String>> transitions = new ArrayList<>(); //NOSONAR

        if (transitionView != null) { //NOSONAR
            String transitionName = ViewCompat.getTransitionName(transitionView); //NOSONAR
            transitions.add(new Pair<>(transitionView, transitionName)); //NOSONAR
            //            transitions.add(new Pair<>(toolbar, "toolbar")); // NOSONAR

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
                Transition moveTransition = TransitionInflater.from(getContext()).inflateTransition(R.transition.image_transition); //NOSONAR
                fragment.setSharedElementEnterTransition(moveTransition); //NOSONAR
                fragment.setSharedElementReturnTransition(moveTransition); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        getNavigationController().pushViewController(fragment, "DetailFragment", transitions); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return "LibraryController"; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ContextualToolbar getContextualToolbar() { //NOSONAR
        return contextualToolbar; //NOSONAR
    } // NOSONAR
} // NOSONAR
