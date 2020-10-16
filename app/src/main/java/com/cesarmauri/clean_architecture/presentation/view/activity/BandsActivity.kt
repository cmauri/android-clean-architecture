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

package com.cesarmauri.clean_architecture.presentation.view.activity

import android.content.Context
import android.content.Intent
import com.cesarmauri.clean_architecture.infrastructure.platform.BaseActivity
import com.cesarmauri.clean_architecture.infrastructure.platform.BaseFragment
import com.cesarmauri.clean_architecture.presentation.view.fragment.BandsFragment

class BandsActivity : BaseActivity() {

    companion object {
        fun intent(context: Context) = Intent(context, BandsActivity::class.java)
    }

    override fun fragment(): BaseFragment = BandsFragment()
}