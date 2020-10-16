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

package com.cesarmauri.clean_architecture.application.interactor

import com.cesarmauri.clean_architecture.application.repository.BandRepository
import com.cesarmauri.clean_architecture.domain.Band
import com.cesarmauri.clean_architecture.infrastructure.platform.SchedulersProvider
import io.reactivex.Single
import javax.inject.Inject

class GetBands
@Inject constructor(private val bandRepository: BandRepository,
                    schedulersProvider: SchedulersProvider)
    : UseCaseSingle<List<Band>, UseCase.None>(schedulersProvider) {

    override fun buildUseCaseSingle(params: None): Single<List<Band>> =
        bandRepository.bands()
}