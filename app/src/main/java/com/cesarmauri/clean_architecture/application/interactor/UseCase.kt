/*
 *    Copyright 2020 by Cesar Mauri Loba
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cesarmauri.clean_architecture.application.interactor

import androidx.annotation.VisibleForTesting
import com.cesarmauri.clean_architecture.infrastructure.platform.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver

/*
 * Base classes for different kinds of reactive interactors
 */
abstract class UseCase {

    private val disposables = CompositeDisposable()

    fun dispose() = disposables.dispose()

    @VisibleForTesting
    internal fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    class None
}

abstract class UseCaseCompletable<Params>
    (private val schedulersProvider: SchedulersProvider) : UseCase() {

    abstract fun buildUseCaseCompletable(params: Params): Completable

    fun execute(observer: DisposableCompletableObserver, params: Params) {
        val observable = buildUseCaseCompletable(params)
            .subscribeOn(schedulersProvider.getIOScheduler())
            .observeOn(schedulersProvider.getUIScheduler())

        addDisposable(observable.subscribeWith(observer))
    }
}

abstract class UseCaseSingle<T, Params>
    (private val schedulersProvider: SchedulersProvider) : UseCase() {

    abstract fun buildUseCaseSingle(params: Params): Single<T>

    fun execute(observer: DisposableSingleObserver<T>, params: Params) {
        val observable = buildUseCaseSingle(params)
            .subscribeOn(schedulersProvider.getIOScheduler())
            .observeOn(schedulersProvider.getUIScheduler())

        addDisposable(observable.subscribeWith(observer))
    }
}

abstract class UseCaseObservable<T, Params>
    (private val schedulersProvider: SchedulersProvider) : UseCase() {

    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Params) {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(schedulersProvider.getIOScheduler())
            .observeOn(schedulersProvider.getUIScheduler())

        addDisposable(observable.subscribeWith(observer))
    }
}
