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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.net.MalformedURLException
import java.time.Month

class AlbumEntityTest {
    companion object {
        private const val TITLE = "title"
        private const val YEAR = 2020
        private const val MONTH = 1
        private const val LINEUP = "lineup"
        private const val OK_URL = "http://url.com"
        private const val KO_URL = "this is not an URL"
    }

    @Test
    fun `all fields`() {
        val model = AlbumEntity(TITLE, YEAR, MONTH, LINEUP, OK_URL).toAlbum()

        assertEquals(Month.of(MONTH), model.month)
    }

    @Test( expected = MalformedURLException::class)
    fun `wrong url`() {
        AlbumEntity(TITLE, YEAR, MONTH, LINEUP, KO_URL).toAlbum()
    }

    @Test
    fun `missing optional fields`() {
        AlbumEntity(TITLE, YEAR, null, null, null).toAlbum()
    }
}
