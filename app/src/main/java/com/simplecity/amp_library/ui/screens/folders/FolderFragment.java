package com.simplecity.amp_library.ui.screens.folders; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.support.design.widget.AppBarLayout; // NOSONAR
import android.support.v7.widget.LinearLayoutManager; // NOSONAR
import android.support.v7.widget.PopupMenu; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.Menu; // NOSONAR
import android.view.MenuInflater; // NOSONAR
import android.view.MenuItem; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.CheckBox; // NOSONAR
import android.widget.Toast; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.ViewBackgroundAction; // NOSONAR
import com.annimon.stream.Collectors; // NOSONAR
import com.annimon.stream.IntStream; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.interfaces.Breadcrumb; // NOSONAR
import com.simplecity.amp_library.interfaces.BreadcrumbListener; // NOSONAR
import com.simplecity.amp_library.interfaces.FileType; // NOSONAR
import com.simplecity.amp_library.model.BaseFileObject; // NOSONAR
import com.simplecity.amp_library.model.InclExclItem; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment; // NOSONAR
import com.simplecity.amp_library.ui.dialog.SongInfoDialog; // NOSONAR
import com.simplecity.amp_library.ui.modelviews.BreadcrumbsView; // NOSONAR
import com.simplecity.amp_library.ui.modelviews.FolderView; // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog; // NOSONAR
import com.simplecity.amp_library.ui.views.BreadcrumbItem; // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbar; // NOSONAR
import com.simplecity.amp_library.ui.views.ThemedStatusBarView; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.ContextualToolbarHelper; // NOSONAR
import com.simplecity.amp_library.utils.FileBrowser; // NOSONAR
import com.simplecity.amp_library.utils.FileHelper; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.extensions.SongExtKt; // NOSONAR
import com.simplecity.amp_library.utils.menu.MenuUtils; // NOSONAR
import com.simplecity.amp_library.utils.menu.folder.FolderMenuUtils; // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager; // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistMenuHelper; // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager; // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.annotations.Nullable; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Function3; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.io.File; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR
import org.jetbrains.annotations.NotNull; // NOSONAR
import test.com.androidnavigation.fragment.BackPressListener; // NOSONAR

import static com.afollestad.aesthetic.Rx.distinctToMainThread; // NOSONAR
import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FolderFragment extends BaseFragment implements //NOSONAR
        BreadcrumbListener, //NOSONAR
        BackPressListener, //NOSONAR
        FolderView.ClickListener, //NOSONAR
        Toolbar.OnMenuItemClickListener, //NOSONAR
        DrawerLockManager.DrawerLock { //NOSONAR

    private static final String TAG = "FolderFragment"; //NOSONAR

    private static final String ARG_CURRENT_DIR = "current_dir"; //NOSONAR

    private static final String ARG_DISPLAYED_IN_TABS = "displayed_in_tabs"; //NOSONAR

    private static final String ARG_TITLE = "title"; //NOSONAR

    ViewModelAdapter adapter; //NOSONAR

    @BindView(R.id.recyclerView) //NOSONAR
    RecyclerView recyclerView; //NOSONAR

    @BindView(R.id.toolbar) //NOSONAR
    Toolbar toolbar; //NOSONAR

    @BindView(R.id.breadcrumb_view) //NOSONAR
    Breadcrumb breadcrumb; //NOSONAR

    @BindView(R.id.contextualToolbar) //NOSONAR
    ContextualToolbar contextualToolbar; //NOSONAR

    @BindView(R.id.app_bar) //NOSONAR
    AppBarLayout appBarLayout; //NOSONAR

    @BindView(R.id.statusBarView) //NOSONAR
    ThemedStatusBarView statusBarView; //NOSONAR

    private CompositeDisposable compositeDisposable = new CompositeDisposable(); //NOSONAR

    String currentDir; //NOSONAR

    boolean displayedInTabs = false; //NOSONAR

    FileBrowser fileBrowser; //NOSONAR

    boolean showBreadcrumbsInList; //NOSONAR

    private boolean isShowingWhitelist; //NOSONAR
    private boolean isShowingBlacklist; //NOSONAR

    private CompositeDisposable disposables; //NOSONAR

    private ContextualToolbarHelper<BaseFileObject> contextualToolbarHelper; //NOSONAR

    @Nullable //NOSONAR
    private BreadcrumbsView breadcrumbsView; //NOSONAR

    private Unbinder unbinder; //NOSONAR

    @Nullable //NOSONAR
    private Disposable setItemsDisposable; //NOSONAR

    @Inject //NOSONAR
    Repository.BlacklistRepository blacklistRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.WhitelistRepository whitelistRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.SongsRepository songsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.PlaylistsRepository playlistsRepository; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    AnalyticsManager analyticsManager; //NOSONAR

    @Inject //NOSONAR
    RingtoneManager ringtoneManager; //NOSONAR

    @Inject //NOSONAR
    PlaylistManager playlistManager; //NOSONAR

    @Inject //NOSONAR
    PlaylistMenuHelper playlistMenuHelper; //NOSONAR

    public static FolderFragment newInstance(String title, boolean isDisplayedInTabs) { //NOSONAR
        FolderFragment fragment = new FolderFragment(); //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putString(ARG_TITLE, title); //NOSONAR
        args.putBoolean(ARG_DISPLAYED_IN_TABS, isDisplayedInTabs); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(final Bundle savedInstanceState) { //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        disposables = new CompositeDisposable(); //NOSONAR

        adapter = new ViewModelAdapter(); //NOSONAR

        fileBrowser = new FileBrowser(settingsManager); //NOSONAR

        if (savedInstanceState != null) { //NOSONAR
            currentDir = savedInstanceState.getString(ARG_CURRENT_DIR); //NOSONAR
        } // NOSONAR

        displayedInTabs = getArguments().getBoolean(ARG_DISPLAYED_IN_TABS); //NOSONAR

        if (displayedInTabs) { //NOSONAR
            setHasOptionsMenu(true); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //NOSONAR

        View rootView = inflater.inflate(R.layout.fragment_folder_browser, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        if (displayedInTabs) { //NOSONAR
            breadcrumbsView = new BreadcrumbsView(currentDir); //NOSONAR
            showBreadcrumbsInList = true; //NOSONAR
            changeBreadcrumbPath(); //NOSONAR
            appBarLayout.setVisibility(View.GONE); //NOSONAR
            statusBarView.setVisibility(View.GONE); //NOSONAR
        } else { //NOSONAR
            showBreadcrumbsInList = false; //NOSONAR
            breadcrumb.addBreadcrumbListener(this); //NOSONAR
            if (!TextUtils.isEmpty(currentDir)) { //NOSONAR
                breadcrumb.changeBreadcrumbPath(currentDir); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (!displayedInTabs) { //NOSONAR
            toolbar.inflateMenu(R.menu.menu_folders); //NOSONAR
            toolbar.setNavigationOnClickListener(v -> getNavigationController().popViewController()); //NOSONAR
            toolbar.setOnMenuItemClickListener(this); //NOSONAR
            updateMenuItems(toolbar.getMenu()); //NOSONAR
        } // NOSONAR

        recyclerView.setRecyclerListener(new RecyclerListener()); //NOSONAR
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //NOSONAR
        recyclerView.setAdapter(adapter); //NOSONAR

        compositeDisposable.add(Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .compose(distinctToMainThread()) //NOSONAR
                .subscribe(color -> ViewBackgroundAction.create(appBarLayout).accept(color), onErrorLogAndRethrow())); //NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (currentDir == null) { //NOSONAR
            disposables.add(Observable.fromCallable(() -> { //NOSONAR
                        if (!TextUtils.isEmpty(currentDir)) { //NOSONAR
                            return new File(currentDir); //NOSONAR
                        } else { //NOSONAR
                            return fileBrowser.getInitialDir(); //NOSONAR
                        } // NOSONAR
                    }).subscribeOn(Schedulers.io()) //NOSONAR
                            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                            .subscribe( //NOSONAR
                                    this::changeDir, //NOSONAR
                                    error -> LogUtils.logException(TAG, "Error in onResume", error)) //NOSONAR
            ); // NOSONAR
        } // NOSONAR

        getNavigationController().addBackPressListener(this); //NOSONAR

        if (!displayedInTabs) { //NOSONAR
            DrawerLockManager.getInstance().addDrawerLock(this); //NOSONAR
        } // NOSONAR

        if (isVisible()) { //NOSONAR
            setupContextualToolbar(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        disposables.clear(); //NOSONAR

        getNavigationController().removeBackPressListener(this); //NOSONAR

        if (!displayedInTabs) { //NOSONAR
            DrawerLockManager.getInstance().removeDrawerLock(this); //NOSONAR
        } // NOSONAR

        super.onPause(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        compositeDisposable.clear(); //NOSONAR
        if (setItemsDisposable != null) { //NOSONAR
            setItemsDisposable.dispose(); //NOSONAR
        } // NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onSaveInstanceState(Bundle outState) { //NOSONAR
        outState.putString(ARG_CURRENT_DIR, currentDir); //NOSONAR
        super.onSaveInstanceState(outState); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { //NOSONAR
        inflater.inflate(R.menu.menu_folders, menu); //NOSONAR
        super.onCreateOptionsMenu(menu, inflater); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPrepareOptionsMenu(Menu menu) { //NOSONAR
        super.onPrepareOptionsMenu(menu); //NOSONAR

        updateMenuItems(menu); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onOptionsItemSelected(MenuItem item) { //NOSONAR
        return this.onMenuItemClick(item); //NOSONAR
    } // NOSONAR

    private void updateMenuItems() { //NOSONAR
        if (displayedInTabs) { //NOSONAR
            getActivity().invalidateOptionsMenu(); //NOSONAR
        } else { //NOSONAR
            updateMenuItems(toolbar.getMenu()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void updateMenuItems(Menu menu) { //NOSONAR

        switch (settingsManager.getFolderBrowserFilesSortOrder()) { //NOSONAR
            case SortManager.SortFiles.DEFAULT: //NOSONAR
                menu.findItem(R.id.sort_files_default).setChecked(true); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.FILE_NAME: //NOSONAR
                menu.findItem(R.id.sort_files_filename).setChecked(true); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.SIZE: //NOSONAR
                menu.findItem(R.id.sort_files_size).setChecked(true); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.ARTIST_NAME: //NOSONAR
                menu.findItem(R.id.sort_files_artist_name).setChecked(true); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.ALBUM_NAME: //NOSONAR
                menu.findItem(R.id.sort_files_album_name).setChecked(true); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.TRACK_NAME: //NOSONAR
                menu.findItem(R.id.sort_files_track_name).setChecked(true); //NOSONAR
                break; //NOSONAR
        } // NOSONAR

        switch (settingsManager.getFolderBrowserFoldersSortOrder()) { //NOSONAR
            case SortManager.SortFolders.DEFAULT: //NOSONAR
                menu.findItem(R.id.sort_folder_default).setChecked(true); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFolders.COUNT: //NOSONAR
                menu.findItem(R.id.sort_folder_count).setChecked(true); //NOSONAR
                break; //NOSONAR
        } // NOSONAR

        menu.findItem(R.id.folder_home_dir).setIcon(fileBrowser.getHomeDirIcon()); //NOSONAR
        menu.findItem(R.id.folder_home_dir).setTitle(fileBrowser.getHomeDirTitle()); //NOSONAR
        menu.findItem(R.id.show_filenames).setChecked(settingsManager.getFolderBrowserShowFileNames()); //NOSONAR
        menu.findItem(R.id.files_ascending).setChecked(settingsManager.getFolderBrowserFilesAscending()); //NOSONAR
        menu.findItem(R.id.folders_ascending).setChecked(settingsManager.getFolderBrowserFoldersAscending()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBreadcrumbItemClick(BreadcrumbItem item) { //NOSONAR
        changeDir(new File(item.getItemPath())); //NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    public void changeDir(File newDir) { //NOSONAR
        disposables.add(Single.zip( //NOSONAR
                whitelistRepository.getWhitelistItems(songsRepository).first(Collections.emptyList()), //NOSONAR
                blacklistRepository.getBlacklistItems(songsRepository).first(Collections.emptyList()), //NOSONAR
                Single.fromCallable(() -> { //NOSONAR
                    final String path = FileHelper.getPath(newDir); //NOSONAR
                    if (TextUtils.isEmpty(path)) { //NOSONAR
                        return new ArrayList<>(); //NOSONAR
                    } // NOSONAR
                    currentDir = path; //NOSONAR
                    return fileBrowser.loadDir(new File(path)); //NOSONAR
                }), // NOSONAR
                (Function3<List<InclExclItem>, List<InclExclItem>, List<BaseFileObject>, List<ViewModel>>) (whitelist, blacklist, baseFileObjects) -> { //NOSONAR
                    List<ViewModel> items = Stream.of(baseFileObjects) //NOSONAR
                            .map(baseFileObject -> { //NOSONAR

                                // Look for an existing FolderView wrapping the BaseFileObject, we'll reuse it if it exists. // NOSONAR
                                FolderView folderView = (FolderView) Stream.of(adapter.items) //NOSONAR
                                        .filter(viewModel -> viewModel instanceof FolderView && (((FolderView) viewModel).baseFileObject.equals(baseFileObject))) //NOSONAR
                                        .findFirst() //NOSONAR
                                        .orElse(null); //NOSONAR

                                if (folderView == null) { //NOSONAR
                                    folderView = new FolderView(baseFileObject, whitelistRepository, blacklistRepository, settingsManager, //NOSONAR
                                            Stream.of(whitelist).anyMatch(inclExclItem -> inclExclItem.path.equals(baseFileObject.path)), //NOSONAR
                                            Stream.of(blacklist).anyMatch(inclExclItem -> inclExclItem.path.equals(baseFileObject.path))); //NOSONAR
                                    folderView.setShowWhitelist(isShowingWhitelist); //NOSONAR
                                    folderView.setShowBlacklist(isShowingBlacklist); //NOSONAR
                                    folderView.setClickListener(FolderFragment.this); //NOSONAR
                                } // NOSONAR

                                return folderView; //NOSONAR
                            }) // NOSONAR
                            .collect(Collectors.toList()); //NOSONAR

                    if (showBreadcrumbsInList && breadcrumbsView != null) { //NOSONAR
                        breadcrumbsView.setBreadcrumbsPath(currentDir); //NOSONAR
                        breadcrumbsView.setListener(FolderFragment.this); //NOSONAR
                        items.add(0, breadcrumbsView); //NOSONAR
                    } // NOSONAR
                    return items; //NOSONAR
                }).subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        adaptableItems -> { //NOSONAR
                            if (adapter != null) { //NOSONAR
                                analyticsManager.dropBreadcrumb(TAG, "setItems()"); //NOSONAR
                                setItemsDisposable = adapter.setItems(adaptableItems); //NOSONAR
                            } // NOSONAR
                            if (breadcrumb != null) { //NOSONAR
                                breadcrumb.changeBreadcrumbPath(currentDir); //NOSONAR
                            } // NOSONAR
                            if (adapter != null) { //NOSONAR
                                changeBreadcrumbPath(); //NOSONAR
                            } // NOSONAR
                            updateMenuItems(); //NOSONAR
                        }, // NOSONAR
                        error -> LogUtils.logException(TAG, "Error changing dir", error)) //NOSONAR
        ); // NOSONAR
    } // NOSONAR

    public void reload() { //NOSONAR
        if (currentDir != null) { //NOSONAR
            changeDir(new File(currentDir)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean consumeBackPress() { //NOSONAR
        if (getUserVisibleHint()) { //NOSONAR
            final File currDir = fileBrowser.getCurrentDir(); //NOSONAR
            final File homeDir = fileBrowser.getHomeDir(); //NOSONAR
            if (currDir != null && homeDir != null && currDir.compareTo(homeDir) != 0) { //NOSONAR
                changeDir(currDir.getParentFile()); //NOSONAR
                return true; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    @Override //NOSONAR
    public void onFileObjectClick(int position, FolderView folderView) { //NOSONAR
        if (contextualToolbarHelper != null && !contextualToolbarHelper.handleClick(folderView, folderView.baseFileObject)) { //NOSONAR
            if (folderView.baseFileObject.fileType == FileType.FILE) { //NOSONAR
                FileHelper.getSongList(songsRepository, new File(folderView.baseFileObject.path), false, true) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                                songs -> { //NOSONAR
                                    int index = -1; //NOSONAR
                                    for (int i = 0, songsSize = songs.size(); i < songsSize; i++) { //NOSONAR
                                        Song song = songs.get(i); //NOSONAR
                                        if (song.path.contains(folderView.baseFileObject.path)) { //NOSONAR
                                            index = i; //NOSONAR
                                            break; //NOSONAR
                                        } // NOSONAR
                                    } // NOSONAR
                                    mediaManager.playAll(songs, index, true, () -> { //NOSONAR
                                        if (isAdded() && getContext() != null) { //NOSONAR
                                            // To do later: Show playback failed toast // NOSONAR
                                        } // NOSONAR
                                        return Unit.INSTANCE; //NOSONAR
                                    }); // NOSONAR
                                }, // NOSONAR
                                error -> LogUtils.logException(TAG, "Error playing all", error)); //NOSONAR
            } else { //NOSONAR
                changeDir(new File(folderView.baseFileObject.path)); //NOSONAR
            } // NOSONAR
        } else if (folderView.baseFileObject.fileType != FileType.FILE) { //NOSONAR
            changeDir(new File(folderView.baseFileObject.path)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onFileObjectOverflowClick(View v, FolderView folderView) { //NOSONAR
        PopupMenu menu = new PopupMenu(getActivity(), v); //NOSONAR
        FolderMenuUtils.INSTANCE.setupFolderMenu(menu, folderView.baseFileObject, playlistMenuHelper); //NOSONAR
        menu.setOnMenuItemClickListener(FolderMenuUtils.INSTANCE.getFolderMenuClickListener(this, mediaManager, songsRepository, folderView, playlistManager, callbacks)); //NOSONAR
        menu.show(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onFileObjectCheckboxClick(CheckBox checkBox, FolderView folderView) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public void changeBreadcrumbPath() { //NOSONAR
        if (breadcrumbsView != null) { //NOSONAR
            breadcrumbsView.setBreadcrumbsPath(currentDir); //NOSONAR
            adapter.notifyItemChanged(adapter.items.indexOf(breadcrumbsView), 0); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setUserVisibleHint(boolean isVisibleToUser) { //NOSONAR
        super.setUserVisibleHint(isVisibleToUser); //NOSONAR
        if (isVisibleToUser) { //NOSONAR
            setupContextualToolbar(); //NOSONAR
        } else { //NOSONAR
            if (contextualToolbarHelper != null) { //NOSONAR
                contextualToolbarHelper.finish(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void setupContextualToolbar() { //NOSONAR
        if (contextualToolbar != null) { //NOSONAR

            contextualToolbar.getMenu().clear(); //NOSONAR
            contextualToolbar.inflateMenu(R.menu.context_menu_folders); //NOSONAR

            contextualToolbarHelper = new ContextualToolbarHelper<>(getContext(), contextualToolbar, new ContextualToolbarHelper.Callback() { //NOSONAR
                @Override //NOSONAR
                public void notifyItemChanged(SelectableViewModel viewModel) { //NOSONAR
                    int index = adapter.items.indexOf(viewModel); //NOSONAR
                    if (index >= 0) { //NOSONAR
                        adapter.notifyItemChanged(index, 0); //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                @Override //NOSONAR
                public void notifyDatasetChanged() { //NOSONAR
                    adapter.notifyItemRangeChanged(0, adapter.items.size(), 0); //NOSONAR
                } // NOSONAR
            }); // NOSONAR

            contextualToolbarHelper.setCanChangeTitle(false); //NOSONAR

            contextualToolbar.setOnMenuItemClickListener(menuItem -> { //NOSONAR
                switch (menuItem.getItemId()) { //NOSONAR
                    case R.id.done: //NOSONAR
                        contextualToolbarHelper.finish(); //NOSONAR
                        showWhitelist(false); //NOSONAR
                        showBlacklist(false); //NOSONAR
                        adapter.notifyItemRangeChanged(0, adapter.getItemCount()); //NOSONAR
                        return true; //NOSONAR
                } // NOSONAR
                return false; //NOSONAR
            }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void showWhitelist(boolean show) { //NOSONAR
        isShowingWhitelist = show; //NOSONAR
        if (isShowingWhitelist) { //NOSONAR
            isShowingBlacklist = false; //NOSONAR
        } // NOSONAR
        Stream.of(adapter.items) //NOSONAR
                .filter(viewModel -> viewModel instanceof FolderView) //NOSONAR
                .forEach(viewModel -> ((FolderView) viewModel).setShowWhitelist(show)); //NOSONAR
        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), 0); //NOSONAR
        contextualToolbar.setTitle(R.string.whitelist_title); //NOSONAR
    } // NOSONAR

    private void showBlacklist(boolean show) { //NOSONAR
        isShowingBlacklist = show; //NOSONAR
        if (isShowingBlacklist) { //NOSONAR
            isShowingWhitelist = false; //NOSONAR
        } // NOSONAR
        Stream.of(adapter.items) //NOSONAR
                .filter(viewModel -> viewModel instanceof FolderView) //NOSONAR
                .forEach(viewModel -> ((FolderView) viewModel).setShowBlacklist(show)); //NOSONAR
        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), 0); //NOSONAR
        contextualToolbar.setTitle(R.string.blacklist_title); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onMenuItemClick(MenuItem menuItem) { //NOSONAR
        switch (menuItem.getItemId()) { //NOSONAR
            case R.id.folder_home_dir: //NOSONAR
                if (fileBrowser.atHomeDirectory()) { //NOSONAR
                    fileBrowser.clearHomeDir(); //NOSONAR
                    updateMenuItems(); //NOSONAR
                } else if (fileBrowser.hasHomeDir()) { //NOSONAR
                    changeDir(fileBrowser.getHomeDir()); //NOSONAR
                } else { //NOSONAR
                    fileBrowser.setHomeDir(); //NOSONAR
                    updateMenuItems(); //NOSONAR
                } // NOSONAR
                return true; //NOSONAR
            case R.id.sort_files_default: //NOSONAR
                settingsManager.setFolderBrowserFilesSortOrder(SortManager.SortFiles.DEFAULT); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_files_filename: //NOSONAR
                settingsManager.setFolderBrowserFilesSortOrder(SortManager.SortFiles.FILE_NAME); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_files_size: //NOSONAR
                settingsManager.setFolderBrowserFilesSortOrder(SortManager.SortFiles.SIZE); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_files_artist_name: //NOSONAR
                settingsManager.setFolderBrowserFilesSortOrder(SortManager.SortFiles.ARTIST_NAME); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_files_album_name: //NOSONAR
                settingsManager.setFolderBrowserFilesSortOrder(SortManager.SortFiles.ALBUM_NAME); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_files_track_name: //NOSONAR
                settingsManager.setFolderBrowserFilesSortOrder(SortManager.SortFiles.TRACK_NAME); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.files_ascending: //NOSONAR
                settingsManager.setFolderBrowserFilesAscending(!menuItem.isChecked()); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_folder_count: //NOSONAR
                settingsManager.setFolderBrowserFoldersSortOrder(SortManager.SortFolders.COUNT); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.sort_folder_default: //NOSONAR
                settingsManager.setFolderBrowserFoldersSortOrder(SortManager.SortFolders.DEFAULT); //NOSONAR
                reload(); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
            case R.id.folders_ascending: //NOSONAR
                settingsManager.setFolderBrowserFoldersAscending(!menuItem.isChecked()); //NOSONAR
                reload(); //NOSONAR
                getActivity().invalidateOptionsMenu(); //NOSONAR
                return true; //NOSONAR
            case R.id.whitelist: //NOSONAR
                contextualToolbarHelper.start(); //NOSONAR
                showWhitelist(true); //NOSONAR
                return true; //NOSONAR
            case R.id.blacklist: //NOSONAR
                contextualToolbarHelper.start(); //NOSONAR
                showBlacklist(true); //NOSONAR
                return true; //NOSONAR
            case R.id.show_filenames: //NOSONAR
                settingsManager.setFolderBrowserShowFileNames(!menuItem.isChecked()); //NOSONAR
                adapter.notifyItemRangeChanged(0, adapter.getItemCount(), 0); //NOSONAR
                updateMenuItems(); //NOSONAR
                return true; //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    FolderMenuUtils.Callbacks callbacks = new FolderMenuUtils.Callbacks() { //NOSONAR

        @Override //NOSONAR
        public void onSongsAddedToQueue(int numSongs) { //NOSONAR
            Toast.makeText(getContext(), getContext().getResources().getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onPlaybackFailed() { //NOSONAR
            // To do later: Improve error message // NOSONAR
            Toast.makeText(getContext(), R.string.emptyplaylist, Toast.LENGTH_SHORT).show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showToast(String message) { //NOSONAR
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showToast(int messageResId) { //NOSONAR
            Toast.makeText(getContext(), messageResId, Toast.LENGTH_LONG).show(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void shareSong(Song song) { //NOSONAR
            SongExtKt.share(song, getContext()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void setRingtone(Song song) { //NOSONAR
            if (RingtoneManager.Companion.requiresDialog(getContext())) { //NOSONAR
                RingtoneManager.Companion.getDialog(getContext()).show(); //NOSONAR
            } else { //NOSONAR
                ringtoneManager.setRingtone(song, () -> { //NOSONAR
                    Toast.makeText(getContext(), R.string.ringtone_set_new, Toast.LENGTH_SHORT).show(); //NOSONAR
                    return Unit.INSTANCE; //NOSONAR
                }); // NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showSongInfo(Song song) { //NOSONAR
            SongInfoDialog.Companion.newInstance(song).show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onPlaylistItemsInserted() { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showTagEditor(Song song) { //NOSONAR
            TaggerDialog.newInstance(song).show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onFileNameChanged(FolderView folderView) { //NOSONAR
            IntStream.range(0, adapter.getItemCount()) //NOSONAR
                    .filter(i -> adapter.items.get(i) == folderView) //NOSONAR
                    .findFirst() //NOSONAR
                    .ifPresent(i -> adapter.notifyItemChanged(i)); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void onFileDeleted(FolderView folderView) { //NOSONAR
            IntStream.range(0, adapter.getItemCount()) //NOSONAR
                    .filter(i -> adapter.items.get(i) == folderView) //NOSONAR
                    .findFirst() //NOSONAR
                    .ifPresent(i -> adapter.notifyItemRemoved(i)); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void playNext(Single<List<Song>> songsSingle) { //NOSONAR
            mediaManager.playNext(songsSingle, message -> { //NOSONAR
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show(); //NOSONAR
                return Unit.INSTANCE; //NOSONAR
            }); // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void blacklist(@NotNull Song song) { //NOSONAR
            MenuUtils.blacklist(blacklistRepository, song); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void blacklist(@NotNull Single<List<Song>> songsSingle) { //NOSONAR
            MenuUtils.blacklist(blacklistRepository, songsSingle); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void whitelist(@NotNull Song song) { //NOSONAR
            MenuUtils.whitelist(whitelistRepository, song); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void whitelist(@NotNull Single<List<Song>> songsSingle) { //NOSONAR
            MenuUtils.whitelist(whitelistRepository, songsSingle); //NOSONAR
        } // NOSONAR
    }; // NOSONAR
} // NOSONAR
