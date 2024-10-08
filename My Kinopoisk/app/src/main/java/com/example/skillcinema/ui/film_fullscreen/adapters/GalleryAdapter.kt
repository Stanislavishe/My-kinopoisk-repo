package com.example.skillcinema.ui.film_fullscreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.Image
import com.example.skillcinema.databinding.ItemGelleryBinding
import com.example.skillcinema.ui.film_fullscreen.holders.GalleryViewHolder

class GalleryAdapter(
    private val onClick: (Image) -> Unit
) : PagingDataAdapter<Image, GalleryViewHolder>(DiffUtilCallBack()) {
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = getItem(position)
        with(holder.binding) {
            image?.let {
                Glide
                    .with(frame.context)
                    .load(it.previewUrl)
                    .into(frame)
            }
            root.setOnClickListener { image?.let(onClick) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GalleryViewHolder(
            ItemGelleryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    class DiffUtilCallBack : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image) =
            oldItem.previewUrl == newItem.previewUrl

        override fun areContentsTheSame(oldItem: Image, newItem: Image) =
            oldItem == newItem
    }
}