package com.simplecity.amp_library.ui.screens.search.voice; // NOSONAR

import android.app.SearchManager; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.Intent; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.IBinder; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseActivity; // NOSONAR
import com.simplecity.amp_library.ui.screens.main.MainActivity; // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.extensions.AlbumExtKt; // NOSONAR
import dagger.android.AndroidInjection; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.Locale; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR
import kotlin.jvm.functions.Function1; // NOSONAR

import static com.simplecity.amp_library.utils.StringUtils.containsIgnoreCase; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class VoiceSearchActivity extends BaseActivity { //NOSONAR

    private static final String TAG = "VoiceSearchActivity"; //NOSONAR

    private String filterString; //NOSONAR

    private Intent intent; //NOSONAR

    private int position = -1; //NOSONAR

    @Inject //NOSONAR
    MediaManager mediaManager; //NOSONAR

    @Inject //NOSONAR
    Repository.SongsRepository songsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumsRepository albumsRepository; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumArtistsRepository albumArtistsRepository; //NOSONAR

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        intent = getIntent(); //NOSONAR

        filterString = intent.getStringExtra(SearchManager.QUERY); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onServiceConnected(ComponentName name, IBinder service) { //NOSONAR
        super.onServiceConnected(name, service); //NOSONAR
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.media.action.MEDIA_PLAY_FROM_SEARCH")) { //NOSONAR
            searchAndPlaySongs(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onServiceDisconnected(ComponentName name) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    private void searchAndPlaySongs() { //NOSONAR

        albumArtistsRepository.getAlbumArtists() //NOSONAR
                .first(Collections.emptyList()) //NOSONAR
                .flatMapObservable(Observable::fromIterable) //NOSONAR
                .filter(albumArtist -> albumArtist.name.toLowerCase(Locale.getDefault()).contains(filterString.toLowerCase())) //NOSONAR
                .flatMapSingle(albumArtist -> albumArtist.getSongsSingle(songsRepository)) //NOSONAR
                .map(songs -> { //NOSONAR
                    Collections.sort(songs, (a, b) -> a.getAlbumArtist().compareTo(b.getAlbumArtist())); //NOSONAR
                    Collections.sort(songs, (a, b) -> a.getAlbum().compareTo(b.getAlbum())); //NOSONAR
                    Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                    Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                    return songs; //NOSONAR
                }); // NOSONAR

        //Search for album-artists, albums & songs matching our filter. Then, create an Observable emitting List<Song> for each type of result. // NOSONAR
        //Then we concat the results, and return the first one which is non-empty. Order is important here, we want album-artist first, if it's // NOSONAR
        //available, then albums, then songs. // NOSONAR
        Observable.concat( //NOSONAR
                //If we have an album artist matching our query, then play the songs by that album artist // NOSONAR
                albumArtistsRepository.getAlbumArtists() //NOSONAR
                        .first(Collections.emptyList()) //NOSONAR
                        .flatMapObservable(Observable::fromIterable) //NOSONAR
                        .filter(albumArtist -> albumArtist.name.toLowerCase(Locale.getDefault()).contains(filterString.toLowerCase())) //NOSONAR
                        .flatMapSingle(albumArtist -> albumArtist.getSongsSingle(songsRepository)) //NOSONAR
                        .map(songs -> { //NOSONAR
                            Collections.sort(songs, (a, b) -> a.getAlbumArtist().compareTo(b.getAlbumArtist())); //NOSONAR
                            Collections.sort(songs, (a, b) -> a.getAlbum().compareTo(b.getAlbum())); //NOSONAR
                            Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                            Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                            return songs; //NOSONAR
                        }), // NOSONAR
                //If we have an album matching our query, then play the songs from that album // NOSONAR
                albumsRepository.getAlbums() //NOSONAR
                        .first(Collections.emptyList()) //NOSONAR
                        .flatMapObservable(Observable::fromIterable) //NOSONAR
                        .filter(album -> containsIgnoreCase(album.name, filterString) //NOSONAR
                                || containsIgnoreCase(album.name, filterString) //NOSONAR
                                || (Stream.of(album.artists).anyMatch(artist -> containsIgnoreCase(artist.name, filterString))) //NOSONAR
                                || containsIgnoreCase(album.albumArtistName, filterString)) //NOSONAR
                        .flatMapSingle(album -> AlbumExtKt.getSongsSingle(album, songsRepository)) //NOSONAR
                        .map(songs -> { //NOSONAR
                            Collections.sort(songs, (a, b) -> a.getAlbum().compareTo(b.getAlbum())); //NOSONAR
                            Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                            Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                            return songs; //NOSONAR
                        }), // NOSONAR
                //If have a song, play that song, as well as others from the same album. // NOSONAR
                songsRepository.getSongs((Function1<? super Song, Boolean>) null) //NOSONAR
                        .first(Collections.emptyList()) //NOSONAR
                        .flatMapObservable(Observable::fromIterable) //NOSONAR
                        .filter(song -> containsIgnoreCase(song.name, filterString) //NOSONAR
                                || containsIgnoreCase(song.albumName, filterString) //NOSONAR
                                || containsIgnoreCase(song.artistName, filterString) //NOSONAR
                                || containsIgnoreCase(song.albumArtistName, filterString)) //NOSONAR
                        .flatMapSingle(song -> AlbumExtKt.getSongsSingle(song.getAlbum(), songsRepository) //NOSONAR
                                .map(songs -> { //NOSONAR
                                    Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.track, b.track)); //NOSONAR
                                    Collections.sort(songs, (a, b) -> ComparisonUtils.compareInt(a.discNumber, b.discNumber)); //NOSONAR
                                    position = songs.indexOf(song); //NOSONAR
                                    return songs; //NOSONAR
                                })) // NOSONAR
        ) // NOSONAR
                .filter(songs -> !songs.isEmpty()) //NOSONAR
                .firstOrError() //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(songs -> { //NOSONAR
                    if (songs != null) { //NOSONAR
                        mediaManager.playAll(songs, position, true, () -> { //NOSONAR
                            // To do later: Show playback error toast // NOSONAR
                            return Unit.INSTANCE; //NOSONAR
                        }); // NOSONAR
                        startActivity(new Intent(this, MainActivity.class)); //NOSONAR
                    } // NOSONAR
                    finish(); //NOSONAR
                }, error -> { //NOSONAR
                    LogUtils.logException(TAG, "Error attempting to playAll()", error); //NOSONAR
                    startActivity(new Intent(this, MainActivity.class)); //NOSONAR
                    finish(); //NOSONAR
                }); // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR
} // NOSONAR
