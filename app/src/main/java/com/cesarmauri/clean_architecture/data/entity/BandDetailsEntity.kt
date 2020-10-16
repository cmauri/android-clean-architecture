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

package com.cesarmauri.clean_architecture.data.entity

import com.cesarmauri.clean_architecture.domain.BandDetails
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL

data class BandDetailsEntity @JsonCreator constructor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("genre") val genre: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("foundation_year") val year: Int?,
    @JsonProperty("picture") val picture: String?,
    @JsonProperty("albums") val albums: List<AlbumEntity>) {

    fun toBandDetails(): BandDetails =
        BandDetails(id, name, genre, description, year,
            if (picture == null) null else URL(picture), albums.map { it.toAlbum() })
}