package com.simplecity.amp_library.playback; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeConsumer; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItem; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueItemKt; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import java.util.Map; // NOSONAR
import java.util.TreeMap; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function0; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QueueManager { //NOSONAR

    private static final String TAG = "QueueManager"; //NOSONAR

    public @interface ShuffleMode { //NOSONAR
        int OFF = 0; //NOSONAR
        int ON = 1; //NOSONAR
    } // NOSONAR

    public @interface RepeatMode { //NOSONAR
        int OFF = 0; //NOSONAR
        int ONE = 1; //NOSONAR
        int ALL = 2; //NOSONAR
    } // NOSONAR

    public @interface EnqueueAction { //NOSONAR
        int NEXT = 0; //NOSONAR
        int LAST = 1; //NOSONAR
    } // NOSONAR

    private final char hexDigits[] = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; //NOSONAR

    @NonNull //NOSONAR
    List<QueueItem> playlist = new ArrayList<>(); //NOSONAR

    @NonNull //NOSONAR
    List<QueueItem> shuffleList = new ArrayList<>(); //NOSONAR

    @ShuffleMode //NOSONAR
    int shuffleMode = ShuffleMode.OFF; //NOSONAR

    @RepeatMode //NOSONAR
    int repeatMode = RepeatMode.OFF; //NOSONAR

    boolean queueReloading; //NOSONAR

    boolean queueIsSaveable = true; //NOSONAR

    int queuePosition = -1; //NOSONAR
    int nextPlayPos = -1; //NOSONAR

    private MusicService.Callbacks musicServiceCallbacks; //NOSONAR

    private Repository.SongsRepository songsRepository; //NOSONAR

    private PlaybackSettingsManager playbackSettingsManager; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    public QueueManager( //NOSONAR
            MusicService.Callbacks musicServiceCallbacks, //NOSONAR
            Repository.SongsRepository songsRepository, //NOSONAR
            PlaybackSettingsManager playbackSettingsManager, //NOSONAR
            SettingsManager settingsManager //NOSONAR
    ) { // NOSONAR
        this.musicServiceCallbacks = musicServiceCallbacks; //NOSONAR
        this.songsRepository = songsRepository; //NOSONAR
        this.playbackSettingsManager = playbackSettingsManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    private void notifyQueueChanged() { //NOSONAR
        saveQueue(true); //NOSONAR
        musicServiceCallbacks.notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
    } // NOSONAR

    private void notifyShuffleChanged() { //NOSONAR
        musicServiceCallbacks.notifyChange(InternalIntents.SHUFFLE_CHANGED); //NOSONAR
    } // NOSONAR

    private void notifyMetaChanged() { //NOSONAR
        musicServiceCallbacks.notifyChange(InternalIntents.META_CHANGED); //NOSONAR
    } // NOSONAR

    public void setRepeatMode(@RepeatMode int repeatMode) { //NOSONAR
        this.repeatMode = repeatMode; //NOSONAR
        saveQueue(false); //NOSONAR
    } // NOSONAR

    void setShuffleMode(@ShuffleMode int shuffleMode) { //NOSONAR
        if (this.shuffleMode == shuffleMode && !getCurrentPlaylist().isEmpty()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (shuffleMode == ShuffleMode.ON) { //NOSONAR
            makeShuffleList(); //NOSONAR
        } // NOSONAR

        this.shuffleMode = shuffleMode; //NOSONAR
        notifyShuffleChanged(); //NOSONAR
        notifyQueueChanged(); //NOSONAR
        saveQueue(false); //NOSONAR
    } // NOSONAR

    public void load(@NonNull List<Song> songs, final int position, @NonNull UnsafeAction openCurrentAndNext) { //NOSONAR

        List<QueueItem> queueItems = QueueItemKt.toQueueItems(songs); //NOSONAR

        if (!playlist.equals(queueItems)) { //NOSONAR
            playlist.clear(); //NOSONAR
            shuffleList.clear(); //NOSONAR

            playlist.addAll(queueItems); //NOSONAR
            QueueItemKt.updateOccurrence(playlist); //NOSONAR
        } // NOSONAR

        queuePosition = position; //NOSONAR

        if (shuffleMode == QueueManager.ShuffleMode.ON) { //NOSONAR
            makeShuffleList(); //NOSONAR
        } // NOSONAR

        openCurrentAndNext.run(); //NOSONAR

        notifyMetaChanged(); //NOSONAR
        notifyQueueChanged(); //NOSONAR
    } // NOSONAR

    void previous() { //NOSONAR
        if (queuePosition > 0) { //NOSONAR
            queuePosition--; //NOSONAR
        } else { //NOSONAR
            queuePosition = getCurrentPlaylist().size() - 1; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void moveQueueItem(int from, int to) { //NOSONAR

        if (from >= getCurrentPlaylist().size()) { //NOSONAR
            from = getCurrentPlaylist().size() - 1; //NOSONAR
        } // NOSONAR
        if (to >= getCurrentPlaylist().size()) { //NOSONAR
            to = getCurrentPlaylist().size() - 1; //NOSONAR
        } // NOSONAR

        getCurrentPlaylist().add(to, getCurrentPlaylist().remove(from)); //NOSONAR

        if (from < to) { //NOSONAR
            if (queuePosition == from) { //NOSONAR
                queuePosition = to; //NOSONAR
            } else if (queuePosition >= from && queuePosition <= to) { //NOSONAR
                queuePosition--; //NOSONAR
            } // NOSONAR
        } else if (to < from) { //NOSONAR
            if (queuePosition == from) { //NOSONAR
                queuePosition = to; //NOSONAR
            } else if (queuePosition >= to && queuePosition <= from) { //NOSONAR
                queuePosition++; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

        notifyQueueChanged(); //NOSONAR
    } // NOSONAR

    void clearQueue() { //NOSONAR
        playlist.clear(); //NOSONAR
        shuffleList.clear(); //NOSONAR

        queuePosition = -1; //NOSONAR
        nextPlayPos = -1; //NOSONAR

        if (!settingsManager.getRememberShuffle()) { //NOSONAR
            setShuffleMode(ShuffleMode.OFF); //NOSONAR
        } // NOSONAR

        notifyQueueChanged(); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    List<QueueItem> getCurrentPlaylist() { //NOSONAR
        if (shuffleMode == ShuffleMode.OFF) { //NOSONAR
            return playlist; //NOSONAR
        } else { //NOSONAR
            return shuffleList; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    QueueItem getCurrentQueueItem() { //NOSONAR
        if (queuePosition >= 0 && queuePosition < getCurrentPlaylist().size()) { //NOSONAR
            return getCurrentPlaylist().get(queuePosition); //NOSONAR
        } // NOSONAR

        return null; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    Song getCurrentSong() { //NOSONAR
        QueueItem currentQueueItem = getCurrentQueueItem(); //NOSONAR
        if (currentQueueItem != null) { //NOSONAR
            return currentQueueItem.getSong(); //NOSONAR
        } else { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return The next position to play, ot -1 if playback should complete. // NOSONAR
     */ // NOSONAR
    int getNextPosition(boolean ignoreRepeatMode) { //NOSONAR
        boolean queueComplete = queuePosition >= getCurrentPlaylist().size() - 1; //NOSONAR

        if (ignoreRepeatMode) { //NOSONAR
            return queueComplete ? 0 : queuePosition + 1; //NOSONAR
        } else { //NOSONAR
            switch (repeatMode) { //NOSONAR
                case RepeatMode.ONE: //NOSONAR
                    return queuePosition < 0 ? 0 : queuePosition; //NOSONAR
                case RepeatMode.OFF: //NOSONAR
                    return queueComplete ? -1 : queuePosition + 1; //NOSONAR
                case RepeatMode.ALL: //NOSONAR
                    return queueComplete ? 0 : queuePosition + 1; //NOSONAR
                default: //NOSONAR
                    return -1; //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Removes the first instance of the Song the playlist & shuffleList. // NOSONAR
     */ // NOSONAR
    void removeQueueItem(QueueItem queueItem, UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR

        QueueItem currentQueueItem = getCurrentQueueItem(); //NOSONAR

        playlist.remove(queueItem); //NOSONAR
        shuffleList.remove(queueItem); //NOSONAR

        if (queueItem == currentQueueItem) { //NOSONAR
            onCurrentSongRemoved(stop, moveToNextTrack); //NOSONAR
        } else { //NOSONAR
            queuePosition = getCurrentPlaylist().indexOf(currentQueueItem); //NOSONAR
        } // NOSONAR

        QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

        notifyQueueChanged(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Removes the range of Songs specified from the playlist & shuffleList. If a Song // NOSONAR
     * within the range is the file currently being played, playback will move // NOSONAR
     * to the next Song after the range. // NOSONAR
     * // NOSONAR
     * @param queueItems the QueueItems to remove // NOSONAR
     */ // NOSONAR
    void removeQueueItems(@NonNull List<QueueItem> queueItems, UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR

        playlist.removeAll(queueItems); //NOSONAR
        shuffleList.removeAll(queueItems); //NOSONAR

        QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

        if (queueItems.contains(getCurrentQueueItem())) { //NOSONAR
            /* // NOSONAR
             * If we remove a list of songs from the current queue, and that list contains our currently // NOSONAR
             * playing song, we need to figure out which song should play next. We'll play the first song // NOSONAR
             * that comes after the list of songs to be removed. // NOSONAR
             * // NOSONAR
             * In this example, let's say Song 7 is currently playing // NOSONAR
             * // NOSONAR
             * Playlist:                    [Song 3,    Song 4,     Song 5,     Song 6,     Song 7,     Song 8] // NOSONAR
             * Indices:                     [0,         1,          2,          3,          4,          5] // NOSONAR
             * // NOSONAR
             * Remove;                                              [Song 5,     Song 6,     Song 7] // NOSONAR
             * // NOSONAR
             * First removed song:                                  Song 5 // NOSONAR
             * Index of first removed song:                         2 // NOSONAR
             * // NOSONAR
             * Playlist after removal:      [Song 3,    Song 4,     Song 8] // NOSONAR
             * Indices:                     [0,         1,          2] // NOSONAR
             * // NOSONAR
             * // NOSONAR
             * So after the removal, we'll play index 2, which is Song 8. // NOSONAR
             */ // NOSONAR
            queuePosition = Collections.indexOfSubList(getCurrentPlaylist(), queueItems); //NOSONAR
            onCurrentSongRemoved(stop, moveToNextTrack); //NOSONAR
        } else { //NOSONAR
            queuePosition = getCurrentPlaylist().indexOf(getCurrentQueueItem()); //NOSONAR
        } // NOSONAR

        notifyQueueChanged(); //NOSONAR
    } // NOSONAR

    void removeSongs(@NonNull List<Song> songs, UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR
        List<QueueItem> queueItems = Stream.of(playlist).filter(value -> songs.contains(value.getSong())).toList(); //NOSONAR
        removeQueueItems(queueItems, stop, moveToNextTrack); //NOSONAR
    } // NOSONAR

    private void onCurrentSongRemoved(UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR
        if (getCurrentPlaylist().isEmpty()) { //NOSONAR
            queuePosition = -1; //NOSONAR
            stop.run(); //NOSONAR
        } else { //NOSONAR
            if (queuePosition >= getCurrentPlaylist().size()) { //NOSONAR
                queuePosition = 0; //NOSONAR
            } // NOSONAR
            moveToNextTrack.run(); //NOSONAR
        } // NOSONAR
        notifyMetaChanged(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Queues a new list for playback // NOSONAR
     * // NOSONAR
     * @param songs The list to queue // NOSONAR
     * @param action The action to take // NOSONAR
     */ // NOSONAR
    public void enqueue(List<Song> songs, @EnqueueAction int action, UnsafeAction setNextTrack, UnsafeAction openCurrentAndNext) { //NOSONAR

        List<QueueItem> queueItems = QueueItemKt.toQueueItems(songs); //NOSONAR

        switch (action) { //NOSONAR
            case EnqueueAction.NEXT: //NOSONAR
                List<QueueItem> otherList = getCurrentPlaylist() == playlist ? shuffleList : playlist; //NOSONAR
                getCurrentPlaylist().addAll(queuePosition + 1, queueItems); //NOSONAR
                otherList.addAll(queueItems); //NOSONAR

                QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

                setNextTrack.run(); //NOSONAR
                notifyQueueChanged(); //NOSONAR
                break; //NOSONAR
            case EnqueueAction.LAST: //NOSONAR
                playlist.addAll(queueItems); //NOSONAR
                shuffleList.addAll(queueItems); //NOSONAR

                QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

                notifyQueueChanged(); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
        if (queuePosition < 0) { //NOSONAR
            queuePosition = 0; //NOSONAR
            openCurrentAndNext.run(); //NOSONAR
            notifyMetaChanged(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Saves our state to preferences, including the queue position, repeat mode & shuffle mode. // NOSONAR
     * // NOSONAR
     * @param saveQueue boolean whether to serialize the playlist/shuffleList and store those in preferences // NOSONAR
     * as well. // NOSONAR
     */ // NOSONAR
    void saveQueue(boolean saveQueue) { //NOSONAR

        if (!queueIsSaveable) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (queueReloading) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (saveQueue) { //NOSONAR
            playbackSettingsManager.setQueueList(serializePlaylist(playlist)); //NOSONAR
            if (shuffleMode == ShuffleMode.ON) { //NOSONAR
                playbackSettingsManager.setShuffleList(serializePlaylist(shuffleList)); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        playbackSettingsManager.setQueuePosition(queuePosition); //NOSONAR
        playbackSettingsManager.setRepeatMode(repeatMode); //NOSONAR
        playbackSettingsManager.setShuffleMode(shuffleMode); //NOSONAR
    } // NOSONAR

    Disposable reloadQueue(@NonNull Function0<Unit> onComplete) { //NOSONAR
        queueReloading = true; //NOSONAR

        shuffleMode = playbackSettingsManager.getShuffleMode(); //NOSONAR
        repeatMode = playbackSettingsManager.getRepeatMode(); //NOSONAR

        return songsRepository.getAllSongs() //NOSONAR
                .first(Collections.emptyList()) //NOSONAR
                .map(QueueItemKt::toQueueItems) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe((UnsafeConsumer<List<QueueItem>>) queueItems -> { //NOSONAR
                    String queueList = playbackSettingsManager.getQueueList(); //NOSONAR
                    if (queueList != null) { //NOSONAR
                        playlist = deserializePlaylist(queueList, queueItems); //NOSONAR

                        final int queuePosition = playbackSettingsManager.getQueuePosition(); //NOSONAR

                        if (queuePosition < 0 || queuePosition >= playlist.size()) { //NOSONAR
                            // The saved playlist is bogus, discard it // NOSONAR
                            playlist.clear(); //NOSONAR
                            queueReloading = false; //NOSONAR
                            onComplete.invoke(); //NOSONAR
                            return; //NOSONAR
                        } // NOSONAR

                        QueueManager.this.queuePosition = queuePosition; //NOSONAR

                        if (repeatMode != RepeatMode.ALL && repeatMode != RepeatMode.ONE) { //NOSONAR
                            repeatMode = RepeatMode.OFF; //NOSONAR
                        } // NOSONAR
                        if (shuffleMode != ShuffleMode.ON) { //NOSONAR
                            shuffleMode = ShuffleMode.OFF; //NOSONAR
                        } // NOSONAR
                        if (shuffleMode == ShuffleMode.ON) { //NOSONAR
                            queueList = playbackSettingsManager.getShuffleList(); //NOSONAR
                            if (queueList != null) { //NOSONAR
                                shuffleList = deserializePlaylist(queueList, queueItems); //NOSONAR

                                if (queuePosition >= shuffleList.size()) { //NOSONAR
                                    // The saved playlist is bogus, discard it // NOSONAR
                                    shuffleList.clear(); //NOSONAR
                                    queueReloading = false; //NOSONAR
                                    onComplete.invoke(); //NOSONAR
                                    return; //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                        } // NOSONAR

                        if (QueueManager.this.queuePosition < 0 || QueueManager.this.queuePosition >= getCurrentPlaylist().size()) { //NOSONAR
                            QueueManager.this.queuePosition = 0; //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                    queueReloading = false; //NOSONAR
                    onComplete.invoke(); //NOSONAR
                }, error -> { //NOSONAR
                    queueReloading = false; //NOSONAR
                    onComplete.invoke(); //NOSONAR
                    LogUtils.logException(TAG, "Reloading queue", error); //NOSONAR
                }); // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Converts a playlist to a String which can be saved to SharedPrefs // NOSONAR
     */ // NOSONAR
    private String serializePlaylist(List<QueueItem> queueItems) { //NOSONAR

        // The current playlist is saved as a list of "reverse hexadecimal" // NOSONAR
        // numbers, which we can generate faster than normal decimal or // NOSONAR
        // hexadecimal numbers, which in turn allows us to save the playlist // NOSONAR
        // more often without worrying too much about performance. // NOSONAR

        StringBuilder q = new StringBuilder(); //NOSONAR

        List<Song> songs = Stream.of(queueItems).map(QueueItem::getSong).toList(); //NOSONAR
        int len = songs.size(); //NOSONAR
        for (int i = 0; i < len; i++) { //NOSONAR
            long n = songs.get(i).id; //NOSONAR
            if (n >= 0) { //NOSONAR
                if (n == 0) { //NOSONAR
                    q.append("0;"); //NOSONAR
                } else { //NOSONAR
                    while (n != 0) { //NOSONAR
                        final int digit = (int) (n & 0xf); //NOSONAR
                        n >>>= 4; //NOSONAR
                        q.append(hexDigits[digit]); //NOSONAR
                    } // NOSONAR
                    q.append(";"); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        return q.toString(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Converts a string representation of a playlist from SharedPrefs into a list of songs. // NOSONAR
     */ // NOSONAR
    private List<QueueItem> deserializePlaylist(String listString, List<QueueItem> queueItems) { //NOSONAR
        List<Long> ids = new ArrayList<>(); //NOSONAR
        int n = 0; //NOSONAR
        int shift = 0; //NOSONAR
        for (int i = 0; i < listString.length(); i++) { //NOSONAR
            char c = listString.charAt(i); //NOSONAR
            if (c == ';') { //NOSONAR
                ids.add((long) n); //NOSONAR
                n = 0; //NOSONAR
                shift = 0; //NOSONAR
            } else { //NOSONAR
                if (c >= '0' && c <= '9') { //NOSONAR
                    n += ((c - '0') << shift); //NOSONAR
                } else if (c >= 'a' && c <= 'f') { //NOSONAR
                    n += ((10 + c - 'a') << shift); //NOSONAR
                } else { //NOSONAR
                    // bogus playlist data // NOSONAR
                    playlist.clear(); //NOSONAR
                    break; //NOSONAR
                } // NOSONAR
                shift += 4; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        Map<Integer, Song> map = new TreeMap<>(); //NOSONAR

        Stream.of(queueItems).map(QueueItem::getSong).forEach(song -> { //NOSONAR
            int index = ids.indexOf(song.id); //NOSONAR
            if (index != -1) { //NOSONAR
                map.put(index, song); //NOSONAR
            } // NOSONAR
        }); // NOSONAR

        return QueueItemKt.toQueueItems(new ArrayList<>(map.values())); //NOSONAR
    } // NOSONAR

    void makeShuffleList() { //NOSONAR
        if (playlist.isEmpty()) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        shuffleList = new ArrayList<>(playlist); //NOSONAR
        QueueItem currentSong = null; //NOSONAR
        if (queuePosition >= 0 && queuePosition < shuffleList.size()) { //NOSONAR
            currentSong = shuffleList.remove(queuePosition); //NOSONAR
        } // NOSONAR

        Collections.shuffle(shuffleList); //NOSONAR

        if (currentSong != null) { //NOSONAR
            shuffleList.add(0, currentSong); //NOSONAR
        } // NOSONAR
        queuePosition = 0; //NOSONAR

        QueueItemKt.updateOccurrence(shuffleList); //NOSONAR
    } // NOSONAR
} // NOSONAR
