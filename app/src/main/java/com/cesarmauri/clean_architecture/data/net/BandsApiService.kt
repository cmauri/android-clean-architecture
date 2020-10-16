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

import com.cesarmauri.clean_architecture.data.entity.BandEntity
import com.cesarmauri.clean_architecture.data.entity.BandDetailsEntity
import com.cesarmauri.clean_architecture.application.exception.ServerErrorException
import com.fasterxml.jackson.databind.JsonMappingException
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BandsApiService
@Inject constructor(retrofit: Retrofit) : BandsApi {
    private val musicApi by lazy { retrofit.create(BandsApi::class.java) }

    override fun bands(): Single<List<BandEntity>> =
        musicApi
            .bands()
            .onErrorResumeNext { error: Throwable -> Single.error(translate(error)) }

    override fun bandDetails(bandId: Int): Single<BandDetailsEntity> =
        musicApi
            .bandDetails(bandId)
            .onErrorResumeNext { error: Throwable -> Single.error(translate(error)) }

    private fun translate(throwable: Throwable): Throwable =
        when(throwable) {
            is JsonMappingException -> ServerErrorException(throwable)
            is HttpException -> ServerErrorException(throwable)
            else -> throwable
        }

}