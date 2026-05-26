package com.simplecity.amp_library.playback;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.model.Genre;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.ui.screens.queue.QueueItem;
import com.simplecity.amp_library.utils.AnalyticsManager;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.MusicServiceConnectionUtils;
import com.simplecity.amp_library.utils.SettingsManager;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MediaManager { //NOSONAR

    public interface Defs { //NOSONAR

        int ADD_TO_PLAYLIST = 0; //NOSONAR
        int PLAYLIST_SELECTED = 1; //NOSONAR
        int NEW_PLAYLIST = 2; //NOSONAR
    }

    private AnalyticsManager analyticsManager; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    public MediaManager(AnalyticsManager analyticsManager, SettingsManager settingsManager) { //NOSONAR
        this.analyticsManager = analyticsManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    private static final String TAG = "MediaManager"; //NOSONAR

    @NonNull //NOSONAR
    public Disposable playAll(@NonNull Single<List<Song>> songsSingle, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        return songsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> playAll(songs, 0, true, onEmpty), //NOSONAR
                        error -> LogUtils.logException(TAG, "playAll error", error) //NOSONAR
                );
    }

    public void playAll(@NonNull List<Song> songs, int position, boolean canClearShuffle, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playAll()"); //NOSONAR
        if (canClearShuffle && !settingsManager.getRememberShuffle()) { //NOSONAR
            setShuffleMode(QueueManager.ShuffleMode.OFF); //NOSONAR
        }

        if (songs.size() == 0 //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder == null //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR

            onEmpty.invoke(); //NOSONAR
            return; //NOSONAR
        }

        if (position < 0) { //NOSONAR
            position = 0; //NOSONAR
        }

        MusicServiceConnectionUtils.serviceBinder.getService().open(songs, position, true); //NOSONAR
    }

    @NonNull //NOSONAR
    public Disposable shuffleAll(@NonNull Single<List<Song>> songsSingle, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        return songsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> shuffleAll(songs, onEmpty), //NOSONAR
                        e -> LogUtils.logException(TAG, "Shuffle all error", e)); //NOSONAR
    }

    public void shuffleAll(@NotNull List<Song> songs, @NotNull Function0<Unit> onEmpty) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "shuffleAll()"); //NOSONAR
        setShuffleMode(QueueManager.ShuffleMode.ON); //NOSONAR
        if (!songs.isEmpty()) { //NOSONAR
            playAll(songs, new Random().nextInt(songs.size()), false, onEmpty); //NOSONAR
        }
    }

    public void playFile(final Uri uri) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, String.format("playFile(%s)", uri)); //NOSONAR
        if (uri == null //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder == null //NOSONAR
                || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        }

        // If this is a file:// URI, just use the path directly instead
        // of going through the open-from-filedescriptor codepath.
        String filename; //NOSONAR
        final String scheme = uri.getScheme(); //NOSONAR
        if ("file".equals(scheme)) { //NOSONAR
            filename = uri.getPath(); //NOSONAR
        } else { //NOSONAR
            filename = uri.toString(); //NOSONAR
        }

        MusicServiceConnectionUtils.serviceBinder.getService().stop(); //NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().openFile(filename, true); //NOSONAR
    }

    @Nullable //NOSONAR
    public String getFilePath() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            Song song = MusicServiceConnectionUtils.serviceBinder.getService().getSong(); //NOSONAR
            if (song != null) { //NOSONAR
                return song.path; //NOSONAR
            }
        }
        return null; //NOSONAR
    }

    public boolean isPlaying() { //NOSONAR
        return MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null && MusicServiceConnectionUtils.serviceBinder.getService() //NOSONAR
                .isPlaying(); //NOSONAR
    }

    public int getShuffleMode() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getShuffleMode(); //NOSONAR
        }
        return 0; //NOSONAR
    }

    public void setShuffleMode(int mode) { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().setShuffleMode(mode); //NOSONAR
        }
    }

    /**
     * @return The current repeat mode
     */
    public int getRepeatMode() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getRepeatMode(); //NOSONAR
        }
        return 0; //NOSONAR
    }

    /**
     * Changes to the next track
     */
    public void next() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "next()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().gotoNext(true); //NOSONAR
        }
    }

    /**
     * Changes to the previous track
     *
     * @param force if true, forces the player to move to the previous position
     */
    public void previous(boolean force) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "previous()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().previous(force); //NOSONAR
        }
    }

    /**
     * Play or pause the music depending on the current state.
     */
    public void togglePlayback() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playOrPause()"); //NOSONAR
        try { //NOSONAR
            if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
                MusicServiceConnectionUtils.serviceBinder.getService().togglePlayback(); //NOSONAR
            }
        } catch (final Exception ignored) { //NOSONAR
            // Intentionally left empty.
        }
    }

    public int getAudioSessionId() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getAudioSessionId(); //NOSONAR
        }
        return 0; //NOSONAR
    }

    /**
     * Note: This does not return a fully populated album artist.
     *
     * @return a partial {@link AlbumArtist} containing a partial {@link Album}
     * which contains the current song.
     */
    public AlbumArtist getAlbumArtist() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            if (getSong() != null) { //NOSONAR
                return getSong().getAlbumArtist(); //NOSONAR
            }
        }
        return null; //NOSONAR
    }

    /**
     * Note: This does not return a fully populated album.
     *
     * @return a partial {@link Album} containing this song.
     */
    public Album getAlbum() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            if (getSong() != null) { //NOSONAR
                return getSong().getAlbum(); //NOSONAR
            }
        }
        return null; //NOSONAR
    }

    @Nullable //NOSONAR
    public Song getSong() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getSong(); //NOSONAR
        }
        return null; //NOSONAR
    }

    @NonNull //NOSONAR
    public Single<Genre> getGenre(ShuttleApplication application) { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            if (getSong() != null) { //NOSONAR
                return getSong().getGenre(application); //NOSONAR
            }
        }
        return Single.error(new IllegalStateException("Genre not found")); //NOSONAR
    }

    public long getPosition() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            try { //NOSONAR
                return MusicServiceConnectionUtils.serviceBinder.getService().getSeekPosition(); //NOSONAR
            } catch (final Exception e) { //NOSONAR
                Log.e(TAG, "getPosition() returned error: " + e.toString()); //NOSONAR
            }
        }
        return 0; //NOSONAR
    }

    /**
     * Method duration.
     *
     * @return {@link long}
     */
    public long getDuration() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            Song song = MusicServiceConnectionUtils.serviceBinder.getService().getSong(); //NOSONAR
            if (song != null) { //NOSONAR
                return song.duration; //NOSONAR
            }
        }
        return 0; //NOSONAR
    }

    /**
     * Method seekTo.
     *
     * @param position the {@link long} position to seek to
     */
    public void seekTo(final long position) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "seekTo()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().seekTo(position); //NOSONAR
        }
    }

    public void moveQueueItem(final int from, final int to) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "moveQueueItem()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().moveQueueItem(from, to); //NOSONAR
        }
    }

    public void toggleShuffleMode() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "toggleShuffleMode()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        }
        MusicServiceConnectionUtils.serviceBinder.getService().toggleShuffleMode(); //NOSONAR
    }

    public void cycleRepeat() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "cycleRepeat()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        }
        MusicServiceConnectionUtils.serviceBinder.getService().toggleRepeat(); //NOSONAR
    }

    public void addToQueue(@NonNull List<Song> songs, @NotNull Function1<Integer, Unit> onAdded) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "addToQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        }
        MusicServiceConnectionUtils.serviceBinder.getService().enqueue(songs, QueueManager.EnqueueAction.LAST); //NOSONAR
        onAdded.invoke(songs.size()); //NOSONAR
    }

    @Nullable //NOSONAR
    public Disposable playNext(@NonNull Single<List<Song>> songsSingle, @NotNull Function1<Integer, Unit> onAdded) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playNext()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return null; //NOSONAR
        }
        return songsSingle //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> playNext(songs, onAdded), //NOSONAR
                        error -> LogUtils.logException(TAG, "playNext error", error) //NOSONAR
                );
    }

    public void playNext(@NonNull List<Song> songs, @NotNull Function1<Integer, Unit> onAdded) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "playNext()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        }
        MusicServiceConnectionUtils.serviceBinder.getService().enqueue(songs, QueueManager.EnqueueAction.NEXT); //NOSONAR
        onAdded.invoke(songs.size()); //NOSONAR
    }

    public void moveToNext(@NotNull QueueItem queueItem) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "moveToNext()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
            return; //NOSONAR
        }
        MusicServiceConnectionUtils.serviceBinder.getService().moveToNext(queueItem); //NOSONAR
    }

    public void setQueuePosition(final int position) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "setQueuePosition()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().setQueuePosition(position); //NOSONAR
        }
    }

    public void clearQueue() { //NOSONAR
        MusicServiceConnectionUtils.serviceBinder.getService().clearQueue(); //NOSONAR
    }

    @NonNull //NOSONAR
    public List<QueueItem> getQueue() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getQueue(); //NOSONAR
        }
        return new ArrayList<>(); //NOSONAR
    }

    public int getQueuePosition() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getQueuePosition(); //NOSONAR
        }
        return 0; //NOSONAR
    }

    public boolean getQueueReloading() { //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            return MusicServiceConnectionUtils.serviceBinder.getService().getQueueReloading(); //NOSONAR
        }
        return false; //NOSONAR
    }

    public void removeFromQueue(@NonNull QueueItem queueItem) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "removeFromQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().removeQueueItem(queueItem); //NOSONAR
        }
    }

    public void removeFromQueue(@NonNull List<QueueItem> queueItems) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "removeFromQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().removeQueueItems(queueItems); //NOSONAR
        }
    }

    public void removeSongsFromQueue(@NotNull List<Song> songs) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "removeSongsFromQueue()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().removeSongs(songs); //NOSONAR
        }
    }

    public void toggleFavorite() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "toggleFavorite()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().toggleFavorite(); //NOSONAR
        }
    }

    public void closeEqualizerSessions(boolean internal, int audioSessionId) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "closeEqualizerSessions()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().closeEqualizerSessions(internal, audioSessionId); //NOSONAR
        }
    }

    public void openEqualizerSession(boolean internal, int audioSessionId) { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "openEqualizerSession()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().openEqualizerSession(internal, audioSessionId); //NOSONAR
        }
    }

    public void updateEqualizer() { //NOSONAR
        analyticsManager.dropBreadcrumb(TAG, "updateEqualizer()"); //NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
            MusicServiceConnectionUtils.serviceBinder.getService().updateEqualizer(); //NOSONAR
        }
    }
}
