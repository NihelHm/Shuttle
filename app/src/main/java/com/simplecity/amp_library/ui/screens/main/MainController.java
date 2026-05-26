package com.simplecity.amp_library.ui.screens.main; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.IntentFilter; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.Handler; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.support.v4.app.FragmentTransaction; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.support.v4.widget.DrawerLayout; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.Toast; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.cantrowitz.rxbroadcast.RxBroadcast; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist; // NOSONAR
import com.simplecity.amp_library.model.Genre; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockController; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerProvider; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.MiniPlayerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay; // NOSONAR
import com.simplecity.amp_library.ui.screens.equalizer.EqualizerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.folders.FolderFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.detail.GenreDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.miniplayer.MiniPlayerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerPresenter; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.upnext.UpNextView; // NOSONAR
import com.simplecity.amp_library.ui.settings.SettingsParentFragment; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.CustomMultiSheetView; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetEventRelay; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetSlideEventRelay; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.SleepTimer; // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR
import test.com.androidnavigation.fragment.BackPressHandler; // NOSONAR
import test.com.androidnavigation.fragment.BaseNavigationController; // NOSONAR
import test.com.androidnavigation.fragment.FragmentInfo; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MainController extends BaseNavigationController implements BackPressHandler, DrawerLockController { //NOSONAR

    private static final String TAG = "MainController"; //NOSONAR

    public static final String STATE_CURRENT_SHEET = "current_sheet"; //NOSONAR

    @Inject //NOSONAR
    NavigationEventRelay navigationEventRelay; //NOSONAR

    @Inject //NOSONAR
    MultiSheetEventRelay multiSheetEventRelay; //NOSONAR

    @Inject //NOSONAR
    MultiSheetSlideEventRelay multiSheetSlideEventRelay; //NOSONAR

    @Inject //NOSONAR
    MediaManager mediaManager; //NOSONAR

    @Inject //NOSONAR
    PlayerPresenter playerPresenter; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    private Handler delayHandler; //NOSONAR

    @BindView(R.id.multiSheetView) //NOSONAR
    CustomMultiSheetView multiSheetView; //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    public static MainController newInstance() { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        MainController fragment = new MainController(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    } // NOSONAR

    public MainController() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAttach(Context context) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onAttach(context); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //NOSONAR

        View rootView = inflater.inflate(R.layout.fragment_main, container, false); //NOSONAR

        ButterKnife.bind(this, rootView); //NOSONAR


        multiSheetView.setMultiSheetEventRelay(multiSheetEventRelay); //NOSONAR
        multiSheetView.setMultiSheetSlideEventRelay(multiSheetSlideEventRelay); //NOSONAR

        if (savedInstanceState == null) { //NOSONAR
            getChildFragmentManager() //NOSONAR
                    .beginTransaction() //NOSONAR
                    .add(multiSheetView.getSheetContainerViewResId(MultiSheetView.Sheet.FIRST), PlayerFragment.newInstance()) //NOSONAR
                    .add(multiSheetView.getSheetPeekViewResId(MultiSheetView.Sheet.FIRST), MiniPlayerFragment.newInstance()) //NOSONAR
                    .add(multiSheetView.getSheetContainerViewResId(MultiSheetView.Sheet.SECOND), QueueFragment.Companion.newInstance()) //NOSONAR
                    .commit(); //NOSONAR
        } else { //NOSONAR
            multiSheetView.restoreSheet(savedInstanceState.getInt(STATE_CURRENT_SHEET)); //NOSONAR
        } // NOSONAR

        ((ViewGroup) multiSheetView.findViewById(multiSheetView.getSheetPeekViewResId(MultiSheetView.Sheet.SECOND))).addView( //NOSONAR
                UpNextView.Companion.newInstance(getContext(), playerPresenter, settingsManager)); //NOSONAR

        toggleBottomSheetVisibility(false, false); //NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (delayHandler != null) { //NOSONAR
            delayHandler.removeCallbacksAndMessages(null); //NOSONAR
        } // NOSONAR
        delayHandler = new Handler(); //NOSONAR

        disposables.add(navigationEventRelay.getEvents() //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .filter(NavigationEventRelay.NavigationEvent::isActionable) //NOSONAR
                .subscribe(navigationEvent -> { //NOSONAR
                    switch (navigationEvent.type) { //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.LIBRARY_SELECTED: //NOSONAR
                            popToRootViewController(); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.FOLDERS_SELECTED: //NOSONAR
                            delayHandler.postDelayed(() -> pushViewController(FolderFragment.newInstance(getString(R.string.folders_title), false), "FolderFragment"), 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.SLEEP_TIMER_SELECTED: //NOSONAR
                            UnsafeAction showToast = () -> Toast.makeText(getContext(), R.string.sleep_timer_started, Toast.LENGTH_SHORT).show(); //NOSONAR
                            SleepTimer.getInstance().getDialog( //NOSONAR
                                    getContext(), //NOSONAR
                                    () -> SleepTimer.getInstance().showMinutesDialog(getContext(), showToast), //NOSONAR
                                    showToast //NOSONAR
                            ).show(); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.EQUALIZER_SELECTED: //NOSONAR
                            delayHandler.postDelayed( //NOSONAR
                                    () -> multiSheetEventRelay.sendEvent(new MultiSheetEventRelay.MultiSheetEvent(MultiSheetEventRelay.MultiSheetEvent.Action.HIDE, MultiSheetView.Sheet.FIRST)), 100); //NOSONAR
                            delayHandler.postDelayed(() -> pushViewController(EqualizerFragment.newInstance(), "EqualizerFragment"), 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.SETTINGS_SELECTED: //NOSONAR
                            delayHandler.postDelayed( //NOSONAR
                                    () -> multiSheetEventRelay.sendEvent(new MultiSheetEventRelay.MultiSheetEvent(MultiSheetEventRelay.MultiSheetEvent.Action.HIDE, MultiSheetView.Sheet.FIRST)), 100); //NOSONAR
                            delayHandler.postDelayed(() -> pushViewController(SettingsParentFragment.newInstance(R.xml.settings_headers, R.string.settings), "Settings Fragment"), 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.SUPPORT_SELECTED: //NOSONAR
                            delayHandler.postDelayed( //NOSONAR
                                    () -> multiSheetEventRelay.sendEvent(new MultiSheetEventRelay.MultiSheetEvent(MultiSheetEventRelay.MultiSheetEvent.Action.HIDE, MultiSheetView.Sheet.FIRST)), 100); //NOSONAR
                            delayHandler.postDelayed(() -> pushViewController(SettingsParentFragment.newInstance(R.xml.settings_support, R.string.pref_title_support), "Support Fragment"), 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.PLAYLIST_SELECTED: //NOSONAR
                            delayHandler.postDelayed(() -> pushViewController(PlaylistDetailFragment.Companion.newInstance((Playlist) navigationEvent.data), "PlaylistDetailFragment"), 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.GO_TO_ARTIST: //NOSONAR
                            multiSheetView.goToSheet(MultiSheetView.Sheet.NONE); //NOSONAR
                            AlbumArtist albumArtist = (AlbumArtist) navigationEvent.data; //NOSONAR
                            delayHandler.postDelayed(() -> { //NOSONAR
                                popToRootViewController(); //NOSONAR
                                pushViewController(ArtistDetailFragment.Companion.newInstance(albumArtist, null), "ArtistDetailFragment"); //NOSONAR
                            }, 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.GO_TO_ALBUM: //NOSONAR
                            multiSheetView.goToSheet(MultiSheetView.Sheet.NONE); //NOSONAR
                            Album album = (Album) navigationEvent.data; //NOSONAR
                            delayHandler.postDelayed(() -> { //NOSONAR
                                popToRootViewController(); //NOSONAR
                                pushViewController(AlbumDetailFragment.Companion.newInstance(album, null), "AlbumDetailFragment"); //NOSONAR
                            }, 250); //NOSONAR
                            break; //NOSONAR
                        case NavigationEventRelay.NavigationEvent.Type.GO_TO_GENRE: //NOSONAR
                            multiSheetView.goToSheet(MultiSheetView.Sheet.NONE); //NOSONAR
                            Genre genre = (Genre) navigationEvent.data; //NOSONAR
                            delayHandler.postDelayed(() -> { //NOSONAR
                                popToRootViewController(); //NOSONAR
                                pushViewController(GenreDetailFragment.Companion.newInstance(genre), "GenreDetailFragment"); //NOSONAR
                            }, 250); //NOSONAR
                            break; //NOSONAR
                    } // NOSONAR
                })); // NOSONAR

        IntentFilter intentFilter = new IntentFilter(); //NOSONAR
        intentFilter.addAction(InternalIntents.SERVICE_CONNECTED); //NOSONAR
        intentFilter.addAction(InternalIntents.QUEUE_CHANGED); //NOSONAR
        disposables.add( //NOSONAR
                RxBroadcast.fromBroadcast(getContext(), intentFilter) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe(intent -> { //NOSONAR
                            toggleBottomSheetVisibility(true, true); //NOSONAR
                        }) // NOSONAR
        ); // NOSONAR

        DrawerLockManager.getInstance().setDrawerLockController(this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        delayHandler.removeCallbacksAndMessages(null); //NOSONAR
        delayHandler = null; //NOSONAR

        disposables.clear(); //NOSONAR

        DrawerLockManager.getInstance().setDrawerLockController(null); //NOSONAR

        super.onPause(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Hide/show the bottom sheet, depending on whether the queue is empty. // NOSONAR
     */ // NOSONAR
    private void toggleBottomSheetVisibility(boolean collapse, boolean animate) { //NOSONAR
        if (!mediaManager.getQueueReloading() && mediaManager.getQueue().isEmpty()) { //NOSONAR
            multiSheetView.hide(collapse, false); //NOSONAR
        } else if (MiniPlayerLockManager.getInstance().canShowMiniPlayer()) { //NOSONAR
            multiSheetView.unhide(animate); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onSaveInstanceState(Bundle outState) { //NOSONAR
        outState.putInt(STATE_CURRENT_SHEET, multiSheetView.getCurrentSheet()); //NOSONAR
        super.onSaveInstanceState(outState); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public FragmentInfo getRootViewControllerInfo() { //NOSONAR
        return LibraryController.fragmentInfo(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean consumeBackPress() { //NOSONAR
        if (multiSheetView.consumeBackPress()) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        return super.consumeBackPress(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void lockDrawer() { //NOSONAR
        ((DrawerProvider) getActivity()).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void unlockDrawer() { //NOSONAR
        // Don't unlock the drawer if one of the sheets is expanded // NOSONAR
        if (multiSheetView.getCurrentSheet() == MultiSheetView.Sheet.FIRST || multiSheetView.getCurrentSheet() == MultiSheetView.Sheet.SECOND) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        ((DrawerProvider) getActivity()).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //NOSONAR
    } // NOSONAR

    // To do later:  Remove once cause of shared element crash is understood. // NOSONAR
    // This is a copy of the superclass method of the same name/signature, with some additional logging // NOSONAR
    // to help ascertain the cause of a crash. // NOSONAR
    @Override //NOSONAR
    public void pushViewController(@NonNull Fragment fragment, @Nullable String tag, @Nullable List<Pair<View, String>> sharedElements) { //NOSONAR
        FragmentTransaction fragmentTransaction = getChildFragmentManager() //NOSONAR
                .beginTransaction(); //NOSONAR

        if (sharedElements != null) { //NOSONAR
            for (Pair<View, String> pair : sharedElements) { //NOSONAR
                try { //NOSONAR
                    fragmentTransaction.addSharedElement(pair.first, pair.second); //NOSONAR
                } catch (IllegalArgumentException e) { //NOSONAR
                    LogUtils.logException(TAG, String.format("Error adding shared element transition.. key: %s, value: %s", pair.first, pair.second), e); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        fragmentTransaction.addToBackStack(null) //NOSONAR
                .replace(test.com.androidnavigation.R.id.mainContainer, fragment, tag) //NOSONAR
                .commit(); //NOSONAR
    } // NOSONAR
} // NOSONAR
