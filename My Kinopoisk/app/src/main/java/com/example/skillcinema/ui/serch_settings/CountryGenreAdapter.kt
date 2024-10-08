package com.example.skillcinema.ui.serch_settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.data.models.search.Ids
import com.example.skillcinema.databinding.ItemCountrGenrBinding

class CountryGenreAdapter(
    private val onClick: (Ids) -> Unit
): ListAdapter<Ids, CountryGenreAdapter.CountGenrViewHolder>(DiffUtilCallBack()) {

    class CountGenrViewHolder(val binding: ItemCountrGenrBinding): ViewHolder(binding.root)
    class DiffUtilCallBack: DiffUtil.ItemCallback<Ids>() {
        override fun areItemsTheSame(oldItem: Ids, newItem: Ids) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Ids, newItem: Ids) = oldItem == newItem
    }

    override fun onBindViewHolder(holder: CountGenrViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding){
            text.text = item?.value ?: return
            root.setOnClickListener {
                item?.let(onClick)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountGenrViewHolder(
            ItemCountrGenrBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
}