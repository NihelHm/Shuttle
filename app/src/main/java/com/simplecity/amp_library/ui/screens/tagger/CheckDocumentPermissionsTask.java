package com.simplecity.amp_library.ui.screens.tagger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.provider.DocumentFile;
import com.simplecity.amp_library.utils.SettingsManager;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class CheckDocumentPermissionsTask extends AsyncTask<Void, Void, Boolean> {

    public interface PermissionCheckListener {
        void onPermissionCheck(boolean hasPermission);
    }

    private Context applicationContext;

    private SettingsManager settingsManager;

    private List<String> paths;
    private List<DocumentFile> documentFiles;

    private PermissionCheckListener listener;

    public CheckDocumentPermissionsTask(Context context, SettingsManager settingsManager, List<String> paths, List<DocumentFile> documentFiles, PermissionCheckListener listener) {
        this.applicationContext = context.getApplicationContext();
        this.settingsManager = settingsManager;
        this.paths = paths;
        this.documentFiles = documentFiles;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return !TaggerUtils.requiresPermission(applicationContext, paths) || TaggerUtils.hasDocumentTreePermission(applicationContext, settingsManager, documentFiles, paths);
    }

    @Override
    protected void onPostExecute(Boolean hasPermission) {
        super.onPostExecute(hasPermission);

        if (listener != null) {
            listener.onPermissionCheck(hasPermission);
        }
    }
}
