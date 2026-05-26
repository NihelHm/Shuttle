@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Build
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

class ResumingServiceManager(val lifecycle: Lifecycle, val analyticsManager: AnalyticsManager) : LifecycleObserver { //NOSONAR

    init { //NOSONAR
        lifecycle.addObserver(this) //NOSONAR
    }

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
                            }

                    ).addTo(disposable) //NOSONAR
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) //NOSONAR
    fun stopped() { //NOSONAR
        disposable.clear() //NOSONAR
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) //NOSONAR
    fun destroy() { //NOSONAR
        lifecycle.removeObserver(this) //NOSONAR
    }
}
