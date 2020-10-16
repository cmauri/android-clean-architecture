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

import com.cesarmauri.clean_architecture.common.SchedulersProviderTest
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.observers.DisposableCompletableObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UseCaseCompletableTest {

    companion object {
        private val TEST_PARAMS = UseCaseTestImpl.Params()
    }

    private lateinit var useCase: UseCaseTestImpl
    private lateinit var completable: Completable

    @Mock
    private lateinit var disposable: DisposableCompletableObserver

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        completable = spy(Completable.fromAction {  })
        useCase = spy(UseCaseTestImpl(completable))
    }

    @Test
    fun `completable is built`() {
        useCase.execute(disposable, TEST_PARAMS)

        verify(useCase).buildUseCaseCompletable(any())
        verify(useCase).addDisposable(any())
    }

    @Test
    fun `completable works`() {
        useCase.execute(disposable, TEST_PARAMS)

        completable.test().await().assertComplete().assertNoErrors()

        assertFalse(disposable.isDisposed)
        verify(disposable).onComplete()
        verify(disposable, times(0)).onError(any())
    }

    private class UseCaseTestImpl(private val completable: Completable) :
        UseCaseCompletable<UseCaseTestImpl.Params>(SchedulersProviderTest()) {
        override fun buildUseCaseCompletable(params: Params): Completable = completable
        class Params
    }
}
