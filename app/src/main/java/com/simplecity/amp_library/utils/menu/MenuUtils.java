package com.simplecity.amp_library.utils.menu; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.model.AlbumArtist; // NOSONAR
import com.simplecity.amp_library.model.Genre; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeConsumer; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.playlists.PlaylistManager; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function0; // NOSONAR
import kotlin.jvm.functions.Function1; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MenuUtils { //NOSONAR

    private static final String TAG = "MenuUtils"; //NOSONAR

    private MenuUtils() { //NOSONAR
        //no instance // NOSONAR
    } // NOSONAR

    // To do later: Remove context requirement // NOSONAR
    public static void addToPlaylist(PlaylistManager playlistManager, Playlist playlist, List<Song> songs, Function1<Integer, Unit> insertCallback) { //NOSONAR
        playlistManager.addToPlaylist(playlist, songs, insertCallback); //NOSONAR
    } // NOSONAR

    public static void addToQueue(MediaManager mediaManager, List<Song> songs, @NonNull UnsafeConsumer<Integer> onSongsAddedToQueue) { //NOSONAR
        mediaManager.addToQueue(songs, numSongs -> { //NOSONAR
            onSongsAddedToQueue.accept(numSongs); //NOSONAR
            return Unit.INSTANCE; //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    public static void whitelist(Repository.WhitelistRepository whitelistRepository, Song song) { //NOSONAR
        whitelistRepository.addSong(song); //NOSONAR
    } // NOSONAR

    public static void whitelist(Repository.WhitelistRepository whitelistRepository, List<Song> songs) { //NOSONAR
        whitelistRepository.addAllSongs(songs); //NOSONAR
    } // NOSONAR

    public static void blacklist(Repository.BlacklistRepository blacklistRepository, Song song) { //NOSONAR
        blacklistRepository.addSong(song); //NOSONAR
    } // NOSONAR

    public static void blacklist(Repository.BlacklistRepository blacklistRepository, List<Song> songs) { //NOSONAR
        blacklistRepository.addAllSongs(songs); //NOSONAR
    } // NOSONAR

    public static void play(MediaManager mediaManager, Single<List<Song>> observable, Function0<Unit> onPlaybackError) { //NOSONAR
        mediaManager.playAll(observable, () -> { //NOSONAR
            onPlaybackError.invoke(); //NOSONAR
            return Unit.INSTANCE; //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    public static void whitelist(Repository.WhitelistRepository whitelistRepository, Single<List<Song>> single) { //NOSONAR
        single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> MenuUtils.whitelist(whitelistRepository, songs), //NOSONAR
                        throwable -> LogUtils.logException(TAG, "whitelist failed", throwable) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    public static void blacklist(Repository.BlacklistRepository blacklistRepository, Single<List<Song>> single) { //NOSONAR
        single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> MenuUtils.blacklist(blacklistRepository, songs), //NOSONAR
                        throwable -> LogUtils.logException(TAG, "blacklist failed", throwable) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    public static void goToArtist(Repository.AlbumArtistsRepository albumArtistsRepository, AlbumArtist currentAlbumArtist, NavigationEventRelay navigationEventRelay) { //NOSONAR
        // MediaManager.getAlbumArtist() is only populate with the album the current Song belongs to. // NOSONAR
        // Let's find the matching AlbumArtist in the DataManager.albumArtistRelay // NOSONAR
        albumArtistsRepository.getAlbumArtists() //NOSONAR
                .first(Collections.emptyList()) //NOSONAR
                .flatMapObservable(Observable::fromIterable) //NOSONAR
                .filter(albumArtist -> currentAlbumArtist != null && albumArtist.name.equals(currentAlbumArtist.name) && albumArtist.albums.containsAll(currentAlbumArtist.albums)) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        albumArtist -> navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.GO_TO_ARTIST, albumArtist, true)), //NOSONAR
                        error -> LogUtils.logException(TAG, "goToArtist error", error) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    public static void goToAlbum(Album album, NavigationEventRelay navigationEventRelay) { //NOSONAR
        navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.GO_TO_ALBUM, album, true)); //NOSONAR
    } // NOSONAR

    @SuppressLint("CheckResult") //NOSONAR
    public static void goToGenre(Single<Genre> genreSingle, NavigationEventRelay navigationEventRelay) { //NOSONAR
        genreSingle //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        (UnsafeConsumer<Genre>) genre -> navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.GO_TO_GENRE, genre, true)), //NOSONAR
                        error -> LogUtils.logException(TAG, "Error retrieving genre", error) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * To do later: Remove context requirement // NOSONAR
     * Add the passed in songs to a new playlist. The 'create playlist dialog' will be presented to the user. // NOSONAR
     * // NOSONAR
     * @param single the songs to be added to the playlist // NOSONAR
     */ // NOSONAR
    public static Disposable newPlaylist(Fragment fragment, Single<List<Song>> single) { //NOSONAR
        return single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> CreatePlaylistDialog.Companion.newInstance(songs).show(fragment.getChildFragmentManager(), "CreatePlaylistFragment"), //NOSONAR
                        throwable -> LogUtils.logException(TAG, "Error adding to new playlist", throwable) //NOSONAR
                ); // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Adds the passed in songs to the queue, then calls onComplete with a message to be displayed // NOSONAR
     * in a toast. // NOSONAR
     * // NOSONAR
     * @param single the songs to be added to the queue. // NOSONAR
     */ // NOSONAR
    public static Disposable addToQueue( //NOSONAR
            MediaManager mediaManager, //NOSONAR
            Single<List<Song>> single, Function1<Integer, Unit> onSongsAddedToQueue) { //NOSONAR
        return single.observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe( //NOSONAR
                        songs -> mediaManager.addToQueue(songs, numSongs -> { //NOSONAR
                            onSongsAddedToQueue.invoke(numSongs); //NOSONAR
                            return Unit.INSTANCE; //NOSONAR
                        }), // NOSONAR
                        throwable -> LogUtils.logException(TAG, "Error adding to queue", throwable) //NOSONAR
                ); // NOSONAR
    } // NOSONAR
} // NOSONAR
