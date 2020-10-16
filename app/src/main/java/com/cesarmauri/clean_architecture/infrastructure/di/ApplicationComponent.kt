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

package com.cesarmauri.clean_architecture.infrastructure.di

import com.cesarmauri.clean_architecture.application.navigation.RouteActivity
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandDetailsFragment
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(routeActivity: RouteActivity)
    fun inject(fragment: BandsFragment)
    fun inject(fragment: BandDetailsFragment)
}