package com.simplecity.amp_library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.FolderObject;
import com.simplecity.amp_library.rx.UnsafeConsumer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.util.Collections;
import java.util.List;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient { //NOSONAR

    private static final String TAG = "CustomMediaScanner"; //NOSONAR

    public interface ScanCompletionListener { //NOSONAR
        void onPathScanned(String path); //NOSONAR

        void onScanCompleted(); //NOSONAR
    }

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
    }

    public static void scanFiles(Context context, List<String> paths, @Nullable ScanCompletionListener listener) { //NOSONAR
        CustomMediaScanner client = new CustomMediaScanner(context, paths, listener); //NOSONAR
        MediaScannerConnection connection = new MediaScannerConnection(context, client); //NOSONAR
        client.connection = connection; //NOSONAR
        connection.connect(); //NOSONAR
    }

    @Override //NOSONAR
    public void onMediaScannerConnected() { //NOSONAR
        scanNextPath(); //NOSONAR
    }

    @Override //NOSONAR
    public void onScanCompleted(String path, Uri uri) { //NOSONAR

        Log.d(TAG, "Scan complete. Path: " + path); //NOSONAR

        scanNextPath(); //NOSONAR
    }

    private void scanNextPath() { //NOSONAR
        if (nextPath >= paths.size()) { //NOSONAR
            scanComplete(applicationContext); //NOSONAR
            return; //NOSONAR
        }
        String path = paths.get(nextPath); //NOSONAR

        connection.scanFile(path, null); //NOSONAR
        nextPath++; //NOSONAR

        if (scanCompletionListener != null) { //NOSONAR
            if (handler != null) { //NOSONAR
                handler.post(() -> scanCompletionListener.onPathScanned(path)); //NOSONAR
            }
        }

        Log.d(TAG, "Scanning file: " + path); //NOSONAR
    }

    private void scanComplete(Context context) { //NOSONAR
        connection.disconnect(); //NOSONAR

        //Notify all media uris of change. This will in turn update any content observers.
        context.getContentResolver().notifyChange(Uri.parse("content://media"), null); //NOSONAR

        if (handler != null) { //NOSONAR
            handler.post(() -> { //NOSONAR
                if (scanCompletionListener != null) { //NOSONAR
                    scanCompletionListener.onScanCompleted(); //NOSONAR
                }
                cleanup(); //NOSONAR
            });
        }
    }

    private void cleanup() { //NOSONAR
        if (handler != null) { //NOSONAR
            handler.removeCallbacksAndMessages(null); //NOSONAR
            handler = null; //NOSONAR
        }
    }

    // To do later: Remove context requirement
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
                        }

                        @Override //NOSONAR
                        public void onScanCompleted() { //NOSONAR
                            if (dialog.isShowing()) { //NOSONAR
                                dialog.dismiss(); //NOSONAR
                            }
                        }
                    });
                });
    }

    public static void scanFile(Context context, String path, UnsafeConsumer<String> message) { //NOSONAR
        CustomMediaScanner.scanFiles(context, Collections.singletonList(path), new CustomMediaScanner.ScanCompletionListener() { //NOSONAR
            @Override //NOSONAR
            public void onPathScanned(String path) { //NOSONAR
                // Intentionally left empty.
            }

            @Override //NOSONAR
            public void onScanCompleted() { //NOSONAR
                message.accept(context.getString(R.string.scan_complete)); //NOSONAR
            }
        });
    }
}
