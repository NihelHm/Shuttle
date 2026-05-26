package com.simplecity.amp_library.saf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.utils.SettingsManager;
import dagger.android.support.AndroidSupportInjection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SafManager { //NOSONAR

    private static final String TAG = "SafManager"; //NOSONAR

    private Context applicationContext; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private static SafManager instance; //NOSONAR

    static final int DOCUMENT_TREE_REQUEST_CODE = 901; //NOSONAR

    public static SafManager getInstance(Context context, SettingsManager settingsManager) { //NOSONAR
        if (instance == null) { //NOSONAR
            instance = new SafManager(context, settingsManager); //NOSONAR
        }
        return instance; //NOSONAR
    }

    private SafManager(Context context, SettingsManager settingsManager) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    /**
     * Check whether files require Storage Access Framework (SAF) / DocumentsProvider for access
     *
     * @param files the files to check
     * @return true if files are located on the SD Card (and thus require the SAF and DocumentsProvider for access)
     */
    public boolean requiresPermission(List<File> files) { //NOSONAR
        for (File file : files) { //NOSONAR
            if (requiresPermission(file)) { //NOSONAR
                return true; //NOSONAR
            }
        }
        return false; //NOSONAR
    }

    /**
     * Checks whether a file requires Storage Access Framework (SAF) / DocumentsProvider for access
     *
     * @param file the File to check
     * @return true if the file is located on the SD Card (and thus require the SAF and DocumentsProvider for access)
     */
    public boolean requiresPermission(File file) { //NOSONAR
        return getExtSdCardFolder(file) != null; //NOSONAR
    }

    /**
     * Checks the passed in paths to see whether the file at the given path is available in our
     * document tree. If it is, and we have write permission, the document file is added to the
     * passed in list of document files.
     */
    public List<DocumentFile> getWriteableDocumentFiles(List<File> files) { //NOSONAR

        List<DocumentFile> documentFiles = new ArrayList<>(); //NOSONAR

        String treeUri = getDocumentTree(); //NOSONAR
        if (treeUri == null) { //NOSONAR
            //We don't have any document tree at all - so we're not going to have permission for any files.
            return documentFiles; //NOSONAR
        }

        //Find the file in the document tree. If it's not there, or it doesn't have permission,
        //we're satisfied we don't have permission.
        for (File file : files) { //NOSONAR
            DocumentFile documentFile = getWriteableDocumentFile(file); //NOSONAR
            if (documentFile != null && documentFile.canWrite()) { //NOSONAR
                documentFiles.add(documentFile); //NOSONAR
            }
        }

        return documentFiles; //NOSONAR
    }

    /**
     * Retrieve a DocumentFile for the passed in file, or null if it can't be created, or is not writeable.
     *
     * @param file File
     * @return a DocumentFile for the passed in file, or null if it can't be created, or is not writeable.
     */
    @Nullable //NOSONAR
    public DocumentFile getWriteableDocumentFile(File file) { //NOSONAR
        DocumentFile documentFile = getDocumentFile(file); //NOSONAR
        if (documentFile != null && documentFile.canWrite()) { //NOSONAR
            return documentFile; //NOSONAR
        }
        return null; //NOSONAR
    }

    /**
     * Retrieve a DocumentFile for the passed in File, or null it can't be created.
     *
     * @param file File
     * @return a DocumentFile for the passed in File, or null it can't be created.
     */
    @Nullable //NOSONAR
    public DocumentFile getDocumentFile(final File file) { //NOSONAR
        String baseFolder = getExtSdCardFolder(file); //NOSONAR

        if (baseFolder == null) { //NOSONAR
            return null; //NOSONAR
        }

        String treeUri = getDocumentTree(); //NOSONAR
        if (treeUri == null) { //NOSONAR
            return null; //NOSONAR
        }

        String relativePath; //NOSONAR
        try { //NOSONAR
            String fullPath = file.getCanonicalPath(); //NOSONAR
            relativePath = fullPath.substring(baseFolder.length() + 1); //NOSONAR
        } catch (IOException e) { //NOSONAR
            return null; //NOSONAR
        }

        // Start with root of SD card and then parse through document tree.
        DocumentFile document = DocumentFile.fromTreeUri(applicationContext, Uri.parse(treeUri)); //NOSONAR

        String[] parts = relativePath.split("/"); //NOSONAR
        for (String part : parts) { //NOSONAR
            DocumentFile nextDocument = document.findFile(part); //NOSONAR
            if (nextDocument != null) { //NOSONAR
                document = nextDocument; //NOSONAR
            }
        }
        if (document.isFile()) { //NOSONAR
            return document; //NOSONAR
        }
        return null; //NOSONAR
    }

    /**
     * @return the persisted document tree, or null if it does not exist.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT) //NOSONAR
    @Nullable //NOSONAR
    public String getDocumentTree() { //NOSONAR
        String treeUri = settingsManager.getDocumentTreeUri(); //NOSONAR
        List<UriPermission> perms = applicationContext.getContentResolver().getPersistedUriPermissions(); //NOSONAR
        for (UriPermission perm : perms) { //NOSONAR
            if (perm.getUri().toString().equals(treeUri) && perm.isWritePermission()) { //NOSONAR
                return treeUri; //NOSONAR
            }
        }
        return null; //NOSONAR
    }

    /**
     * Check whether the file is stored on an SD Card, and if so, return the SD Card path
     *
     * @param file File
     * @return the SD Card path, or null if none is found.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT) //NOSONAR
    @Nullable //NOSONAR
    private String getExtSdCardFolder(final File file) { //NOSONAR
        List<String> extSdPaths = getExtSdCardPaths(); //NOSONAR
        try { //NOSONAR
            for (String extSdPath : extSdPaths) { //NOSONAR
                if (file.getCanonicalPath().startsWith(extSdPath)) { //NOSONAR
                    return extSdPath; //NOSONAR
                }
            }
        } catch (IOException e) { //NOSONAR
            return null; //NOSONAR
        }
        return null; //NOSONAR
    }

    /**
     * @return a list of potential SD Card paths
     */
    @TargetApi(Build.VERSION_CODES.KITKAT) //NOSONAR
    private List<String> getExtSdCardPaths() { //NOSONAR
        List<String> paths = new ArrayList<>(); //NOSONAR
        try { //NOSONAR
            File[] externalFilesDirs = applicationContext.getExternalFilesDirs("external"); //NOSONAR
            if (externalFilesDirs != null && externalFilesDirs.length > 0) { //NOSONAR
                for (File file : externalFilesDirs) { //NOSONAR
                    if (file != null && !file.equals(applicationContext.getExternalFilesDir("external"))) { //NOSONAR
                        int index = file.getAbsolutePath().lastIndexOf("/Android/data"); //NOSONAR
                        if (index < 0) { //NOSONAR
                            Log.w(TAG, "Unexpected external file dir: " + file.getAbsolutePath()); //NOSONAR
                        } else { //NOSONAR
                            String path = file.getAbsolutePath().substring(0, index); //NOSONAR
                            try { //NOSONAR
                                path = new File(path).getCanonicalPath(); //NOSONAR
                            } catch (IOException e) { //NOSONAR
                                // Keep non-canonical path.
                            }
                            paths.add(path); //NOSONAR
                        }
                    }
                }
            }
        } catch (NoSuchMethodError e) { //NOSONAR
            Crashlytics.log("getExtSdCardPaths() failed. " + e.getMessage()); //NOSONAR
        }
        return paths; //NOSONAR
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) //NOSONAR
    public void openDocumentTreePicker(Activity activity) { //NOSONAR
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE); //NOSONAR
        if (intent.resolveActivity(applicationContext.getPackageManager()) != null) { //NOSONAR
            activity.startActivityForResult(intent, DOCUMENT_TREE_REQUEST_CODE); //NOSONAR
        } else { //NOSONAR
            Toast.makeText(activity, R.string.R_string_toast_no_document_provider, Toast.LENGTH_LONG).show(); //NOSONAR
        }
    }

    public static class SafDialog extends DialogFragment { //NOSONAR

        public static final String TAG = "SafDialog"; //NOSONAR

        @Inject //NOSONAR
        SettingsManager settingsManager; //NOSONAR

        public interface SafResultListener { //NOSONAR
            void onResult(@Nullable Uri treeUri); //NOSONAR
        }

        public SafDialog() { //NOSONAR
            // Intentionally left empty.
        }

        public static <T extends AppCompatActivity & SafResultListener> void show(T activity) { //NOSONAR
            new SafDialog().show(activity.getSupportFragmentManager(), TAG); //NOSONAR
        }

        public static <T extends Fragment & SafResultListener> void show(T fragment) { //NOSONAR
            new SafDialog().show(fragment.getChildFragmentManager(), TAG); //NOSONAR
        }

        @Nullable //NOSONAR
        private SafResultListener getListener() { //NOSONAR
            if (getParentFragment() instanceof SafResultListener) { //NOSONAR
                return (SafResultListener) getParentFragment(); //NOSONAR
            } else if (getActivity() instanceof SafResultListener) { //NOSONAR
                return (SafResultListener) getActivity(); //NOSONAR
            }
            return null; //NOSONAR
        }

        @NonNull //NOSONAR
        @Override //NOSONAR
        public Dialog onCreateDialog(Bundle savedInstanceState) { //NOSONAR

            AndroidSupportInjection.inject(this); //NOSONAR

            return new MaterialDialog.Builder(getContext()) //NOSONAR
                    .title(R.string.saf_access_required_title) //NOSONAR
                    .content(R.string.saf_access_required_message) //NOSONAR
                    .positiveText(R.string.saf_show_files_button) //NOSONAR
                    .onPositive((dialog, which) -> { //NOSONAR
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE); //NOSONAR
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) { //NOSONAR
                            startActivityForResult(intent, DOCUMENT_TREE_REQUEST_CODE); //NOSONAR
                        } else { //NOSONAR
                            Toast.makeText(getContext(), R.string.R_string_toast_no_document_provider, Toast.LENGTH_LONG).show(); //NOSONAR
                        }
                    })
                    .autoDismiss(false) //NOSONAR
                    .build(); //NOSONAR
        }

        @Override //NOSONAR
        public void onActivityResult(int requestCode, int resultCode, Intent data) { //NOSONAR
            super.onActivityResult(requestCode, resultCode, data); //NOSONAR
            SafResultListener listener = getListener(); //NOSONAR
            if (requestCode == DOCUMENT_TREE_REQUEST_CODE) { //NOSONAR
                if (resultCode == Activity.RESULT_OK) { //NOSONAR
                    Uri treeUri = data.getData(); //NOSONAR
                    if (treeUri != null) { //NOSONAR
                        getContext().getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION); //NOSONAR
                        settingsManager.setDocumentTreeUri(data.getData().toString()); //NOSONAR
                        if (listener != null) { //NOSONAR
                            listener.onResult(treeUri); //NOSONAR
                        }
                    }
                } else { //NOSONAR
                    if (listener != null) { //NOSONAR
                        listener.onResult(null); //NOSONAR
                    }
                }
                dismiss(); //NOSONAR
            }
        }
    }
}
