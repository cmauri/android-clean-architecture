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

import com.cesarmauri.clean_architecture.application.service.ErrorMessageFormatter
import com.cesarmauri.clean_architecture.application.interactor.GetBands
import com.cesarmauri.clean_architecture.application.interactor.UseCase
import com.cesarmauri.clean_architecture.application.navigation.Navigator
import com.cesarmauri.clean_architecture.domain.Band
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandsFragment
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class BandsPresenter
@Inject constructor(private val getBands: GetBands,
                    private val navigator: Navigator,
                    private val errorFormatter: ErrorMessageFormatter
) {

    private lateinit var view : BandsFragment

    fun init(view: BandsFragment) {
        this.view = view

        getBands.execute(object : DisposableSingleObserver<List<Band>>() {
            override fun onSuccess(t: List<Band>) =
                view.render(t.map { band -> BandViewEntity.from(band) }.toTypedArray())

            override fun onError(e: Throwable) {
                view.notify(errorFormatter.getErrorMessage(e))
                view.close()
            }
        }, UseCase.None())
    }

    fun onBandClicked(band: BandViewEntity) {
        navigator.showBandDetails(view.activity!!, band)
    }
}
