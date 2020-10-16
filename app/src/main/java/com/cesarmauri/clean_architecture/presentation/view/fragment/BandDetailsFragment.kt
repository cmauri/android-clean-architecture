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
import com.cesarmauri.clean_architecture.presentation.entity.AlbumViewEntity
import com.cesarmauri.clean_architecture.presentation.entity.BandDetailsViewEntity
import com.cesarmauri.clean_architecture.presentation.entity.BandViewEntity
import com.cesarmauri.clean_architecture.presentation.presenter.BandDetailsPresenter
import kotlinx.android.synthetic.main.fragment_band_details.*
import kotlinx.android.synthetic.main.row_album.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BandDetailsFragment : BaseFragment() {

    companion object {
        private const val PARAM_BAND = "param_band"

        fun forBand(band: BandViewEntity): BandDetailsFragment {
            val fragment = BandDetailsFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_BAND, band)
            fragment.arguments = arguments
            return fragment
        }
    }

    @Inject lateinit var presenter: BandDetailsPresenter

    override fun layoutId(): Int = R.layout.fragment_band_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init(this, this.arguments!!.get(PARAM_BAND) as BandViewEntity)
    }

    override fun onDestroyView() {
        albumsList.adapter = null
        super.onDestroyView()
    }

    fun render(entity: BandDetailsViewEntity) {
        activity?.toolbar?.title = entity.name
        bandDescription.text = entity.description
        bandGenre.text = entity.genre
        bandFoundationYear.text = entity.foundationYear
        bandPicture.loadFromUrl(entity.picture.toString())

        albumsList.layoutManager = LinearLayoutManager(context)
        albumsList.adapter = AlbumsAdapter(entity.albums) { presenter.onAlbumClicked(it) }
    }
}

class AlbumsAdapter
@Inject constructor(private val items: List<AlbumViewEntity>,
                    private val clickListener: (AlbumViewEntity) -> Unit)
    : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.row_album, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: AlbumViewEntity) {
            itemView.albumPicture.loadFromUrl(album.picture.toString())
            itemView.albumName.text = album.title
            itemView.albumDate.text = album.date
            itemView.setOnClickListener { clickListener(album) }
        }
    }
}
