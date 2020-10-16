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

package com.cesarmauri.clean_architecture.application.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.cesarmauri.clean_architecture.application.exception.YouTubeNotInstalled
import com.cesarmauri.clean_architecture.application.service.AuthenticationService
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.view.activity.BandDetailsActivity
import com.cesarmauri.clean_architecture.presentation.view.activity.BandsActivity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Navigator
@Inject constructor(private val authenticationService: AuthenticationService) {

    fun showMain(activity: FragmentActivity) {
        when (authenticationService.isAuthenticated) {
            true -> showOther(activity)
            false -> showLogin()
        }
    }

    private fun showLogin() {
        // Open here your own login experience
    }

    private fun showOther(activity: FragmentActivity)  {
        activity.startActivity(BandsActivity.intent(activity))
    }

    fun showBandDetails(activity: FragmentActivity, bandViewEntity: BandViewEntity) {
        val intent = BandDetailsActivity.intent(activity, bandViewEntity)
        activity.startActivity(intent)
    }

    fun openYouTubeSearch(activity: FragmentActivity, query: String) {
        val intent = Intent(Intent.ACTION_SEARCH)
        intent.setPackage("com.google.android.youtube")
        intent.putExtra("query", query)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            activity.startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            throw YouTubeNotInstalled(e)
        }
    }
}
