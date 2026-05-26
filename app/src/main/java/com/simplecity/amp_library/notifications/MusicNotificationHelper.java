package com.simplecity.amp_library.notifications; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.app.Notification; // NOSONAR
import android.app.PendingIntent; // NOSONAR
import android.app.Service; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Handler; // NOSONAR
import android.os.Looper; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.app.NotificationCompat; // NOSONAR
import android.support.v4.media.session.MediaSessionCompat; // NOSONAR
import android.util.Log; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.Priority; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.bumptech.glide.request.animation.GlideAnimation; // NOSONAR
import com.bumptech.glide.request.target.SimpleTarget; // NOSONAR
import com.simplecity.amp_library.BuildConfig; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.glide.utils.GlideUtils; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.MusicService; // NOSONAR
import com.simplecity.amp_library.playback.constants.ServiceCommand; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.ConcurrentModificationException; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MusicNotificationHelper extends NotificationHelper { //NOSONAR

    private static final String TAG = "MusicNotificationHelper"; //NOSONAR

    private static final int NOTIFICATION_ID = 150; //NOSONAR

    Notification notification; //NOSONAR

    boolean isFavorite = false; //NOSONAR

    Bitmap bitmap; //NOSONAR

    private Handler handler; //NOSONAR

    private AnalyticsManager analyticsManager; //NOSONAR

    private CompositeDisposable compositeDisposable = new CompositeDisposable(); //NOSONAR

    public MusicNotificationHelper(Context context, AnalyticsManager analyticsManager) { //NOSONAR
        super(context); //NOSONAR

        handler = new Handler(Looper.getMainLooper()); //NOSONAR
        this.analyticsManager = analyticsManager; //NOSONAR
    } // NOSONAR

    public NotificationCompat.Builder getBuilder(Context context, @NonNull Song song, @NonNull MediaSessionCompat.Token mediaSessionToken, @Nullable Bitmap bitmap, boolean isPlaying, //NOSONAR
            boolean isFavorite) { //NOSONAR

        Intent intent = new Intent(BuildConfig.APPLICATION_ID + ".PLAYBACK_VIEWER"); //NOSONAR
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //NOSONAR
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0); //NOSONAR

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID) //NOSONAR
                .setSmallIcon(R.drawable.ic_stat_notification) //NOSONAR
                .setContentIntent(contentIntent) //NOSONAR
                .setChannelId(NOTIFICATION_CHANNEL_ID) //NOSONAR
                .setPriority(NotificationCompat.PRIORITY_MAX) //NOSONAR
                .setContentTitle(song.name) //NOSONAR
                .setContentText(song.artistName + " - " + song.albumName) //NOSONAR
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle() //NOSONAR
                        .setShowActionsInCompactView(0, 1, 2) //NOSONAR
                        .setMediaSession(mediaSessionToken)) //NOSONAR
                .addAction( //NOSONAR
                        R.drawable.ic_skip_previous_24dp, //NOSONAR
                        context.getString(R.string.btn_prev), //NOSONAR
                        MusicService.retrievePlaybackAction(context, ServiceCommand.PREV) //NOSONAR
                ) // NOSONAR
                .addAction( //NOSONAR
                        isPlaying ? R.drawable.ic_pause_24dp : R.drawable.ic_play_24dp, //NOSONAR
                        context.getString(isPlaying ? R.string.btn_pause : R.string.btn_play), //NOSONAR
                        MusicService.retrievePlaybackAction(context, ServiceCommand.TOGGLE_PLAYBACK) //NOSONAR
                ) // NOSONAR
                .addAction( //NOSONAR
                        R.drawable.ic_skip_next_24dp, //NOSONAR
                        context.getString(R.string.btn_skip), //NOSONAR
                        MusicService.retrievePlaybackAction(context, ServiceCommand.NEXT) //NOSONAR
                ) // NOSONAR
                .addAction( //NOSONAR
                        isFavorite ? R.drawable.ic_favorite_24dp_scaled : R.drawable.ic_favorite_border_24dp_scaled, //NOSONAR
                        context.getString(R.string.fav_add), //NOSONAR
                        MusicService.retrievePlaybackAction(context, ServiceCommand.TOGGLE_FAVORITE) //NOSONAR
                ) // NOSONAR
                .setShowWhen(false) //NOSONAR
                .setVisibility(android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC); //NOSONAR

        if (bitmap != null) { //NOSONAR
            builder.setLargeIcon(bitmap); //NOSONAR
        } // NOSONAR

        return builder; //NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    public void notify( //NOSONAR
            Context context, //NOSONAR
            @NonNull Repository.PlaylistsRepository playlistsRepository, //NOSONAR
            @NonNull Repository.SongsRepository songsRepository, //NOSONAR
            @NonNull Song song, boolean isPlaying, //NOSONAR
            @NonNull MediaSessionCompat.Token mediaSessionToken, //NOSONAR
            @NonNull SettingsManager settingsManager, //NOSONAR
            FavoritesPlaylistManager favoritesPlaylistManager //NOSONAR
    ) { // NOSONAR
        notification = getBuilder(context, song, mediaSessionToken, bitmap, isPlaying, isFavorite).build(); //NOSONAR
        notify(NOTIFICATION_ID, notification); //NOSONAR

        compositeDisposable.add(favoritesPlaylistManager.isFavorite(song) //NOSONAR
                .first(false) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(isFavorite -> { //NOSONAR
                    this.isFavorite = isFavorite; //NOSONAR
                    notification = getBuilder(context, song, mediaSessionToken, MusicNotificationHelper.this.bitmap, isPlaying, isFavorite).build(); //NOSONAR
                    notify(notification); //NOSONAR
                }, error -> { //NOSONAR
                    LogUtils.logException(TAG, "MusicNotificationHelper failed to present notification", error); //NOSONAR
                })); // NOSONAR

        handler.post(() -> Glide.with(context) //NOSONAR
                .load(song) //NOSONAR
                .asBitmap() //NOSONAR
                .priority(Priority.IMMEDIATE) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .override(600, 600) //NOSONAR
                .placeholder(PlaceholderProvider.getInstance(context).getPlaceHolderDrawable(song.albumName, false, settingsManager)) //NOSONAR
                .into(new SimpleTarget<Bitmap>() { //NOSONAR
                    @Override //NOSONAR
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) { //NOSONAR
                        MusicNotificationHelper.this.bitmap = resource; //NOSONAR
                        try { //NOSONAR
                            notification = getBuilder(context, song, mediaSessionToken, bitmap, isPlaying, isFavorite).build(); //NOSONAR
                            MusicNotificationHelper.this.notify(notification); //NOSONAR
                        } catch (NullPointerException | ConcurrentModificationException e) { //NOSONAR
                            LogUtils.logException(TAG, "Exception while attempting to update notification with glide image.", e); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR

                    @Override //NOSONAR
                    public void onLoadFailed(Exception e, Drawable errorDrawable) { //NOSONAR
                        MusicNotificationHelper.this.bitmap = GlideUtils.drawableToBitmap(errorDrawable); //NOSONAR
                        super.onLoadFailed(e, errorDrawable); //NOSONAR
                        try { //NOSONAR
                            notification = getBuilder(context, song, mediaSessionToken, bitmap, isPlaying, isFavorite).build(); //NOSONAR
                            MusicNotificationHelper.this.notify(NOTIFICATION_ID, notification); //NOSONAR
                        } catch (IllegalArgumentException error) { //NOSONAR
                            LogUtils.logException(TAG, "Exception while attempting to update notification with error image", error); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                })); // NOSONAR
    } // NOSONAR

    public boolean startForeground( //NOSONAR
            Service service, //NOSONAR
            @NonNull Repository.PlaylistsRepository playlistsRepository, //NOSONAR
            @NonNull Repository.SongsRepository songsRepository, //NOSONAR
            @NonNull Song song, //NOSONAR
            boolean isPlaying, //NOSONAR
            @NonNull MediaSessionCompat.Token mediaSessionToken, //NOSONAR
            SettingsManager settingsManager, //NOSONAR
            FavoritesPlaylistManager favoritesPlaylistManager //NOSONAR
    ) { // NOSONAR
        notify(service, playlistsRepository, songsRepository, song, isPlaying, mediaSessionToken, settingsManager, favoritesPlaylistManager); //NOSONAR
        try { //NOSONAR
            analyticsManager.dropBreadcrumb(TAG, "startForeground() called"); //NOSONAR
            Log.w(TAG, "service.startForeground called"); //NOSONAR
            service.startForeground(NOTIFICATION_ID, notification); //NOSONAR
            return true; //NOSONAR
        } catch (RuntimeException e) { //NOSONAR
            Log.e(TAG, "startForeground not called, error: " + e); //NOSONAR
            LogUtils.logException(TAG, "Error starting foreground notification", e); //NOSONAR
            return false; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void notify(Notification notification) { //NOSONAR
        super.notify(NOTIFICATION_ID, notification); //NOSONAR
    } // NOSONAR

    public void cancel() { //NOSONAR
        super.cancel(NOTIFICATION_ID); //NOSONAR
    } // NOSONAR

    public void tearDown() { //NOSONAR
        compositeDisposable.clear(); //NOSONAR
    } // NOSONAR
} // NOSONAR
