package com.allano.alquran.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.allano.alquran.R
import com.allano.alquran.databinding.FragmentTextClassifierBinding
import org.tensorflow.lite.support.label.Category

class TextClassifierFragment : Fragment(), TextClassificationHelper.TextResultsListener {

    private var _binding: FragmentTextClassifierBinding? = null
    private val binding get() = _binding!!

    private lateinit var classificationHelper: TextClassificationHelper
    private val adapter by lazy { ResultsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextClassifierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi helper
        classificationHelper = TextClassificationHelper(
            context = requireContext(),
            listener = this
        )

        // Setup RecyclerView
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResults.adapter = adapter

        // Setup tombol classify
        binding.btnClassify.setOnClickListener {
            val textToClassify = binding.etInput.text.toString()
            classificationHelper.classify(textToClassify)
        }
    }

    // Callback dari TextClassificationHelper saat hasil tersedia
    override fun onResult(results: List<Category>, inferenceTime: Long) {
        activity?.runOnUiThread {
            binding.tvInferenceTime.text = "Inference Time: $inferenceTime ms"
            adapter.resultsList = results.sortedByDescending { it.score }
            adapter.notifyDataSetChanged()
        }
    }

    // Callback dari TextClassificationHelper jika terjadi error
    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}