package com.simplecity.amp_library.ui.screens.tagger; // NOSONAR

import android.content.Context; // NOSONAR
import android.os.AsyncTask; // NOSONAR
import android.os.ParcelFileDescriptor; // NOSONAR
import android.support.v4.provider.DocumentFile; // NOSONAR
import com.simplecity.amp_library.model.TagUpdate; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import java.io.File; // NOSONAR
import java.io.FileOutputStream; // NOSONAR
import java.io.IOException; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR
import org.jaudiotagger.audio.AudioFile; // NOSONAR
import org.jaudiotagger.audio.AudioFileIO; // NOSONAR
import org.jaudiotagger.audio.exceptions.CannotReadException; // NOSONAR
import org.jaudiotagger.audio.exceptions.CannotWriteException; // NOSONAR
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException; // NOSONAR
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException; // NOSONAR
import org.jaudiotagger.tag.Tag; // NOSONAR
import org.jaudiotagger.tag.TagException; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TaggerTask extends AsyncTask<Object, Integer, Boolean> { //NOSONAR

    public interface TagCompletionListener { //NOSONAR
        void onSuccess(); //NOSONAR

        void onFailure(); //NOSONAR

        void onProgress(int progress); //NOSONAR
    } // NOSONAR

    private Context applicationContext; //NOSONAR

    private TagCompletionListener tagCompletionListener; //NOSONAR

    private boolean showAlbum; //NOSONAR
    private boolean showTrack; //NOSONAR

    private List<String> paths; //NOSONAR
    private List<DocumentFile> documentFiles; //NOSONAR
    private List<File> tempFiles = new ArrayList<>(); //NOSONAR

    private String titleText; //NOSONAR
    private String albumText; //NOSONAR
    private String artistText; //NOSONAR
    private String albumArtistText; //NOSONAR
    private String yearText; //NOSONAR
    private String trackText; //NOSONAR
    private String trackTotalText; //NOSONAR
    private String discText; //NOSONAR
    private String discTotalText; //NOSONAR
    private String lyricsText; //NOSONAR
    private String commentText; //NOSONAR
    private String genreText; //NOSONAR

    public TaggerTask(Context context) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
    } // NOSONAR

    public TaggerTask(Context context, boolean showAlbum, boolean showTrack, List<String> paths, //NOSONAR
            List<DocumentFile> documentFiles, String titleText, String albumText, //NOSONAR
            String artistText, String albumArtistText, String yearText, String trackText, //NOSONAR
            String trackTotalText, String discText, String discTotalText, String lyricsText, //NOSONAR
            String commentText, String genreText, //NOSONAR
            TagCompletionListener listener) { //NOSONAR

        this.applicationContext = context.getApplicationContext(); //NOSONAR
        this.showAlbum = showAlbum; //NOSONAR
        this.showTrack = showTrack; //NOSONAR
        this.paths = paths; //NOSONAR
        this.documentFiles = documentFiles; //NOSONAR
        this.titleText = titleText; //NOSONAR
        this.albumText = albumText; //NOSONAR
        this.artistText = artistText; //NOSONAR
        this.albumArtistText = albumArtistText; //NOSONAR
        this.yearText = yearText; //NOSONAR
        this.trackText = trackText; //NOSONAR
        this.trackTotalText = trackTotalText; //NOSONAR
        this.discText = discText; //NOSONAR
        this.discTotalText = discTotalText; //NOSONAR
        this.lyricsText = lyricsText; //NOSONAR
        this.commentText = commentText; //NOSONAR
        this.genreText = genreText; //NOSONAR
        this.tagCompletionListener = listener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected Boolean doInBackground(Object... params) { //NOSONAR

        boolean success = false; //NOSONAR

        boolean requiresPermission = TaggerUtils.requiresPermission(applicationContext, paths); //NOSONAR

        for (int i = 0; i < paths.size(); i++) { //NOSONAR
            final String path = paths.get(i); //NOSONAR
            try { //NOSONAR

                File orig = new File(path); //NOSONAR
                AudioFile audioFile = AudioFileIO.read(orig); //NOSONAR
                Tag tag = audioFile.getTag(); //NOSONAR
                if (tag == null) { //NOSONAR
                    break; //NOSONAR
                } // NOSONAR

                TagUpdate tagUpdate = new TagUpdate(tag); //NOSONAR

                tagUpdate.softSetArtist(artistText); //NOSONAR
                tagUpdate.softSetAlbumArtist(albumArtistText); //NOSONAR
                tagUpdate.softSetGenre(genreText); //NOSONAR
                tagUpdate.softSetYear(yearText); //NOSONAR

                if (showAlbum) { //NOSONAR
                    tagUpdate.softSetAlbum(albumText); //NOSONAR
                    tagUpdate.softSetDiscTotal(discTotalText); //NOSONAR
                } // NOSONAR

                if (showTrack) { //NOSONAR
                    tagUpdate.softSetTitle(titleText); //NOSONAR
                    tagUpdate.softSetTrack(trackText); //NOSONAR
                    tagUpdate.softSetTrackTotal(trackTotalText); //NOSONAR
                    tagUpdate.softSetDisc(discText); //NOSONAR
                    tagUpdate.softSetLyrics(lyricsText); //NOSONAR
                    tagUpdate.softSetComment(commentText); //NOSONAR
                } // NOSONAR

                File temp = null; //NOSONAR
                if (tagUpdate.hasChanged()) { //NOSONAR

                    if (TaggerUtils.requiresPermission(applicationContext, paths)) { //NOSONAR
                        temp = new File(applicationContext.getFilesDir(), orig.getName()); //NOSONAR
                        tempFiles.add(temp); //NOSONAR
                        TaggerUtils.copyFile(orig, temp); //NOSONAR

                        audioFile = AudioFileIO.read(temp); //NOSONAR
                        tag = audioFile.getTag(); //NOSONAR
                        if (tag == null) { //NOSONAR
                            break; //NOSONAR
                        } // NOSONAR
                    } // NOSONAR

                    tagUpdate.updateTag(tag); //NOSONAR
                    AudioFileIO.write(audioFile); //NOSONAR

                    if (requiresPermission && temp != null) { //NOSONAR
                        DocumentFile documentFile = documentFiles.get(i); //NOSONAR
                        if (documentFile != null) { //NOSONAR
                            ParcelFileDescriptor pfd = applicationContext.getContentResolver().openFileDescriptor(documentFile.getUri(), "w"); //NOSONAR
                            if (pfd != null) { //NOSONAR
                                FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor()); //NOSONAR
                                TaggerUtils.copyFile(temp, fileOutputStream); //NOSONAR
                                pfd.close(); //NOSONAR
                            } // NOSONAR
                            if (temp.delete()) { //NOSONAR
                                if (tempFiles.contains(temp)) { //NOSONAR
                                    tempFiles.remove(temp); //NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR

                publishProgress(i); //NOSONAR
                success = true; //NOSONAR
            } catch (CannotWriteException | IOException | CannotReadException | InvalidAudioFrameException | TagException | ReadOnlyFileException e) { //NOSONAR
                e.printStackTrace(); //NOSONAR
            } finally { //NOSONAR
                //Try to clean up our temp files // NOSONAR
                if (tempFiles != null && tempFiles.size() != 0) { //NOSONAR
                    for (int j = tempFiles.size() - 1; j >= 0; j--) { //NOSONAR
                        File file = tempFiles.get(j); //NOSONAR
                        file.delete(); //NOSONAR
                        tempFiles.remove(j); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        return success; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onPostExecute(Boolean success) { //NOSONAR

        if (tagCompletionListener != null) { //NOSONAR
            if (success) { //NOSONAR
                tagCompletionListener.onSuccess(); //NOSONAR
            } else { //NOSONAR
                tagCompletionListener.onFailure(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onProgressUpdate(Integer... object) { //NOSONAR

        if (tagCompletionListener != null) { //NOSONAR
            tagCompletionListener.onProgress(object[0] + 1); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    //Builders // NOSONAR

    public TaggerTask setPaths(List<String> paths) { //NOSONAR
        this.paths = paths; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask setDocumentfiles(List<DocumentFile> documentFiles) { //NOSONAR
        this.documentFiles = documentFiles; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask showAlbum(boolean showAlbum) { //NOSONAR
        this.showAlbum = showAlbum; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask showTrack(boolean showTrack) { //NOSONAR
        this.showTrack = showTrack; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask title(String titleText) { //NOSONAR
        this.titleText = titleText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask album(String albumText) { //NOSONAR
        this.albumText = albumText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask artist(String artistText) { //NOSONAR
        this.artistText = artistText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask albumArtist(String albumArtistText) { //NOSONAR
        this.albumArtistText = albumArtistText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask year(String yearText) { //NOSONAR
        this.yearText = yearText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask track(String trackText) { //NOSONAR
        this.trackText = trackText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask trackTotal(String trackTotalText) { //NOSONAR
        this.trackTotalText = trackTotalText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask disc(String discText) { //NOSONAR
        this.discText = discText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask discTotal(String discTotalText) { //NOSONAR
        this.discTotalText = discTotalText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask lyrics(String lyricsText) { //NOSONAR
        this.lyricsText = lyricsText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask comment(String commentText) { //NOSONAR
        this.commentText = commentText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask genre(String genreText) { //NOSONAR
        this.genreText = genreText; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask listener(TagCompletionListener listener) { //NOSONAR
        this.tagCompletionListener = listener; //NOSONAR
        return this; //NOSONAR
    } // NOSONAR

    public TaggerTask build() { //NOSONAR
        return new TaggerTask(applicationContext, showAlbum, showTrack, paths, documentFiles, titleText, //NOSONAR
                albumText, artistText, albumArtistText, yearText, trackText, trackTotalText, //NOSONAR
                discText, discTotalText, lyricsText, commentText, genreText, //NOSONAR
                tagCompletionListener); //NOSONAR
    } // NOSONAR
} // NOSONAR
