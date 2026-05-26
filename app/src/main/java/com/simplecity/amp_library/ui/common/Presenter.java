package com.simplecity.amp_library.ui.common; // NOSONAR

import android.support.annotation.CallSuper; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Presenter<V> { //NOSONAR

    @NonNull //NOSONAR
    private final CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    protected void addDisposable(@NonNull Disposable disposable) { //NOSONAR
        this.disposables.add(disposable); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    private V view; //NOSONAR

    @Nullable //NOSONAR
    protected V getView() { //NOSONAR
        return view; //NOSONAR
    } // NOSONAR

    @CallSuper //NOSONAR
    public void bindView(@NonNull V view) { //NOSONAR

        final V previousView = this.view; //NOSONAR

        if (previousView != null) { //NOSONAR
            throw new IllegalStateException("Previous view is not unbound! previousView = " + previousView); //NOSONAR
        } // NOSONAR

        this.view = view; //NOSONAR
    } // NOSONAR

    @CallSuper //NOSONAR
    public void unbindView(@NonNull V view) { //NOSONAR
        final V previousView = this.view; //NOSONAR

        if (previousView == view) { //NOSONAR
            this.view = null; //NOSONAR
        } else { //NOSONAR
            throw new IllegalStateException("Unexpected view! previousView = " + previousView + ", view to unbind = " + view); //NOSONAR
        } // NOSONAR

        // Unsubscribe all disposables that need to be unsubscribed in this lifecycle state. // NOSONAR
        disposables.clear(); //NOSONAR
    } // NOSONAR
} // NOSONAR
