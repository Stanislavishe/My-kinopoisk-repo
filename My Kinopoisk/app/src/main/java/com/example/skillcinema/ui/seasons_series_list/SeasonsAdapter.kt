package com.example.skillcinema.ui.seasons_series_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.data.models.seasonsList.Episode
import com.example.skillcinema.data.models.seasonsList.Season
import com.example.skillcinema.databinding.ItemSeriyaBinding

class SeasonsAdapter : ListAdapter<Episode, SeasonsAdapter.EpisodeVIewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeVIewHolder(
            ItemSeriyaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: EpisodeVIewHolder, position: Int){
        val episode = getItem(position)
        with(holder.binding){
            title.text = "${episode.episodeNumber} серия. ${episode.nameRu ?: episode.nameEn ?: ""}"
            date.text = episode.releaseDate?.replace("-", ".")
        }
    }

    class EpisodeVIewHolder(val binding: ItemSeriyaBinding) : ViewHolder(binding.root)

    class DiffUtilCallBack : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode) =
            oldItem.episodeNumber == newItem.episodeNumber

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode) =
            oldItem == newItem
    }
}