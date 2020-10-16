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

import io.reactivex.disposables.Disposable
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class UseCaseTest {

    private lateinit var useCase: UseCaseImpl

    @Before
    fun setUp() {
        useCase = UseCaseImpl()
    }

    @Test
    fun `dispose works`() {
        val disposable = DisposableImpl()

        useCase.addDisposable(disposable)

        assertFalse(disposable.isDisposed)

        useCase.dispose()

        assertTrue(disposable.isDisposed)
    }

    private class DisposableImpl : Disposable {
        private var _isDisposed = false
        override fun dispose() { _isDisposed = true }
        override fun isDisposed(): Boolean = _isDisposed
    }

    private class UseCaseImpl : UseCase()
}