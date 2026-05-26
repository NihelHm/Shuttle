package com.simplecity.amp_library.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.glide.loader.ArtworkModelLoader;
import com.simplecity.amp_library.model.ArtworkProvider;
import com.simplecity.amp_library.notifications.NotificationHelper;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.inject.Inject;

/**
 * A service which will download all artist & album artworkProvider, via an AsyncTask, and display the progress in a notification.
 * The notification includes a 'cancel' button, and the AsyncTask & associated HttpRequests can be cancelled.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkDownloadService extends Service { //NOSONAR

    private static final String TAG = "ArtworkDownloadService"; //NOSONAR

    private static final String ACTION_CANCEL = "com.simplecity.shuttle.artwork_cancel"; //NOSONAR

    private static final int NOTIFICATION_ID = 200; //NOSONAR

    private int progress = 0; //NOSONAR
    private int max = 100; //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    NotificationHelper notificationHelper; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumsRepository albumsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumArtistsRepository albumArtistsRepository; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    private NotificationCompat.Builder getNotificationBuilder() { //NOSONAR

        final ComponentName serviceName = new ComponentName(this, ArtworkDownloadService.class); //NOSONAR
        Intent intent = new Intent(ACTION_CANCEL); //NOSONAR
        intent.setComponent(serviceName); //NOSONAR
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0); //NOSONAR

        return new NotificationCompat.Builder(this, NotificationHelper.NOTIFICATION_CHANNEL_ID) //NOSONAR
                .setContentTitle(getResources().getString(R.string.notif_downloading_art)) //NOSONAR
                .setSmallIcon(android.R.drawable.stat_sys_download) //NOSONAR
                .setOngoing(true) //NOSONAR
                .setProgress(100, 0, true) //NOSONAR
                .addAction(new NotificationCompat.Action(R.drawable.ic_close_24dp, getString(R.string.cancel), pendingIntent)); //NOSONAR
    }

    @Override //NOSONAR
    public void onCreate() { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(); //NOSONAR

        notificationHelper = new NotificationHelper(this); //NOSONAR

        if (!ShuttleUtils.isOnline(this, false)) { //NOSONAR
            Toast toast = Toast.makeText(this, getResources().getString(R.string.connection_unavailable), Toast.LENGTH_SHORT); //NOSONAR
            toast.show(); //NOSONAR
            stopSelf(); //NOSONAR
            return; //NOSONAR
        }

        notificationHelper.notify(NOTIFICATION_ID, getNotificationBuilder().build()); //NOSONAR

        Single<List<ArtworkProvider>> sharedItemsSingle = albumArtistsRepository.getAlbumArtists() //NOSONAR
                .first(Collections.emptyList()) //NOSONAR
                .<ArtworkProvider>flatMapObservable(Observable::fromIterable) //NOSONAR
                .mergeWith(albumsRepository.getAlbums() //NOSONAR
                        .first(Collections.emptyList()) //NOSONAR
                        .flatMapObservable(Observable::fromIterable)) //NOSONAR
                .toList(); //NOSONAR

        disposables.add(sharedItemsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(list -> { //NOSONAR
                    max = list.size(); //NOSONAR
                    updateProgress(); //NOSONAR
                }, error -> LogUtils.logException(TAG, "Error determining max", error))); //NOSONAR

        disposables.add(sharedItemsSingle.flatMapObservable(Observable::fromIterable) //NOSONAR
                .flatMap(artworkProvider -> Observable.just(artworkProvider) //NOSONAR
                        .map(artwork -> { //NOSONAR
                            FutureTarget<File> futureTarget = Glide.with(ArtworkDownloadService.this) //NOSONAR
                                    .using(new ArtworkModelLoader(this, true), InputStream.class) //NOSONAR
                                    .load(artwork) //NOSONAR
                                    .as(InputStream.class) //NOSONAR
                                    .downloadOnly(SimpleTarget.SIZE_ORIGINAL, SimpleTarget.SIZE_ORIGINAL); //NOSONAR
                            try { //NOSONAR
                                futureTarget.get(30, TimeUnit.SECONDS); //NOSONAR
                            } catch (InterruptedException | ExecutionException | TimeoutException e) { //NOSONAR
                                Log.e(TAG, "Error downloading artworkProvider: " + e); //NOSONAR
                            }
                            Glide.clear(futureTarget); //NOSONAR
                            return artwork; //NOSONAR
                        }))
                .subscribeOn(Schedulers.computation()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(item -> updateProgress(), error -> LogUtils.logException(TAG, "Error downloading artwork", error))); //NOSONAR
    }

    @Override //NOSONAR
    public void onTaskRemoved(Intent rootIntent) { //NOSONAR

        // Some users like to crash the entire app and then wonder why the service stop working.
        // If they remove the task, shut everything down.

        stopSelf(); //NOSONAR

        super.onTaskRemoved(rootIntent); //NOSONAR
    }

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR
        if (disposables != null) { //NOSONAR
            disposables.clear(); //NOSONAR
        }
        notificationHelper.cancel(NOTIFICATION_ID); //NOSONAR
        super.onDestroy(); //NOSONAR
    }

    /**
     * Increments the progress count and updates the notification with the new value.
     * If the progress is equal to (or greater) than our count, then this task is finished.
     * The notification is dismissed and this service is stopped.
     */
    private void updateProgress() { //NOSONAR
        progress++; //NOSONAR

        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(); //NOSONAR
        notificationBuilder.setProgress(max, progress, false); //NOSONAR
        notificationHelper.notify(NOTIFICATION_ID, notificationBuilder.build()); //NOSONAR

        if (progress >= max) { //NOSONAR
            notificationHelper.cancel(NOTIFICATION_ID); //NOSONAR
        }
    }

    @Override //NOSONAR
    public int onStartCommand(Intent intent, int flags, int startId) { //NOSONAR
        if (intent != null) { //NOSONAR
            final String action = intent.getAction(); //NOSONAR
            if (action != null && action.equals(ACTION_CANCEL)) { //NOSONAR
                //Handle a notification cancel action click:
                disposables.clear(); //NOSONAR
                notificationHelper.cancel(NOTIFICATION_ID); //NOSONAR
                stopSelf(); //NOSONAR
            }
        }
        return super.onStartCommand(intent, flags, startId); //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public IBinder onBind(Intent intent) { //NOSONAR
        //Nothing to do.
        return null; //NOSONAR
    }
}
