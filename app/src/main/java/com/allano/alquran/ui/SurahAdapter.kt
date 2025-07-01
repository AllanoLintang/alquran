package com.allano.alquran.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allano.alquran.R // Import R to access drawables
import com.allano.alquran.data.local.SurahEntity
import com.allano.alquran.databinding.ItemSurahBinding


class SurahAdapter(
    private val onItemClicked: (SurahEntity) -> Unit,
    private val onFavoriteClicked: (SurahEntity) -> Unit
) : ListAdapter<SurahEntity, SurahAdapter.SurahViewHolder>(SurahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val binding = ItemSurahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurahViewHolder(binding, onItemClicked, onFavoriteClicked)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val surah = getItem(position)
        holder.bind(surah)
    }

    class SurahViewHolder(
        private val binding: ItemSurahBinding,
        private val onItemClicked: (SurahEntity) -> Unit,
        private val onFavoriteClicked: (SurahEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(surah: SurahEntity) {
            binding.tvSurahNumber.text = surah.number.toString()
            binding.tvSurahName.text = surah.englishName
            binding.tvSurahTranslation.text = surah.englishNameTranslation
            binding.tvSurahInfo.text = "${surah.revelationType} - ${surah.numberOfAyahs} verses"


            if (surah.isFavorite) {
                binding.ivFavorite.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_star_border)
            }


            itemView.setOnClickListener {
                Log.d("SurahAdapter", "Item clicked: ${surah.englishName}")
                onItemClicked(surah)
            }
            binding.ivFavorite.setOnClickListener {
                Log.d("SurahAdapter", "Favorite clicked: ${surah.englishName}")
                onFavoriteClicked(surah)
            }
        }
    }
}

class SurahDiffCallback : DiffUtil.ItemCallback<SurahEntity>() {
    override fun areItemsTheSame(oldItem: SurahEntity, newItem: SurahEntity): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: SurahEntity, newItem: SurahEntity): Boolean {
        return oldItem == newItem
    }
}
