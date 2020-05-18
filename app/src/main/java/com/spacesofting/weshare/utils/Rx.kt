package com.spacesofting.weshare.utils

import androidx.databinding.ObservableField
import com.google.android.gms.common.api.ApiException

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

/*fun <T> mapAndLogErrorObservable(messagesFactory: MessagesFactory) = ObservableTransformer<T, T> { observable ->
    observable.onErrorResumeNext { t: Throwable ->
        Observable.error(mapAndLogError(t, messagesFactory))
    }
}

fun <T> mapAndLogErrorSingle(messagesFactory: MessagesFactory) = SingleTransformer<T, T> { single ->
    single.onErrorResumeNext {
        Single.error(mapAndLogError(it, messagesFactory))
    }
}

fun mapAndLogErrorCompletable(messagesFactory: MessagesFactory) = CompletableTransformer { completable ->
    completable.onErrorResumeNext {
        Completable.error(mapAndLogError(it, messagesFactory))
    }
}*/

/*private fun mapAndLogError(throwable: Throwable, messagesFactory: MessagesFactory): AppError {
    return if (throwable is AppError) {
        throwable
    } else {
        Timber.e(throwable)
        mapThrowable(throwable, messagesFactory)
    }
}*/
/*
fun mapThrowable(throwable: Throwable, messagesFactory: MessagesFactory? = null): AppError {
    val message = messagesFactory?.create(throwable) ?: ""
    var xbetErrors: List<ApiError> = emptyList()
    var validationErrors: Map<String, List<ApiError>> = emptyMap()
    if (throwable is ApiException) {
        xbetErrors = throwable.apiError.errors[XBET_ERRORS_KEY] ?: xbetErrors
        validationErrors = throwable.apiError.errors.filter { it.key != XBET_ERRORS_KEY }
    }
    return AppError(message, throwable, xbetErrors, validationErrors)
}*/

fun searchObservable(field: ObservableField<String>): Observable<String> {
    return Observable.create { emitter ->
        val propertyChangedCallback = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: androidx.databinding.Observable, i: Int) {
                if (!emitter.isDisposed) {
                    emitter.onNext(field.get() ?: "")
                }
            }
        }
        field.addOnPropertyChangedCallback(propertyChangedCallback)
        emitter.setDisposable(Disposables.fromRunnable {
            field.removeOnPropertyChangedCallback(propertyChangedCallback)
        })
    }
}

fun <T> Observable<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> =
    this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Flowable<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> =
    this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Single<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Single<T> =
    this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Maybe<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Maybe<T> =
    this.subscribeOn(subscribeOn).observeOn(observeOn)

fun Completable.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Completable =
    this.subscribeOn(subscribeOn).observeOn(observeOn)