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

package com.cesarmauri.clean_architecture.presentation.presenter

import com.cesarmauri.clean_architecture.application.service.ErrorMessageFormatter
import com.cesarmauri.clean_architecture.application.exception.NetworkConnectionException
import com.cesarmauri.clean_architecture.application.interactor.GetBands
import com.cesarmauri.clean_architecture.application.navigation.Navigator
import com.cesarmauri.clean_architecture.domain.Band
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandsFragment
import com.nhaarman.mockitokotlin2.*
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BandsPresenterTest {

    private lateinit var presenter: BandsPresenter

    @Mock private lateinit var getBands: GetBands
    @Mock private lateinit var navigator: Navigator
    @Mock private lateinit var errorFormatter: ErrorMessageFormatter
    @Mock private lateinit var view: BandsFragment

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = BandsPresenter(getBands, navigator, errorFormatter)

        whenever(errorFormatter.getErrorMessage(any())).thenReturn("error")

        whenever(view.activity).thenReturn(mock())

        verifyZeroInteractions(getBands)
        verifyZeroInteractions(view)
        verifyZeroInteractions(navigator)
        verifyZeroInteractions(errorFormatter)
    }

    @Test
    fun `render band list`() {
        var response : DisposableSingleObserver<List<Band>>? = null
        whenever(getBands.execute(any(), any())).thenAnswer {
            response = it.getArgument(0)
            Unit
        }

        presenter.init(view)

        response!!.onSuccess(listOf())

        verify(view).render(any())

        verifyNoMoreInteractions(view)
        verifyZeroInteractions(navigator)
    }

    @Test
    fun `show error`() {
        var response : DisposableSingleObserver<List<Band>>? = null
        whenever(getBands.execute(any(), any())).thenAnswer {
            response = it.getArgument(0)
            Unit
        }

        presenter.init(view)

        response!!.onError(NetworkConnectionException())

        verify(view).notify(anyString())
        verify(view).close()

        verifyNoMoreInteractions(view)
        verifyZeroInteractions(navigator)
    }

    @Test
    fun `click band`() {
        val band = BandViewEntity(1, "name", "url")

        presenter.init(view)
        presenter.onBandClicked(band)

        verify(navigator).showBandDetails(any(), eq(band))
        verifyNoMoreInteractions(navigator)
    }
}
