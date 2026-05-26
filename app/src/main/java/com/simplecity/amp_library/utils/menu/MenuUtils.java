package com.simplecity.amp_library.utils.menu;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.model.Genre;
import com.simplecity.amp_library.model.Playlist;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.MediaManager;
import com.simplecity.amp_library.rx.UnsafeConsumer;
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay;
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.playlists.PlaylistManager;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MenuUtils { //NOSONAR

    private static final String TAG = "MenuUtils"; //NOSONAR

    private MenuUtils() { //NOSONAR
        //no instance
    }

    // To do later: Remove context requirement
    public static void addToPlaylist(PlaylistManager playlistManager, Playlist playlist, List<Song> songs, Function1<Integer, Unit> insertCallback) { //NOSONAR
        playlistManager.addToPlaylist(playlist, songs, insertCallback); //NOSONAR
    }

    public static void addToQueue(MediaManager mediaManager, List<Song> songs, @NonNull UnsafeConsumer<Integer> onSongsAddedToQueue) { //NOSONAR
        mediaManager.addToQueue(songs, numSongs -> { //NOSONAR
            onSongsAddedToQueue.accept(numSongs); //NOSONAR
            return Unit.INSTANCE; //NOSONAR
        });
    }

    public static void whitelist(Repository.WhitelistRepository whitelistRepository, Song song) { //NOSONAR
        whitelistRepository.addSong(song); //NOSONAR
    }

    public static void whitelist(Repository.WhitelistRepository whitelistRepository, List<Song> songs) { //NOSONAR
        whitelistRepository.addAllSongs(songs); //NOSONAR
    }

    public static void blacklist(Repository.BlacklistRepository blacklistRepository, Song song) { //NOSONAR
        blacklistRepository.addSong(song); //NOSONAR
    }

    public static void blacklist(Repository.BlacklistRepository blacklistRepository, List<Song> songs) { //NOSONAR
        blacklistRepository.addAllSongs(songs); //NOSONAR
    }

    public static void play(MediaManager mediaManager, Single<List<Song>> observable, Function0<Unit> onPlaybackError) { //NOSONAR
        mediaManager.playAll(observable, () -> { //NOSONAR
            onPlaybackError.invoke(); //NOSONAR
            return Unit.INSTANCE; //NOSONAR
        });
    }

    @SuppressLint("CheckResult") //NOSONAR
    public static void whitelist(Repository.WhitelistRepository whitelistRepository, Single<List<Song>> single) { //NOSONAR
        single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> MenuUtils.whitelist(whitelistRepository, songs), //NOSONAR
                        throwable -> LogUtils.logException(TAG, "whitelist failed", throwable) //NOSONAR
                );
    }

    @SuppressLint("CheckResult") //NOSONAR
    public static void blacklist(Repository.BlacklistRepository blacklistRepository, Single<List<Song>> single) { //NOSONAR
        single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> MenuUtils.blacklist(blacklistRepository, songs), //NOSONAR
                        throwable -> LogUtils.logException(TAG, "blacklist failed", throwable) //NOSONAR
                );
    }

    @SuppressLint("CheckResult") //NOSONAR
    public static void goToArtist(Repository.AlbumArtistsRepository albumArtistsRepository, AlbumArtist currentAlbumArtist, NavigationEventRelay navigationEventRelay) { //NOSONAR
        // MediaManager.getAlbumArtist() is only populate with the album the current Song belongs to.
        // Let's find the matching AlbumArtist in the DataManager.albumArtistRelay
        albumArtistsRepository.getAlbumArtists() //NOSONAR
                .first(Collections.emptyList()) //NOSONAR
                .flatMapObservable(Observable::fromIterable) //NOSONAR
                .filter(albumArtist -> currentAlbumArtist != null && albumArtist.name.equals(currentAlbumArtist.name) && albumArtist.albums.containsAll(currentAlbumArtist.albums)) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        albumArtist -> navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.GO_TO_ARTIST, albumArtist, true)), //NOSONAR
                        error -> LogUtils.logException(TAG, "goToArtist error", error) //NOSONAR
                );
    }

    public static void goToAlbum(Album album, NavigationEventRelay navigationEventRelay) { //NOSONAR
        navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.GO_TO_ALBUM, album, true)); //NOSONAR
    }

    @SuppressLint("CheckResult") //NOSONAR
    public static void goToGenre(Single<Genre> genreSingle, NavigationEventRelay navigationEventRelay) { //NOSONAR
        genreSingle //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        (UnsafeConsumer<Genre>) genre -> navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.GO_TO_GENRE, genre, true)), //NOSONAR
                        error -> LogUtils.logException(TAG, "Error retrieving genre", error) //NOSONAR
                );
    }

    /**
     * To do later: Remove context requirement
     * Add the passed in songs to a new playlist. The 'create playlist dialog' will be presented to the user.
     *
     * @param single the songs to be added to the playlist
     */
    public static Disposable newPlaylist(Fragment fragment, Single<List<Song>> single) { //NOSONAR
        return single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> CreatePlaylistDialog.Companion.newInstance(songs).show(fragment.getChildFragmentManager(), "CreatePlaylistFragment"), //NOSONAR
                        throwable -> LogUtils.logException(TAG, "Error adding to new playlist", throwable) //NOSONAR
                );
    }

    /**
     * Adds the passed in songs to the queue, then calls onComplete with a message to be displayed
     * in a toast.
     *
     * @param single the songs to be added to the queue.
     */
    public static Disposable addToQueue( //NOSONAR
            MediaManager mediaManager, //NOSONAR
            Single<List<Song>> single, Function1<Integer, Unit> onSongsAddedToQueue) { //NOSONAR
        return single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> mediaManager.addToQueue(songs, numSongs -> { //NOSONAR
                            onSongsAddedToQueue.invoke(numSongs); //NOSONAR
                            return Unit.INSTANCE; //NOSONAR
                        }),
                        throwable -> LogUtils.logException(TAG, "Error adding to queue", throwable) //NOSONAR
                );
    }
}
