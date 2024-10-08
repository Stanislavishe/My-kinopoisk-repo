package com.example.skillcinema.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.search.SearchResult
import com.example.skillcinema.data.models.staffs.Filmography
import com.example.skillcinema.databinding.ItemFilmographyBinding
import com.example.skillcinema.ui.filmography.FilmographyAdapter

class SearchAdapter(
    private val onClick: (SearchResult) -> Unit
) : PagingDataAdapter<SearchResult, FilmographyAdapter.MediumFilmViewHolder>(DiffUtilCallBack()) {

    override fun onBindViewHolder(holder: FilmographyAdapter.MediumFilmViewHolder, position: Int) {
        val film = getItem(position)
        with(holder.binding) {
            filmName.text = film?.nameRu ?: film?.nameEn ?: film?.nameOriginal ?: ""
            if(film?.ratingKinopoisk == null) rating.isVisible = false
            else rating.text = film.ratingKinopoisk.toString()
            filmYear.text = if(film?.year != null) film.year.toString() else ""
            Glide
                .with(filmImage.context)
                .load(film?.posterUrlPreview)
                .into(filmImage)
            root.setOnClickListener { film?.let(onClick) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FilmographyAdapter.MediumFilmViewHolder(
        ItemFilmographyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    class DiffUtilCallBack : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem.kinopoiskId == newItem.kinopoiskId

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem == newItem
    }
}