package com.example.skillcinema.ui.profile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.entity.FilmOrSerial
import com.example.skillcinema.entity.InterestedMovies
import com.example.skillcinema.entity.MovieFromCollection
import com.example.skillcinema.ui.SmallFilmViewHolder

class CollectionMoviesAdapter(
    private val onClick: (InterestedMovies) -> Unit
): Adapter<SmallFilmViewHolder>(){
    private var data = emptyList<InterestedMovies>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<InterestedMovies>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallFilmViewHolder(
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SmallFilmViewHolder, position: Int) {
        val film = data.getOrNull(position)
        with(holder.binding){
            imageFilm.foreground.alpha = 0
            viewedIcon.visibility = View.GONE
            filmName.text = film?.name
            filmGenre.text = film?.genre
            if(film?.rating == null) reiting.visibility = View.GONE
            else reiting.text = film.rating.toString()
            film?.let {
                Glide
                    .with(imageFilm.context)
                    .load(it.url)
                    .into(imageFilm)
            }
            root.setOnClickListener {
                film?.let(onClick)
            }
        }
    }
}