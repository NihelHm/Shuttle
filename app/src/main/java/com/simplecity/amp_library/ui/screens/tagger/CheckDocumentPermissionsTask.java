package com.simplecity.amp_library.ui.screens.tagger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.provider.DocumentFile;
import com.simplecity.amp_library.utils.SettingsManager;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CheckDocumentPermissionsTask extends AsyncTask<Void, Void, Boolean> { //NOSONAR

    public interface PermissionCheckListener { //NOSONAR
        void onPermissionCheck(boolean hasPermission); //NOSONAR
    }

    private Context applicationContext; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private List<String> paths; //NOSONAR
    private List<DocumentFile> documentFiles; //NOSONAR

    private PermissionCheckListener listener; //NOSONAR

    public CheckDocumentPermissionsTask(Context context, SettingsManager settingsManager, List<String> paths, List<DocumentFile> documentFiles, PermissionCheckListener listener) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
        this.paths = paths; //NOSONAR
        this.documentFiles = documentFiles; //NOSONAR
        this.listener = listener; //NOSONAR
    }

    @Override //NOSONAR
    protected Boolean doInBackground(Void... params) { //NOSONAR
        return !TaggerUtils.requiresPermission(applicationContext, paths) || TaggerUtils.hasDocumentTreePermission(applicationContext, settingsManager, documentFiles, paths); //NOSONAR
    }

    @Override //NOSONAR
    protected void onPostExecute(Boolean hasPermission) { //NOSONAR
        super.onPostExecute(hasPermission); //NOSONAR

        if (listener != null) { //NOSONAR
            listener.onPermissionCheck(hasPermission); //NOSONAR
        }
    }
}
