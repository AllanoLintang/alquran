package com.allano.alquran.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.allano.alquran.R
import com.allano.alquran.databinding.ItemClassificationResultBinding
import org.tensorflow.lite.support.label.Category

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    // Daftar untuk menampung hasil klasifikasi dari TFLite
    var resultsList: List<Category> = emptyList()

    // ViewHolder bertanggung jawab untuk menampung dan mengikat data ke view
    class ViewHolder(private val binding: ItemClassificationResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(label: String, score: Float) {
            // Menggunakan binding untuk mengakses view di dalam layout
            with(binding) {
                // Mengambil string dari strings.xml dan memformatnya dengan label dan skor
                resultTextView.text = binding.root.context.getString(
                    R.string.result_display_text,
                    label,
                    score)
            }
        }
    }

    // Fungsi ini dipanggil saat RecyclerView perlu membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Membuat binding object dari layout item_classification_result.xml
        val binding =
            ItemClassificationResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return ViewHolder(binding)
    }

    // Fungsi ini dipanggil untuk menampilkan data pada posisi tertentu
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Mengambil kategori (hasil) dari daftar berdasarkan posisi
        val category = resultsList[position]
        // Memanggil fungsi bind di ViewHolder untuk menampilkan data
        holder.bind(category.label, category.score)
    }

    // Fungsi ini mengembalikan jumlah total item dalam daftar
    override fun getItemCount() = resultsList.size
}