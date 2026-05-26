package com.simplecity.amp_library.ui.screens.tagger; // NOSONAR

import android.annotation.TargetApi; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.UriPermission; // NOSONAR
import android.net.Uri; // NOSONAR
import android.os.Build; // NOSONAR
import android.support.v4.provider.DocumentFile; // NOSONAR
import android.util.Log; // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog; // NOSONAR
import com.crashlytics.android.Crashlytics; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import java.io.File; // NOSONAR
import java.io.FileInputStream; // NOSONAR
import java.io.FileOutputStream; // NOSONAR
import java.io.IOException; // NOSONAR
import java.nio.channels.FileChannel; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TaggerUtils { //NOSONAR

    private static final String TAG = "TaggerUtils"; //NOSONAR

    //This class is never instantiated // NOSONAR
    private TaggerUtils() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) //NOSONAR
    public static String getDocumentTree(Context context, SettingsManager settingsManager) { //NOSONAR
        String treeUri = settingsManager.getDocumentTreeUri(); //NOSONAR
        List<UriPermission> perms = context.getContentResolver().getPersistedUriPermissions(); //NOSONAR
        for (UriPermission perm : perms) { //NOSONAR
            if (perm.getUri().toString().equals(treeUri) && perm.isWritePermission()) return treeUri; //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Checks the passed in paths to see whether the file at the given path is available in our // NOSONAR
     * document tree. If it is, and we have write permission, the document file is added to the // NOSONAR
     * passed in list of document files. // NOSONAR
     * // NOSONAR
     * @param documentFiles a list of document files to be populated // NOSONAR
     * @param paths a list of paths // NOSONAR
     * @return true if we have permission for all files at the passed in paths // NOSONAR
     */ // NOSONAR
    static boolean hasDocumentTreePermission(Context context, SettingsManager settingsManager, List<DocumentFile> documentFiles, List<String> paths) { //NOSONAR

        boolean hasDocumentTreePermission = false; //NOSONAR

        String treeUri = getDocumentTree(context, settingsManager); //NOSONAR
        if (treeUri == null) { //NOSONAR
            //We don't have any document tree at all - so we're not going to have permission for any files. // NOSONAR
            return false; //NOSONAR
        } // NOSONAR

        //Find the file in the document tree. If it's not there, or it doesn't have permission, // NOSONAR
        //we're satisfied we don't have permission. // NOSONAR
        for (String path : paths) { //NOSONAR
            File file = new File(path); //NOSONAR
            DocumentFile documentFile = getDocumentFile(context, Uri.parse(treeUri), file); //NOSONAR
            if (documentFile != null) { //NOSONAR
                hasDocumentTreePermission = documentFile.canWrite(); //NOSONAR
            } // NOSONAR
            if (hasDocumentTreePermission) { //NOSONAR
                documentFiles.add(documentFile); //NOSONAR
            } else { //NOSONAR
                documentFiles.clear(); //NOSONAR
                break; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return hasDocumentTreePermission; //NOSONAR
    } // NOSONAR

    static DocumentFile getDocumentFile(Context context, Uri treeUri, final File file) { //NOSONAR
        String baseFolder = getExtSdCardFolder(context, file); //NOSONAR

        if (baseFolder == null) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        if (treeUri == null) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        String relativePath; //NOSONAR
        try { //NOSONAR
            String fullPath = file.getCanonicalPath(); //NOSONAR
            relativePath = fullPath.substring(baseFolder.length() + 1); //NOSONAR
        } catch (IOException e) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        // start with root of SD card and then parse through document tree. // NOSONAR
        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri); //NOSONAR

        String[] parts = relativePath.split("/"); //NOSONAR
        for (String part : parts) { //NOSONAR
            DocumentFile nextDocument = document.findFile(part); //NOSONAR
            if (nextDocument != null) { //NOSONAR
                document = nextDocument; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (document.isFile()) { //NOSONAR
            return document; //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @TargetApi(Build.VERSION_CODES.KITKAT) //NOSONAR
    static String getExtSdCardFolder(Context context, File file) { //NOSONAR
        String[] extSdPaths = getExtSdCardPaths(context); //NOSONAR
        try { //NOSONAR
            for (String extSdPath : extSdPaths) { //NOSONAR
                if (file.getCanonicalPath().startsWith(extSdPath)) { //NOSONAR
                    return extSdPath; //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } catch (IOException e) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    static boolean requiresPermission(Context context, List<String> paths) { //NOSONAR
        boolean requiresPermission = false; //NOSONAR
        for (String path : paths) { //NOSONAR
            File file = new File(path); //NOSONAR
            requiresPermission = getExtSdCardFolder(context, file) != null; //NOSONAR
            if (requiresPermission) { //NOSONAR
                break; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        return requiresPermission; //NOSONAR
    } // NOSONAR

    @TargetApi(Build.VERSION_CODES.KITKAT) //NOSONAR
    static String[] getExtSdCardPaths(Context context) { //NOSONAR
        List<String> paths = new ArrayList<>(); //NOSONAR
        try { //NOSONAR
            File[] externalFilesDirs = context.getExternalFilesDirs("external"); //NOSONAR
            if (externalFilesDirs != null && externalFilesDirs.length > 0) { //NOSONAR
                for (File file : externalFilesDirs) { //NOSONAR
                    if (file != null && !file.equals(context.getExternalFilesDir("external"))) { //NOSONAR
                        int index = file.getAbsolutePath().lastIndexOf("/Android/data"); //NOSONAR
                        if (index < 0) { //NOSONAR
                            Log.w(TAG, "Unexpected external file dir: " + file.getAbsolutePath()); //NOSONAR
                        } else { //NOSONAR
                            String path = file.getAbsolutePath().substring(0, index); //NOSONAR
                            try { //NOSONAR
                                path = new File(path).getCanonicalPath(); //NOSONAR
                            } catch (IOException e) { //NOSONAR
                                // Keep non-canonical path. // NOSONAR
                            } // NOSONAR
                            paths.add(path); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } catch (NoSuchMethodError e) { //NOSONAR
            Crashlytics.log("getExtSdCardPaths() failed. " + e.getMessage()); //NOSONAR
        } // NOSONAR
        return paths.toArray(new String[paths.size()]); //NOSONAR
    } // NOSONAR

    static void copyFile(File sourceFile, File destFile) throws IOException { //NOSONAR
        if (!destFile.getParentFile().exists()) { //NOSONAR
            destFile.getParentFile().mkdirs(); //NOSONAR
        } // NOSONAR

        if (!destFile.exists()) { //NOSONAR
            destFile.createNewFile(); //NOSONAR
        } // NOSONAR

        FileChannel source = null; //NOSONAR
        FileChannel destination = null; //NOSONAR

        try { //NOSONAR
            source = new FileInputStream(sourceFile).getChannel(); //NOSONAR
            destination = new FileOutputStream(destFile).getChannel(); //NOSONAR
            destination.transferFrom(source, 0, source.size()); //NOSONAR
        } finally { //NOSONAR
            if (source != null) { //NOSONAR
                source.close(); //NOSONAR
            } // NOSONAR
            if (destination != null) { //NOSONAR
                destination.close(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    static void copyFile(File sourceFile, FileOutputStream outputStream) throws IOException { //NOSONAR

        FileChannel source = null; //NOSONAR
        FileChannel destination = null; //NOSONAR

        try { //NOSONAR
            source = new FileInputStream(sourceFile).getChannel(); //NOSONAR
            destination = outputStream.getChannel(); //NOSONAR
            destination.transferFrom(source, 0, source.size()); //NOSONAR
        } finally { //NOSONAR
            if (source != null) { //NOSONAR
                source.close(); //NOSONAR
            } // NOSONAR
            if (destination != null) { //NOSONAR
                destination.close(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    static void showChooseDocumentDialog(Context context, MaterialDialog.SingleButtonCallback listener, boolean hasChecked) { //NOSONAR
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context) //NOSONAR
                .title(R.string.edit_tags) //NOSONAR
                .content(hasChecked ? R.string.tag_editor_document_tree_permission_failed : R.string.tag_editor_document_tree_message) //NOSONAR
                .positiveText(R.string.button_ok) //NOSONAR
                .onPositive(listener); //NOSONAR
        if (hasChecked) { //NOSONAR
            builder.negativeText(R.string.cancel); //NOSONAR
        } // NOSONAR
        builder.show(); //NOSONAR
    } // NOSONAR
} // NOSONAR
