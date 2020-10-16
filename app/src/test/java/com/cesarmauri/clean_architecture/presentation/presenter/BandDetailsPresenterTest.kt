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

import com.cesarmauri.clean_architecture.application.exception.NetworkConnectionException
import com.cesarmauri.clean_architecture.application.exception.YouTubeNotInstalled
import com.cesarmauri.clean_architecture.application.interactor.GetBandDetails
import com.cesarmauri.clean_architecture.application.navigation.Navigator
import com.cesarmauri.clean_architecture.application.service.ErrorMessageFormatter
import com.cesarmauri.clean_architecture.domain.BandDetails
import com.cesarmauri.clean_architecture.presentation.entity.AlbumViewEntity
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandDetailsFragment
import com.nhaarman.mockitokotlin2.*
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.net.URL

class BandDetailsPresenterTest {

    companion object {
        val SAMPLE_BAND= BandDetails(0, "", "", "",
            0, null, listOf())
    }

    @Mock private lateinit var navigator: Navigator
    @Mock private lateinit var errorFormatter: ErrorMessageFormatter
    @Mock private lateinit var view: BandDetailsFragment
    @Mock private lateinit var getBandDetails: GetBandDetails

    private lateinit var presenter: BandDetailsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = BandDetailsPresenter(getBandDetails, navigator, errorFormatter)

        whenever(errorFormatter.getErrorMessage(any())).thenReturn("error")
        whenever(view.activity).thenReturn(mock())

        verifyZeroInteractions(getBandDetails)
    }

    @Test
    fun `render data`() {
        var response : DisposableSingleObserver<BandDetails>? = null
        whenever(getBandDetails.execute(any(), any())).thenAnswer {
            response = it.getArgument(0)
            Unit
        }

        presenter.init(view, BandViewEntity(1, "name", ""))

        response!!.onSuccess(SAMPLE_BAND)

        verify(view).render(any())
    }

    @Test
    fun `show error`() {
        var response : DisposableSingleObserver<BandDetails>? = null
        whenever(getBandDetails.execute(any(), any())).thenAnswer {
            response = it.getArgument(0)
            Unit
        }

        presenter.init(view, BandViewEntity(1, "name", ""))

        response!!.onError(NetworkConnectionException())

        verify(view).notify(anyString())
        verify(view).close()
    }

    @Test
    fun `album clicked`() {
        presenter.init(view, BandViewEntity(1, "name", ""))
        presenter.onAlbumClicked(AlbumViewEntity(
            "title", "date", URL("https://pic")))
        verify(navigator).openYouTubeSearch(any(), eq("name title"))
    }

    @Test
    fun `album clicked no youtube`() {
        whenever(navigator.openYouTubeSearch(any(), any()))
            .thenAnswer { throw YouTubeNotInstalled() }
        presenter.init(view, BandViewEntity(1, "name", ""))
        presenter.onAlbumClicked(AlbumViewEntity(
            "title", "date", URL("https://pic")))
        verify(view).notify(any())
    }
}
