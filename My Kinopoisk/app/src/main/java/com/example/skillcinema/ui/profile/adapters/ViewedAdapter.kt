package com.example.skillcinema.ui.profile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.entity.MoviesViewed
import com.example.skillcinema.ui.SmallFilmViewHolder

class ViewedAdapter(
    private val onFilmClick: (MoviesViewed) -> Unit
): Adapter<SmallFilmViewHolder>() {
    private var data = emptyList<MoviesViewed>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<MoviesViewed>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallFilmViewHolder(
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SmallFilmViewHolder, position: Int) {
        val item = data.getOrNull(position)
        with(holder.binding){
            imageFilm.foreground.alpha = 0
            viewedIcon.visibility = View.GONE
            val rating = item?.rating
            if(rating == null) reiting.visibility = View.GONE
            else reiting.text = rating.toString()
            filmName.text = item?.name
            filmGenre.text = item?.genre
            item?.let {
                Glide
                    .with(imageFilm.context)
                    .load(it.url)
                    .into(imageFilm)
            }
            root.setOnClickListener { item?.let(onFilmClick) }
        }
    }
}