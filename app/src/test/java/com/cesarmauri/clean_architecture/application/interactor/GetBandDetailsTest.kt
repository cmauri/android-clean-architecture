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
import com.cesarmauri.clean_architecture.domain.BandDetails
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.net.URL

class GetBandDetailsTest {

    private lateinit var getBandDetails: GetBandDetails

    private lateinit var response: Single<BandDetails>

    @Mock
    private lateinit var bandRepository: BandRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        getBandDetails = GetBandDetails(bandRepository, SchedulersProviderTest())
        response = Single.just(
            BandDetails(1, "name", "genre", "description",
                2020, URL("http://url.com"), listOf()))

        whenever(bandRepository.bandDetails(anyInt())).thenReturn(response)
    }

    @Test
    fun `build observable`() {
        val response = getBandDetails.buildUseCaseSingle(1)

        verify(bandRepository, times(1)).bandDetails(1)

        assertEquals(this.response, response)
    }
}