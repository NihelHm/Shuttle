package com.simplecity.amp_library.playback; // NOSONAR

import android.Manifest; // NOSONAR
import android.content.ContentUris; // NOSONAR
import android.content.ContentValues; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.pm.PackageManager; // NOSONAR
import android.database.sqlite.SQLiteException; // NOSONAR
import android.net.Uri; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.media.session.MediaSessionCompat; // NOSONAR
import android.util.Log; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.services.Equalizer; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItem; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItemKt; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.SleepTimer; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.List; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function1; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlaybackManager implements Playback.Callbacks { //NOSONAR

    private static final String TAG = "PlaybackManager"; //NOSONAR

    private Context context; //NOSONAR

    private QueueManager queueManager; //NOSONAR

    private PlaybackSettingsManager playbackSettingsManager; //NOSONAR

    private MediaSessionManager mediaSessionManager; //NOSONAR

    private Equalizer equalizer; //NOSONAR

    private long lastPlayedTime; //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    private boolean pauseOnTrackFinish = false; //NOSONAR

    private MusicService.Callbacks musicServiceCallbacks; //NOSONAR

    private Repository.SongsRepository songsRepository; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private boolean playOnQueueReload = false; //NOSONAR

    @NonNull //NOSONAR
    Playback playback; //NOSONAR

    PlaybackManager( //NOSONAR
            Context context, //NOSONAR
            QueueManager queueManager, //NOSONAR
            PlaybackSettingsManager playbackSettingsManager, //NOSONAR
            Repository.SongsRepository songsRepository, //NOSONAR
            Repository.AlbumsRepository albumsRepository, //NOSONAR
            Repository.AlbumArtistsRepository albumArtistsRepository, //NOSONAR
            Repository.GenresRepository genresRepository, //NOSONAR
            Repository.PlaylistsRepository playlistsRepository, //NOSONAR
            MusicService.Callbacks musicServiceCallbacks, //NOSONAR
            SettingsManager settingsManager //NOSONAR
    ) { // NOSONAR

        playback = new MediaPlayerPlayback(context); //NOSONAR
        playback.setCallbacks(this); //NOSONAR

        this.context = context.getApplicationContext(); //NOSONAR

        this.queueManager = queueManager; //NOSONAR

        this.playbackSettingsManager = playbackSettingsManager; //NOSONAR

        this.musicServiceCallbacks = musicServiceCallbacks; //NOSONAR

        this.songsRepository = songsRepository; //NOSONAR

        this.settingsManager = settingsManager; //NOSONAR

        mediaSessionManager = new MediaSessionManager( //NOSONAR
                context, queueManager, //NOSONAR
                this, //NOSONAR
                playbackSettingsManager, //NOSONAR
                settingsManager, //NOSONAR
                songsRepository, //NOSONAR
                albumsRepository, //NOSONAR
                albumArtistsRepository, //NOSONAR
                genresRepository, //NOSONAR
                playlistsRepository //NOSONAR
        ); // NOSONAR

        equalizer = new Equalizer(context, settingsManager); //NOSONAR

        disposables.add(SleepTimer.getInstance().getCurrentTimeObservable() //NOSONAR
                .subscribe(remainingTime -> { //NOSONAR
                    if (remainingTime == 0) { //NOSONAR
                        if (SleepTimer.getInstance().playToEnd) { //NOSONAR
                            pauseOnTrackFinish = true; //NOSONAR
                        } else { //NOSONAR
                            stop(true); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                }, throwable -> LogUtils.logException(TAG, "Error consuming SleepTimer observable", throwable))); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    public Playback getPlayback() { //NOSONAR
        return playback; //NOSONAR
    } // NOSONAR

    public void setQueuePosition(int position) { //NOSONAR
        stop(false); //NOSONAR
        queueManager.queuePosition = position; //NOSONAR
        load(true, true, 0); //NOSONAR
    } // NOSONAR

    void clearQueue() { //NOSONAR
        stop(true); //NOSONAR
        queueManager.clearQueue(); //NOSONAR
    } // NOSONAR

    void reloadQueue() { //NOSONAR
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        disposables.add(queueManager.reloadQueue(() -> { //NOSONAR
                    load(true, playOnQueueReload, playbackSettingsManager.getSeekPosition()); //NOSONAR
                    playOnQueueReload = false; //NOSONAR
                    return Unit.INSTANCE; //NOSONAR
                }) // NOSONAR
        ); // NOSONAR
    } // NOSONAR

    void removeQueueItems(List<QueueItem> queueItems) { //NOSONAR
        queueManager.removeQueueItems(queueItems, () -> stop(true), () -> { //NOSONAR
            boolean wasPlaying = isPlaying(); //NOSONAR
            stop(false); //NOSONAR
            load(true, wasPlaying, 0); //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    void removeSongs(List<Song> songs) { //NOSONAR
        queueManager.removeSongs(songs, () -> stop(true), () -> { //NOSONAR
            boolean wasPlaying = isPlaying(); //NOSONAR
            stop(false); //NOSONAR
            load(true, wasPlaying, 0); //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    void removeQueueItem(QueueItem queueItem) { //NOSONAR
        queueManager.removeQueueItem(queueItem, () -> stop(true), () -> { //NOSONAR
            boolean wasPlaying = isPlaying(); //NOSONAR
            stop(false); //NOSONAR
            load(true, wasPlaying, 0); //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    void moveQueueItem(int from, int to) { //NOSONAR
        queueManager.moveQueueItem(from, to); //NOSONAR
    } // NOSONAR

    private void notifyChange(String what) { //NOSONAR
        musicServiceCallbacks.notifyChange(what); //NOSONAR
    } // NOSONAR

    void playAutoShuffleList() { //NOSONAR
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) { //NOSONAR
            disposables.add(songsRepository.getSongs((Function1<? super Song, Boolean>) null) //NOSONAR
                    .firstOrError() //NOSONAR
                    .subscribeOn(Schedulers.io()) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe(songs -> { //NOSONAR
                        queueManager.playlist = QueueItemKt.toQueueItems(songs); //NOSONAR
                        queueManager.queuePosition = -1; //NOSONAR
                        queueManager.makeShuffleList(); //NOSONAR
                        queueManager.setShuffleMode(QueueManager.ShuffleMode.ON); //NOSONAR
                        notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
                        queueManager.queuePosition = 0; //NOSONAR
                        load(true, true, 0); //NOSONAR
                    }, error -> LogUtils.logException(TAG, "Error playing auto shuffle list", error))); //NOSONAR
        } else { //NOSONAR
            queueManager.shuffleMode = QueueManager.ShuffleMode.OFF; //NOSONAR
            queueManager.saveQueue(false); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    MediaSessionCompat.Token getMediaSessionToken() { //NOSONAR
        return mediaSessionManager.getSessionToken(); //NOSONAR
    } // NOSONAR

    void closeEqualizerSessions(boolean internal, int audioSessionId) { //NOSONAR
        equalizer.closeEqualizerSessions(internal, audioSessionId); //NOSONAR
    } // NOSONAR

    void openEqualizerSession(boolean internal, int audioSessionId) { //NOSONAR
        equalizer.openEqualizerSession(internal, audioSessionId); //NOSONAR
    } // NOSONAR

    void updateEqualizer() { //NOSONAR
        equalizer.update(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @param force true to ignore the current repeat mode. // NOSONAR
     * @return The next position to play, or -1 of playback should complete. // NOSONAR
     */ // NOSONAR
    private int getNextPosition(boolean force) { //NOSONAR
        return queueManager.getNextPosition(force); //NOSONAR
    } // NOSONAR

    public void load(@NonNull List<Song> songs, int queuePosition, Boolean playWhenReady, long seekPosition) { //NOSONAR
        queueManager.load( //NOSONAR
                songs, //NOSONAR
                queuePosition, //NOSONAR
                () -> load(true, playWhenReady, seekPosition) //NOSONAR
        ); // NOSONAR
    } // NOSONAR

    private void load(boolean setNext, Boolean playWhenReady, long seekPosition) { //NOSONAR
        if (queueManager.getCurrentPlaylist().isEmpty() || queueManager.queuePosition < 0 || queueManager.queuePosition >= queueManager.getCurrentPlaylist().size()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        stop(false); //NOSONAR

        loadAttempt(1, playWhenReady, seekPosition, success -> { //NOSONAR
            if (success) { //NOSONAR

                notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
                notifyChange(InternalIntents.META_CHANGED); //NOSONAR

                restoreBookmark(); //NOSONAR

                if (setNext) { //NOSONAR
                    setNextTrack(); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                musicServiceCallbacks.scheduleDelayedShutdown(); //NOSONAR
            } // NOSONAR
            return Unit.INSTANCE; //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    private void loadAttempt(int attempt, Boolean playWhenReady, long seekPosition, @Nullable Function1<Boolean, Unit> completion) { //NOSONAR

        Song song = queueManager.getCurrentSong(); //NOSONAR
        if (song == null) { //NOSONAR
            if (completion != null) { //NOSONAR
                completion.invoke(false); //NOSONAR
            } // NOSONAR
            return; //NOSONAR
        } // NOSONAR

        load(song, playWhenReady, seekPosition, success -> { //NOSONAR
            if (success) { //NOSONAR
                if (completion != null) { //NOSONAR
                    completion.invoke(true); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                if (attempt < 10) { //NOSONAR
                    int position = getNextPosition(false); //NOSONAR
                    if (position < 0) { //NOSONAR
                        if (completion != null) { //NOSONAR
                            completion.invoke(false); //NOSONAR
                        } // NOSONAR
                    } else { //NOSONAR
                        queueManager.queuePosition = position; //NOSONAR
                        queueManager.saveQueue(false); //NOSONAR
                        loadAttempt(attempt + 1, playWhenReady, seekPosition, completion); //NOSONAR
                    } // NOSONAR
                } else { //NOSONAR
                    if (completion != null) { //NOSONAR
                        completion.invoke(false); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
            return Unit.INSTANCE; //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    private void load(@NonNull Song song, Boolean playWhenReady, long seekPosition, @Nullable Function1<Boolean, Unit> completion) { //NOSONAR
        playback.load(song, playWhenReady, seekPosition, success -> { //NOSONAR
            if (success) { //NOSONAR
                if (playWhenReady) { //NOSONAR
                    musicServiceCallbacks.cancelShutdown(); //NOSONAR
                } // NOSONAR
                if (completion != null) { //NOSONAR
                    completion.invoke(true); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                stop(true); //NOSONAR
                if (completion != null) { //NOSONAR
                    completion.invoke(false); //NOSONAR
                } // NOSONAR
            } // NOSONAR
            return Unit.INSTANCE; //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    void loadFile(String path, Boolean playWhenReady) { //NOSONAR
        if (path == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Uri uri = Uri.parse(path); //NOSONAR
        long id = -1; //NOSONAR
        try { //NOSONAR
            id = Long.valueOf(uri.getLastPathSegment()); //NOSONAR
        } catch (NumberFormatException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        Function1<? super Song, Boolean> predicate; //NOSONAR

        long finalId = id; //NOSONAR
        if (finalId != -1 && (path.startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()) || path.startsWith(MediaStore.Files.getContentUri("external").toString()))) { //NOSONAR
            predicate = song -> song.id == finalId; //NOSONAR
        } else { //NOSONAR
            if (uri != null && path.startsWith("content://")) { //NOSONAR
                path = uri.getPath(); //NOSONAR
            } // NOSONAR
            String finalPath = path; //NOSONAR
            predicate = song -> song.path.contains(finalPath); //NOSONAR
        } // NOSONAR

        disposables.add(songsRepository.getSongs(predicate) //NOSONAR
                .firstOrError() //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(songs -> { //NOSONAR
                    if (!songs.isEmpty() && queueManager.getCurrentSong() != null) { //NOSONAR
                        load(queueManager.getCurrentSong(), playWhenReady, (long) 0, null); //NOSONAR
                    } // NOSONAR
                }, error -> LogUtils.logException(TAG, "Error opening file", error))); //NOSONAR
    } // NOSONAR

    private void restoreBookmark() { //NOSONAR
        // Go to bookmark if needed // NOSONAR
        if (queueManager.getCurrentSong() != null && queueManager.getCurrentSong().isPodcast) { //NOSONAR
            long bookmark = queueManager.getCurrentSong().bookMark; //NOSONAR
            // Start playing a little bit before the bookmark, so it's easier to get back in to the narrative. // NOSONAR
            seekTo(bookmark - 5000); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void setNextTrack() { //NOSONAR
        queueManager.nextPlayPos = getNextPosition(false); //NOSONAR
        if (queueManager.nextPlayPos >= 0 //NOSONAR
                && !queueManager.getCurrentPlaylist().isEmpty() //NOSONAR
                && queueManager.nextPlayPos < queueManager.getCurrentPlaylist().size()) { //NOSONAR
            final Song nextSong = queueManager.getCurrentPlaylist().get(queueManager.nextPlayPos).getSong(); //NOSONAR
            playback.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + nextSong.id); //NOSONAR
        } else { //NOSONAR
            playback.setNextDataSource(null); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void enqueue(List<Song> songs, int action) { //NOSONAR
        queueManager.enqueue( //NOSONAR
                songs, //NOSONAR
                action, //NOSONAR
                this::setNextTrack, //NOSONAR
                () -> load(true, true, 0)); //NOSONAR
    } // NOSONAR

    void saveState() { //NOSONAR
        if (playback.isInitialized()) { //NOSONAR
            playbackSettingsManager.setSeekPosition(playback.getPosition()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void release() { //NOSONAR
        if (isPlaying() || playback.willResumePlayback()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        mediaSessionManager.setActive(false); //NOSONAR
    } // NOSONAR

    void destroy() { //NOSONAR

        playback.stop(); //NOSONAR

        // Release all MediaPlayer resources, including the native player and wakelocks // NOSONAR
        playback.release(); //NOSONAR

        disposables.clear(); //NOSONAR

        mediaSessionManager.destroy(); //NOSONAR

        equalizer.release(); //NOSONAR
        equalizer.closeEqualizerSessions(true, getAudioSessionId()); //NOSONAR
    } // NOSONAR

    boolean recentlyPlayed() { //NOSONAR
        return isPlaying() || System.currentTimeMillis() - lastPlayedTime < 5 * 60 * 1000 /* 5 mins */; //NOSONAR
    } // NOSONAR

    int getAudioSessionId() { //NOSONAR
        return playback.getAudioSessionId(); //NOSONAR
    } // NOSONAR

    public void seekTo(long position) { //NOSONAR
        if (position < 0) { //NOSONAR
            position = 0; //NOSONAR
        } else if (position > playback.getDuration()) { //NOSONAR
            position = playback.getDuration(); //NOSONAR
        } // NOSONAR

        playback.seekTo(position); //NOSONAR

        notifyChange(InternalIntents.POSITION_CHANGED); //NOSONAR
    } // NOSONAR

    long getSeekPosition() { //NOSONAR
        return playback.getPosition(); //NOSONAR
    } // NOSONAR

    public void pause(boolean fade) { //NOSONAR
        if (isPlaying()) { //NOSONAR
            updateLastPlayedTime(); //NOSONAR
            saveBookmarkIfNeeded(); //NOSONAR
        } // NOSONAR
        playback.pause(fade); //NOSONAR
        equalizer.closeEqualizerSessions(false, getAudioSessionId()); //NOSONAR
        notifyChange(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
        musicServiceCallbacks.scheduleDelayedShutdown(); //NOSONAR
    } // NOSONAR

    private void updateLastPlayedTime() { //NOSONAR
        lastPlayedTime = System.currentTimeMillis(); //NOSONAR
    } // NOSONAR

    public boolean isPlaying() { //NOSONAR
        return playback.isPlaying(); //NOSONAR
    } // NOSONAR

    boolean willResumePlayback() { //NOSONAR
        return playback.willResumePlayback(); //NOSONAR
    } // NOSONAR

    public void stop(boolean goToIdle) { //NOSONAR

        if (isPlaying()) { //NOSONAR
            updateLastPlayedTime(); //NOSONAR
            saveBookmarkIfNeeded(); //NOSONAR
        } // NOSONAR

        playback.stop(); //NOSONAR

        if (goToIdle) { //NOSONAR
            musicServiceCallbacks.scheduleDelayedShutdown(); //NOSONAR
            lastPlayedTime = System.currentTimeMillis(); //NOSONAR
        } else { //NOSONAR
            musicServiceCallbacks.stopForegroundImpl(false, true); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @param force true to ignore the repeat mode. // NOSONAR
     * @return true if the we've successfully moved to the next track. // NOSONAR
     */ // NOSONAR
    public boolean next(boolean force) { //NOSONAR
        notifyChange(InternalIntents.TRACK_ENDING); //NOSONAR

        int nextPosition = getNextPosition(force); //NOSONAR
        if (nextPosition < 0) { //NOSONAR
            musicServiceCallbacks.scheduleDelayedShutdown(); //NOSONAR
            return false; //NOSONAR
        } // NOSONAR

        setQueuePosition(nextPosition); //NOSONAR
        return true; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @param force true to ignore the current seek position & repeat mode. // NOSONAR
     */ // NOSONAR
    public void previous(boolean force) { //NOSONAR
        if (force || getSeekPosition() <= 2000) { //NOSONAR
            queueManager.previous(); //NOSONAR
            stop(false); //NOSONAR
            load(false, true, 0); //NOSONAR
        } else { //NOSONAR
            seekTo(0); //NOSONAR
            play(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void play() { //NOSONAR
        if (settingsManager.getEqualizerEnabled()) { //NOSONAR
            //Shutdown any existing external audio sessions // NOSONAR
            equalizer.closeEqualizerSessions(false, getAudioSessionId()); //NOSONAR

            //Start internal equalizer session (will only turn on if enabled) // NOSONAR
            equalizer.openEqualizerSession(true, getAudioSessionId()); //NOSONAR
        } else { //NOSONAR
            equalizer.openEqualizerSession(false, getAudioSessionId()); //NOSONAR
        } // NOSONAR

        mediaSessionManager.setActive(true); //NOSONAR

        if (playback.isInitialized()) { //NOSONAR
            // If we are at the end of the song, go to the next song first // NOSONAR
            long duration = playback.getDuration(); //NOSONAR
            if (queueManager.repeatMode != QueueManager.RepeatMode.ONE && duration > 2000 && playback.getPosition() >= duration - 2000) { //NOSONAR
                next(true); //NOSONAR
            } else { //NOSONAR
                playback.start(); //NOSONAR
            } // NOSONAR

            musicServiceCallbacks.cancelShutdown(); //NOSONAR
            musicServiceCallbacks.updateNotification(); //NOSONAR
        } else if (queueManager.getCurrentPlaylist().isEmpty()) { //NOSONAR
            // This is mostly so that if you press 'play' on a bluetooth headset without ever having played anything before, it will still play something. // NOSONAR
            if (queueManager.queueReloading) { //NOSONAR
                playOnQueueReload = true; //NOSONAR
            } else { //NOSONAR
                playAutoShuffleList(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        notifyChange(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
    } // NOSONAR

    void togglePlayback() { //NOSONAR
        if (isPlaying()) { //NOSONAR
            pause(true); //NOSONAR
        } else { //NOSONAR
            play(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void saveBookmarkIfNeeded() { //NOSONAR
        Song currentSong = queueManager.getCurrentSong(); //NOSONAR
        if (currentSong != null && currentSong.isPodcast) { //NOSONAR
            long pos = getSeekPosition(); //NOSONAR
            long duration = queueManager.getCurrentSong().duration; //NOSONAR
            if (pos < 5000 || (pos + 5000) > duration) { //NOSONAR
                // If we're near the start or end, clear the bookmark // NOSONAR
                pos = 0; //NOSONAR
            } // NOSONAR

            currentSong.bookMark = pos; //NOSONAR

            try { //NOSONAR
                // Write 'pos' to the bookmark field // NOSONAR
                ContentValues values = new ContentValues(); //NOSONAR
                values.put(MediaStore.Audio.Media.BOOKMARK, pos); //NOSONAR
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, queueManager.getCurrentSong().id); //NOSONAR
                if (uri != null) { //NOSONAR
                    context.getContentResolver().update(uri, values, null, null); //NOSONAR
                } else { //NOSONAR
                    Log.e(TAG, "Save bookmark failed (uri null)"); //NOSONAR
                } // NOSONAR
            } catch (SQLiteException error) { //NOSONAR
                Log.e(TAG, "Save bookmark failed, error: " + error.getLocalizedMessage()); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onTrackEnded(@NonNull Playback playback, boolean trackDidChange) { //NOSONAR
        if (getPlayback() != playback) return; //NOSONAR

        notifyChange(InternalIntents.TRACK_ENDING); //NOSONAR

        if (pauseOnTrackFinish) { //NOSONAR
            pause(false); //NOSONAR
            pauseOnTrackFinish = false; //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (queueManager.repeatMode == QueueManager.RepeatMode.ONE) { //NOSONAR
            seekTo(0); //NOSONAR
            play(); //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (trackDidChange) { //NOSONAR
            queueManager.queuePosition = queueManager.nextPlayPos; //NOSONAR
            notifyChange(InternalIntents.META_CHANGED); //NOSONAR
            setNextTrack(); //NOSONAR
        } else { //NOSONAR
            if (!next(false)) { //NOSONAR
                // If we failed to move to the next track, then playback is complete. // NOSONAR
                notifyChange(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPlayStateChanged(@NonNull Playback playback) { //NOSONAR
        if (getPlayback() != playback) return; //NOSONAR

        notifyChange(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onError(@NonNull Playback playback, @NonNull String message) { //NOSONAR
        if (getPlayback() != playback) return; //NOSONAR

        if (isPlaying()) { //NOSONAR
            next(true); //NOSONAR
        } else { //NOSONAR
            load(true, false, 0); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void switchToPlayback(@NonNull Playback playback, long seekPosition) { //NOSONAR
        Playback oldPlayback = this.playback; //NOSONAR
        boolean wasPlaying = oldPlayback.isPlaying(); //NOSONAR

        this.playback = playback; //NOSONAR

        playback.setCallbacks(this); //NOSONAR
        playback.seekTo(seekPosition); //NOSONAR

        boolean playWhenReady = wasPlaying && playback.getResumeWhenSwitched(); //NOSONAR

        if (wasPlaying && !playWhenReady) { //NOSONAR
            // If we were playing, and now we're not, we need to update the playback state // NOSONAR
            notifyChange(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
        } // NOSONAR

        oldPlayback.stop(); //NOSONAR

        Song song = queueManager.getCurrentSong(); //NOSONAR
        if (song != null) { //NOSONAR
            playback.load(song, playWhenReady, seekPosition, null); //NOSONAR
        } else { //NOSONAR
            Log.e(TAG, "Current song null"); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
