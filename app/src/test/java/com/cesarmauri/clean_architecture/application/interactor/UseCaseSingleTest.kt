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
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UseCaseSingleTest {

    companion object {
        private val TEST_PAYLOAD = Payload(1)
        private val TEST_PARAMS = UseCaseTestImpl.Params()
    }

    private lateinit var useCase: UseCaseTestImpl
    private lateinit var single: Single<Payload>

    @Mock
    private lateinit var disposable: DisposableSingleObserver<Payload>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        single = spy(Single.just(TEST_PAYLOAD))
        useCase = spy(UseCaseTestImpl(single))
    }

    @Test
    fun `single is built`() {
        useCase.execute(disposable, TEST_PARAMS)

        verify(useCase).buildUseCaseSingle(any())
        verify(useCase).addDisposable(any())
    }

    @Test
    fun `single works`() {
        useCase.execute(disposable, TEST_PARAMS)

        single.test().await().assertComplete().assertNoErrors()

        assertFalse(disposable.isDisposed)
        verify(disposable).onSuccess(eq(TEST_PAYLOAD))
        verify(disposable, times(0)).onError(any())
    }

    private class UseCaseTestImpl(private val single: Single<Payload>) :
        UseCaseSingle<Payload, UseCaseTestImpl.Params>(SchedulersProviderTest()) {
        override fun buildUseCaseSingle(params: Params): Single<Payload> = single
        class Params
    }

    private data class Payload(val id: Int)
}