package com.simplecity.amp_library.utils;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.interfaces.FileType;
import com.simplecity.amp_library.model.BaseFileObject;
import com.simplecity.amp_library.model.FileObject;
import com.simplecity.amp_library.model.FolderObject;
import com.simplecity.amp_library.model.TagInfo;
import com.simplecity.amp_library.utils.sorting.SortManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FileBrowser { //NOSONAR

    private static final String TAG = "FileBrowser"; //NOSONAR

    @Nullable //NOSONAR
    private File currentDir; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    public FileBrowser(SettingsManager settingsManager) { //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    /**
     * Loads the specified folder.
     *
     * @param directory The file object to points to the directory to load.
     * @return An {@link List<BaseFileObject>} object that holds the data of the specified directory.
     */
    @WorkerThread //NOSONAR
    public List<BaseFileObject> loadDir(File directory) { //NOSONAR

        ThreadUtils.ensureNotOnMainThread(); //NOSONAR

        currentDir = directory; //NOSONAR

        List<BaseFileObject> folderObjects = new ArrayList<>(); //NOSONAR
        List<BaseFileObject> fileObjects = new ArrayList<>(); //NOSONAR

        //Grab a list of all files/subdirs within the specified directory.
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
                            }
                        }
                    } else { //NOSONAR
                        continue; //NOSONAR
                    }
                    if (!folderObjects.contains(baseFileObject)) { //NOSONAR
                        folderObjects.add(baseFileObject); //NOSONAR
                    }
                } else { //NOSONAR
                    baseFileObject = new FileObject(); //NOSONAR
                    baseFileObject.path = FileHelper.getPath(file); //NOSONAR
                    baseFileObject.name = FileHelper.getName(file.getName()); //NOSONAR
                    baseFileObject.size = file.length(); //NOSONAR
                    ((FileObject) baseFileObject).extension = FileHelper.getExtension(file.getName()); //NOSONAR
                    if (TextUtils.isEmpty(((FileObject) baseFileObject).extension)) { //NOSONAR
                        continue; //NOSONAR
                    }
                    ((FileObject) baseFileObject).tagInfo = new TagInfo(baseFileObject.path); //NOSONAR

                    if (!fileObjects.contains(baseFileObject)) { //NOSONAR
                        fileObjects.add(baseFileObject); //NOSONAR
                    }
                }
            }
        }

        sortFileObjects(fileObjects); //NOSONAR
        sortFolderObjects(folderObjects); //NOSONAR

        if (!settingsManager.getFolderBrowserFilesAscending()) { //NOSONAR
            Collections.reverse(fileObjects); //NOSONAR
        }

        if (!settingsManager.getFolderBrowserFoldersAscending()) { //NOSONAR
            Collections.reverse(folderObjects); //NOSONAR
        }

        folderObjects.addAll(fileObjects); //NOSONAR

        if (!FileHelper.isRootDirectory(currentDir)) { //NOSONAR
            FolderObject parentObject = new FolderObject(); //NOSONAR
            parentObject.fileType = FileType.PARENT; //NOSONAR
            parentObject.name = FileHelper.PARENT_DIRECTORY; //NOSONAR
            parentObject.path = FileHelper.getPath(currentDir) + "/" + FileHelper.PARENT_DIRECTORY; //NOSONAR
            folderObjects.add(0, parentObject); //NOSONAR
        }

        return folderObjects; //NOSONAR
    }

    @Nullable //NOSONAR
    public File getCurrentDir() { //NOSONAR
        return currentDir; //NOSONAR
    }

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
            }
        }

        dir = new File("/"); //NOSONAR

        files = dir.list((dir1, filename) -> dir1.isDirectory() && filename.toLowerCase().contains("storage")); //NOSONAR

        if (files != null && files.length > 0) { //NOSONAR
            dir = new File(dir + "/" + files[0]); //NOSONAR
            //If there's an extsdcard path in our base dir, let's navigate to that. External SD cards are cool.
            files = dir.list((dir1, filename) -> dir1.isDirectory() && filename.toLowerCase().contains("extsdcard")); //NOSONAR
            if (files != null && files.length > 0) { //NOSONAR
                dir = new File(dir + "/" + files[0]); //NOSONAR
            } else { //NOSONAR
                //If we have external storage, use that as our initial dir
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) { //NOSONAR
                    dir = Environment.getExternalStorageDirectory(); //NOSONAR
                }
            }
        } else { //NOSONAR
            //If we have external storage, use that as our initial dir
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) { //NOSONAR
                dir = Environment.getExternalStorageDirectory(); //NOSONAR
            }
        }

        //Whether or not there was an sdcard, let's see if there's a 'music' dir for us to navigate to
        if (dir != null) { //NOSONAR
            files = dir.list((dir1, filename) -> dir1.isDirectory() && filename.toLowerCase().contains("music")); //NOSONAR
        }
        if (files != null && files.length > 0) { //NOSONAR
            dir = new File(dir + "/" + files[0]); //NOSONAR
        }

        return dir; //NOSONAR
    }

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
        }
    }

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
        }
    }

    public void clearHomeDir() { //NOSONAR
        settingsManager.setFolderBrowserInitialDir(""); //NOSONAR
    }

    public void setHomeDir() { //NOSONAR
        if (currentDir != null) { //NOSONAR
            settingsManager.setFolderBrowserInitialDir(currentDir.getPath()); //NOSONAR
        }
    }

    public File getHomeDir() { //NOSONAR
        return new File(settingsManager.getFolderBrowserInitialDir()); //NOSONAR
    }

    public boolean hasHomeDir() { //NOSONAR
        return !TextUtils.isEmpty(getHomeDir().getPath()); //NOSONAR
    }

    public boolean atHomeDirectory() { //NOSONAR
        final File currDir = getCurrentDir(); //NOSONAR
        final File homeDir = getHomeDir(); //NOSONAR
        return currDir != null && homeDir != null && currDir.compareTo(homeDir) == 0; //NOSONAR
    }

    public int getHomeDirIcon() { //NOSONAR
        int icon = R.drawable.ic_folder_outline; //NOSONAR
        if (atHomeDirectory()) { //NOSONAR
            icon = R.drawable.ic_folder_remove; //NOSONAR
        } else if (hasHomeDir()) { //NOSONAR
            icon = R.drawable.ic_folder_nav; //NOSONAR
        }
        return icon; //NOSONAR
    }

    public int getHomeDirTitle() { //NOSONAR
        int title = R.string.set_home_dir; //NOSONAR
        if (atHomeDirectory()) { //NOSONAR
            title = R.string.remove_home_dir; //NOSONAR
        } else if (hasHomeDir()) { //NOSONAR
            title = R.string.nav_home_dir; //NOSONAR
        }
        return title; //NOSONAR
    }

    private Comparator sizeComparator() { //NOSONAR
        return (Comparator<BaseFileObject>) (lhs, rhs) -> (int) (rhs.size - lhs.size); //NOSONAR
    }

    private Comparator filenameComparator() { //NOSONAR
        return (Comparator<BaseFileObject>) (lhs, rhs) -> lhs.name.compareToIgnoreCase(rhs.name); //NOSONAR
    }

    //    private Comparator durationComparator() {
    //        return (Comparator<FileObject>) (lhs, rhs) -> (int) (rhs.duration - lhs.duration);
    //    }

    private Comparator trackNumberComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> lhs.tagInfo.trackNumber - rhs.tagInfo.trackNumber; //NOSONAR
    }

    private Comparator folderCountComparator() { //NOSONAR
        return (Comparator<FolderObject>) (lhs, rhs) -> rhs.folderCount - lhs.folderCount; //NOSONAR
    }

    private Comparator fileCountComparator() { //NOSONAR
        return (Comparator<FolderObject>) (lhs, rhs) -> rhs.fileCount - lhs.fileCount; //NOSONAR
    }

    private Comparator artistNameComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> { //NOSONAR
            if (lhs.tagInfo.artistName == null || rhs.tagInfo.artistName == null) { //NOSONAR
                return nullCompare(lhs.tagInfo.artistName, rhs.tagInfo.artistName); //NOSONAR
            }
            return lhs.tagInfo.artistName.compareToIgnoreCase(rhs.tagInfo.artistName); //NOSONAR
        };
    }

    private Comparator albumNameComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> { //NOSONAR
            if (lhs.tagInfo.albumName == null || rhs.tagInfo.albumName == null) { //NOSONAR
                return nullCompare(lhs.tagInfo.albumName, rhs.tagInfo.albumName); //NOSONAR
            }
            return lhs.tagInfo.albumName.compareToIgnoreCase(rhs.tagInfo.albumName); //NOSONAR
        };
    }

    private Comparator trackNameComparator() { //NOSONAR
        return (Comparator<FileObject>) (lhs, rhs) -> { //NOSONAR
            if (lhs.tagInfo.trackName == null || rhs.tagInfo.trackName == null) { //NOSONAR
                return nullCompare(lhs.tagInfo.trackName, rhs.tagInfo.trackName); //NOSONAR
            }
            return lhs.tagInfo.trackName.compareToIgnoreCase(rhs.tagInfo.trackName); //NOSONAR
        };
    }

    <T extends Comparable<T>> int nullCompare(T a, T b) { //NOSONAR
        return a == null ? (b == null ? 0 : Integer.MIN_VALUE) : (b == null ? Integer.MAX_VALUE : a.compareTo(b)); //NOSONAR
    }
}
