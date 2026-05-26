package com.simplecity.amp_library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.model.BaseFileObject;
import com.simplecity.amp_library.model.FileObject;
import com.simplecity.amp_library.model.Song;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FileHelper { //NOSONAR

    private final static String TAG = "FileHelper"; //NOSONAR

    /**
     * The root directory
     */
    public static final String ROOT_DIRECTORY = "/"; //NOSONAR

    /**
     * The parent directory
     */
    public static final String PARENT_DIRECTORY = ".."; //NOSONAR

    /**
     * The current directory
     */
    public static final String CURRENT_DIRECTORY = "."; //NOSONAR

    /**
     * Method that check if a file is a symbolic link.
     *
     * @param file File to check
     * @return boolean If file is a symbolic link
     * @throws IOException If real file couldn't be checked
     */
    public static boolean isSymlink(File file) throws IOException { //NOSONAR
        if (file == null) { //NOSONAR
            return false; //NOSONAR
        }
        String absPath = file.getAbsolutePath(); //NOSONAR
        String canonPath = file.getCanonicalPath(); //NOSONAR

        return !(TextUtils.isEmpty(absPath) || TextUtils.isEmpty(canonPath)) && absPath.compareTo(canonPath) != 0; //NOSONAR
    }

    /**
     * Method that resolves a symbolic link to the real file or directory.
     *
     * @param file File to check
     * @return File The real file or directory
     * @throws IOException If real file couldn't be resolved
     */
    public static File resolveSymlink(File file) throws IOException { //NOSONAR
        return file.getCanonicalFile(); //NOSONAR
    }

    /**
     * Returns the name of a string, excluding the extension
     *
     * @param name the name (path) of the file
     * @return the name of the file, excluding the extension
     */
    public static String getName(String name) { //NOSONAR
        String ext = getExtension(name); //NOSONAR
        if (ext == null) { //NOSONAR
            return name; //NOSONAR
        }
        return name.substring(0, name.length() - ext.length() - 1); //NOSONAR
    }

    /**
     * Returns the extension of the file
     *
     * @param name the File to retrieve the extension from
     * @return String the extension of the file
     */
    public static String getExtension(String name) { //NOSONAR
        final char dot = '.'; //NOSONAR
        int pos = name.lastIndexOf(dot); //NOSONAR
        if (pos == -1 || pos == 0) { // Hidden files don't have extensions //NOSONAR
            return null; //NOSONAR
        }

        return name.substring(pos + 1); //NOSONAR
    }

    /**
     * Returns true if the folder is the root directory
     *
     * @param folder The folder to check
     * @return true if the folder is the root directory
     */
    public static boolean isRootDirectory(File folder) { //NOSONAR
        return folder.getPath().compareTo(FileHelper.ROOT_DIRECTORY) == 0; //NOSONAR
    }

    /**
     * Returns true if this OldFileObject can has read & write access
     *
     * @param file the File to check
     * @return boolean true if this OldFileObject can has read & write access
     */
    public static boolean canReadWrite(File file) { //NOSONAR
        return file.canRead() && file.canWrite(); //NOSONAR
    }

    /**
     * Resolves the /storage/emulated/legacy paths to
     * their true folder path representations. Required
     * for Nexii and other devices with no SD card.
     *
     * @return The true, resolved file path to the input path.
     */
    @SuppressLint("SdCardPath") //NOSONAR
    public static String getPath(File file) { //NOSONAR

        if (file == null) { //NOSONAR
            return null; //NOSONAR
        }

        String filePath = file.getAbsolutePath(); //NOSONAR

        try { //NOSONAR
            if (isSymlink(file)) { //NOSONAR
                file = resolveSymlink(file); //NOSONAR
                filePath = file.getAbsolutePath(); //NOSONAR
            }
        } catch (IOException ignored) { //NOSONAR
            // Intentionally left empty.
        }

        if (!TextUtils.isEmpty(filePath) && filePath.equals("/storage/emulated/0") || //NOSONAR
                filePath.equals("/storage/emulated/0/") || //NOSONAR
                filePath.equals("/storage/emulated/legacy") || //NOSONAR
                filePath.equals("/storage/emulated/legacy/") || //NOSONAR
                filePath.equals("/storage/sdcard0") || //NOSONAR
                filePath.equals("/storage/sdcard0/") || //NOSONAR
                filePath.equals("/sdcard") || //NOSONAR
                filePath.equals("/sdcard/") || //NOSONAR
                filePath.equals("/mnt/sdcard") || //NOSONAR
                filePath.equals("/mnt/sdcard/")) { //NOSONAR

            filePath = Environment.getExternalStorageDirectory().toString(); //NOSONAR
        }

        return filePath; //NOSONAR
    }

    /**
     * Gets a formatted, human readable file size String
     *
     * @param size long, the size of the file in bytes
     * @return String a formatted, human readable file size
     */
    public static String getHumanReadableSize(long size) { //NOSONAR
        if (size <= 0) { //NOSONAR
            return "0"; //NOSONAR
        }
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" }; //NOSONAR
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024)); //NOSONAR
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups]; //NOSONAR
    }

    public static long getDuration(Context context, BaseFileObject baseFileObject) { //NOSONAR
        int duration = 0; //NOSONAR
        if (baseFileObject != null && !TextUtils.isEmpty(baseFileObject.path)) { //NOSONAR
            Uri uri = Uri.parse(baseFileObject.path); //NOSONAR
            if (uri != null) { //NOSONAR
                MediaPlayer mediaPlayer = MediaPlayer.create(context, uri); //NOSONAR
                if (mediaPlayer != null) { //NOSONAR
                    duration = mediaPlayer.getDuration(); //NOSONAR
                    mediaPlayer.reset(); //NOSONAR
                    mediaPlayer.release(); //NOSONAR
                }
            }
        }
        return duration; //NOSONAR
    }

    /**
     * Recursively collects all the files for the given directory and
     * all of its sub-directories. Must be called Asynchronously.
     *
     * @param file the File to retrieve the song Id's from
     * @param recursive whether to recursively check the sub-directories for song Id's
     * @return long[] a list of the songId's for the given fileObject's directory & sub-directories
     */
    public static Observable<List<String>> getPathList(final File file, final boolean recursive, final boolean inSameDir) { //NOSONAR
        return Observable.fromCallable( //NOSONAR
                () -> walk(file, new ArrayList<>(), recursive, inSameDir)) //NOSONAR
                .subscribeOn(Schedulers.io()); //NOSONAR
    }

    /**
     * Recursively collects all the songs for the given directory and
     * all of its sub-directories. Must be called Asynchronously.
     *
     * @param file the File to retrieve the song Id's from
     * @param recursive whether to recursively check the sub-directories for song Id's
     * @return List<Song> a list of the songs for the given fileObject's directory & sub-directories
     */
    public static Single<List<Song>> getSongList(Repository.SongsRepository songsRepository, File file, boolean recursive, boolean inSameDir) { //NOSONAR
        return Single.fromCallable( //NOSONAR
                () -> walk(file, new ArrayList<>(), recursive, inSameDir)) //NOSONAR
                .flatMap(filePaths -> songsRepository.getSongs(song -> song.path.contains(FileHelper.getPath(inSameDir ? file.getParentFile() : file))) //NOSONAR
                        .first(Collections.emptyList())) //NOSONAR
                .subscribeOn(Schedulers.io()); //NOSONAR
    }

    /**
     * Gets the song for a given file
     */
    public static Single<Song> getSong(Repository.SongsRepository songsRepository, File file) { //NOSONAR
        return songsRepository.getSongs(song -> song.path.contains(FileHelper.getPath(file))) //NOSONAR
                .firstOrError() //NOSONAR
                .flatMap(songs -> { //NOSONAR
                    try { //NOSONAR
                        return Single.just(Stream.of(songs).findFirst().get()); //NOSONAR
                    } catch (NoSuchElementException e) { //NOSONAR
                        return Single.error(e); //NOSONAR
                    }
                })
                .subscribeOn(Schedulers.io()); //NOSONAR
    }

    /**
     * Recursively 'walks' the files subdirectories, gathering a list of paths.
     *
     * @param root the root file to walk
     * @param paths the paths will be added to this List
     * @param recursive whether to recursively walk subdirectories
     * @param inSameDir whether files in the same dir as root should be included
     * @return a List of paths
     */
    @WorkerThread //NOSONAR
    private static List<String> walk(File root, final List<String> paths, final boolean recursive, final boolean inSameDir) { //NOSONAR

        if (inSameDir) { //NOSONAR
            root = root.getParentFile(); //NOSONAR
        }

        if (!root.isDirectory()) { //NOSONAR
            paths.add(root.getAbsolutePath()); //NOSONAR
            return paths; //NOSONAR
        }

        File[] list = root.listFiles(getAudioFilter()); //NOSONAR
        if (list != null) { //NOSONAR
            for (File f : list) { //NOSONAR
                if (f.isDirectory()) { //NOSONAR
                    if (recursive) { //NOSONAR
                        walk(f, paths, true, false); //NOSONAR
                    }
                } else { //NOSONAR
                    paths.add(f.getAbsolutePath()); //NOSONAR
                }
            }
        }

        return paths; //NOSONAR
    }

    /**
     * Delete a File recursively.
     *
     * @param file the File to delete
     * @return true if the deletion was successful
     */
    public static boolean deleteFile(File file) { //NOSONAR
        return DeleteRecursive(file); //NOSONAR
    }

    /**
     * Recursively delete a File
     *
     * @param fileOrDirectory the file or directory to delete
     * @return true id the deletion was successful
     */
    private static boolean DeleteRecursive(File fileOrDirectory) { //NOSONAR
        if (fileOrDirectory == null) { //NOSONAR
            return false; //NOSONAR
        } else if (fileOrDirectory.isDirectory()) { //NOSONAR
            File[] fileList = fileOrDirectory.listFiles(); //NOSONAR
            if (fileList != null) { //NOSONAR
                for (File child : fileList) //NOSONAR
                    DeleteRecursive(child); //NOSONAR
            }
        }
        return fileOrDirectory.delete(); //NOSONAR
    }

    /**
     * Renames an {@link FileObject} to the passed in newName
     *
     * @param context Context
     * @param baseFileObject the FileObject representation of the file to rename
     * @param newName the new name of the file
     */
    public static boolean renameFile(Context context, BaseFileObject baseFileObject, String newName) { //NOSONAR
        if (newName == null) { //NOSONAR
            return false; //NOSONAR
        }

        if (baseFileObject instanceof FileObject) { //NOSONAR
            String ext = ((FileObject) baseFileObject).extension; //NOSONAR
            if (ext == null) { //NOSONAR
                ext = ""; //NOSONAR
            }
            newName = newName + "." + ext; //NOSONAR
        }
        File file = new File(baseFileObject.path); //NOSONAR
        File newFile = new File(baseFileObject.getParent(), newName); //NOSONAR
        if (file.renameTo(newFile)) { //NOSONAR
            baseFileObject.name = FileHelper.getName(newFile.getName()); //NOSONAR
            CustomMediaScanner.scanFiles(context, Collections.singletonList(file.getPath()), null); //NOSONAR
            return true; //NOSONAR
        }
        return false; //NOSONAR
    }

    /**
     * An array of accepted/supported audio extensions.
     */
    public static String[] sExtensions = new String[] { //NOSONAR
            "mp3", "3gp", "mp4", "m4a", //NOSONAR
            "aac", "ts", "flac", "mid", //NOSONAR
            "xmf", "mxmf", "midi", "rtttl", //NOSONAR
            "rtx", "ota", "imy", "ogg", //NOSONAR
            "mkv", "wav" //NOSONAR
    };

    /**
     * An {@link FileFilter} which only accepts directories & supported audio filetypes, based on extension
     */
    public static FileFilter getAudioFilter() { //NOSONAR
        return file -> { //NOSONAR
            if (!file.isHidden() && file.canRead()) { //NOSONAR
                if (file.isDirectory()) { //NOSONAR
                    return true; //NOSONAR
                } else { //NOSONAR
                    String ext = getExtension(file.getName()); //NOSONAR
                    for (String allowedExtension : sExtensions) { //NOSONAR
                        if (!TextUtils.isEmpty(ext)) { //NOSONAR
                            if (allowedExtension.equalsIgnoreCase(ext)) { //NOSONAR
                                return true; //NOSONAR
                            }
                        }
                    }
                }
            }
            return false; //NOSONAR
        };
    }
}
