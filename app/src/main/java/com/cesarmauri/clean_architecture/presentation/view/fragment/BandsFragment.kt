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

package com.cesarmauri.clean_architecture.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesarmauri.clean_architecture.R
import com.cesarmauri.clean_architecture.infrastructure.extensions.loadFromUrl
import com.cesarmauri.clean_architecture.infrastructure.platform.BaseFragment
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.presenter.BandsPresenter
import kotlinx.android.synthetic.main.fragment_band_details.*
import kotlinx.android.synthetic.main.fragment_bands.*
import kotlinx.android.synthetic.main.row_band.view.*
import javax.inject.Inject

class BandsFragment : BaseFragment() {

    @Inject lateinit var presenter: BandsPresenter

    override fun layoutId() = R.layout.fragment_bands

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init(this)
    }

    override fun onDestroyView() {
        bandsList.adapter = null
        super.onDestroyView()
    }

    fun render(bands: Array<BandViewEntity>) {
        bandsList.layoutManager = LinearLayoutManager(context)
        bandsList.adapter = BandsAdapter(bands) { presenter.onBandClicked(it) }
    }
}

class BandsAdapter
@Inject constructor(private val items: Array<BandViewEntity>,
                    private val clickListener: (BandViewEntity) -> Unit)
    : RecyclerView.Adapter<BandsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.row_band, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bandViewEntity: BandViewEntity) {
            itemView.bandPicture.loadFromUrl(bandViewEntity.picture)
            itemView.bandName.text = bandViewEntity.name
            itemView.setOnClickListener { clickListener(bandViewEntity) }
        }
    }
}
