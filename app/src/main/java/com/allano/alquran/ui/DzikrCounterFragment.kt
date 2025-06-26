package com.allano.alquran.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.allano.alquran.databinding.FragmentDzikrCounterBinding
import kotlinx.coroutines.launch

class DzikrCounterFragment : Fragment() {

    private var _binding: FragmentDzikrCounterBinding? = null
    private val binding get() = _binding!!

    // Initialize the ViewModel
    private val viewModel: DzikrCounterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDzikrCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the click listeners
        binding.btnIncrement.setOnClickListener {
            viewModel.increment()
        }

        binding.btnReset.setOnClickListener {
            viewModel.reset()
        }

        // Observe the count from the ViewModel and update the TextView
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.count.collect { currentCount ->
                    binding.tvCount.text = currentCount.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
