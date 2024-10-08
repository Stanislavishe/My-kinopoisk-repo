package com.example.skillcinema.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.premiers.Movie
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.ui.SmallFilmViewHolder
import com.example.skillcinema.ui.home.HomeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PremiersAdapter(
    private val onFilmClick: (Movie) -> Unit,
    private val viewModel: HomeViewModel,
    private val scope: LifecycleCoroutineScope
) : ListAdapter<Movie, SmallFilmViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallFilmViewHolder(
            ItemFilmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: SmallFilmViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            imageFilm.foreground.alpha = 0
            viewedIcon.isVisible = false
            reiting.isVisible = false
            filmName.text = item?.nameRu ?: ""
            filmGenre.text = item?.genres?.joinToString(", ") { it.genre }
            viewModel.viewedFlow.onEach {
                it.forEach { id ->
                    if (item.kinopoiskId == id) {
                        imageFilm.foreground.alpha = 255
                        viewedIcon.isVisible = true
                    }
                }
            }.launchIn(scope)
            item?.let {
                Glide
                    .with(imageFilm.context)
                    .load(it.posterUrlPreview)
                    .into(imageFilm)
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let(onFilmClick)
        }
    }
    class DiffUtilCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.kinopoiskId == newItem.kinopoiskId

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            newItem == oldItem
    }
}

