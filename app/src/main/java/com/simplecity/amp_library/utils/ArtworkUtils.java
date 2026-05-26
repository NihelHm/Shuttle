package com.simplecity.amp_library.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.Song;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkUtils { //NOSONAR

    private static final String TAG = "ArtworkUtils"; //NOSONAR

    //This class is never instantiated
    private ArtworkUtils() { //NOSONAR
        // Intentionally left empty.
    }

    /**
     * Searches the parent directory of the passed in path for [cover/album/artwork].[png/jpg/jpeg]
     * using regex and returns a {@link InputStream} representing the artwork
     */
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
                    }
                }
            }
        }
        return fileInputStream; //NOSONAR
    }

    /**
     * Returns a FileInputStream for the given file, or null if the file is invalid
     */
    @WorkerThread //NOSONAR
    public static InputStream getFileArtwork(@Nullable File file) { //NOSONAR

        if (file == null || !file.exists() || file.length() < 10 * 1024) { //NOSONAR
            return null; //NOSONAR
        }

        FileInputStream fileInputStream = null; //NOSONAR

        try { //NOSONAR
            fileInputStream = new FileInputStream(file); //NOSONAR
        } catch (FileNotFoundException | NoSuchElementException e) { //NOSONAR
            Log.e(TAG, "getFileArtwork failed: " + e.toString()); //NOSONAR
        }

        return fileInputStream; //NOSONAR
    }

    /**
     * Retrieves the Artwork for the given album id from the MediaStore as an {@link InputStream}
     */
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
                            // Intentionally left empty.
                        }
                    }
                }
            } catch (NullPointerException ignored) { //NOSONAR
                // Intentionally left empty.
            } finally { //NOSONAR
                cursor.close(); //NOSONAR
            }
        }

        return fileInputStream; //NOSONAR
    }

    /**
     * Retrieves the Artwork for the given {@link Song} from the MediaStore as an {@link InputStream}
     */
    @WorkerThread //NOSONAR
    public static InputStream getMediaStoreArtwork(Context context, @NonNull Song song) { //NOSONAR
        return getMediaStoreArtwork(context, song.albumId); //NOSONAR
    }

    /**
     * Retrieves the Artwork for the given {@link Album} from the MediaStore as an {@link InputStream}
     */
    @WorkerThread //NOSONAR
    public static InputStream getMediaStoreArtwork(Context context, @NonNull Album album) { //NOSONAR
        return getMediaStoreArtwork(context, album.id); //NOSONAR
    }

    /**
     * Retrieves the Artwork from the id3 tags of the file at the given path.
     */
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
                        }
                    }
                }
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ignored) { //NOSONAR
                // Intentionally left empty.
            }
        }

        return inputStream; //NOSONAR
    }

    /**
     * Searches the parent directory of the passed in path for [cover/album/artwork].[png/jpg/jpeg]
     * using regex and returns a {@link List<File>} representing the artwork
     */
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
                        }
                    }
                }
            }
        }
        return fileArray; //NOSONAR
    }
}
