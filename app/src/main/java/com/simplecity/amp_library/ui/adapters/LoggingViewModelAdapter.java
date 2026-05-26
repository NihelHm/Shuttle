package com.simplecity.amp_library.ui.adapters; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.util.Log; // NOSONAR
import com.crashlytics.android.Crashlytics; // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallback; // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LoggingViewModelAdapter extends ViewModelAdapter { //NOSONAR

    private static final String TAG = "LoggingVMAdapter"; //NOSONAR

    String tag; //NOSONAR

    public LoggingViewModelAdapter(String tag) { //NOSONAR
        this.tag = tag; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public synchronized Disposable setItems(List<ViewModel> items, @Nullable CompletionListUpdateCallback callback) { //NOSONAR

        Crashlytics.log(Log.DEBUG, TAG, String.format("setItems called for: '%s'", tag)); //NOSONAR

        return super.setItems(items, new CompletionListUpdateCallback() { //NOSONAR

            @Override //NOSONAR
            public void onComplete() { //NOSONAR

                Crashlytics.log(Log.DEBUG, TAG, String.format("setItems complete for: '%s'. Dispatching updates.", tag)); //NOSONAR

                if (callback != null) { //NOSONAR
                    callback.onComplete(); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onInserted(int position, int count) { //NOSONAR
                if (callback != null) { //NOSONAR
                    callback.onInserted(position, count); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onRemoved(int position, int count) { //NOSONAR
                if (callback != null) { //NOSONAR
                    callback.onRemoved(position, count); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onMoved(int fromPosition, int toPosition) { //NOSONAR
                if (callback != null) { //NOSONAR
                    callback.onMoved(fromPosition, toPosition); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onChanged(int position, int count, Object payload) { //NOSONAR
                if (callback != null) { //NOSONAR
                    callback.onChanged(position, count, payload); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR
} // NOSONAR
