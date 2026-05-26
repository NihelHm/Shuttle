package com.simplecity.amp_library.utils; // NOSONAR

import android.Manifest; // NOSONAR
import com.greysonparrelli.permiso.Permiso; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PermissionUtils { //NOSONAR

    private PermissionUtils() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public interface PermissionCallback { //NOSONAR
        void onSuccess(); //NOSONAR
    } // NOSONAR

    private static void simplePermissionRequest(final PermissionCallback callback, String... permissions) { //NOSONAR
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() { //NOSONAR
            @Override //NOSONAR
            public void onPermissionResult(Permiso.ResultSet resultSet) { //NOSONAR
                if (resultSet.areAllPermissionsGranted()) { //NOSONAR
                    callback.onSuccess(); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) { //NOSONAR
                callback.onRationaleProvided(); //NOSONAR
            } // NOSONAR
        }, permissions); //NOSONAR
    } // NOSONAR

    public static void RequestStoragePermissions(final PermissionCallback callback) { //NOSONAR
        simplePermissionRequest(callback, Manifest.permission.WRITE_EXTERNAL_STORAGE); //NOSONAR
    } // NOSONAR
} // NOSONAR
