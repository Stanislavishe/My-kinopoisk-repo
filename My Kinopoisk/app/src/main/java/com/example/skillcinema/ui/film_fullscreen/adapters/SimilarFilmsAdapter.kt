package com.example.skillcinema.ui.film_fullscreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.simularsList.SimilarFilm
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.ui.SmallFilmViewHolder
import com.example.skillcinema.ui.home.HomeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SimilarFilmsAdapter(
    private val onClick: (SimilarFilm) -> Unit,
    private val viewModel: HomeViewModel,
    private val scope: LifecycleCoroutineScope
) : ListAdapter<SimilarFilm, SmallFilmViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallFilmViewHolder(
            ItemFilmBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: SmallFilmViewHolder, position: Int) {
        val films = getItem(position)

        with(holder.binding) {
            imageFilm.foreground.alpha = 0
            viewedIcon.isVisible = false
            filmName.text = films?.nameRu ?: films?.nameEn ?: films?.nameOriginal ?: ""
            filmGenre.visibility = View.GONE
            reiting.visibility = View.GONE
            films?.let {
                Glide
                    .with(imageFilm.context)
                    .load(it.posterUrlPreview)
                    .into(imageFilm)
            }
            viewModel.viewedFlow.onEach {
                it.forEach { id ->
                    if (films?.filmId == id) {
                        imageFilm.foreground.alpha = 255
                        viewedIcon.isVisible = true
                    }
                }
            }.launchIn(scope)
            root.setOnClickListener {
                films?.let(onClick)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<SimilarFilm>() {
        override fun areItemsTheSame(oldItem: SimilarFilm, newItem: SimilarFilm) =
            oldItem.filmId == newItem.filmId

        override fun areContentsTheSame(oldItem: SimilarFilm, newItem: SimilarFilm) =
            oldItem == newItem
    }
    
}