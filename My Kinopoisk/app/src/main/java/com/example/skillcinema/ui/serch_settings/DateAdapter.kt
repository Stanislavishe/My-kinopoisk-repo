package com.example.skillcinema.ui.serch_settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.databinding.ChipBinding

class DateAdapter(
    private val onClick: (Int) -> Unit
) : Adapter<DateAdapter.DateViewHolder>() {
    private val limit = 12
    private var data: List<Int> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Int>) {
        this.data = data
        notifyDataSetChanged()
    }

    class DateViewHolder(val binding: ChipBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DateViewHolder(
            ChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = if (data.size < limit) data.size else limit

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val item = data.getOrNull(position)
        holder.binding.singleChip.text = item.toString()
        holder.binding.singleChip.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) item?.let(onClick)
        }
    }
}