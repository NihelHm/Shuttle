package com.simplecity.amp_library.utils; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.content.Context; // NOSONAR
import android.media.MediaPlayer; // NOSONAR
import android.net.Uri; // NOSONAR
import android.os.Environment; // NOSONAR
import android.support.annotation.WorkerThread; // NOSONAR
import android.text.TextUtils; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.model.BaseFileObject; // NOSONAR
import com.simplecity.amp_library.model.FileObject; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.io.File; // NOSONAR
import java.io.FileFilter; // NOSONAR
import java.io.IOException; // NOSONAR
import java.text.DecimalFormat; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import java.util.NoSuchElementException; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FileHelper { //NOSONAR

    private final static String TAG = "FileHelper"; //NOSONAR

    /** // NOSONAR
     * The root directory // NOSONAR
     */ // NOSONAR
    public static final String ROOT_DIRECTORY = "/"; //NOSONAR

    /** // NOSONAR
     * The parent directory // NOSONAR
     */ // NOSONAR
    public static final String PARENT_DIRECTORY = ".."; //NOSONAR

    /** // NOSONAR
     * The current directory // NOSONAR
     */ // NOSONAR
    public static final String CURRENT_DIRECTORY = "."; //NOSONAR

    /** // NOSONAR
     * Method that check if a file is a symbolic link. // NOSONAR
     * // NOSONAR
     * @param file File to check // NOSONAR
     * @return boolean If file is a symbolic link // NOSONAR
     * @throws IOException If real file couldn't be checked // NOSONAR
     */ // NOSONAR
    public static boolean isSymlink(File file) throws IOException { //NOSONAR
        if (file == null) { //NOSONAR
            return false; //NOSONAR
        } // NOSONAR
        String absPath = file.getAbsolutePath(); //NOSONAR
        String canonPath = file.getCanonicalPath(); //NOSONAR

        return !(TextUtils.isEmpty(absPath) || TextUtils.isEmpty(canonPath)) && absPath.compareTo(canonPath) != 0; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Method that resolves a symbolic link to the real file or directory. // NOSONAR
     * // NOSONAR
     * @param file File to check // NOSONAR
     * @return File The real file or directory // NOSONAR
     * @throws IOException If real file couldn't be resolved // NOSONAR
     */ // NOSONAR
    public static File resolveSymlink(File file) throws IOException { //NOSONAR
        return file.getCanonicalFile(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the name of a string, excluding the extension // NOSONAR
     * // NOSONAR
     * @param name the name (path) of the file // NOSONAR
     * @return the name of the file, excluding the extension // NOSONAR
     */ // NOSONAR
    public static String getName(String name) { //NOSONAR
        String ext = getExtension(name); //NOSONAR
        if (ext == null) { //NOSONAR
            return name; //NOSONAR
        } // NOSONAR
        return name.substring(0, name.length() - ext.length() - 1); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the extension of the file // NOSONAR
     * // NOSONAR
     * @param name the File to retrieve the extension from // NOSONAR
     * @return String the extension of the file // NOSONAR
     */ // NOSONAR
    public static String getExtension(String name) { //NOSONAR
        final char dot = '.'; //NOSONAR
        int pos = name.lastIndexOf(dot); //NOSONAR
        if (pos == -1 || pos == 0) { // Hidden files don't have extensions //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        return name.substring(pos + 1); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns true if the folder is the root directory // NOSONAR
     * // NOSONAR
     * @param folder The folder to check // NOSONAR
     * @return true if the folder is the root directory // NOSONAR
     */ // NOSONAR
    public static boolean isRootDirectory(File folder) { //NOSONAR
        return folder.getPath().compareTo(FileHelper.ROOT_DIRECTORY) == 0; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns true if this OldFileObject can has read & write access // NOSONAR
     * // NOSONAR
     * @param file the File to check // NOSONAR
     * @return boolean true if this OldFileObject can has read & write access // NOSONAR
     */ // NOSONAR
    public static boolean canReadWrite(File file) { //NOSONAR
        return file.canRead() && file.canWrite(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Resolves the /storage/emulated/legacy paths to // NOSONAR
     * their true folder path representations. Required // NOSONAR
     * for Nexii and other devices with no SD card. // NOSONAR
     * // NOSONAR
     * @return The true, resolved file path to the input path. // NOSONAR
     */ // NOSONAR
    @SuppressLint("SdCardPath") //NOSONAR
    public static String getPath(File file) { //NOSONAR

        if (file == null) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        String filePath = file.getAbsolutePath(); //NOSONAR

        try { //NOSONAR
            if (isSymlink(file)) { //NOSONAR
                file = resolveSymlink(file); //NOSONAR
                filePath = file.getAbsolutePath(); //NOSONAR
            } // NOSONAR
        } catch (IOException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

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
        } // NOSONAR

        return filePath; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Gets a formatted, human readable file size String // NOSONAR
     * // NOSONAR
     * @param size long, the size of the file in bytes // NOSONAR
     * @return String a formatted, human readable file size // NOSONAR
     */ // NOSONAR
    public static String getHumanReadableSize(long size) { //NOSONAR
        if (size <= 0) { //NOSONAR
            return "0"; //NOSONAR
        } // NOSONAR
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" }; //NOSONAR
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024)); //NOSONAR
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups]; //NOSONAR
    } // NOSONAR

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
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        return duration; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Recursively collects all the files for the given directory and // NOSONAR
     * all of its sub-directories. Must be called Asynchronously. // NOSONAR
     * // NOSONAR
     * @param file the File to retrieve the song Id's from // NOSONAR
     * @param recursive whether to recursively check the sub-directories for song Id's // NOSONAR
     * @return long[] a list of the songId's for the given fileObject's directory & sub-directories // NOSONAR
     */ // NOSONAR
    public static Observable<List<String>> getPathList(final File file, final boolean recursive, final boolean inSameDir) { //NOSONAR
        return Observable.fromCallable( //NOSONAR
                () -> walk(file, new ArrayList<>(), recursive, inSameDir)) //NOSONAR
                .subscribeOn(Schedulers.io()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Recursively collects all the songs for the given directory and // NOSONAR
     * all of its sub-directories. Must be called Asynchronously. // NOSONAR
     * // NOSONAR
     * @param file the File to retrieve the song Id's from // NOSONAR
     * @param recursive whether to recursively check the sub-directories for song Id's // NOSONAR
     * @return List<Song> a list of the songs for the given fileObject's directory & sub-directories // NOSONAR
     */ // NOSONAR
    public static Single<List<Song>> getSongList(Repository.SongsRepository songsRepository, File file, boolean recursive, boolean inSameDir) { //NOSONAR
        return Single.fromCallable( //NOSONAR
                () -> walk(file, new ArrayList<>(), recursive, inSameDir)) //NOSONAR
                .flatMap(filePaths -> songsRepository.getSongs(song -> song.path.contains(FileHelper.getPath(inSameDir ? file.getParentFile() : file))) //NOSONAR
                        .first(Collections.emptyList())) //NOSONAR
                .subscribeOn(Schedulers.io()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Gets the song for a given file // NOSONAR
     */ // NOSONAR
    public static Single<Song> getSong(Repository.SongsRepository songsRepository, File file) { //NOSONAR
        return songsRepository.getSongs(song -> song.path.contains(FileHelper.getPath(file))) //NOSONAR
                .firstOrError() //NOSONAR
                .flatMap(songs -> { //NOSONAR
                    try { //NOSONAR
                        return Single.just(Stream.of(songs).findFirst().get()); //NOSONAR
                    } catch (NoSuchElementException e) { //NOSONAR
                        return Single.error(e); //NOSONAR
                    } // NOSONAR
                }) // NOSONAR
                .subscribeOn(Schedulers.io()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Recursively 'walks' the files subdirectories, gathering a list of paths. // NOSONAR
     * // NOSONAR
     * @param root the root file to walk // NOSONAR
     * @param paths the paths will be added to this List // NOSONAR
     * @param recursive whether to recursively walk subdirectories // NOSONAR
     * @param inSameDir whether files in the same dir as root should be included // NOSONAR
     * @return a List of paths // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    private static List<String> walk(File root, final List<String> paths, final boolean recursive, final boolean inSameDir) { //NOSONAR

        if (inSameDir) { //NOSONAR
            root = root.getParentFile(); //NOSONAR
        } // NOSONAR

        if (!root.isDirectory()) { //NOSONAR
            paths.add(root.getAbsolutePath()); //NOSONAR
            return paths; //NOSONAR
        } // NOSONAR

        File[] list = root.listFiles(getAudioFilter()); //NOSONAR
        if (list != null) { //NOSONAR
            for (File f : list) { //NOSONAR
                if (f.isDirectory()) { //NOSONAR
                    if (recursive) { //NOSONAR
                        walk(f, paths, true, false); //NOSONAR
                    } // NOSONAR
                } else { //NOSONAR
                    paths.add(f.getAbsolutePath()); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        return paths; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Delete a File recursively. // NOSONAR
     * // NOSONAR
     * @param file the File to delete // NOSONAR
     * @return true if the deletion was successful // NOSONAR
     */ // NOSONAR
    public static boolean deleteFile(File file) { //NOSONAR
        return DeleteRecursive(file); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Recursively delete a File // NOSONAR
     * // NOSONAR
     * @param fileOrDirectory the file or directory to delete // NOSONAR
     * @return true id the deletion was successful // NOSONAR
     */ // NOSONAR
    private static boolean DeleteRecursive(File fileOrDirectory) { //NOSONAR
        if (fileOrDirectory == null) { //NOSONAR
            return false; //NOSONAR
        } else if (fileOrDirectory.isDirectory()) { //NOSONAR
            File[] fileList = fileOrDirectory.listFiles(); //NOSONAR
            if (fileList != null) { //NOSONAR
                for (File child : fileList) //NOSONAR
                    DeleteRecursive(child); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return fileOrDirectory.delete(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Renames an {@link FileObject} to the passed in newName // NOSONAR
     * // NOSONAR
     * @param context Context // NOSONAR
     * @param baseFileObject the FileObject representation of the file to rename // NOSONAR
     * @param newName the new name of the file // NOSONAR
     */ // NOSONAR
    public static boolean renameFile(Context context, BaseFileObject baseFileObject, String newName) { //NOSONAR
        if (newName == null) { //NOSONAR
            return false; //NOSONAR
        } // NOSONAR

        if (baseFileObject instanceof FileObject) { //NOSONAR
            String ext = ((FileObject) baseFileObject).extension; //NOSONAR
            if (ext == null) { //NOSONAR
                ext = ""; //NOSONAR
            } // NOSONAR
            newName = newName + "." + ext; //NOSONAR
        } // NOSONAR
        File file = new File(baseFileObject.path); //NOSONAR
        File newFile = new File(baseFileObject.getParent(), newName); //NOSONAR
        if (file.renameTo(newFile)) { //NOSONAR
            baseFileObject.name = FileHelper.getName(newFile.getName()); //NOSONAR
            CustomMediaScanner.scanFiles(context, Collections.singletonList(file.getPath()), null); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * An array of accepted/supported audio extensions. // NOSONAR
     */ // NOSONAR
    public static String[] sExtensions = new String[] { //NOSONAR
            "mp3", "3gp", "mp4", "m4a", //NOSONAR
            "aac", "ts", "flac", "mid", //NOSONAR
            "xmf", "mxmf", "midi", "rtttl", //NOSONAR
            "rtx", "ota", "imy", "ogg", //NOSONAR
            "mkv", "wav" //NOSONAR
    }; // NOSONAR

    /** // NOSONAR
     * An {@link FileFilter} which only accepts directories & supported audio filetypes, based on extension // NOSONAR
     */ // NOSONAR
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
                            } // NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
            return false; //NOSONAR
        }; // NOSONAR
    } // NOSONAR
} // NOSONAR
