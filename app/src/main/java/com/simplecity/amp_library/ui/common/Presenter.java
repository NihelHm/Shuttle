package com.simplecity.amp_library.ui.common;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class Presenter<V> {

    @NonNull
    private final CompositeDisposable disposables = new CompositeDisposable();

    protected void addDisposable(@NonNull Disposable disposable) {
        this.disposables.add(disposable);
    }

    @Nullable
    private V view;

    @Nullable
    protected V getView() {
        return view;
    }

    @CallSuper
    public void bindView(@NonNull V view) {

        final V previousView = this.view;

        if (previousView != null) {
            throw new IllegalStateException("Previous view is not unbound! previousView = " + previousView);
        }

        this.view = view;
    }

    @CallSuper
    public void unbindView(@NonNull V view) {
        final V previousView = this.view;

        if (previousView == view) {
            this.view = null;
        } else {
            throw new IllegalStateException("Unexpected view! previousView = " + previousView + ", view to unbind = " + view);
        }

        // Unsubscribe all disposables that need to be unsubscribed in this lifecycle state.
        disposables.clear();
    }
}
