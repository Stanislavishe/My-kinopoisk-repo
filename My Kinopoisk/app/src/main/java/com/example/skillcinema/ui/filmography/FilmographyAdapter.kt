package com.example.skillcinema.ui.filmography

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.data.models.staffs.Filmography
import com.example.skillcinema.databinding.ItemFilmographyBinding
import com.example.skillcinema.use_case.FilmsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FilmographyAdapter(
    private val filmsUseCase: FilmsUseCase,
    private val scope: CoroutineScope,
    private val onClick: (SingleFilm) -> Unit
): ListAdapter<Filmography, FilmographyAdapter.MediumFilmViewHolder>(DiffUtilCallBack()) {

    class MediumFilmViewHolder(val binding: ItemFilmographyBinding) : ViewHolder(binding.root)

    class DiffUtilCallBack : DiffUtil.ItemCallback<Filmography>() {
        override fun areItemsTheSame(oldItem: Filmography, newItem: Filmography) =
            oldItem.filmId == newItem.filmId

        override fun areContentsTheSame(oldItem: Filmography, newItem: Filmography) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MediumFilmViewHolder(
            ItemFilmographyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MediumFilmViewHolder, position: Int) {
        val filmId = getItem(position).filmId

        scope.launch {
            val film = filmsUseCase.getItemFilm(filmId)
            with(holder.binding) {
                if (film.ratingKinopoisk == null) {
                    rating.visibility = View.GONE
                } else rating.text = film.ratingKinopoisk.toString()

                filmName.text = film.nameRu ?: film.nameEn ?: film.nameOriginal ?: ""
                filmYear.text = if(film.year != null) film.year.toString() else ""
                Glide
                    .with(filmImage.context)
                    .load(film.posterUrlPreview)
                    .into(filmImage)
            }
            holder.binding.root.setOnClickListener {
                film.let(onClick)
            }
        }

    }
}