package com.simplecity.amp_library.playback; // NOSONAR

import android.net.Uri; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.util.Log; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist; // NOSONAR
import com.simplecity.amp_library.model.Genre; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItem; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.MusicServiceConnectionUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR
import java.util.Random; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function0; // NOSONAR
import kotlin.jvm.functions.Function1; // NOSONAR
import org.jetbrains.annotations.NotNull; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MediaManager { //NOSONAR

    public interface Defs { //NOSONAR

        int ADD_TO_PLAYLIST = 0; //NOSONAR
        int PLAYLIST_SELECTED = 1; //NOSONAR
        int NEW_PLAYLIST = 2; //NOSONAR
    } // NOSONAR

    private AnalyticsManager analyticsManager; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    public MediaManager(AnalyticsManager analyticsManager, SettingsManager settingsManager) { //NOSONAR
        this.analyticsManager = analyticsManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    private static final String TAG = "MediaManager"; //NOSONAR

    @NonNull //NOSONAR
    public Disposable playAll(@NonNull Single<List<Song>> songsSingle, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        return songsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> playAll(songs, 0, true, onEmpty), //NOSONAR
                        error -> LogUtils.logException(TAG, "playAll error", error) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    public void playAll(@NonNull List<Song> songs, int position, boolean canClearShuffle, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playAll()"); //NOSONAR
        if (canClearShuffle && !settingsManager.getRememberShuffle()) { //NOSONAR
            setShuffleMode(QueueManager.ShuffleMode.OFF); //NOSONAR
        } // NOSONAR

        if (songs.size() == 0 //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder == null //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR

            onEmpty.invoke(); //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (position < 0) { //NOSONAR
            position = 0; //NOSONAR
        } // NOSONAR

        MusicServiceConnectionUtils.serviceBinder.getService().open(songs, position, true); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    public Disposable shuffleAll(@NonNull Single<List<Song>> songsSingle, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        return songsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> shuffleAll(songs, onEmpty), //NOSONAR
                        e -> LogUtils.logException(TAG, "Shuffle all error", e)); //NOSONAR
    } // NOSONAR

    public void shuffleAll(@NotNull List<Song> songs, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "shuffleAll()"); //NOSONAR
        setShuffleMode(QueueManager.ShuffleMode.ON); //NOSONAR
        if (!songs.isEmpty()) { //NOSONAR
            playAll(songs, new Random().nextInt(songs.size()), false, onEmpty); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void playFile(final Uri uri) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, String.format("playFile(%s)", uri)); //NOSONAR
        if (uri == null //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder == null //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        // If this is a file:// URI, just use the path directly instead // NOSONAR
        // of going through the open-from-filedescriptor codepath. // NOSONAR
        String filename; //NOSONAR
        final String scheme = uri.getScheme(); //NOSONAR
        if ("file".equals(scheme)) { //NOSONAR
            filename = uri.getPath(); //NOSONAR
        } else { //NOSONAR
            filename = uri.toString(); //NOSONAR
        } // NOSONAR

        MusicServiceConnectionUtils.serviceBinder.getService().stop(); //NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().openFile(filename, true); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    public String getFilePath() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            Song song = MusicServiceConnectionUtils.serviceBinder.getService().getSong(); //NOSONAR
            if (song != null) { //NOSONAR
                return song.path; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    public boolean isPlaying() { //NOSONAR
        return MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null && MusicServiceConnectionUtils.serviceBinder.getService() //NOSONAR
                .isPlaying(); //NOSONAR
    } // NOSONAR

    public int getShuffleMode() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getShuffleMode(); //NOSONAR
        } // NOSONAR
        return 0; //NOSONAR
    } // NOSONAR

    public void setShuffleMode(int mode) { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().setShuffleMode(mode); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return The current repeat mode // NOSONAR
     */ // NOSONAR
    public int getRepeatMode() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getRepeatMode(); //NOSONAR
        } // NOSONAR
        return 0; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Changes to the next track // NOSONAR
     */ // NOSONAR
    public void next() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "next()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().gotoNext(true); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Changes to the previous track // NOSONAR
     * // NOSONAR
     * @param force if true, forces the player to move to the previous position // NOSONAR
     */ // NOSONAR
    public void previous(boolean force) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "previous()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().previous(force); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Play or pause the music depending on the current state. // NOSONAR
     */ // NOSONAR
    public void togglePlayback() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playOrPause()"); //NOSONAR
        try { //NOSONAR
            if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
                MusicServiceConnectionUtils.serviceBinder.getService().togglePlayback(); //NOSONAR
            } // NOSONAR
        } catch (final Exception ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
    } // NOSONAR

    public int getAudioSessionId() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getAudioSessionId(); //NOSONAR
        } // NOSONAR
        return 0; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Note: This does not return a fully populated album artist. // NOSONAR
     * // NOSONAR
     * @return a partial {@link AlbumArtist} containing a partial {@link Album} // NOSONAR
     * which contains the current song. // NOSONAR
     */ // NOSONAR
    public AlbumArtist getAlbumArtist() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            if (getSong() != null) { //NOSONAR
                return getSong().getAlbumArtist(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Note: This does not return a fully populated album. // NOSONAR
     * // NOSONAR
     * @return a partial {@link Album} containing this song. // NOSONAR
     */ // NOSONAR
    public Album getAlbum() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            if (getSong() != null) { //NOSONAR
                return getSong().getAlbum(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    public Song getSong() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getSong(); //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    public Single<Genre> getGenre(ShuttleApplication application) { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            if (getSong() != null) { //NOSONAR
                return getSong().getGenre(application); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return Single.error(new IllegalStateException("Genre not found")); //NOSONAR
    } // NOSONAR

    public long getPosition() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            try { //NOSONAR
                return MusicServiceConnectionUtils.serviceBinder.getService().getSeekPosition(); //NOSONAR
            } catch (final Exception e) { //NOSONAR
                Log.e(TAG, "getPosition() returned error: " + e.toString()); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return 0; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Method duration. // NOSONAR
     * // NOSONAR
     * @return {@link long} // NOSONAR
     */ // NOSONAR
    public long getDuration() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            Song song = MusicServiceConnectionUtils.serviceBinder.getService().getSong(); //NOSONAR
            if (song != null) { //NOSONAR
                return song.duration; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return 0; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Method seekTo. // NOSONAR
     * // NOSONAR
     * @param position the {@link long} position to seek to // NOSONAR
     */ // NOSONAR
    public void seekTo(final long position) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "seekTo()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().seekTo(position); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void moveQueueItem(final int from, final int to) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "moveQueueItem()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().moveQueueItem(from, to); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void toggleShuffleMode() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "toggleShuffleMode()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().toggleShuffleMode(); //NOSONAR
    } // NOSONAR

    public void cycleRepeat() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "cycleRepeat()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().toggleRepeat(); //NOSONAR
    } // NOSONAR

    public void addToQueue(@NonNull List<Song> songs, @NotNull Function1<Integer, Unit> onAdded) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "addToQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().enqueue(songs, QueueManager.EnqueueAction.LAST); //NOSONAR
        onAdded.invoke(songs.size()); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    public Disposable playNext(@NonNull Single<List<Song>> songsSingle, @NotNull Function1<Integer, Unit> onAdded) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playNext()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
        return songsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> playNext(songs, onAdded), //NOSONAR
                        error -> LogUtils.logException(TAG, "playNext error", error) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    public void playNext(@NonNull List<Song> songs, @NotNull Function1<Integer, Unit> onAdded) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playNext()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().enqueue(songs, QueueManager.EnqueueAction.NEXT); //NOSONAR
        onAdded.invoke(songs.size()); //NOSONAR
    } // NOSONAR

    public void moveToNext(@NotNull QueueItem queueItem) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "moveToNext()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().moveToNext(queueItem); //NOSONAR
    } // NOSONAR

    public void setQueuePosition(final int position) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "setQueuePosition()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().setQueuePosition(position); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void clearQueue() { //NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().clearQueue(); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    public List<QueueItem> getQueue() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getQueue(); //NOSONAR
        } // NOSONAR
        return new ArrayList<>(); //NOSONAR
    } // NOSONAR

    public int getQueuePosition() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getQueuePosition(); //NOSONAR
        } // NOSONAR
        return 0; //NOSONAR
    } // NOSONAR

    public boolean getQueueReloading() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getQueueReloading(); //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    public void removeFromQueue(@NonNull QueueItem queueItem) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "removeFromQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().removeQueueItem(queueItem); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void removeFromQueue(@NonNull List<QueueItem> queueItems) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "removeFromQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().removeQueueItems(queueItems); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void removeSongsFromQueue(@NotNull List<Song> songs) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "removeSongsFromQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().removeSongs(songs); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void toggleFavorite() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "toggleFavorite()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().toggleFavorite(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void closeEqualizerSessions(boolean internal, int audioSessionId) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "closeEqualizerSessions()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().closeEqualizerSessions(internal, audioSessionId); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void openEqualizerSession(boolean internal, int audioSessionId) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "openEqualizerSession()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().openEqualizerSession(internal, audioSessionId); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void updateEqualizer() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "updateEqualizer()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().updateEqualizer(); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
