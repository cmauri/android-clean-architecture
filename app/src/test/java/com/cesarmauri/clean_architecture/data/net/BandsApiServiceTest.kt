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

package com.cesarmauri.clean_architecture.data.net

import com.cesarmauri.clean_architecture.data.entity.BandDetailsEntity
import com.cesarmauri.clean_architecture.data.entity.BandEntity
import com.cesarmauri.clean_architecture.application.exception.ServerErrorException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.cesarmauri.clean_architecture.common.readFromResources
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_BAD_GATEWAY
import java.util.concurrent.TimeUnit

class BandsApiServiceTest {
    private lateinit var typeFactory: TypeFactory
    private lateinit var bandsApi: BandsApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var objectMapper: ObjectMapper

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        objectMapper = ObjectMapper()
        typeFactory = objectMapper.typeFactory

        bandsApi = BandsApiService(Retrofit.Builder()
            .baseUrl((mockWebServer.url("/")))
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `band list`() = bandsSuccess("bands.json")

    @Test
    fun `band list empty`() = bandsSuccess("empty_list.json")

    @Test
    fun `band list void response`() =
        bandsError("void.json", ServerErrorException::class.java)

    @Test
    fun `band list server error`() =
        bandsError("bands.json", ServerErrorException::class.java, HTTP_BAD_GATEWAY)

    @Test
    fun `band details`() = bandDetailsSuccess("band_details.json")

    @Test
    fun `band details empty optional fields`() =
        bandDetailsSuccess("band_details_missing_fields.json")


    private fun bandsSuccess(jsonPath: String) {
        val jsonString = readFromResources(jsonPath)

        val original: List<BandEntity> = objectMapper.readValue(jsonString,
            typeFactory.constructCollectionType(List::class.java, BandEntity::class.java))

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonString))

        bandsApi.bands().test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue { original == it }

        assertEquals("/bands.json", mockWebServer.takeRequest().path)
    }

    private fun <T> bandsError(jsonPath: String, clazz: Class<T>,
                               httpResponse: Int = HttpURLConnection.HTTP_OK) {
        val jsonString = readFromResources(jsonPath)

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(httpResponse)
            .setBody(jsonString))

        bandsApi.bands().test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValueCount(0)
            .assertError{ clazz.isInstance(it) }
    }


    private fun bandDetailsSuccess(jsonPath: String) {
        val jsonString = readFromResources(jsonPath)

        val original: BandDetailsEntity = objectMapper.readValue(jsonString,
            typeFactory.constructType(BandDetailsEntity::class.java))

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonString)

        mockWebServer.enqueue(response)

        bandsApi.bandDetails(1).test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue { original == it }

        assertEquals("/band_1.json", mockWebServer.takeRequest().path)
    }
}
