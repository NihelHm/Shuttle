package com.simplecity.amp_library.utils; // NOSONAR

import android.content.ContentUris; // NOSONAR
import android.content.Context; // NOSONAR
import android.database.Cursor; // NOSONAR
import android.net.Uri; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.WorkerThread; // NOSONAR
import android.util.Log; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import java.io.ByteArrayInputStream; // NOSONAR
import java.io.File; // NOSONAR
import java.io.FileInputStream; // NOSONAR
import java.io.FileNotFoundException; // NOSONAR
import java.io.IOException; // NOSONAR
import java.io.InputStream; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR
import java.util.NoSuchElementException; // NOSONAR
import java.util.regex.Pattern; // NOSONAR
import org.jaudiotagger.audio.AudioFile; // NOSONAR
import org.jaudiotagger.audio.AudioFileIO; // NOSONAR
import org.jaudiotagger.audio.exceptions.CannotReadException; // NOSONAR
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException; // NOSONAR
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException; // NOSONAR
import org.jaudiotagger.tag.Tag; // NOSONAR
import org.jaudiotagger.tag.TagException; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkUtils { //NOSONAR

    private static final String TAG = "ArtworkUtils"; //NOSONAR

    //This class is never instantiated // NOSONAR
    private ArtworkUtils() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Searches the parent directory of the passed in path for [cover/album/artwork].[png/jpg/jpeg] // NOSONAR
     * using regex and returns a {@link InputStream} representing the artwork // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static InputStream getFolderArtwork(@Nullable final String path) { //NOSONAR

        InputStream fileInputStream = null; //NOSONAR

        if (path != null) { //NOSONAR
            File[] files; //NOSONAR
            File file = new File(path); //NOSONAR
            File parent = file.getParentFile(); //NOSONAR
            if (parent.exists() && parent.isDirectory()) { //NOSONAR
                final Pattern pattern = Pattern.compile("(folder|cover|album).*\\.(jpg|jpeg|png)", Pattern.CASE_INSENSITIVE); //NOSONAR
                files = parent.listFiles(file1 -> pattern.matcher(file1.getName()).matches()); //NOSONAR

                if (files.length > 0) { //NOSONAR
                    try { //NOSONAR
                        File artworkFile = Stream.of(files) //NOSONAR
                                .filter(aFile -> aFile.exists() && aFile.length() > 1024) //NOSONAR
                                .max((a, b) -> (int) (a.length() / 1024 - b.length() / 1024)) //NOSONAR
                                .get(); //NOSONAR

                        fileInputStream = getFileArtwork(artworkFile); //NOSONAR
                    } catch (NoSuchElementException e) { //NOSONAR
                        Log.e(TAG, "getFolderArtwork failed: " + e.toString()); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        return fileInputStream; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns a FileInputStream for the given file, or null if the file is invalid // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static InputStream getFileArtwork(@Nullable File file) { //NOSONAR

        if (file == null || !file.exists() || file.length() < 10 * 1024) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        FileInputStream fileInputStream = null; //NOSONAR

        try { //NOSONAR
            fileInputStream = new FileInputStream(file); //NOSONAR
        } catch (FileNotFoundException | NoSuchElementException e) { //NOSONAR
            Log.e(TAG, "getFileArtwork failed: " + e.toString()); //NOSONAR
        } // NOSONAR

        return fileInputStream; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Retrieves the Artwork for the given album id from the MediaStore as an {@link InputStream} // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static InputStream getMediaStoreArtwork(Context context, long albumId) { //NOSONAR

        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, albumId); //NOSONAR

        FileInputStream fileInputStream = null; //NOSONAR

        Cursor cursor = context //NOSONAR
                .getContentResolver() //NOSONAR
                .query(contentUri, new String[] { MediaStore.Audio.Albums.ALBUM_ART }, null, null, null); //NOSONAR

        if (cursor != null) { //NOSONAR
            try { //NOSONAR
                if (cursor.moveToFirst()) { //NOSONAR
                    File file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))); //NOSONAR
                    if (file.exists()) { //NOSONAR
                        try { //NOSONAR
                            fileInputStream = new FileInputStream(file); //NOSONAR
                        } catch (FileNotFoundException ignored) { //NOSONAR
                            // Intentionally left empty. // NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } catch (NullPointerException ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } finally { //NOSONAR
                cursor.close(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        return fileInputStream; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Retrieves the Artwork for the given {@link Song} from the MediaStore as an {@link InputStream} // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static InputStream getMediaStoreArtwork(Context context, @NonNull Song song) { //NOSONAR
        return getMediaStoreArtwork(context, song.albumId); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Retrieves the Artwork for the given {@link Album} from the MediaStore as an {@link InputStream} // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static InputStream getMediaStoreArtwork(Context context, @NonNull Album album) { //NOSONAR
        return getMediaStoreArtwork(context, album.id); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Retrieves the Artwork from the id3 tags of the file at the given path. // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static InputStream getTagArtwork(@Nullable String filePath) { //NOSONAR

        InputStream inputStream = null; //NOSONAR

        if (filePath != null) { //NOSONAR
            try { //NOSONAR
                AudioFile audioFIle = AudioFileIO.read(new File(filePath)); //NOSONAR
                if (audioFIle != null) { //NOSONAR
                    Tag tag = audioFIle.getTag(); //NOSONAR
                    if (tag != null) { //NOSONAR
                        org.jaudiotagger.tag.datatype.Artwork artwork = tag.getFirstArtwork(); //NOSONAR
                        if (artwork != null) { //NOSONAR
                            inputStream = new ByteArrayInputStream(artwork.getBinaryData()); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR

        return inputStream; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Searches the parent directory of the passed in path for [cover/album/artwork].[png/jpg/jpeg] // NOSONAR
     * using regex and returns a {@link List<File>} representing the artwork // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public static List<File> getAllFolderArtwork(@Nullable final String path) { //NOSONAR
        List<File> fileArray = new ArrayList<>(); //NOSONAR

        if (path != null) { //NOSONAR
            File[] files; //NOSONAR
            File parent = new File(path).getParentFile(); //NOSONAR
            if (parent.exists() && parent.isDirectory()) { //NOSONAR
                final Pattern pattern = Pattern.compile("(folder|cover|album).*\\.(jpg|jpeg|png)", Pattern.CASE_INSENSITIVE); //NOSONAR
                files = parent.listFiles(file1 -> pattern.matcher(file1.getName()).matches()); //NOSONAR

                if (files.length != 0) { //NOSONAR
                    for (File file : files) { //NOSONAR
                        if (file.exists()) { //NOSONAR
                            fileArray.add(file); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        return fileArray; //NOSONAR
    } // NOSONAR
} // NOSONAR
