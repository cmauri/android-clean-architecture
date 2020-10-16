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

package com.cesarmauri.clean_architecture.presentation.entity

import com.cesarmauri.clean_architecture.domain.Album
import com.cesarmauri.clean_architecture.domain.BandDetails
import java.net.URL

data class BandDetailsViewEntity(
    val name: String,
    val genre: String?,
    val description: String?,
    val foundationYear: String,
    val picture: URL?,
    val albums: List<AlbumViewEntity>) {

    companion object {
        fun from(bandDetails: BandDetails) = BandDetailsViewEntity(bandDetails.name,
            bandDetails.genre, bandDetails.description, bandDetails.foundation_year.toString(),
            bandDetails.picture, bandDetails.albums.map { AlbumViewEntity.from(it) })
    }
}