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

import com.cesarmauri.clean_architecture.application.repository.BandRepository
import com.cesarmauri.clean_architecture.common.SchedulersProviderTest
import com.cesarmauri.clean_architecture.domain.Band
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.URL

class GetBandsTest {

    private lateinit var getBands: GetBands

    private lateinit var response: Single<List<Band>>

    @Mock private lateinit var bandRepository: BandRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        getBands = GetBands(bandRepository, SchedulersProviderTest())

        response = Single.just(listOf(
            Band(1,"name1", URL("http://url1.com")),
            Band(2,"name2", URL("http://url2.com")),
        ))

        doReturn(response).`when`(bandRepository).bands()
    }

    @Test
    fun `build observable`() {
        val response = getBands.buildUseCaseSingle(UseCase.None())

        verify(bandRepository, times(1)).bands()

        assertEquals(this.response, response)
    }
}