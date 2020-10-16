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
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface BandsApi {

    companion object {
        private const val PARAM_BAND_ID = "bandId"
    }

    @GET("bands.json") fun bands(): Single<List<BandEntity>>

    @GET("band_{$PARAM_BAND_ID}.json")
    fun bandDetails(@Path(PARAM_BAND_ID) bandId: Int): Single<BandDetailsEntity>
}