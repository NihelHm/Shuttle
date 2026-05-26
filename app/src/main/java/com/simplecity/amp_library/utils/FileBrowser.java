package com.simplecity.amp_library.utils; // NOSONAR

import android.os.Environment; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.WorkerThread; // NOSONAR
import android.text.TextUtils; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.interfaces.FileType; // NOSONAR
import com.simplecity.amp_library.model.BaseFileObject; // NOSONAR
import com.simplecity.amp_library.model.FileObject; // NOSONAR
import com.simplecity.amp_library.model.FolderObject; // NOSONAR
import com.simplecity.amp_library.model.TagInfo; // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager; // NOSONAR
import java.io.File; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.Comparator; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FileBrowser { //NOSONAR

    private static final String TAG = "FileBrowser"; //NOSONAR

    @Nullable //NOSONAR
    private File currentDir; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    public FileBrowser(SettingsManager settingsManager) { //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Loads the specified folder. // NOSONAR
     * // NOSONAR
     * @param directory The file object to points to the directory to load. // NOSONAR
     * @return An {@link List<BaseFileObject>} object that holds the data of the specified directory. // NOSONAR
     */ // NOSONAR
    @WorkerThread //NOSONAR
    public List<BaseFileObject> loadDir(File directory) { //NOSONAR

        ThreadUtils.ensureNotOnMainThread(); //NOSONAR

        currentDir = directory; //NOSONAR

        List<BaseFileObject> folderObjects = new ArrayList<>(); //NOSONAR
        List<BaseFileObject> fileObjects = new ArrayList<>(); //NOSONAR

        //Grab a list of all files/subdirs within the specified directory. // NOSONAR
        File[] files = directory.listFiles(FileHelper.getAudioFilter()); //NOSONAR

        if (files != null) { //NOSONAR
            for (File file : files) { //NOSONAR
                BaseFileObject baseFileObject; //NOSONAR

                if (file.isDirectory()) { //NOSONAR
                    baseFileObject = new FolderObject(); //NOSONAR
                    baseFileObject.path = FileHelper.getPath(file); //NOSONAR
                    baseFileObject.name = file.getName(); //NOSONAR
                    File[] listOfFiles = file.listFiles(FileHelper.getAudioFilter()); //NOSONAR
                    if (listOfFiles != null && listOfFiles.length > 0) { //NOSONAR
                        for (File listOfFile : listOfFiles) { //NOSONAR
                            if (listOfFile.isDirectory()) { //NOSONAR
                                ((FolderObject) baseFileObject).folderCount++; //NOSONAR
                            } else { //NOSONAR
                                ((FolderObject) baseFileObject).fileCount++; //NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    } else { //NOSONAR
                        continue; //NOSONAR
                    } // NOSONAR
                    if (!folderObjects.contains(baseFileObject)) { //NOSONAR
                        folderObjects.add(baseFileObject); //NOSONAR
                    } // NOSONAR
                } else { //NOSONAR
                    baseFileObject = new FileObject(); //NOSONAR
                    baseFileObject.path = FileHelper.getPath(file); //NOSONAR
                    baseFileObject.name = FileHelper.getName(file.getName()); //NOSONAR
                    baseFileObject.size = file.length(); //NOSONAR
                    ((FileObject) baseFileObject).extension = FileHelper.getExtension(file.getName()); //NOSONAR
                    if (TextUtils.isEmpty(((FileObject) baseFileObject).extension)) { //NOSONAR
                        continue; //NOSONAR
                    } // NOSONAR
                    ((FileObject) baseFileObject).tagInfo = new TagInfo(baseFileObject.path); //NOSONAR

                    if (!fileObjects.contains(baseFileObject)) { //NOSONAR
                        fileObjects.add(baseFileObject); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        sortFileObjects(fileObjects); //NOSONAR
        sortFolderObjects(folderObjects); //NOSONAR

        if (!settingsManager.getFolderBrowserFilesAscending()) { //NOSONAR
            Collections.reverse(fileObjects); //NOSONAR
        } // NOSONAR

        if (!settingsManager.getFolderBrowserFoldersAscending()) { //NOSONAR
            Collections.reverse(folderObjects); //NOSONAR
        } // NOSONAR

        folderObjects.addAll(fileObjects); //NOSONAR

        if (!FileHelper.isRootDirectory(currentDir)) { //NOSONAR
            FolderObject parentObject = new FolderObject(); //NOSONAR
            parentObject.fileType = FileType.PARENT; //NOSONAR
            parentObject.name = FileHelper.PARENT_DIRECTORY; //NOSONAR
            parentObject.path = FileHelper.getPath(currentDir) + "/" + FileHelper.PARENT_DIRECTORY; //NOSONAR
            folderObjects.add(0, parentObject); //NOSONAR
        } // NOSONAR

        return folderObjects; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    public File getCurrentDir() { //NOSONAR
        return currentDir; //NOSONAR
    } // NOSONAR

    @WorkerThread //NOSONAR
    public File getInitialDir() { //NOSONAR

        ThreadUtils.ensureNotOnMainThread(); //NOSONAR

        File dir; //NOSONAR
        String[] files; //NOSONAR

        String settingsDir = settingsManager.getFolderBrowserInitialDir(); //NOSONAR
        if (settingsDir != null) { //NOSONAR
            File file = new File(settingsDir); //NOSONAR
            if (file.exists()) { //NOSONAR
                return file; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        dir = new File("/"); //NOSONAR

        files = dir.list((dir1, filename) -> dir1.isDirectory() && filename.toLowerCase().contains("storage")); //NOSONAR

        if (files != null && files.length > 0) { //NOSONAR
            dir = new File(dir + "/" + files[0]); //NOSONAR
            //If there's an extsdcard path in our base dir, let's navigate to that. External SD cards are cool. // NOSONAR
            files = dir.list((dir1, filename) -> dir1.isDirectory() && filename.toLowerCase().contains("extsdcard")); //NOSONAR
            if (files != null && files.length > 0) { //NOSONAR
                dir = new File(dir + "/" + files[0]); //NOSONAR
            } else { //NOSONAR
                //If we have external storage, use that as our initial dir // NOSONAR
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) { //NOSONAR
                    dir = Environment.getExternalStorageDirectory(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } else { //NOSONAR
            //If we have external storage, use that as our initial dir // NOSONAR
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) { //NOSONAR
                dir = Environment.getExternalStorageDirectory(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        //Whether or not there was an sdcard, let's see if there's a 'music' dir for us to navigate to // NOSONAR
        if (dir != null) { //NOSONAR
            files = dir.list((dir1, filename) -> dir1.isDirectory() && filename.toLowerCase().contains("music")); //NOSONAR
        } // NOSONAR
        if (files != null && files.length > 0) { //NOSONAR
            dir = new File(dir + "/" + files[0]); //NOSONAR
        } // NOSONAR

        return dir; //NOSONAR
    } // NOSONAR

    public void sortFolderObjects(List<BaseFileObject> baseFileObjects) { //NOSONAR

        switch (settingsManager.getFolderBrowserFoldersSortOrder()) { //NOSONAR
            case SortManager.SortFolders.COUNT: //NOSONAR
                Collections.sort(baseFileObjects, fileCountComparator()); //NOSONAR
                Collections.sort(baseFileObjects, folderCountComparator()); //NOSONAR
                break; //NOSONAR

            case SortManager.SortFolders.DEFAULT: //NOSONAR
            default: //NOSONAR
                Collections.sort(baseFileObjects, filenameComparator()); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void sortFileObjects(List<BaseFileObject> baseFileObjects) { //NOSONAR
        switch (settingsManager.getFolderBrowserFilesSortOrder()) { //NOSONAR
            case SortManager.SortFiles.SIZE: //NOSONAR
                Collections.sort(baseFileObjects, sizeComparator()); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.FILE_NAME: //NOSONAR
                Collections.sort(baseFileObjects, filenameComparator()); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.ARTIST_NAME: //NOSONAR
                Collections.sort(baseFileObjects, artistNameComparator()); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.ALBUM_NAME: //NOSONAR
                Collections.sort(baseFileObjects, albumNameComparator()); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.TRACK_NAME: //NOSONAR
                Collections.sort(baseFileObjects, trackNameComparator()); //NOSONAR
                break; //NOSONAR
            case SortManager.SortFiles.DEFAULT: //NOSONAR
            default: //NOSONAR
                Collections.sort(baseFileObjects, trackNumberComparator()); //NOSONAR
                Collections.sort(baseFileObjects, albumNameComparator()); //NOSONAR
                Collections.sort(baseFileObjects, artistNameComparator()); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void clearHomeDir() { //NOSONAR
        settingsManager.setFolderBrowserInitialDir(""); //NOSONAR
    } // NOSONAR

    public void setHomeDir() { //NOSONAR
        if (currentDir != null) { //NOSONAR
            settingsManager.setFolderBrowserInitialDir(currentDir.getPath()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public File getHomeDir() { //NOSONAR
        return new File(settingsManager.getFolderBrowserInitialDir()); //NOSONAR
    } // NOSONAR

    public boolean hasHomeDir() { //NOSONAR
        return !TextUtils.isEmpty(getHomeDir().getPath()); //NOSONAR
    } // NOSONAR

    public boolean atHomeDirectory() { //NOSONAR
        final File currDir = getCurrentDir(); //NOSONAR
        final File homeDir = getHomeDir(); //NOSONAR
        return currDir != null && homeDir != null && currDir.compareTo(homeDir) == 0; //NOSONAR
    } // NOSONAR

    public int getHomeDirIcon() { //NOSONAR
        int icon = R.drawable.ic_folder_outline; //NOSONAR
        if (atHomeDirectory()) { //NOSONAR
            icon = R.drawable.ic_folder_remove; //NOSONAR
        } else if (hasHomeDir()) { //NOSONAR
            icon = R.drawable.ic_folder_nav; //NOSONAR
        } // NOSONAR
        return icon; //NOSONAR
    } // NOSONAR

    public int getHomeDirTitle() { //NOSONAR
        int title = R.string.set_home_dir; //NOSONAR
        if (atHomeDirectory()) { //NOSONAR
            title = R.string.remove_home_dir; //NOSONAR
        } else if (hasHomeDir()) { //NOSONAR
            title = R.string.nav_home_dir; //NOSONAR
        } // NOSONAR
        return title; //NOSONAR
    } // NOSONAR

    private Comparator sizeComparator() { //NOSONAR
        return (Comparator<BaseFileObject>) (lhs, rhs) -> (int) (rhs.size - lhs.size); //NOSONAR
    } // NOSONAR

    private Comparator filenameComparator() { //NOSONAR
        return (Comparator<BaseFileObject>) (lhs, rhs) -> lhs.name.compareToIgnoreCase(rhs.name); //NOSONAR
    } // NOSONAR

    //    private Comparator durationComparator() { // NOSONAR
    //        return (Comparator<FileObject>) (lhs, rhs) -> (int) (rhs.duration - lhs.duration); // NOSONAR
    //    } // NOSONAR

    private Comparator trackNumberComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> lhs.tagInfo.trackNumber - rhs.tagInfo.trackNumber; //NOSONAR
    } // NOSONAR

    private Comparator folderCountComparator() { //NOSONAR
        return (Comparator<FolderObject>) (lhs, rhs) -> rhs.folderCount - lhs.folderCount; //NOSONAR
    } // NOSONAR

    private Comparator fileCountComparator() { //NOSONAR
        return (Comparator<FolderObject>) (lhs, rhs) -> rhs.fileCount - lhs.fileCount; //NOSONAR
    } // NOSONAR

    private Comparator artistNameComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> { //NOSONAR
            if (lhs.tagInfo.artistName == null || rhs.tagInfo.artistName == null) { //NOSONAR
                return nullCompare(lhs.tagInfo.artistName, rhs.tagInfo.artistName); //NOSONAR
            } // NOSONAR
            return lhs.tagInfo.artistName.compareToIgnoreCase(rhs.tagInfo.artistName); //NOSONAR
        }; // NOSONAR
    } // NOSONAR

    private Comparator albumNameComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> { //NOSONAR
            if (lhs.tagInfo.albumName == null || rhs.tagInfo.albumName == null) { //NOSONAR
                return nullCompare(lhs.tagInfo.albumName, rhs.tagInfo.albumName); //NOSONAR
            } // NOSONAR
            return lhs.tagInfo.albumName.compareToIgnoreCase(rhs.tagInfo.albumName); //NOSONAR
        }; // NOSONAR
    } // NOSONAR

    private Comparator trackNameComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> { //NOSONAR
            if (lhs.tagInfo.trackName == null || rhs.tagInfo.trackName == null) { //NOSONAR
                return nullCompare(lhs.tagInfo.trackName, rhs.tagInfo.trackName); //NOSONAR
            } // NOSONAR
            return lhs.tagInfo.trackName.compareToIgnoreCase(rhs.tagInfo.trackName); //NOSONAR
        }; // NOSONAR
    } // NOSONAR

    <T extends Comparable<T>> int nullCompare(T a, T b) { //NOSONAR
        return a == null ? (b == null ? 0 : Integer.MIN_VALUE) : (b == null ? Integer.MAX_VALUE : a.compareTo(b)); //NOSONAR
    } // NOSONAR
} // NOSONAR
