package com.example.skillcinema.ui.actor_fillscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.data.models.staffs.Filmography
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.ui.SmallFilmViewHolder
import com.example.skillcinema.use_case.FilmsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BestsAdapter(
    private val filmsUseCase: FilmsUseCase,
    private val scope: CoroutineScope,
    private val onClick: (SingleFilm) -> Unit
) : ListAdapter<Filmography, SmallFilmViewHolder>(DiffUtilCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallFilmViewHolder(
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SmallFilmViewHolder, position: Int) {
        val filmId = getItem(position).filmId

        scope.launch {
            val film = filmsUseCase.getItemFilm(filmId)
            with(holder.binding) {
                imageFilm.foreground.alpha = 0
                viewedIcon.visibility = View.GONE
                if (film.ratingKinopoisk == null) {
                    reiting.visibility = View.GONE
                } else reiting.text = film.ratingKinopoisk.toString()
                filmName.text = film.nameRu ?: film.nameEn ?: film.nameOriginal ?: ""
                filmGenre.text = film.genres.joinToString(", ") { it.genre }
                Glide
                    .with(imageFilm.context)
                    .load(film.posterUrlPreview)
                    .into(imageFilm)
            }
            holder.binding.root.setOnClickListener {
                film.let(onClick)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Filmography>() {
        override fun areItemsTheSame(oldItem: Filmography, newItem: Filmography) =
            oldItem.filmId == newItem.filmId

        override fun areContentsTheSame(oldItem: Filmography, newItem: Filmography) =
            oldItem == newItem
    }
}