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

import com.cesarmauri.clean_architecture.domain.Album
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.time.Month

data class AlbumEntity @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("year") val year: Int,
    @JsonProperty("month") val month: Int?,
    @JsonProperty("lineUp") val lineUp: String?,
    @JsonProperty("picture") val picture: String?) {

    fun toAlbum(): Album =
        Album(title, year,
            if (month == null) null else Month.of(month),
            lineUp,
            if (picture == null) null else URL(picture))
}