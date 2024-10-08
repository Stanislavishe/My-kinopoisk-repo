package com.example.skillcinema.ui.film_fullscreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.data.models.staffs.Staff
import com.example.skillcinema.databinding.ItemSmallActorBinding
import com.example.skillcinema.ui.film_fullscreen.holders.SmallStaffViewHolder

class SmallStaffAdapter(
    private val onClick: (Staff) -> Unit
): ListAdapter<Staff, SmallStaffViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallStaffViewHolder(
            ItemSmallActorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: SmallStaffViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding){
            actorName.text = item?.nameRu ?: item?.nameEn ?: "Untitled"
            description.text = item?.professionText ?: "?"
            item?.let {
                Glide
                    .with(actorPhoto.context)
                    .load(it.posterUrl)
                    .into(actorPhoto)
            }
            root.setOnClickListener {
                item?.let(onClick)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Staff>() {
        override fun areItemsTheSame(oldItem: Staff, newItem: Staff) =
            oldItem.staffId == newItem.staffId

        override fun areContentsTheSame(oldItem: Staff, newItem: Staff) =
            oldItem == newItem
    }


}