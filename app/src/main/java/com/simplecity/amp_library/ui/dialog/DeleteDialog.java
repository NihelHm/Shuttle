package com.simplecity.amp_library.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.provider.DocumentFile;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Supplier;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.MediaManager;
import com.simplecity.amp_library.saf.SafManager;
import com.simplecity.amp_library.sql.providers.PlayCountTable;
import com.simplecity.amp_library.utils.CustomMediaScanner;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.extensions.AlbumExtKt;
import com.simplecity.amp_library.utils.extensions.SongExtKt;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DeleteDialog extends DialogFragment implements SafManager.SafDialog.SafResultListener { //NOSONAR

    public @interface Type { //NOSONAR
        int ARTISTS = 0; //NOSONAR
        int ALBUMS = 1; //NOSONAR
        int SONGS = 2; //NOSONAR
    }

    private static final String TAG = "DeleteDialog"; //NOSONAR

    private static final String ARG_TYPE = "type"; //NOSONAR
    private static final String ARG_DELETE_MESSAGE_ID = "delete_message_id"; //NOSONAR

    private static final String ARG_ARTISTS = "artists"; //NOSONAR
    private static final String ARG_ALBUMS = "artists"; //NOSONAR
    private static final String ARG_SONGS = "songs"; //NOSONAR

    private static final String BUNDLE_SONGS_FOR_NORMAL_DELETION = "songs_for_normal_deletion"; //NOSONAR
    private static final String BUNDLE_SONGS_FOR_SAF_DELETION = "songs_for_saf_deletion"; //NOSONAR

    @Type //NOSONAR
    private int type; //NOSONAR

    @StringRes //NOSONAR
    private int deleteMessageId; //NOSONAR

    @Inject //NOSONAR
    MediaManager mediaManager; //NOSONAR

    @Inject //NOSONAR
    Repository.SongsRepository songsRepository; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    private List<AlbumArtist> artists; //NOSONAR
    private List<Album> albums; //NOSONAR
    private List<Song> songs; //NOSONAR

    private List<Song> songsForNormalDeletion = new ArrayList<>(); //NOSONAR
    private List<DocumentFile> documentFilesForDeletion = new ArrayList<>(); //NOSONAR
    private List<Song> songsForSafDeletion = new ArrayList<>(); //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    public interface ListArtistsRef extends Supplier<List<AlbumArtist>> { //NOSONAR
        // Intentionally left empty.
    }

    public static DeleteDialog newInstance(@NonNull ListArtistsRef artists) { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putInt(ARG_TYPE, Type.ARTISTS); //NOSONAR
        args.putInt(ARG_DELETE_MESSAGE_ID, artists.get().size() == 1 ? R.string.delete_album_artist_desc : R.string.delete_album_artist_desc_multiple); //NOSONAR
        args.putSerializable(ARG_ARTISTS, (Serializable) artists.get()); //NOSONAR
        DeleteDialog fragment = new DeleteDialog(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    public interface ListAlbumsRef extends Supplier<List<Album>> { //NOSONAR
        // Intentionally left empty.
    }

    public static DeleteDialog newInstance(@NonNull ListAlbumsRef albums) { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putInt(ARG_TYPE, Type.ALBUMS); //NOSONAR
        args.putInt(ARG_DELETE_MESSAGE_ID, albums.get().size() == 1 ? R.string.delete_album_desc : R.string.delete_album_desc_multiple); //NOSONAR
        args.putSerializable(ARG_ALBUMS, (Serializable) albums.get()); //NOSONAR
        DeleteDialog fragment = new DeleteDialog(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    public interface ListSongsRef extends Supplier<List<Song>> { //NOSONAR
        // Intentionally left empty.
    }

    public static DeleteDialog newInstance(@NonNull ListSongsRef songs) { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putInt(ARG_TYPE, Type.SONGS); //NOSONAR
        args.putInt(ARG_DELETE_MESSAGE_ID, songs.get().size() == 1 ? R.string.delete_song_desc : R.string.delete_song_desc_multiple); //NOSONAR
        args.putSerializable(ARG_SONGS, (Serializable) songs.get()); //NOSONAR
        DeleteDialog fragment = new DeleteDialog(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        deleteMessageId = getArguments().getInt(ARG_DELETE_MESSAGE_ID); //NOSONAR

        type = getArguments().getInt(ARG_TYPE); //NOSONAR
        switch (type) { //NOSONAR
            case Type.ARTISTS: //NOSONAR
                artists = (List<AlbumArtist>) getArguments().getSerializable(ARG_ARTISTS); //NOSONAR
                break; //NOSONAR
            case Type.ALBUMS: //NOSONAR
                albums = (List<Album>) getArguments().getSerializable(ARG_ALBUMS); //NOSONAR
                break; //NOSONAR
            case Type.SONGS: //NOSONAR
                songs = (List<Song>) getArguments().getSerializable(ARG_SONGS); //NOSONAR
                break; //NOSONAR
        }

        if (savedInstanceState != null) { //NOSONAR
            songsForNormalDeletion = (List<Song>) savedInstanceState.getSerializable(BUNDLE_SONGS_FOR_NORMAL_DELETION); //NOSONAR
            songsForSafDeletion = (List<Song>) savedInstanceState.getSerializable(BUNDLE_SONGS_FOR_SAF_DELETION); //NOSONAR
        }
    }

    @NonNull //NOSONAR
    @Override //NOSONAR
    public Dialog onCreateDialog(Bundle savedInstanceState) { //NOSONAR

        List<String> names = new ArrayList<>(); //NOSONAR
        switch (type) { //NOSONAR
            case Type.ARTISTS: //NOSONAR
                names = Stream.of(artists).map(albumArtist -> albumArtist.name).toList(); //NOSONAR
                break; //NOSONAR
            case Type.ALBUMS: //NOSONAR
                names = Stream.of(albums).map(album -> album.name).toList(); //NOSONAR
                break; //NOSONAR
            case Type.SONGS: //NOSONAR
                names = Stream.of(songs).map(song -> song.name).toList(); //NOSONAR
                break; //NOSONAR
        }

        String message; //NOSONAR
        if (names.isEmpty()) { //NOSONAR
            message = getString(R.string.delete_songs_unknown); //NOSONAR
        } else { //NOSONAR
            if (names.size() > 1) { //NOSONAR
                message = String.format(getString(deleteMessageId), Stream.of(names) //NOSONAR
                        .map(itemName -> "\n\u2022 " + itemName) //NOSONAR
                        .collect(Collectors.joining()) + "\n"); //NOSONAR
            } else { //NOSONAR
                message = String.format(getString(deleteMessageId), names.get(0)); //NOSONAR
            }
        }

        return new MaterialDialog.Builder(getContext()) //NOSONAR
                .iconRes(R.drawable.ic_warning_24dp) //NOSONAR
                .title(R.string.delete_item) //NOSONAR
                .content(message) //NOSONAR
                .positiveText(R.string.button_ok) //NOSONAR
                .onPositive((materialDialog, dialogAction) -> deleteSongsOrShowSafDialog()) //NOSONAR
                .negativeText(R.string.cancel) //NOSONAR
                .onNegative((materialDialog, dialogAction) -> dismiss()) //NOSONAR
                .autoDismiss(false) //NOSONAR
                .build(); //NOSONAR
    }

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        super.onPause(); //NOSONAR

        disposables.clear(); //NOSONAR
    }

    @Override //NOSONAR
    public void onSaveInstanceState(Bundle outState) { //NOSONAR
        outState.putSerializable(BUNDLE_SONGS_FOR_NORMAL_DELETION, (Serializable) songsForNormalDeletion); //NOSONAR
        outState.putSerializable(BUNDLE_SONGS_FOR_SAF_DELETION, (Serializable) songsForSafDeletion); //NOSONAR
        super.onSaveInstanceState(outState); //NOSONAR
    }

    @NonNull //NOSONAR
    Single<List<Song>> getSongs() { //NOSONAR
        switch (type) { //NOSONAR
            case Type.ARTISTS: //NOSONAR
                return Observable.fromIterable(artists) //NOSONAR
                        .flatMapSingle(albumArtist -> albumArtist.getSongsSingle(songsRepository)) //NOSONAR
                        .reduce(Collections.<Song>emptyList(), (songs, songs2) -> Stream.concat(Stream.of(songs), Stream.of(songs2)).toList()) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()); //NOSONAR
            case Type.ALBUMS: //NOSONAR
                return Observable.fromIterable(albums) //NOSONAR
                        .flatMapSingle(album -> AlbumExtKt.getSongsSingle(album, songsRepository)) //NOSONAR
                        .reduce(Collections.<Song>emptyList(), (songs, songs2) -> Stream.concat(Stream.of(songs), Stream.of(songs2)).toList()) //NOSONAR
                        .subscribeOn(Schedulers.io()) //NOSONAR
                        .observeOn(AndroidSchedulers.mainThread()); //NOSONAR
            case Type.SONGS: //NOSONAR
                return Single.just(songs); //NOSONAR
        }
        return Single.just(Collections.emptyList()); //NOSONAR
    }

    public void show(FragmentManager fragmentManager) { //NOSONAR
        show(fragmentManager, TAG); //NOSONAR
    }

    @SuppressLint("CheckResult") //NOSONAR
    void deleteSongsOrShowSafDialog() { //NOSONAR
        disposables.add(getSongs().map(songs -> { //NOSONAR
            // Keep track of the songs we want to delete, for later.
            Stream.of(songs).forEach(song -> { //NOSONAR
                if (SafManager.getInstance(getContext(), settingsManager).requiresPermission(new File(song.path))) { //NOSONAR
                    songsForSafDeletion.add(song); //NOSONAR
                } else { //NOSONAR
                    songsForNormalDeletion.add(song); //NOSONAR
                }
            });

            boolean requiresSafDialog = false; //NOSONAR
            if (!songsForSafDeletion.isEmpty()) { //NOSONAR
                // We're gonna need SAF access to delete some songs.
                // We may be able to build a list of document files if the user has been here before..
                List<DocumentFile> documentFiles = SafManager.getInstance(getContext(), settingsManager).getWriteableDocumentFiles(Stream.of(songsForSafDeletion) //NOSONAR
                        .map(song -> new File(song.path)) //NOSONAR
                        .toList()); //NOSONAR

                if (documentFiles.size() == songsForSafDeletion.size()) { //NOSONAR
                    // We have all the document files we need. No need to show SAF dialog.
                    this.documentFilesForDeletion.addAll(documentFiles); //NOSONAR
                } else { //NOSONAR
                    // We'll have to show the SAF dialog
                    requiresSafDialog = true; //NOSONAR
                }
            }
            return requiresSafDialog; //NOSONAR
        }).observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(requiresSafDialog -> { //NOSONAR
                    if (requiresSafDialog) { //NOSONAR
                        if (DeleteDialog.this.isAdded()) { //NOSONAR
                            SafManager.SafDialog.show(DeleteDialog.this); //NOSONAR
                        } else { //NOSONAR
                            LogUtils.logException(TAG, "Failed to delete songs.. Couldn't show SAFDialog", null); //NOSONAR
                            Toast.makeText(getContext(), getString(R.string.delete_songs_failure_toast), Toast.LENGTH_SHORT).show(); //NOSONAR
                        }
                    } else { //NOSONAR
                        disposables.add(deleteSongs() //NOSONAR
                                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                                .subscribeOn(Schedulers.io()) //NOSONAR
                                .subscribe(deletedSongs -> { //NOSONAR
                                    if (DeleteDialog.this.isAdded()) { //NOSONAR
                                        if (deletedSongs > 0) { //NOSONAR
                                            Toast.makeText(getContext(), getString(R.string.delete_songs_success_toast, deletedSongs), Toast.LENGTH_SHORT).show(); //NOSONAR
                                        } else { //NOSONAR
                                            Toast.makeText(getContext(), getString(R.string.delete_songs_failure_toast), Toast.LENGTH_SHORT).show(); //NOSONAR
                                        }
                                        dismiss(); //NOSONAR
                                    }
                                }, error -> { //NOSONAR
                                    LogUtils.logException(TAG, "Failed to delete songs", error); //NOSONAR
                                    if (DeleteDialog.this.isAdded()) { //NOSONAR
                                        Toast.makeText(getContext(), getString(R.string.delete_songs_failure_toast), Toast.LENGTH_SHORT).show(); //NOSONAR
                                    }
                                }));
                    }
                }, error -> LogUtils.logException(TAG, "Failed to delete songs", error))); //NOSONAR
    }

    @SuppressLint("CheckResult") //NOSONAR
    Single<Integer> deleteSongs() { //NOSONAR

        return Single.fromCallable(() -> { //NOSONAR
            int deletedSongs = 0; //NOSONAR
            if (!documentFilesForDeletion.isEmpty()) { //NOSONAR
                deletedSongs += Stream.of(documentFilesForDeletion).filter(DocumentFile::delete).count(); //NOSONAR
                tidyUp(songsForSafDeletion); //NOSONAR
                documentFilesForDeletion.clear(); //NOSONAR
                songsForSafDeletion.clear(); //NOSONAR
            }

            if (!songsForNormalDeletion.isEmpty()) { //NOSONAR
                deletedSongs += Stream.of(songsForNormalDeletion).filter(SongExtKt::delete).count(); //NOSONAR
                tidyUp(songsForNormalDeletion); //NOSONAR
                songsForNormalDeletion.clear(); //NOSONAR
            }
            return deletedSongs; //NOSONAR
        });
    }

    void tidyUp(@NonNull List<Song> deletedSongs) { //NOSONAR
        if (deletedSongs.isEmpty()) { //NOSONAR
            return; //NOSONAR
        }

        // Remove songs from current play queue
        mediaManager.removeSongsFromQueue(deletedSongs); //NOSONAR

        // Remove songs from play count table
        ArrayList<ContentProviderOperation> operations = Stream.of(deletedSongs).map(song -> ContentProviderOperation //NOSONAR
                .newDelete(PlayCountTable.URI) //NOSONAR
                .withSelection(PlayCountTable.COLUMN_ID + "=" + song.id, null) //NOSONAR
                .build()) //NOSONAR
                .collect(Collectors.toCollection(ArrayList::new)); //NOSONAR
        try { //NOSONAR
            getContext().getContentResolver().applyBatch(PlayCountTable.AUTHORITY, operations); //NOSONAR
        } catch (RemoteException | OperationApplicationException e) { //NOSONAR
            e.printStackTrace(); //NOSONAR
        }

        CustomMediaScanner.scanFiles(getContext(), Stream.of(deletedSongs) //NOSONAR
                .map(song -> song.path) //NOSONAR
                .toList(), null); //NOSONAR
    }

    @SuppressLint("CheckResult") //NOSONAR
    @Override //NOSONAR
    public void onResult(@Nullable Uri treeUri) { //NOSONAR
        if (treeUri != null) { //NOSONAR
            disposables.add(Completable.fromAction(() -> documentFilesForDeletion = SafManager.getInstance(getContext(), settingsManager).getWriteableDocumentFiles(Stream.of(songsForSafDeletion) //NOSONAR
                    .map(song -> new File(song.path)) //NOSONAR
                    .toList())) //NOSONAR
                    .andThen(deleteSongs()) //NOSONAR
                    .subscribeOn(Schedulers.io()) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe(deletedSongs -> { //NOSONAR
                        if (deletedSongs > 0) { //NOSONAR
                            Toast.makeText(getContext(), getString(R.string.delete_songs_success_toast, deletedSongs), Toast.LENGTH_SHORT).show(); //NOSONAR
                        } else { //NOSONAR
                            Toast.makeText(getContext(), getString(R.string.delete_songs_failure_toast), Toast.LENGTH_SHORT).show(); //NOSONAR
                        }
                        dismiss(); //NOSONAR
                    }, error -> LogUtils.logException(TAG, "Failed to delete songs", error))); //NOSONAR
        } else { //NOSONAR
            Toast.makeText(getContext(), R.string.delete_songs_failure_toast, Toast.LENGTH_LONG).show(); //NOSONAR
            dismiss(); //NOSONAR
        }
    }
}
