package com.example.skillcinema.ui.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.databinding.ItemCollectionListBinding
import com.example.skillcinema.entity.Collection
import com.example.skillcinema.ui.profile.ProfileViewModel

class CollectionsListAdapter(
    private val viewModel: ProfileViewModel
) :
    ListAdapter<Collection, CollectionsListAdapter.SingleCollectionListViewHolder>(DiffUtilCallBack()) {

    class SingleCollectionListViewHolder(val binding: ItemCollectionListBinding) :
        ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = SingleCollectionListViewHolder(
        ItemCollectionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SingleCollectionListViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            checkbox.text = item.name
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.addInCollection(checkbox.text.toString())
                else viewModel.deleteOnCollection(checkbox.text.toString())
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