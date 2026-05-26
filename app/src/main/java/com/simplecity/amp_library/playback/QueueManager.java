package com.simplecity.amp_library.playback;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.constants.InternalIntents;
import com.simplecity.amp_library.rx.UnsafeAction;
import com.simplecity.amp_library.rx.UnsafeConsumer;
import com.simplecity.amp_library.ui.screens.queue.QueueItem;
import com.simplecity.amp_library.ui.screens.queue.QueueItemKt;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.SettingsManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QueueManager { //NOSONAR

    private static final String TAG = "QueueManager"; //NOSONAR

    public @interface ShuffleMode { //NOSONAR
        int OFF = 0; //NOSONAR
        int ON = 1; //NOSONAR
    }

    public @interface RepeatMode { //NOSONAR
        int OFF = 0; //NOSONAR
        int ONE = 1; //NOSONAR
        int ALL = 2; //NOSONAR
    }

    public @interface EnqueueAction { //NOSONAR
        int NEXT = 0; //NOSONAR
        int LAST = 1; //NOSONAR
    }

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
    ) {
        this.musicServiceCallbacks = musicServiceCallbacks; //NOSONAR
        this.songsRepository = songsRepository; //NOSONAR
        this.playbackSettingsManager = playbackSettingsManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    private void notifyQueueChanged() { //NOSONAR
        saveQueue(true); //NOSONAR
        musicServiceCallbacks.notifyChange(InternalIntents.QUEUE_CHANGED); //NOSONAR
    }

    private void notifyShuffleChanged() { //NOSONAR
        musicServiceCallbacks.notifyChange(InternalIntents.SHUFFLE_CHANGED); //NOSONAR
    }

    private void notifyMetaChanged() { //NOSONAR
        musicServiceCallbacks.notifyChange(InternalIntents.META_CHANGED); //NOSONAR
    }

    public void setRepeatMode(@RepeatMode int repeatMode) { //NOSONAR
        this.repeatMode = repeatMode; //NOSONAR
        saveQueue(false); //NOSONAR
    }

    void setShuffleMode(@ShuffleMode int shuffleMode) { //NOSONAR
        if (this.shuffleMode == shuffleMode && !getCurrentPlaylist().isEmpty()) { //NOSONAR
            return; //NOSONAR
        }
        if (shuffleMode == ShuffleMode.ON) { //NOSONAR
            makeShuffleList(); //NOSONAR
        }

        this.shuffleMode = shuffleMode; //NOSONAR
        notifyShuffleChanged(); //NOSONAR
        notifyQueueChanged(); //NOSONAR
        saveQueue(false); //NOSONAR
    }

    public void load(@NonNull List<Song> songs, final int position, @NonNull UnsafeAction openCurrentAndNext) { //NOSONAR

        List<QueueItem> queueItems = QueueItemKt.toQueueItems(songs); //NOSONAR

        if (!playlist.equals(queueItems)) { //NOSONAR
            playlist.clear(); //NOSONAR
            shuffleList.clear(); //NOSONAR

            playlist.addAll(queueItems); //NOSONAR
            QueueItemKt.updateOccurrence(playlist); //NOSONAR
        }

        queuePosition = position; //NOSONAR

        if (shuffleMode == QueueManager.ShuffleMode.ON) { //NOSONAR
            makeShuffleList(); //NOSONAR
        }

        openCurrentAndNext.run(); //NOSONAR

        notifyMetaChanged(); //NOSONAR
        notifyQueueChanged(); //NOSONAR
    }

    void previous() { //NOSONAR
        if (queuePosition > 0) { //NOSONAR
            queuePosition--; //NOSONAR
        } else { //NOSONAR
            queuePosition = getCurrentPlaylist().size() - 1; //NOSONAR
        }
    }

    void moveQueueItem(int from, int to) { //NOSONAR

        if (from >= getCurrentPlaylist().size()) { //NOSONAR
            from = getCurrentPlaylist().size() - 1; //NOSONAR
        }
        if (to >= getCurrentPlaylist().size()) { //NOSONAR
            to = getCurrentPlaylist().size() - 1; //NOSONAR
        }

        getCurrentPlaylist().add(to, getCurrentPlaylist().remove(from)); //NOSONAR

        if (from < to) { //NOSONAR
            if (queuePosition == from) { //NOSONAR
                queuePosition = to; //NOSONAR
            } else if (queuePosition >= from && queuePosition <= to) { //NOSONAR
                queuePosition--; //NOSONAR
            }
        } else if (to < from) { //NOSONAR
            if (queuePosition == from) { //NOSONAR
                queuePosition = to; //NOSONAR
            } else if (queuePosition >= to && queuePosition <= from) { //NOSONAR
                queuePosition++; //NOSONAR
            }
        }

        QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

        notifyQueueChanged(); //NOSONAR
    }

    void clearQueue() { //NOSONAR
        playlist.clear(); //NOSONAR
        shuffleList.clear(); //NOSONAR

        queuePosition = -1; //NOSONAR
        nextPlayPos = -1; //NOSONAR

        if (!settingsManager.getRememberShuffle()) { //NOSONAR
            setShuffleMode(ShuffleMode.OFF); //NOSONAR
        }

        notifyQueueChanged(); //NOSONAR
    }

    @NonNull //NOSONAR
    List<QueueItem> getCurrentPlaylist() { //NOSONAR
        if (shuffleMode == ShuffleMode.OFF) { //NOSONAR
            return playlist; //NOSONAR
        } else { //NOSONAR
            return shuffleList; //NOSONAR
        }
    }

    @Nullable //NOSONAR
    QueueItem getCurrentQueueItem() { //NOSONAR
        if (queuePosition >= 0 && queuePosition < getCurrentPlaylist().size()) { //NOSONAR
            return getCurrentPlaylist().get(queuePosition); //NOSONAR
        }

        return null; //NOSONAR
    }

    @Nullable //NOSONAR
    Song getCurrentSong() { //NOSONAR
        QueueItem currentQueueItem = getCurrentQueueItem(); //NOSONAR
        if (currentQueueItem != null) { //NOSONAR
            return currentQueueItem.getSong(); //NOSONAR
        } else { //NOSONAR
            return null; //NOSONAR
        }
    }

    /**
     * @return The next position to play, ot -1 if playback should complete.
     */
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
            }
        }
    }

    /**
     * Removes the first instance of the Song the playlist & shuffleList.
     */
    void removeQueueItem(QueueItem queueItem, UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR

        QueueItem currentQueueItem = getCurrentQueueItem(); //NOSONAR

        playlist.remove(queueItem); //NOSONAR
        shuffleList.remove(queueItem); //NOSONAR

        if (queueItem == currentQueueItem) { //NOSONAR
            onCurrentSongRemoved(stop, moveToNextTrack); //NOSONAR
        } else { //NOSONAR
            queuePosition = getCurrentPlaylist().indexOf(currentQueueItem); //NOSONAR
        }

        QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

        notifyQueueChanged(); //NOSONAR
    }

    /**
     * Removes the range of Songs specified from the playlist & shuffleList. If a Song
     * within the range is the file currently being played, playback will move
     * to the next Song after the range.
     *
     * @param queueItems the QueueItems to remove
     */
    void removeQueueItems(@NonNull List<QueueItem> queueItems, UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR

        playlist.removeAll(queueItems); //NOSONAR
        shuffleList.removeAll(queueItems); //NOSONAR

        QueueItemKt.updateOccurrence(getCurrentPlaylist()); //NOSONAR

        if (queueItems.contains(getCurrentQueueItem())) { //NOSONAR
            /*
             * If we remove a list of songs from the current queue, and that list contains our currently
             * playing song, we need to figure out which song should play next. We'll play the first song
             * that comes after the list of songs to be removed.
             *
             * In this example, let's say Song 7 is currently playing
             *
             * Playlist:                    [Song 3,    Song 4,     Song 5,     Song 6,     Song 7,     Song 8]
             * Indices:                     [0,         1,          2,          3,          4,          5]
             *
             * Remove;                                              [Song 5,     Song 6,     Song 7]
             *
             * First removed song:                                  Song 5
             * Index of first removed song:                         2
             *
             * Playlist after removal:      [Song 3,    Song 4,     Song 8]
             * Indices:                     [0,         1,          2]
             *
             *
             * So after the removal, we'll play index 2, which is Song 8.
             */
            queuePosition = Collections.indexOfSubList(getCurrentPlaylist(), queueItems); //NOSONAR
            onCurrentSongRemoved(stop, moveToNextTrack); //NOSONAR
        } else { //NOSONAR
            queuePosition = getCurrentPlaylist().indexOf(getCurrentQueueItem()); //NOSONAR
        }

        notifyQueueChanged(); //NOSONAR
    }

    void removeSongs(@NonNull List<Song> songs, UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR
        List<QueueItem> queueItems = Stream.of(playlist).filter(value -> songs.contains(value.getSong())).toList(); //NOSONAR
        removeQueueItems(queueItems, stop, moveToNextTrack); //NOSONAR
    }

    private void onCurrentSongRemoved(UnsafeAction stop, UnsafeAction moveToNextTrack) { //NOSONAR
        if (getCurrentPlaylist().isEmpty()) { //NOSONAR
            queuePosition = -1; //NOSONAR
            stop.run(); //NOSONAR
        } else { //NOSONAR
            if (queuePosition >= getCurrentPlaylist().size()) { //NOSONAR
                queuePosition = 0; //NOSONAR
            }
            moveToNextTrack.run(); //NOSONAR
        }
        notifyMetaChanged(); //NOSONAR
    }

    /**
     * Queues a new list for playback
     *
     * @param songs The list to queue
     * @param action The action to take
     */
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
        }
        if (queuePosition < 0) { //NOSONAR
            queuePosition = 0; //NOSONAR
            openCurrentAndNext.run(); //NOSONAR
            notifyMetaChanged(); //NOSONAR
        }
    }

    /**
     * Saves our state to preferences, including the queue position, repeat mode & shuffle mode.
     *
     * @param saveQueue boolean whether to serialize the playlist/shuffleList and store those in preferences
     * as well.
     */
    void saveQueue(boolean saveQueue) { //NOSONAR

        if (!queueIsSaveable) { //NOSONAR
            return; //NOSONAR
        }

        if (queueReloading) { //NOSONAR
            return; //NOSONAR
        }

        if (saveQueue) { //NOSONAR
            playbackSettingsManager.setQueueList(serializePlaylist(playlist)); //NOSONAR
            if (shuffleMode == ShuffleMode.ON) { //NOSONAR
                playbackSettingsManager.setShuffleList(serializePlaylist(shuffleList)); //NOSONAR
            }
        }

        playbackSettingsManager.setQueuePosition(queuePosition); //NOSONAR
        playbackSettingsManager.setRepeatMode(repeatMode); //NOSONAR
        playbackSettingsManager.setShuffleMode(shuffleMode); //NOSONAR
    }

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
                            // The saved playlist is bogus, discard it
                            playlist.clear(); //NOSONAR
                            queueReloading = false; //NOSONAR
                            onComplete.invoke(); //NOSONAR
                            return; //NOSONAR
                        }

                        QueueManager.this.queuePosition = queuePosition; //NOSONAR

                        if (repeatMode != RepeatMode.ALL && repeatMode != RepeatMode.ONE) { //NOSONAR
                            repeatMode = RepeatMode.OFF; //NOSONAR
                        }
                        if (shuffleMode != ShuffleMode.ON) { //NOSONAR
                            shuffleMode = ShuffleMode.OFF; //NOSONAR
                        }
                        if (shuffleMode == ShuffleMode.ON) { //NOSONAR
                            queueList = playbackSettingsManager.getShuffleList(); //NOSONAR
                            if (queueList != null) { //NOSONAR
                                shuffleList = deserializePlaylist(queueList, queueItems); //NOSONAR

                                if (queuePosition >= shuffleList.size()) { //NOSONAR
                                    // The saved playlist is bogus, discard it
                                    shuffleList.clear(); //NOSONAR
                                    queueReloading = false; //NOSONAR
                                    onComplete.invoke(); //NOSONAR
                                    return; //NOSONAR
                                }
                            }
                        }

                        if (QueueManager.this.queuePosition < 0 || QueueManager.this.queuePosition >= getCurrentPlaylist().size()) { //NOSONAR
                            QueueManager.this.queuePosition = 0; //NOSONAR
                        }
                    }
                    queueReloading = false; //NOSONAR
                    onComplete.invoke(); //NOSONAR
                }, error -> { //NOSONAR
                    queueReloading = false; //NOSONAR
                    onComplete.invoke(); //NOSONAR
                    LogUtils.logException(TAG, "Reloading queue", error); //NOSONAR
                });
    }

    /**
     * Converts a playlist to a String which can be saved to SharedPrefs
     */
    private String serializePlaylist(List<QueueItem> queueItems) { //NOSONAR

        // The current playlist is saved as a list of "reverse hexadecimal"
        // numbers, which we can generate faster than normal decimal or
        // hexadecimal numbers, which in turn allows us to save the playlist
        // more often without worrying too much about performance.

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
                    }
                    q.append(";"); //NOSONAR
                }
            }
        }

        return q.toString(); //NOSONAR
    }

    /**
     * Converts a string representation of a playlist from SharedPrefs into a list of songs.
     */
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
                    // bogus playlist data
                    playlist.clear(); //NOSONAR
                    break; //NOSONAR
                }
                shift += 4; //NOSONAR
            }
        }

        Map<Integer, Song> map = new TreeMap<>(); //NOSONAR

        Stream.of(queueItems).map(QueueItem::getSong).forEach(song -> { //NOSONAR
            int index = ids.indexOf(song.id); //NOSONAR
            if (index != -1) { //NOSONAR
                map.put(index, song); //NOSONAR
            }
        });

        return QueueItemKt.toQueueItems(new ArrayList<>(map.values())); //NOSONAR
    }

    void makeShuffleList() { //NOSONAR
        if (playlist.isEmpty()) { //NOSONAR
            return; //NOSONAR
        }

        shuffleList = new ArrayList<>(playlist); //NOSONAR
        QueueItem currentSong = null; //NOSONAR
        if (queuePosition >= 0 && queuePosition < shuffleList.size()) { //NOSONAR
            currentSong = shuffleList.remove(queuePosition); //NOSONAR
        }

        Collections.shuffle(shuffleList); //NOSONAR

        if (currentSong != null) { //NOSONAR
            shuffleList.add(0, currentSong); //NOSONAR
        }
        queuePosition = 0; //NOSONAR

        QueueItemKt.updateOccurrence(shuffleList); //NOSONAR
    }
}
