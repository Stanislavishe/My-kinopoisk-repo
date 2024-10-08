package com.example.skillcinema.ui.profile.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.skillcinema.databinding.ItemCollectionBinding
import com.example.skillcinema.entity.Collection
import com.example.skillcinema.ui.profile.CollectionViewHolder
import com.example.skillcinema.ui.profile.ProfileViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CollectionsAdapter(
    private val onClick: (Collection) -> Unit
) : ListAdapter<Collection, CollectionViewHolder>(DiffUtilCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CollectionViewHolder(
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = getItem(position)
        with(holder.binding) {
            collectionName.text = collection.name
            icon.setImageResource(collection.icon)
            root.setOnClickListener {
                collection?.let(onClick)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection) =
            newItem.name == oldItem.name

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection) =
            newItem == oldItem
    }
}