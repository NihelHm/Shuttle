package com.simplecity.amp_library.utils; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.content.Context; // NOSONAR
import android.media.MediaScannerConnection; // NOSONAR
import android.net.Uri; // NOSONAR
import android.os.Handler; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.util.Log; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.FolderObject; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeConsumer; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import java.io.File; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import me.zhanghai.android.materialprogressbar.MaterialProgressBar; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient { //NOSONAR

    private static final String TAG = "CustomMediaScanner"; //NOSONAR

    public interface ScanCompletionListener { //NOSONAR
        void onPathScanned(String path); //NOSONAR

        void onScanCompleted(); //NOSONAR
    } // NOSONAR

    private Context applicationContext; //NOSONAR

    private final List<String> paths; //NOSONAR

    @Nullable //NOSONAR
    private final ScanCompletionListener scanCompletionListener; //NOSONAR

    private MediaScannerConnection connection; //NOSONAR
    private int nextPath; //NOSONAR

    private Handler handler; //NOSONAR

    private CustomMediaScanner(Context context, List<String> paths, @Nullable ScanCompletionListener listener) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
        this.paths = paths; //NOSONAR
        scanCompletionListener = listener; //NOSONAR
        handler = new Handler(context.getMainLooper()); //NOSONAR
    } // NOSONAR

    public static void scanFiles(Context context, List<String> paths, @Nullable ScanCompletionListener listener) { //NOSONAR
        CustomMediaScanner client = new CustomMediaScanner(context, paths, listener); //NOSONAR
        MediaScannerConnection connection = new MediaScannerConnection(context, client); //NOSONAR
        client.connection = connection; //NOSONAR
        connection.connect(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onMediaScannerConnected() { //NOSONAR
        scanNextPath(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onScanCompleted(String path, Uri uri) { //NOSONAR

        Log.d(TAG, "Scan complete. Path: " + path); //NOSONAR

        scanNextPath(); //NOSONAR
    } // NOSONAR

    private void scanNextPath() { //NOSONAR
        if (nextPath >= paths.size()) { //NOSONAR
            scanComplete(applicationContext); //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        String path = paths.get(nextPath); //NOSONAR

        connection.scanFile(path, null); //NOSONAR
        nextPath++; //NOSONAR

        if (scanCompletionListener != null) { //NOSONAR
            if (handler != null) { //NOSONAR
                handler.post(() -> scanCompletionListener.onPathScanned(path)); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        Log.d(TAG, "Scanning file: " + path); //NOSONAR
    } // NOSONAR

    private void scanComplete(Context context) { //NOSONAR
        connection.disconnect(); //NOSONAR

        //Notify all media uris of change. This will in turn update any content observers. // NOSONAR
        context.getContentResolver().notifyChange(Uri.parse("content://media"), null); //NOSONAR

        if (handler != null) { //NOSONAR
            handler.post(() -> { //NOSONAR
                if (scanCompletionListener != null) { //NOSONAR
                    scanCompletionListener.onScanCompleted(); //NOSONAR
                } // NOSONAR
                cleanup(); //NOSONAR
            }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void cleanup() { //NOSONAR
        if (handler != null) { //NOSONAR
            handler.removeCallbacksAndMessages(null); //NOSONAR
            handler = null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    // To do later: Remove context requirement // NOSONAR
    public static Disposable scanFile(Context context, FolderObject folderObject) { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null); //NOSONAR
        TextView pathsTextView = view.findViewById(R.id.paths); //NOSONAR
        pathsTextView.setText(folderObject.path); //NOSONAR

        MaterialProgressBar indeterminateProgress = view.findViewById(R.id.indeterminateProgress); //NOSONAR
        MaterialProgressBar horizontalProgress = view.findViewById(R.id.horizontalProgress); //NOSONAR

        MaterialDialog dialog = new MaterialDialog.Builder(context) //NOSONAR
                .title(R.string.scanning) //NOSONAR
                .customView(view, false) //NOSONAR
                .negativeText(R.string.close) //NOSONAR
                .show(); //NOSONAR

        return FileHelper.getPathList(new File(folderObject.path), true, false) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(paths -> { //NOSONAR
                    ViewUtils.fadeOut(indeterminateProgress, null); //NOSONAR
                    ViewUtils.fadeIn(horizontalProgress, null); //NOSONAR
                    horizontalProgress.setMax(paths.size()); //NOSONAR

                    CustomMediaScanner.scanFiles(context, paths, new ScanCompletionListener() { //NOSONAR
                        @Override //NOSONAR
                        public void onPathScanned(String path) { //NOSONAR
                            horizontalProgress.setProgress(horizontalProgress.getProgress() + 1); //NOSONAR
                            pathsTextView.setText(path); //NOSONAR
                        } // NOSONAR

                        @Override //NOSONAR
                        public void onScanCompleted() { //NOSONAR
                            if (dialog.isShowing()) { //NOSONAR
                                dialog.dismiss(); //NOSONAR
                            } // NOSONAR
                        } // NOSONAR
                    }); // NOSONAR
                }); // NOSONAR
    } // NOSONAR

    public static void scanFile(Context context, String path, UnsafeConsumer<String> message) { //NOSONAR
        CustomMediaScanner.scanFiles(context, Collections.singletonList(path), new CustomMediaScanner.ScanCompletionListener() { //NOSONAR
            @Override //NOSONAR
            public void onPathScanned(String path) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onScanCompleted() { //NOSONAR
                message.accept(context.getString(R.string.scan_complete)); //NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR
} // NOSONAR
