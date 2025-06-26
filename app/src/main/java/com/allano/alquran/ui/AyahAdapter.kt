package com.allano.alquran.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allano.alquran.data.local.AyahEntity
import com.allano.alquran.databinding.ItemAyahBinding

class AyahAdapter : ListAdapter<AyahEntity, AyahAdapter.AyahViewHolder>(AyahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        val binding = ItemAyahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AyahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AyahViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AyahViewHolder(private val binding: ItemAyahBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ayahEntity: AyahEntity) {
            binding.tvAyahNumber.text = ayahEntity.numberInSurah.toString()
            binding.tvAyahTextArabic.text = ayahEntity.arabicText
            binding.tvAyahTextEnglish.text = ayahEntity.englishText
        }
    }
}

class AyahDiffCallback : DiffUtil.ItemCallback<AyahEntity>() {
    override fun areItemsTheSame(oldItem: AyahEntity, newItem: AyahEntity): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: AyahEntity, newItem: AyahEntity): Boolean {
        return oldItem == newItem
    }
}