@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils // NOSONAR

import android.arch.lifecycle.Lifecycle // NOSONAR
import android.arch.lifecycle.LifecycleObserver // NOSONAR
import android.arch.lifecycle.OnLifecycleEvent // NOSONAR
import android.content.Context // NOSONAR
import android.content.Intent // NOSONAR
import android.os.Build // NOSONAR
import io.reactivex.Single // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.rxkotlin.addTo // NOSONAR
import io.reactivex.rxkotlin.subscribeBy // NOSONAR
import java.util.concurrent.TimeUnit // NOSONAR

class ResumingServiceManager(val lifecycle: Lifecycle, val analyticsManager: AnalyticsManager) : LifecycleObserver { //NOSONAR

    init { //NOSONAR
        lifecycle.addObserver(this) //NOSONAR
    } // NOSONAR

    val disposable: CompositeDisposable = CompositeDisposable() //NOSONAR

    fun startService(context: Context, intent: Intent, completion: (() -> Unit)? = null) { //NOSONAR
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //NOSONAR
            context.startService(intent) //NOSONAR
            completion?.invoke() //NOSONAR
        } else { //NOSONAR
            Single.just(true) //NOSONAR
                    .delaySubscription(300, TimeUnit.MILLISECONDS) //NOSONAR
                    .subscribeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribeBy( //NOSONAR
                            onSuccess = { //NOSONAR
                                analyticsManager.dropBreadcrumb("ResumingServiceManager", "Starting service after 300ms delay") //NOSONAR
                                context.startService(intent) //NOSONAR
                                completion?.invoke() //NOSONAR
                            } // NOSONAR

                    ).addTo(disposable) //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) //NOSONAR
    fun stopped() { //NOSONAR
        disposable.clear() //NOSONAR
    } // NOSONAR

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) //NOSONAR
    fun destroy() { //NOSONAR
        lifecycle.removeObserver(this) //NOSONAR
    } // NOSONAR
} // NOSONAR
