package com.example.skillcinema.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.populap_movies.PopularMovies
import com.example.skillcinema.data.models.premiers.Movie
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.ui.SmallFilmViewHolder
import com.example.skillcinema.ui.home.HomeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PopularsAdapter(
    private val onClick:(PopularMovies) -> Unit,
    private val viewModel: HomeViewModel,
    private val scope: LifecycleCoroutineScope
) :
    PagingDataAdapter<PopularMovies, SmallFilmViewHolder>(DiffUtilCallBack()) {

    override fun onBindViewHolder(holder: SmallFilmViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            imageFilm.foreground.alpha = 0
            viewedIcon.isVisible = false
            filmName.text = item?.nameRu ?: item?.nameEn ?: item?.nameOriginal ?: ""
            filmGenre.text = item?.genres?.joinToString(", ") { it.genre }
            if(item?.ratingKinopoisk == null) reiting.isVisible = false
            else reiting.text = item.ratingKinopoisk.toString()
            item?.let {
                Glide
                    .with(imageFilm.context)
                    .load(it.posterUrlPreview)
                    .into(imageFilm)
            }
            viewModel.viewedFlow.onEach {
                it.forEach { id ->
                    if (item?.kinopoiskId == id) {
                        imageFilm.foreground.alpha = 255
                        viewedIcon.isVisible = true
                    }
                }
            }.launchIn(scope)
        }
        holder.binding.root.setOnClickListener {
            item?.let(onClick)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallFilmViewHolder(
            ItemFilmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    class DiffUtilCallBack : DiffUtil.ItemCallback<PopularMovies>() {
        override fun areItemsTheSame(oldItem: PopularMovies, newItem: PopularMovies) =
            oldItem.kinopoiskId == newItem.kinopoiskId


        override fun areContentsTheSame(oldItem: PopularMovies, newItem: PopularMovies) =
            newItem == oldItem
    }
}