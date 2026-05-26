@file:Suppress("NOTHING_TO_INLINE") //NOSONAR

package com.simplecity.amp_library // NOSONAR

import android.util.Log // NOSONAR
import io.reactivex.* // NOSONAR

inline fun <reified T> printEvent(tag: String, success: T?, error: Throwable?) = //NOSONAR
        when { //NOSONAR
            success == null && error == null -> Log.d(tag, "Complete") /* Only with Maybe */ //NOSONAR
            success != null -> Log.d(tag, "Success $success") //NOSONAR
            error != null -> Log.d(tag, "Error $error") //NOSONAR
            else -> -1 /* Cannot happen*/ //NOSONAR
        } // NOSONAR
inline fun printEvent(tag: String, error: Throwable?) = //NOSONAR
        when { //NOSONAR
            error != null -> Log.d(tag, "Error $error") //NOSONAR
            else -> Log.d(tag, "Complete") //NOSONAR
        } // NOSONAR
/** // NOSONAR
 * Example usage of [log]: // NOSONAR
Single.timer(1, TimeUnit.SECONDS) // NOSONAR
.log() // NOSONAR
.subscribe({ // NOSONAR
    // Intentionally left empty. // NOSONAR
}, { // NOSONAR
    // Intentionally left empty. // NOSONAR
}) // NOSONAR
 */ // NOSONAR
inline fun tag() = //NOSONAR
        Thread.currentThread().stackTrace //NOSONAR
                .first { it.fileName.endsWith(".kt") } //NOSONAR
                .let { stack -> "Poo: ${stack.fileName.removeSuffix(".kt")}::${stack.methodName}:${stack.lineNumber}" } //NOSONAR
inline fun <reified T> Single<T>.log(): Single<T> { //NOSONAR
    val tag = tag() //NOSONAR
    return doOnEvent { success, error -> printEvent(tag, success, error) } //NOSONAR
            .doOnSubscribe { Log.d(tag, "Subscribe") } //NOSONAR
            .doOnDispose { Log.d(tag, "Dispose") } //NOSONAR
} // NOSONAR
inline fun <reified T> Maybe<T>.log(): Maybe<T> { //NOSONAR
    val tag = tag() //NOSONAR
    return doOnEvent { success, error -> printEvent(tag, success, error) } //NOSONAR
            .doOnSubscribe { Log.d(tag, "Subscribe") } //NOSONAR
            .doOnDispose { Log.d(tag, "Dispose") } //NOSONAR
} // NOSONAR
inline fun Completable.log(): Completable { //NOSONAR
    val tag = tag() //NOSONAR
    return doOnEvent { printEvent(tag, it) } //NOSONAR
            .doOnSubscribe { Log.d(tag, "Subscribe") } //NOSONAR
            .doOnDispose {Log.d(tag, "Dispose") } //NOSONAR
} // NOSONAR
inline fun <reified T> Observable<T>.log(): Observable<T> { //NOSONAR
    val line = tag() //NOSONAR
    return doOnEach { Log.d(line, "Each $it") } //NOSONAR
            .doOnSubscribe { Log.d(line, "Subscribe") } //NOSONAR
            .doOnDispose { Log.d(line, "Dispose") } //NOSONAR
} // NOSONAR
inline fun <reified T> Flowable<T>.log(): Flowable<T> { //NOSONAR
    val line = tag() //NOSONAR
    return doOnEach { Log.d(line, "Each $it") } //NOSONAR
            .doOnSubscribe { Log.d(line, "Subscribe") } //NOSONAR
            .doOnCancel { Log.d(line, "Cancel") } //NOSONAR
} // NOSONAR
