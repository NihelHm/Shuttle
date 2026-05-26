package com.simplecity.amp_library.playback; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.app.AlarmManager; // NOSONAR
import android.app.PendingIntent; // NOSONAR
import android.content.BroadcastReceiver; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.IntentFilter; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.Handler; // NOSONAR
import android.os.IBinder; // NOSONAR
import android.os.SystemClock; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.media.MediaBrowserCompat; // NOSONAR
import android.support.v4.media.MediaBrowserServiceCompat; // NOSONAR
import android.util.Log; // NOSONAR
import android.widget.Toast; // NOSONAR
import com.crashlytics.android.Crashlytics; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.androidauto.MediaIdHelper; // NOSONAR
import com.simplecity.amp_library.androidauto.PackageValidator; // NOSONAR
import com.simplecity.amp_library.cast.CastManager; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.notifications.MusicNotificationHelper; // NOSONAR
import com.simplecity.amp_library.playback.constants.ExternalIntents; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.playback.constants.MediaButtonCommand; // NOSONAR
import com.simplecity.amp_library.playback.constants.ServiceCommand; // NOSONAR
import com.simplecity.amp_library.playback.constants.ShortcutCommands; // NOSONAR
import com.simplecity.amp_library.playback.constants.WidgetManager; // NOSONAR
import com.simplecity.amp_library.services.Equalizer; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItem; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager; // NOSONAR
import dagger.android.AndroidInjection; // NOSONAR
import io.reactivex.Completable; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.ConcurrentModificationException; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR

@SuppressLint("InlinedApi") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MusicService extends MediaBrowserServiceCompat { //NOSONAR

    @interface NotifyMode { //NOSONAR
        int NONE = 0; //NOSONAR
        int FOREGROUND = 1; //NOSONAR
        int BACKGROUND = 2; //NOSONAR
    } // NOSONAR

    private static final String TAG = "MusicService"; //NOSONAR

    private MusicServiceCallbacks musicServiceCallbacks = new MusicServiceCallbacks(); //NOSONAR

    private QueueManager queueManager; //NOSONAR

    @Nullable //NOSONAR
    private CastManager castManager; //NOSONAR

    private BluetoothManager bluetoothManager; //NOSONAR

    private HeadsetManager headsetManager; //NOSONAR

    @Inject //NOSONAR
    WidgetManager widgetManager; //NOSONAR

    private ScrobbleManager scrobbleManager; //NOSONAR

    private final IBinder binder = new LocalBinder(this); //NOSONAR

    private BroadcastReceiver unmountReceiver = null; //NOSONAR

    private int serviceStartId = -1; //NOSONAR

    private boolean serviceInUse = false; //NOSONAR

    private MusicNotificationHelper notificationHelper; //NOSONAR

    private static NotificationStateHandler notificationStateHandler; //NOSONAR

    private AlarmManager alarmManager; //NOSONAR

    private PendingIntent shutdownIntent; //NOSONAR

    private boolean shutdownScheduled; //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    private PackageValidator mPackageValidator; //NOSONAR

    private PlaybackManager playbackManager; //NOSONAR

    private DummyNotificationHelper dummyNotificationHelper = new DummyNotificationHelper(); //NOSONAR

    @Inject //NOSONAR
    Repository.SongsRepository songsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumsRepository albumsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumArtistsRepository albumArtistsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.PlaylistsRepository playlistsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.GenresRepository genresRepository; //NOSONAR

    @Inject //NOSONAR
    PlaybackSettingsManager playbackSettingsManager; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    AnalyticsManager analyticsManager; //NOSONAR

    @Inject //NOSONAR
    FavoritesPlaylistManager favoritesPlaylistManager; //NOSONAR

    @SuppressLint("InlinedApi") //NOSONAR
    @Override //NOSONAR
    public void onCreate() { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(); //NOSONAR

        queueManager = new QueueManager( //NOSONAR
                musicServiceCallbacks, //NOSONAR
                songsRepository, //NOSONAR
                playbackSettingsManager, //NOSONAR
                settingsManager //NOSONAR
        ); // NOSONAR

        playbackManager = new PlaybackManager( //NOSONAR
                this, //NOSONAR
                queueManager, //NOSONAR
                playbackSettingsManager, //NOSONAR
                songsRepository, //NOSONAR
                albumsRepository, //NOSONAR
                albumArtistsRepository, //NOSONAR
                genresRepository, //NOSONAR
                playlistsRepository, //NOSONAR
                musicServiceCallbacks, //NOSONAR
                settingsManager //NOSONAR
        ); // NOSONAR

        scrobbleManager = new ScrobbleManager(playbackSettingsManager); //NOSONAR

        mPackageValidator = new PackageValidator(this); //NOSONAR

        setSessionToken(playbackManager.getMediaSessionToken()); //NOSONAR

        if (CastManager.isCastAvailable(this, settingsManager)) { //NOSONAR
            castManager = new CastManager(this, playbackManager); //NOSONAR
        } // NOSONAR

        bluetoothManager = new BluetoothManager(playbackManager, analyticsManager, musicServiceCallbacks, settingsManager); //NOSONAR

        headsetManager = new HeadsetManager(playbackManager, playbackSettingsManager); //NOSONAR

        notificationHelper = new MusicNotificationHelper(this, analyticsManager); //NOSONAR

        notificationStateHandler = new NotificationStateHandler(this); //NOSONAR

        headsetManager.registerHeadsetPlugReceiver(this); //NOSONAR
        bluetoothManager.registerBluetoothReceiver(this); //NOSONAR
        bluetoothManager.registerA2dpServiceListener(this); //NOSONAR

        registerExternalStorageListener(); //NOSONAR

        IntentFilter intentFilter = new IntentFilter(); //NOSONAR
        intentFilter.addAction(ServiceCommand.COMMAND); //NOSONAR
        intentFilter.addAction(ServiceCommand.TOGGLE_PLAYBACK); //NOSONAR
        intentFilter.addAction(ServiceCommand.PAUSE); //NOSONAR
        intentFilter.addAction(ServiceCommand.NEXT); //NOSONAR
        intentFilter.addAction(ServiceCommand.PREV); //NOSONAR
        intentFilter.addAction(ServiceCommand.STOP); //NOSONAR
        intentFilter.addAction(ServiceCommand.SHUFFLE); //NOSONAR
        intentFilter.addAction(ServiceCommand.REPEAT); //NOSONAR
        intentFilter.addAction(ExternalIntents.PLAY_STATUS_REQUEST); //NOSONAR
        registerReceiver(intentReceiver, intentFilter); //NOSONAR

        // Initialize the delayed shutdown intent // NOSONAR
        Intent shutdownIntent = new Intent(this, MusicService.class); //NOSONAR
        shutdownIntent.setAction(ServiceCommand.SHUTDOWN); //NOSONAR

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); //NOSONAR
        this.shutdownIntent = PendingIntent.getService(this, 0, shutdownIntent, 0); //NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "onCreate(), scheduling delayed shutdown"); //NOSONAR
        scheduleDelayedShutdown(); //NOSONAR

        playbackManager.reloadQueue(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public IBinder onBind(final Intent intent) { //NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "onBind().. cancelShutdown()"); //NOSONAR
        cancelShutdown(); //NOSONAR
        serviceInUse = true; //NOSONAR

        // For Android auto, need to call super, or onGetRoot won't be called. // NOSONAR
        if (intent != null && "android.media.browse.MediaBrowserService".equals(intent.getAction())) { //NOSONAR
            return super.onBind(intent); //NOSONAR
        } // NOSONAR

        return binder; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) { //NOSONAR
        // To ensure you are not allowing any arbitrary app to browse your app's contents, you // NOSONAR
        // need to check the origin: // NOSONAR
        if (!mPackageValidator.isCallerAllowed(this, clientPackageName, clientUid)) { //NOSONAR
            // If the request comes from an untrusted package, return an empty browser root. // NOSONAR
            // If you return null, then the media browser will not be able to connect and // NOSONAR
            // no further calls will be made to other media browsing methods. // NOSONAR
            Log.i(TAG, String.format("OnGetRoot: Browsing NOT ALLOWED for unknown caller. Returning empty browser root so all apps can use MediaController.%s", clientPackageName)); //NOSONAR
            return new MediaBrowserServiceCompat.BrowserRoot("EMPTY_ROOT", null); //NOSONAR
        } // NOSONAR
        return new BrowserRoot("media:/root/", null); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onLoadChildren(@NonNull String parentMediaId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) { //NOSONAR
        if ("EMPTY_ROOT".equals(parentMediaId)) { //NOSONAR
            result.sendResult(new ArrayList<>()); //NOSONAR
        } else { //NOSONAR
            result.detach(); //NOSONAR
            // if music library is ready, return immediately // NOSONAR
            new MediaIdHelper( //NOSONAR
                    (ShuttleApplication) getApplication(), //NOSONAR
                    songsRepository, //NOSONAR
                    albumsRepository, //NOSONAR
                    albumArtistsRepository, //NOSONAR
                    genresRepository, //NOSONAR
                    playlistsRepository //NOSONAR
            ).getChildren(parentMediaId, mediaItems -> { //NOSONAR
                result.sendResult(mediaItems); //NOSONAR
                return Unit.INSTANCE; //NOSONAR
            }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onRebind(Intent intent) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onRebind().. cancelShutdown()"); //NOSONAR
        cancelShutdown(); //NOSONAR
        serviceInUse = true; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onUnbind(Intent intent) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onUnbind()"); //NOSONAR

        serviceInUse = false; //NOSONAR
        saveState(true); //NOSONAR

        if (playbackManager.isPlaying() || playbackManager.willResumePlayback()) { //NOSONAR
            // Something is currently playing, or will be playing once an in-progress action requesting audio focus ends, so don't stop the service now. // NOSONAR
            return true; //NOSONAR

            // If there is a playlist but playback is paused, then wait a while before stopping the service, so that pause/resume isn't slow. // NOSONAR
            // Also delay stopping the service if we're transitioning between tracks. // NOSONAR
        } else if (!queueManager.getCurrentPlaylist().isEmpty()) { //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, "onUnbind() scheduling delayed shutdown."); //NOSONAR
            scheduleDelayedShutdown(); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "stopSelf() called"); //NOSONAR
        stopSelf(serviceStartId); //NOSONAR

        return true; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onTaskRemoved(Intent rootIntent) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onTaskRemoved()"); //NOSONAR

        // Fixme: // NOSONAR
        //  playbackManager.willResumePlayback() returns true even after we've manually paused. // NOSONAR
        //  This means we don't call stopSelf(), which in turn causes the service to act as if it has crashed, and will recreate itself unnecessarily. // NOSONAR

        if (!isPlaying() && !playbackManager.willResumePlayback()) { //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, "stopSelf() called"); //NOSONAR
            stopSelf(); //NOSONAR
        } // NOSONAR

        super.onTaskRemoved(rootIntent); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onDestroy()"); //NOSONAR

        saveState(true); //NOSONAR

        //Shutdown the EQ // NOSONAR
        Intent shutdownEqualizer = new Intent(MusicService.this, Equalizer.class); //NOSONAR
        stopService(shutdownEqualizer); //NOSONAR

        alarmManager.cancel(shutdownIntent); //NOSONAR

        // Remove any callbacks from the handlers // NOSONAR
        notificationStateHandler.removeCallbacksAndMessages(null); //NOSONAR

        if (castManager != null) { //NOSONAR
            castManager.destroy(); //NOSONAR
        } // NOSONAR

        headsetManager.unregisterHeadsetPlugReceiver(this); //NOSONAR
        bluetoothManager.unregisterBluetoothReceiver(this); //NOSONAR
        bluetoothManager.unregisterA2dpServiceListener(this); //NOSONAR

        unregisterReceiver(intentReceiver); //NOSONAR
        if (unmountReceiver != null) { //NOSONAR
            unregisterReceiver(unmountReceiver); //NOSONAR
            unmountReceiver = null; //NOSONAR
        } // NOSONAR

        playbackManager.destroy(); //NOSONAR

        dummyNotificationHelper.teardown(this); //NOSONAR
        notificationHelper.tearDown(); //NOSONAR

        disposables.clear(); //NOSONAR

        super.onDestroy(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int onStartCommand(Intent intent, int flags, int startId) { //NOSONAR
        serviceStartId = startId; //NOSONAR

        if (intent != null) { //NOSONAR
            String action = intent.getAction(); //NOSONAR
            String command = intent.getStringExtra(MediaButtonCommand.CMD_NAME); //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, String.format("onStartCommand() Action: %s, Command: %s", action, command)); //NOSONAR
            if (command != null) { //NOSONAR
                action = commandToAction(command); //NOSONAR
            } // NOSONAR

            if (action != null) { //NOSONAR
                switch (action) { //NOSONAR
                    case ServiceCommand.NEXT: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: // NOSONAR
                        // - With queue: Force to next song, start playback, update notification, no ANR // NOSONAR
                        // - No queue: ANR // NOSONAR

                        // Possible solution: (A) Show the Shuttle notification, despite the fact that music isn't playing. Need to customise notification to allow for an empty queue (no current song) // NOSONAR
                        // We could try to generate a queue of random songs as well, but there's no guarantee the user has music on their device. // NOSONAR

                        gotoNext(true); //NOSONAR

                        break; //NOSONAR
                    case ServiceCommand.PREV: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: // NOSONAR
                        // - With queue: ANR // NOSONAR
                        // - No queue: ANR // NOSONAR

                        // Possible solution: (A) Show the Shuttle notification, despite the fact that music isn't playing. Need to customise notification to allow for an empty queue (no current song) // NOSONAR

                        previous(false); //NOSONAR

                        break; //NOSONAR
                    case ServiceCommand.PAUSE: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: // NOSONAR
                        // - With queue: ANR // NOSONAR
                        // - No queue: ANR // NOSONAR

                        // Possible solution: (A) Show the Shuttle notification, despite the fact that music isn't playing. Need to customise notification to allow for an empty queue (no current song) // NOSONAR

                        pause(true); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.PLAY: //NOSONAR
                    case ShortcutCommands.PLAY: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: // NOSONAR
                        // - No queue: ANR // NOSONAR
                        // - With queue: No ANR // NOSONAR

                        // Possible solution: (A) Show the Shuttle notification, despite the fact that music isn't playing. Need to customise notification to allow for an empty queue (no current song) // NOSONAR

                        play(); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.TOGGLE_PLAYBACK: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        if (isPlaying()) { //NOSONAR

                            // It's not possible to be playing and the service not be started. No ANR // NOSONAR

                            pause(intent.getBooleanExtra(MediaButtonCommand.FORCE_PREVIOUS, false)); //NOSONAR
                        } else { //NOSONAR

                            // Same as ServiceCommand.PLAY // NOSONAR

                            play(); //NOSONAR
                        } // NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.STOP: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is already started & we're not playing: ANR // NOSONAR
                        // If the service is not already started: ANR // NOSONAR

                        pause(false); //NOSONAR
                        releaseServiceUiAndStop(); //NOSONAR
                        notificationStateHandler.removeCallbacksAndMessages(null); //NOSONAR
                        //For some reason, the notification will only go away if this call is delayed. // NOSONAR
                        new Handler().postDelayed(() -> stopForegroundImpl(true, false), 150); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.SHUFFLE: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: ANR // NOSONAR

                        toggleShuffleMode(); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.REPEAT: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: ANR // NOSONAR

                        toggleRepeat(); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.TOGGLE_FAVORITE: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: ANR // NOSONAR

                        toggleFavorite(); //NOSONAR
                        break; //NOSONAR
                    case ExternalIntents.PLAY_STATUS_REQUEST: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: ANR // NOSONAR

                        notifyChange(ExternalIntents.PLAY_STATUS_RESPONSE); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.SHUTDOWN: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If the service is not already started: ANR // NOSONAR

                        shutdownScheduled = false; //NOSONAR
                        releaseServiceUiAndStop(); //NOSONAR
                        return START_NOT_STICKY; //NOSONAR
                    case ShortcutCommands.SHUFFLE_ALL: //NOSONAR
                        dummyNotificationHelper.showDummyNotification(this); //NOSONAR

                        // If service is not already started: ANR // NOSONAR

                        queueManager.makeShuffleList(); //NOSONAR
                        playAutoShuffleList(); //NOSONAR
                        break; //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        // Make sure the service will shut down on its own if it was just started but not bound to and nothing is playing // NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "onStartCommand() scheduling delayed shutdown"); //NOSONAR
        scheduleDelayedShutdown(); //NOSONAR

        return START_STICKY; //NOSONAR
    } // NOSONAR

    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() { //NOSONAR
        @Override //NOSONAR
        public void onReceive(final Context context, final Intent intent) { //NOSONAR

            String action = intent.getAction(); //NOSONAR

            String command = intent.getStringExtra(MediaButtonCommand.CMD_NAME); //NOSONAR
            if (command != null) { //NOSONAR
                action = commandToAction(command); //NOSONAR
                widgetManager.processCommand(MusicService.this, intent, command); //NOSONAR
            } // NOSONAR

            if (action != null) { //NOSONAR
                analyticsManager.dropBreadcrumb(TAG, String.format("onReceive() Action: %s, Command: %s", action, command)); //NOSONAR
                switch (action) { //NOSONAR
                    case ServiceCommand.NEXT: //NOSONAR
                        gotoNext(true); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.PREV: //NOSONAR
                        previous(false); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.TOGGLE_PLAYBACK: //NOSONAR
                        if (isPlaying()) { //NOSONAR
                            pause(true); //NOSONAR
                        } else { //NOSONAR
                            play(); //NOSONAR
                        } // NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.PAUSE: //NOSONAR
                        pause(true); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.PLAY: //NOSONAR
                        play(); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.STOP: //NOSONAR
                        pause(false); //NOSONAR
                        releaseServiceUiAndStop(); //NOSONAR
                        break; //NOSONAR
                    case ServiceCommand.TOGGLE_FAVORITE: //NOSONAR
                        toggleFavorite(); //NOSONAR
                        break; //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    }; // NOSONAR

    @Nullable //NOSONAR
    public String commandToAction(@NonNull String command) { //NOSONAR
        switch (command) { //NOSONAR
            case MediaButtonCommand.NEXT: //NOSONAR
                return ServiceCommand.NEXT; //NOSONAR
            case MediaButtonCommand.PREVIOUS: //NOSONAR
                return ServiceCommand.PREV; //NOSONAR
            case MediaButtonCommand.TOGGLE_PAUSE: //NOSONAR
                return ServiceCommand.TOGGLE_PLAYBACK; //NOSONAR
            case MediaButtonCommand.PAUSE: //NOSONAR
                return ServiceCommand.PAUSE; //NOSONAR
            case MediaButtonCommand.PLAY: //NOSONAR
                return ServiceCommand.PLAY; //NOSONAR
            case MediaButtonCommand.STOP: //NOSONAR
                return ServiceCommand.STOP; //NOSONAR
            case MediaButtonCommand.TOGGLE_FAVORITE: //NOSONAR
                return ServiceCommand.TOGGLE_FAVORITE; //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR


    /** // NOSONAR
     * Release resources and destroy the service. // NOSONAR
     */ // NOSONAR
    void releaseServiceUiAndStop() { //NOSONAR

        // If we're currently playing, or we're going to be playing in the near future, don't shutdown. // NOSONAR
        if (isPlaying() || playbackManager.willResumePlayback()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "releaseServiceUiAndStop()"); //NOSONAR

        playbackManager.release(); //NOSONAR

        cancelNotification(); //NOSONAR

        if (!serviceInUse) { //NOSONAR
            saveState(true); //NOSONAR

            //Shutdown the EQ // NOSONAR
            Intent shutdownEqualizer = new Intent(MusicService.this, Equalizer.class); //NOSONAR
            stopService(shutdownEqualizer); //NOSONAR

            analyticsManager.dropBreadcrumb(TAG, "stopSelf() called"); //NOSONAR
            stopSelf(serviceStartId); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Called when we receive a ACTION_MEDIA_EJECT notification. // NOSONAR
     */ // NOSONAR
    public void closeExternalStorageFiles() { //NOSONAR
        // Stop playback and clean up if the SD card is going to be unmounted. // NOSONAR
        stop(); //NOSONAR
        notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
        notifyChange(InternalIntents.META_CHANGED); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Registers an intent to listen for ACTION_MEDIA_EJECT notifications. The intent will call closeExternalStorageFiles() if the external // NOSONAR
     * media is going to be ejected, so scs can clean up any files they have open. // NOSONAR
     */ // NOSONAR
    public void registerExternalStorageListener() { //NOSONAR
        if (unmountReceiver == null) { //NOSONAR
            unmountReceiver = new BroadcastReceiver() { //NOSONAR
                @Override //NOSONAR
                public void onReceive(Context context, Intent intent) { //NOSONAR
                    final String action = intent.getAction(); //NOSONAR
                    if (Intent.ACTION_MEDIA_EJECT.equals(action)) { //NOSONAR
                        saveState(true); //NOSONAR
                        queueManager.queueIsSaveable = false; //NOSONAR
                        closeExternalStorageFiles(); //NOSONAR
                    } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) { //NOSONAR
                        queueManager.queueIsSaveable = true; //NOSONAR
                        playbackManager.reloadQueue(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            }; // NOSONAR
            IntentFilter intentFilter = new IntentFilter(); //NOSONAR
            intentFilter.addAction(Intent.ACTION_MEDIA_EJECT); //NOSONAR
            intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED); //NOSONAR
            intentFilter.addDataScheme("file"); //NOSONAR
            registerReceiver(unmountReceiver, intentFilter); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Save the current state of the player in preferences. This stores the player's seek position, and calls {@link QueueManager#saveQueue(boolean)} // NOSONAR
     * to save our current playback position, shuffle mode, etc. // NOSONAR
     * // NOSONAR
     * @param saveQueue whether to save the queue as well // NOSONAR
     */ // NOSONAR
    void saveState(boolean saveQueue) { //NOSONAR
        playbackManager.saveState(); //NOSONAR
        queueManager.saveQueue(saveQueue); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Queues a new list for playback // NOSONAR
     * // NOSONAR
     * @param songs The list to queue // NOSONAR
     * @param action The action to take // NOSONAR
     */ // NOSONAR
    public void enqueue(List<Song> songs, @QueueManager.EnqueueAction final int action) { //NOSONAR
        playbackManager.enqueue(songs, action); //NOSONAR
    } // NOSONAR

    public void moveToNext(QueueItem queueItem) { //NOSONAR
        List<QueueItem> playlist = queueManager.getCurrentPlaylist(); //NOSONAR
        int fromIndex = playlist.indexOf(queueItem); //NOSONAR

        QueueItem currentQueueItem = queueManager.getCurrentQueueItem(); //NOSONAR
        int toIndex = playlist.indexOf(currentQueueItem) + 1; //NOSONAR

        if (fromIndex != toIndex) { //NOSONAR
            playbackManager.moveQueueItem(fromIndex, toIndex); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the track to be played // NOSONAR
     */ // NOSONAR
    protected void setNextTrack() { //NOSONAR
        playbackManager.setNextTrack(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Opens a list of songs for playback // NOSONAR
     * // NOSONAR
     * @param songs The list of songs to open // NOSONAR
     * @param position The position to start playback at // NOSONAR
     */ // NOSONAR
    public void open(@NonNull List<Song> songs, int position, Boolean playWhenReady) { //NOSONAR
        playbackManager.load(songs, position, playWhenReady, 0); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Opens a file and prepares it for playback // NOSONAR
     * // NOSONAR
     * @param path The path of the file to open // NOSONAR
     */ // NOSONAR
    public void openFile(String path, Boolean playWhenReady) { //NOSONAR
        playbackManager.loadFile(path, playWhenReady); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Starts playback of a previously opened file // NOSONAR
     */ // NOSONAR
    public void play() { //NOSONAR
        playbackManager.play(); //NOSONAR
    } // NOSONAR

    public void togglePlayback() { //NOSONAR
        playbackManager.togglePlayback(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Stops playback // NOSONAR
     */ // NOSONAR
    public void stop() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "stop()"); //NOSONAR
        playbackManager.stop(true); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Pauses playback // NOSONAR
     * // NOSONAR
     * @param canFade whether we are allowed to fade out before pausing. // NOSONAR
     */ // NOSONAR
    public void pause(boolean canFade) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "pause()"); //NOSONAR
        playbackManager.pause(canFade); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns whether something is currently playing // NOSONAR
     * // NOSONAR
     * @return true if something is playing (or will be playing shortly, in case we're currently transitioning between tracks), false if not // NOSONAR
     */ // NOSONAR
    public boolean isPlaying() { //NOSONAR
        return playbackManager.isPlaying(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return true if is playing or has played recently // NOSONAR
     */ // NOSONAR
    private boolean recentlyPlayed() { //NOSONAR
        return playbackManager.recentlyPlayed(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Changes from the current track to the previous track // NOSONAR
     */ // NOSONAR
    public void previous(boolean force) { //NOSONAR
        playbackManager.previous(force); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Changes from the current track to the next track // NOSONAR
     * // NOSONAR
     * @param force true to move to the next song regardless of repeat mode. // NOSONAR
     */ // NOSONAR
    public void gotoNext(boolean force) { //NOSONAR
        playbackManager.next(force); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the current playback position in milliseconds // NOSONAR
     */ // NOSONAR
    public long getSeekPosition() { //NOSONAR
        return playbackManager.getSeekPosition(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Seeks to the position specified. // NOSONAR
     * // NOSONAR
     * @param position The position to seek to, in milliseconds // NOSONAR
     */ // NOSONAR
    public void seekTo(long position) { //NOSONAR
        playbackManager.seekTo(position); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return int the audio session ID. // NOSONAR
     */ // NOSONAR
    public int getAudioSessionId() { //NOSONAR
        return playbackManager.getAudioSessionId(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Creates a shuffled list of all songs and begins playback // NOSONAR
     */ // NOSONAR
    public void playAutoShuffleList() { //NOSONAR
        playbackManager.playAutoShuffleList(); //NOSONAR
    } // NOSONAR

    @QueueManager.ShuffleMode //NOSONAR
    public int getShuffleMode() { //NOSONAR
        return queueManager.shuffleMode; //NOSONAR
    } // NOSONAR

    public void setShuffleMode(@QueueManager.ShuffleMode int shufflemode) { //NOSONAR
        queueManager.setShuffleMode(shufflemode); //NOSONAR
    } // NOSONAR

    @QueueManager.RepeatMode //NOSONAR
    public int getRepeatMode() { //NOSONAR
        return queueManager.repeatMode; //NOSONAR
    } // NOSONAR

    public void setRepeatMode(@QueueManager.RepeatMode int repeatMode) { //NOSONAR
        queueManager.setRepeatMode(repeatMode); //NOSONAR
        setNextTrack(); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    public Song getSong() { //NOSONAR
        return queueManager.getCurrentSong(); //NOSONAR
    } // NOSONAR

    public void toggleFavorite() { //NOSONAR
        Song song = queueManager.getCurrentSong(); //NOSONAR
        if (song != null) { //NOSONAR
            favoritesPlaylistManager.toggleFavorite(song, isFavorite -> { //NOSONAR
                if (isFavorite) { //NOSONAR
                    Toast.makeText(MusicService.this, getString(R.string.song_to_favourites, song.name), Toast.LENGTH_SHORT).show(); //NOSONAR
                } else { //NOSONAR
                    Toast.makeText(MusicService.this, getString(R.string.song_removed_from_favourites, song.name), Toast.LENGTH_SHORT).show(); //NOSONAR
                } // NOSONAR
                notifyChange(InternalIntents.FAVORITE_CHANGED); //NOSONAR
                return Unit.INSTANCE; //NOSONAR
            }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void showToast(int resId) { //NOSONAR
        Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT).show(); //NOSONAR
    } // NOSONAR

    public void toggleShuffleMode() { //NOSONAR
        switch (getShuffleMode()) { //NOSONAR
            case QueueManager.ShuffleMode.OFF: //NOSONAR
                setShuffleMode(QueueManager.ShuffleMode.ON); //NOSONAR
                notifyChange(InternalIntents.SHUFFLE_CHANGED); //NOSONAR
                queueManager.makeShuffleList(); //NOSONAR
                notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
                if (getRepeatMode() == QueueManager.RepeatMode.ONE) { //NOSONAR
                    setRepeatMode(QueueManager.RepeatMode.ALL); //NOSONAR
                } // NOSONAR
                showToast(R.string.shuffle_on_notif); //NOSONAR
                break; //NOSONAR
            case QueueManager.ShuffleMode.ON: //NOSONAR
                setShuffleMode(QueueManager.ShuffleMode.OFF); //NOSONAR
                notifyChange(InternalIntents.SHUFFLE_CHANGED); //NOSONAR
                if (this.queueManager.queuePosition >= 0 && this.queueManager.queuePosition < queueManager.shuffleList.size()) { //NOSONAR
                    int playPos = queueManager.playlist.indexOf(queueManager.shuffleList.get(this.queueManager.queuePosition)); //NOSONAR
                    if (playPos != -1) { //NOSONAR
                        this.queueManager.queuePosition = playPos; //NOSONAR
                    } // NOSONAR
                } // NOSONAR
                notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
                showToast(R.string.shuffle_off_notif); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void toggleRepeat() { //NOSONAR
        switch (getRepeatMode()) { //NOSONAR
            case QueueManager.RepeatMode.OFF: //NOSONAR
                setRepeatMode(QueueManager.RepeatMode.ALL); //NOSONAR
                showToast(R.string.repeat_all_notif); //NOSONAR
                break; //NOSONAR
            case QueueManager.RepeatMode.ALL: //NOSONAR
                setRepeatMode(QueueManager.RepeatMode.ONE); //NOSONAR
                showToast(R.string.repeat_current_notif); //NOSONAR
                break; //NOSONAR
            case QueueManager.RepeatMode.ONE: //NOSONAR
                setRepeatMode(QueueManager.RepeatMode.OFF); //NOSONAR
                showToast(R.string.repeat_off_notif); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
        notifyChange(InternalIntents.REPEAT_CHANGED); //NOSONAR
    } // NOSONAR

    public void clearQueue() { //NOSONAR
        playbackManager.clearQueue(); //NOSONAR
    } // NOSONAR

    public List<QueueItem> getQueue() { //NOSONAR
        return queueManager.getCurrentPlaylist(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return the position in the queue // NOSONAR
     */ // NOSONAR
    public int getQueuePosition() { //NOSONAR
        return queueManager.queuePosition; //NOSONAR
    } // NOSONAR

    public boolean getQueueReloading() { //NOSONAR
        synchronized (this) { //NOSONAR
            return queueManager.queueReloading; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Starts playing the track at the given position in the queue. // NOSONAR
     * // NOSONAR
     * @param position The position in the queue of the track that will be played. // NOSONAR
     */ // NOSONAR
    public void setQueuePosition(int position) { //NOSONAR
        playbackManager.setQueuePosition(position); //NOSONAR
    } // NOSONAR

    public void removeQueueItems(List<QueueItem> queueItems) { //NOSONAR
        playbackManager.removeQueueItems(queueItems); //NOSONAR
    } // NOSONAR

    public void removeSongs(List<Song> songs) { //NOSONAR
        playbackManager.removeSongs(songs); //NOSONAR
    } // NOSONAR

    public void removeQueueItem(QueueItem queueItem) { //NOSONAR
        playbackManager.removeQueueItem(queueItem); //NOSONAR
    } // NOSONAR

    public void moveQueueItem(int from, int to) { //NOSONAR
        playbackManager.moveQueueItem(from, to); //NOSONAR
    } // NOSONAR

    // EQ // NOSONAR

    public void closeEqualizerSessions(boolean internal, int audioSessionId) { //NOSONAR
        playbackManager.closeEqualizerSessions(internal, audioSessionId); //NOSONAR
    } // NOSONAR

    public void openEqualizerSession(boolean internal, int audioSessionId) { //NOSONAR
        playbackManager.openEqualizerSession(internal, audioSessionId); //NOSONAR
    } // NOSONAR

    public void updateEqualizer() { //NOSONAR
        playbackManager.updateEqualizer(); //NOSONAR
    } // NOSONAR

    private void scheduleDelayedShutdown() { //NOSONAR
        if (isPlaying() || serviceInUse || playbackManager.willResumePlayback()) { //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, //NOSONAR
                    String.format("scheduleDelayedShutdown called.. returning early. isPlaying: %s service in use: %s will resume playback: %s", //NOSONAR
                            isPlaying(), serviceInUse, playbackManager.willResumePlayback())); //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        analyticsManager.dropBreadcrumb(TAG, "scheduleDelayedShutdown for 5 mins from now"); //NOSONAR
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5 * 60 * 1000 /* 5 mins */, shutdownIntent); //NOSONAR
        shutdownScheduled = true; //NOSONAR
    } // NOSONAR

    private void cancelShutdown() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "cancelShutdown() called. Shutdown scheduled: " + shutdownScheduled); //NOSONAR
        if (shutdownScheduled) { //NOSONAR
            alarmManager.cancel(shutdownIntent); //NOSONAR
            shutdownScheduled = false; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    // Notifications // NOSONAR

    void updateNotification() { //NOSONAR

        final int notifyMode; //NOSONAR

        if (isPlaying()) { //NOSONAR
            notifyMode = NotifyMode.FOREGROUND; //NOSONAR
        } else if (recentlyPlayed()) { //NOSONAR
            notifyMode = NotifyMode.BACKGROUND; //NOSONAR
        } else { //NOSONAR
            notifyMode = NotifyMode.NONE; //NOSONAR
        } // NOSONAR

        switch (notifyMode) { //NOSONAR
            case NotifyMode.FOREGROUND: //NOSONAR
                startForegroundImpl(); //NOSONAR
                break; //NOSONAR
            case NotifyMode.BACKGROUND: //NOSONAR
                try { //NOSONAR
                    if (queueManager.getCurrentSong() != null) { //NOSONAR
                        notificationHelper.notify(this, playlistsRepository, songsRepository, queueManager.getCurrentSong(), isPlaying(), playbackManager.getMediaSessionToken(), settingsManager, //NOSONAR
                                favoritesPlaylistManager); //NOSONAR
                    } // NOSONAR
                } catch (ConcurrentModificationException e) { //NOSONAR
                    LogUtils.logException(TAG, "Exception while attempting to show notification", e); //NOSONAR
                } // NOSONAR
                stopForegroundImpl(false, false); //NOSONAR
                break; //NOSONAR
            case NotifyMode.NONE: //NOSONAR
                stopForegroundImpl(false, false); //NOSONAR
                notificationHelper.cancel(); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void cancelNotification() { //NOSONAR
        stopForegroundImpl(true, true); //NOSONAR
        notificationHelper.cancel(); //NOSONAR
    } // NOSONAR

    public static PendingIntent retrievePlaybackAction(Context context, final String action) { //NOSONAR
        final ComponentName serviceName = new ComponentName(context, MusicService.class); //NOSONAR
        Intent intent = new Intent(action); //NOSONAR
        intent.setComponent(serviceName); //NOSONAR

        return PendingIntent.getService(context, 0, intent, 0); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Starts the foreground notification, and cancels any stop messages // NOSONAR
     */ // NOSONAR
    private void startForegroundImpl() { //NOSONAR
        try { //NOSONAR
            notificationStateHandler.sendEmptyMessage(NotificationStateHandler.START_FOREGROUND); //NOSONAR
            Song song = queueManager.getCurrentSong(); //NOSONAR
            if (song != null) { //NOSONAR
                Log.i(TAG, "startForeground called"); //NOSONAR
                if (notificationHelper.startForeground( //NOSONAR
                        this, //NOSONAR
                        playlistsRepository, //NOSONAR
                        songsRepository, //NOSONAR
                        queueManager.getCurrentSong(), //NOSONAR
                        isPlaying(), //NOSONAR
                        playbackManager.getMediaSessionToken(), //NOSONAR
                        settingsManager, //NOSONAR
                        favoritesPlaylistManager //NOSONAR
                )) { // NOSONAR
                    dummyNotificationHelper.setForegroundedByApp(true); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                Log.e(TAG, "startForeground should have been called, but song is null"); //NOSONAR
            } // NOSONAR
        } catch (NullPointerException | ConcurrentModificationException e) { //NOSONAR
            Crashlytics.log("startForegroundImpl error: " + e.getMessage()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Stops the foreground notification // NOSONAR
     * // NOSONAR
     * @param removeNotification true to remove the notification as well as stop the service running in the foreground // NOSONAR
     * @param withDelay true to delay the stop call by 1.5 seconds, allowing subsequent start calls to cancel this call // NOSONAR
     */ // NOSONAR
    void stopForegroundImpl(boolean removeNotification, boolean withDelay) { //NOSONAR
        if (withDelay) { //NOSONAR
            notificationStateHandler.sendEmptyMessageDelayed(NotificationStateHandler.STOP_FOREGROUND, 1500); //NOSONAR
        } else { //NOSONAR
            stopForeground(removeNotification); //NOSONAR
            dummyNotificationHelper.setForegroundedByApp(false); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    // Event management // NOSONAR

    private Bundle getExtras(@NonNull Song song) { //NOSONAR
        Bundle extras = new Bundle(); //NOSONAR
        extras.putLong("id", song.id); //NOSONAR
        extras.putString("artist", song.artistName); //NOSONAR
        extras.putString("album", song.albumName); //NOSONAR
        extras.putString("track", song.name); //NOSONAR
        extras.putInt("shuffleMode", getShuffleMode()); //NOSONAR
        extras.putInt("repeatMode", getRepeatMode()); //NOSONAR
        extras.putBoolean("playing", isPlaying()); //NOSONAR
        extras.putLong("duration", song.duration); //NOSONAR
        extras.putLong("position", getSeekPosition()); //NOSONAR
        extras.putLong("ListSize", queueManager.getCurrentPlaylist().size()); //NOSONAR
        return extras; //NOSONAR
    } // NOSONAR

    private Intent getTaskerIntent(@NonNull Song song) { //NOSONAR
        Intent intent = new Intent(ExternalIntents.TASKER); //NOSONAR
        intent.putExtra("%MTRACK", isPlaying() ? song.name : ""); //NOSONAR
        return intent; //NOSONAR
    } // NOSONAR

    private Intent getPebbleIntent(@NonNull Song song) { //NOSONAR
        Intent intent = new Intent(ExternalIntents.PEBBLE); //NOSONAR
        intent.putExtra("artist", song.artistName); //NOSONAR
        intent.putExtra("album", song.albumName); //NOSONAR
        intent.putExtra("track", song.name); //NOSONAR
        return intent; //NOSONAR
    } // NOSONAR

    void notifyChange(String action) { //NOSONAR
        switch (action) { //NOSONAR
            case InternalIntents.TRACK_ENDING: //NOSONAR
                onTrackEnded(); //NOSONAR
                return; //NOSONAR
            case InternalIntents.FAVORITE_CHANGED: //NOSONAR
                updateNotification(); //NOSONAR
                return; //NOSONAR
            case InternalIntents.PLAY_STATE_CHANGED: //NOSONAR
                onPlayStateChanged(); //NOSONAR
                break; //NOSONAR
            case InternalIntents.META_CHANGED: //NOSONAR
                onMetaChanged(); //NOSONAR
                break; //NOSONAR
            case InternalIntents.QUEUE_CHANGED: //NOSONAR
                onQueueChanged(); //NOSONAR
                break; //NOSONAR
        } // NOSONAR

        Intent intent = new Intent(action); //NOSONAR
        Song currentSong = queueManager.getCurrentSong(); //NOSONAR
        if (currentSong != null) { //NOSONAR
            intent.putExtras(getExtras(currentSong)); //NOSONAR
        } // NOSONAR
        sendBroadcast(intent); //NOSONAR

        widgetManager.notifyChange(this, action); //NOSONAR

        saveState(false); //NOSONAR
    } // NOSONAR

    private void onQueueChanged() { //NOSONAR
        if (isPlaying()) { //NOSONAR
            setNextTrack(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void onMetaChanged() { //NOSONAR
        updateNotification(); //NOSONAR

        if (queueManager.getCurrentSong() != null) { //NOSONAR
            queueManager.getCurrentSong().setStartTime(); //NOSONAR

            sendBroadcast(getTaskerIntent(queueManager.getCurrentSong())); //NOSONAR

            sendBroadcast(getPebbleIntent(queueManager.getCurrentSong())); //NOSONAR

            bluetoothManager.sendMetaChangedIntent(this, getExtras(queueManager.getCurrentSong())); //NOSONAR

            scrobbleManager.scrobbleBroadcast(this, ScrobbleManager.ScrobbleStatus.START, queueManager.getCurrentSong()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void onPlayStateChanged() { //NOSONAR
        updateNotification(); //NOSONAR

        if (queueManager.getCurrentSong() != null) { //NOSONAR

            bluetoothManager.sendPlayStateChangedIntent(this, getExtras(queueManager.getCurrentSong())); //NOSONAR

            sendBroadcast(getTaskerIntent(queueManager.getCurrentSong())); //NOSONAR

            if (isPlaying()) { //NOSONAR
                queueManager.getCurrentSong().setResumed(); //NOSONAR
                sendBroadcast(getPebbleIntent(queueManager.getCurrentSong())); //NOSONAR
            } else { //NOSONAR
                queueManager.getCurrentSong().setPaused(); //NOSONAR
            } // NOSONAR
            scrobbleManager.scrobbleBroadcast(this, isPlaying() ? ScrobbleManager.ScrobbleStatus.RESUME : ScrobbleManager.ScrobbleStatus.PAUSE, queueManager.getCurrentSong()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void onTrackEnded() { //NOSONAR
        //We're just about to change tracks, so 'current song' is the song that just finished // NOSONAR
        Song finishedSong = queueManager.getCurrentSong(); //NOSONAR
        if (finishedSong != null) { //NOSONAR
            if (finishedSong.hasPlayed()) { //NOSONAR
                disposables.add( //NOSONAR
                        Completable.fromAction(() -> ShuttleUtils.incrementPlayCount(this, finishedSong)) //NOSONAR
                                .subscribeOn(Schedulers.io()) //NOSONAR
                                .subscribe(() -> { //NOSONAR
                                    // Nothing to do // NOSONAR
                                }, error -> LogUtils.logException(TAG, "Error incrementing play count", error)) //NOSONAR
                ); // NOSONAR
            } // NOSONAR
            scrobbleManager.scrobbleBroadcast(this, ScrobbleManager.ScrobbleStatus.COMPLETE, finishedSong); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public interface Callbacks { //NOSONAR

        void notifyChange(String action); //NOSONAR

        void scheduleDelayedShutdown(); //NOSONAR

        void cancelShutdown(); //NOSONAR

        void updateNotification(); //NOSONAR

        void stopForegroundImpl(boolean removeNotification, boolean withDelay); //NOSONAR
    } // NOSONAR

    class MusicServiceCallbacks implements Callbacks { //NOSONAR

        @Override //NOSONAR
        public void notifyChange(String action) { //NOSONAR
            MusicService.this.notifyChange(action); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void scheduleDelayedShutdown() { //NOSONAR
            MusicService.this.scheduleDelayedShutdown(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void cancelShutdown() { //NOSONAR
            MusicService.this.cancelShutdown(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void updateNotification() { //NOSONAR
            MusicService.this.updateNotification(); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void stopForegroundImpl(boolean removeNotification, boolean withDelay) { //NOSONAR
            MusicService.this.stopForegroundImpl(removeNotification, withDelay); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
