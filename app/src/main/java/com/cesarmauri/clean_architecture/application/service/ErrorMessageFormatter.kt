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

package com.cesarmauri.clean_architecture.application.service

import android.content.Context
import com.cesarmauri.clean_architecture.R
import com.cesarmauri.clean_architecture.application.exception.NetworkConnectionException
import com.cesarmauri.clean_architecture.application.exception.ServerErrorException
import com.cesarmauri.clean_architecture.application.exception.YouTubeNotInstalled
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Format localized common error messages
 */
@Singleton
class ErrorMessageFormatter @Inject constructor(context: Context) {

    private val r = context.resources

    fun getErrorMessage(e: Throwable): String =
        when (e) {
            is NetworkConnectionException -> r.getString(R.string.failure_network_connection)
            is ServerErrorException -> r.getString(R.string.failure_server_error)
            is YouTubeNotInstalled -> r.getString(R.string.failure_youtube_not_installed)
            else -> r.getString(R.string.failure_unknown_error, e.localizedMessage)
        }
}