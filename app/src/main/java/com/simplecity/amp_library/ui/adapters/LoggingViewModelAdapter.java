package com.simplecity.amp_library.ui.adapters;

import android.support.annotation.Nullable;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.simplecityapps.recycler_adapter.adapter.CompletionListUpdateCallback;
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;
import io.reactivex.disposables.Disposable;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class LoggingViewModelAdapter extends ViewModelAdapter {

    private static final String TAG = "LoggingVMAdapter";

    String tag;

    public LoggingViewModelAdapter(String tag) {
        this.tag = tag;
    }

    @Nullable
    @Override
    public synchronized Disposable setItems(List<ViewModel> items, @Nullable CompletionListUpdateCallback callback) {

        Crashlytics.log(Log.DEBUG, TAG, String.format("setItems called for: '%s'", tag));

        return super.setItems(items, new CompletionListUpdateCallback() {

            @Override
            public void onComplete() {

                Crashlytics.log(Log.DEBUG, TAG, String.format("setItems complete for: '%s'. Dispatching updates.", tag));

                if (callback != null) {
                    callback.onComplete();
                }
            }

            @Override
            public void onInserted(int position, int count) {
                if (callback != null) {
                    callback.onInserted(position, count);
                }
            }

            @Override
            public void onRemoved(int position, int count) {
                if (callback != null) {
                    callback.onRemoved(position, count);
                }
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                if (callback != null) {
                    callback.onMoved(fromPosition, toPosition);
                }
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                if (callback != null) {
                    callback.onChanged(position, count, payload);
                }
            }
        });
    }
}
