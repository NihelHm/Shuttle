package com.simplecity.amp_library.ui.screens.tagger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.utils.CustomMediaScanner;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.SettingsManager;
import dagger.android.support.AndroidSupportInjection;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TaggerDialog extends DialogFragment { //NOSONAR

    public static final String TAG = "TaggerDialog"; //NOSONAR

    public static final int DOCUMENT_TREE_REQUEST_CODE = 901; //NOSONAR

    public static final String ARG_MODEL = "model"; //NOSONAR

    private MaterialDialog materialDialog; //NOSONAR

    private boolean hasCheckedPermissions; //NOSONAR

    private AlbumArtist albumArtist; //NOSONAR
    private Album album; //NOSONAR
    private Song song; //NOSONAR

    List<String> originalSongPaths = new ArrayList<>(); //NOSONAR
    private List<DocumentFile> documentFiles = new ArrayList<>(); //NOSONAR

    private boolean showAlbum = true; //NOSONAR
    private boolean showTrack = true; //NOSONAR

    private EditText albumArtistEditText; //NOSONAR
    private EditText artistEditText; //NOSONAR
    private EditText albumEditText; //NOSONAR
    private EditText titleEditText; //NOSONAR
    private EditText genreEditText; //NOSONAR
    private EditText yearEditText; //NOSONAR
    private EditText trackEditText; //NOSONAR
    private EditText trackTotalEditText; //NOSONAR
    private EditText discEditText; //NOSONAR
    private EditText discTotalEditText; //NOSONAR
    private EditText lyricsEditText; //NOSONAR
    private EditText commentEditText; //NOSONAR

    private TextInputLayout albumInputLayout; //NOSONAR
    private TextInputLayout titleInputLayout; //NOSONAR
    private TextInputLayout trackInputLayout; //NOSONAR
    private TextInputLayout discInputLayout; //NOSONAR
    private TextInputLayout lyricsInputLayout; //NOSONAR
    private TextInputLayout commentInputLayout; //NOSONAR

    private String artistName; //NOSONAR
    private String albumName; //NOSONAR
    private String albumArtistName; //NOSONAR
    private String title; //NOSONAR
    private String genre; //NOSONAR
    private String year; //NOSONAR
    private String track; //NOSONAR
    private String trackTotal; //NOSONAR
    private String disc; //NOSONAR
    private String discTotal; //NOSONAR
    private String lyrics; //NOSONAR
    private String comment; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    public static TaggerDialog newInstance(Serializable model) { //NOSONAR

        Bundle args = new Bundle(); //NOSONAR
        args.putSerializable(ARG_MODEL, model); //NOSONAR
        TaggerDialog fragment = new TaggerDialog(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    @Override //NOSONAR
    public void onAttach(Context context) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onAttach(context); //NOSONAR
    }

    @Override //NOSONAR
    public void onCreate(@Nullable Bundle savedInstanceState) { //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        Serializable model = getArguments().getSerializable(ARG_MODEL); //NOSONAR
        if (model instanceof AlbumArtist) { //NOSONAR
            albumArtist = (AlbumArtist) model; //NOSONAR

            originalSongPaths = Stream.of(albumArtist.albums) //NOSONAR
                    .flatMap(value -> Stream.of(value.paths)) //NOSONAR
                    .toList(); //NOSONAR
            showAlbum = false; //NOSONAR
            showTrack = false; //NOSONAR
        } else if (model instanceof Album) { //NOSONAR
            album = (Album) model; //NOSONAR
            originalSongPaths = album.paths; //NOSONAR
            showTrack = false; //NOSONAR
        } else if (model instanceof Song) { //NOSONAR
            song = (Song) model; //NOSONAR
            originalSongPaths.add(song.path); //NOSONAR
        }

        if (originalSongPaths == null || originalSongPaths.isEmpty()) { //NOSONAR
            dismiss(); //NOSONAR

            //To do later: refine & extract
            Toast.makeText(getContext(), R.string.tag_retrieve_error, Toast.LENGTH_LONG).show(); //NOSONAR
        }
    }

    @Override //NOSONAR
    public Dialog onCreateDialog(Bundle savedInstanceState) { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tagger, null, false); //NOSONAR

        setupViews(customView); //NOSONAR

        populateViews(); //NOSONAR

        materialDialog = new MaterialDialog.Builder(getContext()) //NOSONAR
                .title(R.string.edit_tags) //NOSONAR
                .customView(customView, false) //NOSONAR
                .positiveText(R.string.save) //NOSONAR
                .onPositive((dialog, which) -> saveTags()) //NOSONAR
                .negativeText(R.string.close) //NOSONAR
                .onNegative((dialog, which) -> dismiss()) //NOSONAR
                .autoDismiss(false) //NOSONAR
                .build(); //NOSONAR

        return materialDialog; //NOSONAR
    }

    private void setupViews(View rootView) { //NOSONAR

        titleEditText = rootView.findViewById(R.id.new_track_name); //NOSONAR
        titleInputLayout = getParent(titleEditText); //NOSONAR

        albumEditText = rootView.findViewById(R.id.new_album_name); //NOSONAR
        albumInputLayout = getParent(albumEditText); //NOSONAR

        artistEditText = rootView.findViewById(R.id.new_artist_name); //NOSONAR

        albumArtistEditText = rootView.findViewById(R.id.new_album_artist_name); //NOSONAR

        genreEditText = rootView.findViewById(R.id.new_genre_name); //NOSONAR

        yearEditText = rootView.findViewById(R.id.new_year_number); //NOSONAR

        trackEditText = rootView.findViewById(R.id.new_track_number); //NOSONAR
        trackInputLayout = getParent(trackEditText); //NOSONAR

        trackTotalEditText = rootView.findViewById(R.id.new_track_total); //NOSONAR

        discEditText = rootView.findViewById(R.id.new_disc_number); //NOSONAR
        discInputLayout = getParent(discEditText); //NOSONAR

        discTotalEditText = rootView.findViewById(R.id.new_disc_total); //NOSONAR

        lyricsEditText = rootView.findViewById(R.id.new_lyrics); //NOSONAR
        lyricsInputLayout = getParent(lyricsEditText); //NOSONAR

        commentEditText = rootView.findViewById(R.id.new_comment); //NOSONAR
        commentInputLayout = getParent(commentEditText); //NOSONAR

        if (albumArtist != null || album != null) { //NOSONAR
            titleInputLayout.setVisibility(View.GONE); //NOSONAR
            titleEditText.setVisibility(View.GONE); //NOSONAR
            trackInputLayout.setVisibility(View.GONE); //NOSONAR
            trackEditText.setVisibility(View.GONE); //NOSONAR
            trackTotalEditText.setVisibility(View.GONE); //NOSONAR
            discInputLayout.setVisibility(View.GONE); //NOSONAR
            discEditText.setVisibility(View.GONE); //NOSONAR
            lyricsInputLayout.setVisibility(View.GONE); //NOSONAR
            lyricsEditText.setVisibility(View.GONE); //NOSONAR
            commentInputLayout.setVisibility(View.GONE); //NOSONAR
            commentEditText.setVisibility(View.GONE); //NOSONAR
        }

        if (albumArtist != null) { //NOSONAR
            albumInputLayout.setVisibility(View.GONE); //NOSONAR
            albumEditText.setVisibility(View.GONE); //NOSONAR
        }
    }

    void populateViews() { //NOSONAR

        if (originalSongPaths == null || originalSongPaths.isEmpty()) { //NOSONAR
            return; //NOSONAR
        }

        try { //NOSONAR
            AudioFile mAudioFile = AudioFileIO.read(new File(originalSongPaths.get(0))); //NOSONAR
            Tag tag = mAudioFile.getTag(); //NOSONAR

            if (tag == null) { //NOSONAR
                return; //NOSONAR
            }

            title = tag.getFirst(FieldKey.TITLE); //NOSONAR
            albumName = tag.getFirst(FieldKey.ALBUM); //NOSONAR
            artistName = tag.getFirst(FieldKey.ARTIST); //NOSONAR
            try { //NOSONAR
                albumArtistName = tag.getFirst(FieldKey.ALBUM_ARTIST); //NOSONAR
            } catch (UnsupportedOperationException ignored) { //NOSONAR
                // Intentionally left empty.
            }
            genre = tag.getFirst(FieldKey.GENRE); //NOSONAR
            year = tag.getFirst(FieldKey.YEAR); //NOSONAR
            track = tag.getFirst(FieldKey.TRACK); //NOSONAR
            try { //NOSONAR
                trackTotal = tag.getFirst(FieldKey.TRACK_TOTAL); //NOSONAR
            } catch (UnsupportedOperationException ignored) { //NOSONAR
                // Intentionally left empty.
            }
            try { //NOSONAR
                disc = tag.getFirst(FieldKey.DISC_NO); //NOSONAR
            } catch (UnsupportedOperationException ignored) { //NOSONAR
                // Intentionally left empty.
            }
            try { //NOSONAR
                discTotal = tag.getFirst(FieldKey.DISC_TOTAL); //NOSONAR
            } catch (UnsupportedOperationException ignored) { //NOSONAR
                // Intentionally left empty.
            }
            try { //NOSONAR
                lyrics = tag.getFirst(FieldKey.LYRICS); //NOSONAR
            } catch (UnsupportedOperationException ignored) { //NOSONAR
                // Intentionally left empty.
            }
            try { //NOSONAR
                comment = tag.getFirst(FieldKey.COMMENT); //NOSONAR
            } catch (UnsupportedOperationException ignored) { //NOSONAR
                // Intentionally left empty.
            }
        } catch (IOException | InvalidAudioFrameException | TagException | ReadOnlyFileException | CannotReadException e) { //NOSONAR
            Log.e(TAG, "Failed to read tags. " + e.toString()); //NOSONAR
        }

        titleEditText.setText(title); //NOSONAR
        titleEditText.setSelection(titleEditText.getText().length()); //NOSONAR

        albumEditText.setText(albumName); //NOSONAR
        albumEditText.setSelection(albumEditText.getText().length()); //NOSONAR

        artistEditText.setText(artistName); //NOSONAR
        artistEditText.setSelection(artistEditText.getText().length()); //NOSONAR

        albumArtistEditText.setText(albumArtistName); //NOSONAR
        albumArtistEditText.setSelection(albumArtistEditText.getText().length()); //NOSONAR

        genreEditText.setText(genre); //NOSONAR
        genreEditText.setSelection(genreEditText.getText().length()); //NOSONAR

        yearEditText.setText(String.valueOf(year)); //NOSONAR
        yearEditText.setSelection(yearEditText.getText().length()); //NOSONAR

        trackEditText.setText(String.valueOf(track)); //NOSONAR
        trackEditText.setSelection(trackEditText.getText().length()); //NOSONAR

        trackTotalEditText.setText(String.valueOf(trackTotal)); //NOSONAR
        trackTotalEditText.setSelection(trackTotalEditText.getText().length()); //NOSONAR

        discEditText.setText(String.valueOf(disc)); //NOSONAR
        discEditText.setSelection(discEditText.getText().length()); //NOSONAR

        discTotalEditText.setText(String.valueOf(discTotal)); //NOSONAR
        discTotalEditText.setSelection(discTotalEditText.getText().length()); //NOSONAR

        lyricsEditText.setText(lyrics); //NOSONAR
        lyricsEditText.setSelection(lyricsEditText.getText().length()); //NOSONAR

        commentEditText.setText(comment); //NOSONAR
        commentEditText.setSelection(commentEditText.getText().length()); //NOSONAR
    }

    private void saveTags() { //NOSONAR

        ProgressDialog progressDialog = new ProgressDialog(getContext()); //NOSONAR
        progressDialog.setMessage(getString(R.string.tag_editor_check_permission)); //NOSONAR
        progressDialog.setIndeterminate(true); //NOSONAR
        progressDialog.setCancelable(false); //NOSONAR
        progressDialog.show(); //NOSONAR

        CheckDocumentPermissionsTask task = new CheckDocumentPermissionsTask(getContext(), settingsManager, originalSongPaths, documentFiles, hasPermission -> { //NOSONAR

            if (isResumed() && progressDialog.isShowing()) { //NOSONAR
                progressDialog.dismiss(); //NOSONAR
            }

            if (!isResumed() || getContext() == null) { //NOSONAR
                LogUtils.logException(TAG, "Save tags returning early.. Context null or dialog not resumed.", null); //NOSONAR
                return; //NOSONAR
            }

            if (hasPermission) { //NOSONAR

                final ProgressDialog saveProgressDialog = new ProgressDialog(getContext()); //NOSONAR
                saveProgressDialog.setMessage(getResources().getString(R.string.saving_tags)); //NOSONAR
                saveProgressDialog.setMax(originalSongPaths.size()); //NOSONAR
                saveProgressDialog.setIndeterminate(false); //NOSONAR
                saveProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //NOSONAR
                saveProgressDialog.setCancelable(false); //NOSONAR
                saveProgressDialog.show(); //NOSONAR

                TaggerTask.TagCompletionListener listener = new TaggerTask.TagCompletionListener() { //NOSONAR
                    @Override //NOSONAR
                    public void onSuccess() { //NOSONAR

                        CustomMediaScanner.scanFiles(getContext(), originalSongPaths, null); //NOSONAR

                        if (getContext() != null && isResumed()) { //NOSONAR
                            saveProgressDialog.dismiss(); //NOSONAR

                            dismiss(); //NOSONAR
                        }
                    }

                    @Override //NOSONAR
                    public void onFailure() { //NOSONAR

                        if (getContext() != null && isResumed()) { //NOSONAR
                            saveProgressDialog.dismiss(); //NOSONAR
                            Toast.makeText(getContext(), R.string.tag_error, Toast.LENGTH_LONG).show(); //NOSONAR
                            dismiss(); //NOSONAR
                        }
                    }

                    @Override //NOSONAR
                    public void onProgress(int progress) { //NOSONAR
                        saveProgressDialog.setProgress(progress); //NOSONAR
                    }
                };

                TaggerTask taggerTask = new TaggerTask(getContext()) //NOSONAR
                        .showAlbum(showAlbum) //NOSONAR
                        .showTrack(showTrack) //NOSONAR
                        .setPaths(originalSongPaths) //NOSONAR
                        .setDocumentfiles(documentFiles) //NOSONAR
                        .title(titleEditText.getText().toString()) //NOSONAR
                        .album(albumEditText.getText().toString()) //NOSONAR
                        .artist(artistEditText.getText().toString()) //NOSONAR
                        .albumArtist(albumArtistEditText.getText().toString()) //NOSONAR
                        .year(yearEditText.getText().toString()) //NOSONAR
                        .track(trackEditText.getText().toString()) //NOSONAR
                        .trackTotal(trackTotalEditText.getText().toString()) //NOSONAR
                        .disc(discEditText.getText().toString()) //NOSONAR
                        .discTotal(discTotalEditText.getText().toString()) //NOSONAR
                        .lyrics(lyricsEditText.getText().toString()) //NOSONAR
                        .comment(commentEditText.getText().toString()) //NOSONAR
                        .genre(genreEditText.getText().toString()) //NOSONAR
                        .listener(listener) //NOSONAR
                        .build(); //NOSONAR
                taggerTask.execute(); //NOSONAR
            } else { //NOSONAR
                TaggerUtils.showChooseDocumentDialog(getContext(), (dialog1, which1) -> { //NOSONAR
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE); //NOSONAR
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) { //NOSONAR
                        this.startActivityForResult(intent, DOCUMENT_TREE_REQUEST_CODE); //NOSONAR
                    } else { //NOSONAR
                        Toast.makeText(getContext(), R.string.R_string_toast_no_document_provider, Toast.LENGTH_LONG).show(); //NOSONAR
                    }
                }, hasCheckedPermissions); //NOSONAR
                hasCheckedPermissions = true; //NOSONAR
            }
        });
        task.execute(); //NOSONAR
    }

    @Override //NOSONAR
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //NOSONAR
        super.onActivityResult(requestCode, resultCode, data); //NOSONAR
        switch (requestCode) { //NOSONAR
            case DOCUMENT_TREE_REQUEST_CODE: //NOSONAR
                if (resultCode == Activity.RESULT_OK) { //NOSONAR
                    Uri treeUri = data.getData(); //NOSONAR
                    getContext().getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION); //NOSONAR
                    settingsManager.setDocumentTreeUri(data.getData().toString()); //NOSONAR
                    saveTags(); //NOSONAR
                }
                break; //NOSONAR
        }
    }

    public void show(FragmentManager fragmentManager) { //NOSONAR
        show(fragmentManager, TAG); //NOSONAR
    }

    private TextInputLayout getParent(EditText editText) { //NOSONAR
        if (editText.getParent() instanceof TextInputLayout) { //NOSONAR
            return (TextInputLayout) editText.getParent(); //NOSONAR
        } else if (editText.getParent() instanceof FrameLayout) { //NOSONAR
            return (TextInputLayout) editText.getParent().getParent(); //NOSONAR
        }
        return null; //NOSONAR
    }
}
