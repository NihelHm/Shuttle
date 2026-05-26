package com.simplecity.amp_library.ui.screens.main; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.ContentUris; // NOSONAR
import android.content.Intent; // NOSONAR
import android.net.Uri; // NOSONAR
import android.os.Build; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.IBinder; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.view.GravityCompat; // NOSONAR
import android.support.v4.widget.DrawerLayout; // NOSONAR
import android.support.v7.app.ActionBarDrawerToggle; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.util.Log; // NOSONAR
import android.view.View; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.greysonparrelli.permiso.Permiso; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR
import com.simplecity.amp_library.model.Query; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.playback.constants.ShortcutCommands; // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseActivity; // NOSONAR
import com.simplecity.amp_library.ui.common.ToolbarListener; // NOSONAR
import com.simplecity.amp_library.ui.dialog.ChangelogDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerProvider; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.MusicServiceConnectionUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ThemeUtils; // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager; // NOSONAR
import dagger.android.AndroidInjection; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR
import java.util.concurrent.TimeUnit; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR
import test.com.androidnavigation.fragment.BackPressHandler; // NOSONAR
import test.com.androidnavigation.fragment.BackPressListener; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MainActivity extends BaseActivity implements //NOSONAR
        ToolbarListener, //NOSONAR
        BackPressHandler, //NOSONAR
        DrawerProvider { //NOSONAR

    private static final String TAG = "MainActivity"; //NOSONAR

    private List<BackPressListener> backPressListeners = new ArrayList<>(); //NOSONAR

    private DrawerLayout drawerLayout; //NOSONAR

    private View navigationView; //NOSONAR

    private boolean hasPendingPlaybackRequest; //NOSONAR

    @Inject //NOSONAR
    NavigationEventRelay navigationEventRelay; //NOSONAR

    @Inject //NOSONAR
    MediaManager mediaManager; //NOSONAR

    @Inject //NOSONAR
    Repository.SongsRepository songsRepository; //NOSONAR

    @Inject //NOSONAR
    AnalyticsManager analyticsManager; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "onCreate()"); //NOSONAR

        // If we haven't set any defaults, do that now // NOSONAR
        if (Aesthetic.isFirstTime(this)) { //NOSONAR

            ThemeUtils.Theme theme = ThemeUtils.getRandom(); //NOSONAR

            Aesthetic.get(this) //NOSONAR
                    .activityTheme(theme.isDark ? R.style.AppTheme : R.style.AppTheme_Light) //NOSONAR
                    .isDark(theme.isDark) //NOSONAR
                    .colorPrimaryRes(theme.primaryColor) //NOSONAR
                    .colorAccentRes(theme.accentColor) //NOSONAR
                    .colorStatusBarAuto() //NOSONAR
                    .apply(); //NOSONAR

            analyticsManager.logInitialTheme(theme); //NOSONAR
        } // NOSONAR

        setContentView(R.layout.activity_main); //NOSONAR

        Permiso.getInstance().setActivity(this); //NOSONAR

        navigationView = findViewById(R.id.navView); //NOSONAR

        //Ensure the drawer draws a content scrim over the status bar. // NOSONAR
        drawerLayout = findViewById(R.id.drawer_layout); //NOSONAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) { //NOSONAR
            drawerLayout.setOnApplyWindowInsetsListener((view, windowInsets) -> { //NOSONAR
                navigationView.dispatchApplyWindowInsets(windowInsets); //NOSONAR
                return windowInsets.replaceSystemWindowInsets(0, 0, 0, 0); //NOSONAR
            }); // NOSONAR
        } // NOSONAR

        if (savedInstanceState == null) { //NOSONAR
            getSupportFragmentManager() //NOSONAR
                    .beginTransaction() //NOSONAR
                    .add(R.id.mainContainer, MainController.newInstance()) //NOSONAR
                    .commit(); //NOSONAR
        } // NOSONAR

        handleIntent(getIntent()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onCreate()"); //NOSONAR

        showChangelogDialog(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onServiceConnected(ComponentName name, IBinder service) { //NOSONAR
        super.onServiceConnected(name, service); //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onServiceConnected()"); //NOSONAR

        handlePendingPlaybackRequest(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onNewIntent(Intent intent) { //NOSONAR
        super.onNewIntent(intent); //NOSONAR

        handleIntent(intent); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onPause() { //NOSONAR
        super.onPause(); //NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "onPause()"); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDestroy() { //NOSONAR
        super.onDestroy(); //NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "onDestroy()"); //NOSONAR
    } // NOSONAR

    private void handleIntent(Intent intent) { //NOSONAR
        Single.fromCallable(() -> { //NOSONAR
            boolean handled = false; //NOSONAR
            if (ShortcutCommands.PLAYLIST.equals(intent.getAction())) { //NOSONAR
                Playlist playlist = (Playlist) intent.getExtras().getSerializable(PlaylistManager.ARG_PLAYLIST); //NOSONAR
                NavigationEventRelay.NavigationEvent navigationEvent = new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.PLAYLIST_SELECTED, playlist, true); //NOSONAR
                navigationEventRelay.sendEvent(navigationEvent); //NOSONAR
                handled = true; //NOSONAR
            } else if (ShortcutCommands.FOLDERS.equals(intent.getAction())) { //NOSONAR
                NavigationEventRelay.NavigationEvent foldersSelectedEvent = new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.FOLDERS_SELECTED, null, true); //NOSONAR
                navigationEventRelay.sendEvent(foldersSelectedEvent); //NOSONAR
                handled = true; //NOSONAR
            } // NOSONAR

            if (!handled) { //NOSONAR
                handlePlaybackRequest(intent); //NOSONAR
            } else { //NOSONAR
                setIntent(new Intent()); //NOSONAR
            } // NOSONAR

            return true; //NOSONAR
        }) // NOSONAR
                .delaySubscription(350, TimeUnit.MILLISECONDS) //NOSONAR
                .subscribe( //NOSONAR
                        aBoolean -> { //NOSONAR
                            // Intentionally left empty. // NOSONAR
                        }, // NOSONAR
                        throwable -> LogUtils.logException(TAG, "handleIntent error", throwable) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    private void handlePendingPlaybackRequest() { //NOSONAR
        if (hasPendingPlaybackRequest) { //NOSONAR
            handlePlaybackRequest(getIntent()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    private void handlePlaybackRequest(Intent intent) { //NOSONAR
        if (intent == null) { //NOSONAR
            return; //NOSONAR
        } else if (MusicServiceConnectionUtils.serviceBinder == null) { //NOSONAR
            hasPendingPlaybackRequest = true; //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        final Uri uri = intent.getData(); //NOSONAR
        final String mimeType = intent.getType(); //NOSONAR

        if (uri != null && uri.toString().length() > 0) { //NOSONAR
            mediaManager.playFile(uri); //NOSONAR
            // Make sure to process intent only once // NOSONAR
            setIntent(new Intent()); //NOSONAR
        } else if (MediaStore.Audio.Playlists.CONTENT_TYPE.equals(mimeType)) { //NOSONAR
            long id = parseIdFromIntent(intent, "playlistId", "playlist"); //NOSONAR
            if (id >= 0) { //NOSONAR
                Query query = Playlist.getQuery(); //NOSONAR
                query.uri = ContentUris.withAppendedId(query.uri, id); //NOSONAR
                SqlBriteUtils.createSingle(this, (cursor) -> new Playlist(this, cursor), query, null) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                        .subscribe( //NOSONAR
                                playlist -> { //NOSONAR
                                    mediaManager.playAll(songsRepository.getSongs(playlist).first(new ArrayList<>()), //NOSONAR
                                            () -> { // NOSONAR
                                                // To do later: Show playback failure toast // NOSONAR
                                                return Unit.INSTANCE; //NOSONAR
                                            }); // NOSONAR
                                    // Make sure to process intent only once // NOSONAR
                                    setIntent(new Intent()); //NOSONAR
                                }, // NOSONAR
                                error -> LogUtils.logException(TAG, "Error handling playback request", error) //NOSONAR
                        ); // NOSONAR
            } // NOSONAR
        } // NOSONAR

        hasPendingPlaybackRequest = false; //NOSONAR
    } // NOSONAR

    private long parseIdFromIntent(Intent intent, String longKey, String stringKey) { //NOSONAR
        long id = intent.getLongExtra(longKey, -1); //NOSONAR
        if (id < 0) { //NOSONAR
            String idString = intent.getStringExtra(stringKey); //NOSONAR
            if (idString != null) { //NOSONAR
                try { //NOSONAR
                    id = Long.parseLong(idString); //NOSONAR
                } catch (NumberFormatException e) { //NOSONAR
                    Log.e(TAG, e.getMessage()); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        return id; //NOSONAR
    } // NOSONAR

    private void showChangelogDialog() { //NOSONAR
        int storedVersionCode = settingsManager.getStoredVersionCode(); //NOSONAR

        // If we've stored a version code in the past, and it's lower than the current version code, // NOSONAR
        // we can show the changelog. // NOSONAR
        // Don't show the changelog for first time users. // NOSONAR
        if (storedVersionCode != -1 && storedVersionCode < BuildConfig.VERSION_CODE) { //NOSONAR
            if (settingsManager.getShowChangelogOnLaunch()) { //NOSONAR
                ChangelogDialog.Companion.newInstance().show(getSupportFragmentManager()); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        settingsManager.setVersionCode(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBackPressed() { //NOSONAR
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) { //NOSONAR
            drawerLayout.closeDrawer(GravityCompat.START); //NOSONAR
        } else { //NOSONAR
            if (!backPressListeners.isEmpty()) { //NOSONAR
                for (int i = backPressListeners.size() - 1; i >= 0; i--) { //NOSONAR
                    BackPressListener backPressListener = backPressListeners.get(i); //NOSONAR
                    if (backPressListener.consumeBackPress()) { //NOSONAR
                        return; //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
            super.onBackPressed(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void toolbarAttached(Toolbar toolbar) { //NOSONAR
        DrawerLayout drawer = findViewById(R.id.drawer_layout); //NOSONAR
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) { //NOSONAR
            @Override //NOSONAR
            public void onDrawerSlide(View drawerView, float slideOffset) { //NOSONAR
                super.onDrawerSlide(drawerView, 0); //NOSONAR
            } // NOSONAR
        }; // NOSONAR
        drawer.addDrawerListener(toggle); //NOSONAR
        toggle.syncState(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void addBackPressListener(@NonNull BackPressListener listener) { //NOSONAR
        if (!backPressListeners.contains(listener)) { //NOSONAR
            backPressListeners.add(listener); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void removeBackPressListener(@NonNull BackPressListener listener) { //NOSONAR
        if (backPressListeners.contains(listener)) { //NOSONAR
            backPressListeners.remove(listener); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return "MainActivity"; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public DrawerLayout getDrawerLayout() { //NOSONAR
        return drawerLayout; //NOSONAR
    } // NOSONAR
} // NOSONAR
