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

package com.cesarmauri.clean_architecture.presentation.presenter

import com.cesarmauri.clean_architecture.application.exception.YouTubeNotInstalled
import com.cesarmauri.clean_architecture.application.service.ErrorMessageFormatter
import com.cesarmauri.clean_architecture.application.interactor.GetBandDetails
import com.cesarmauri.clean_architecture.application.navigation.Navigator
import com.cesarmauri.clean_architecture.domain.BandDetails
import com.cesarmauri.clean_architecture.presentation.entity.AlbumViewEntity
import com.cesarmauri.clean_architecture.presentation.entity.BandDetailsViewEntity
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandDetailsFragment
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class BandDetailsPresenter
@Inject constructor(private val getBandDetails: GetBandDetails,
                    private val navigator: Navigator,
                    private val errorFormatter: ErrorMessageFormatter
) {

    private lateinit var view: BandDetailsFragment
    private lateinit var band: BandViewEntity

    fun init(view: BandDetailsFragment, band: BandViewEntity) {
        this.view = view
        this.band = band

        getBandDetails.execute(object : DisposableSingleObserver<BandDetails>() {
            override fun onSuccess(bandDetails: BandDetails) =
                view.render(BandDetailsViewEntity.from(bandDetails))

            override fun onError(e: Throwable) {
                view.notify(errorFormatter.getErrorMessage(e))
                view.close()
            }
        },
        band.id)
    }

    fun onAlbumClicked(album: AlbumViewEntity) {
        try {
            navigator.openYouTubeSearch(view.activity!!, "${band.name} ${album.title}")
        }
        catch(e: YouTubeNotInstalled) {
            view.notify(errorFormatter.getErrorMessage(e))
        }
    }
}
