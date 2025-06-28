package com.allano.alquran.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.allano.alquran.R
import com.allano.alquran.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for the "View Quran" button
        binding.buttonQuranList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_surahListFragment)
        }

        // --- NEW ---
        // Add the missing click listener for the "Favorites" button
        binding.buttonFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_favoriteFragment)
        }

        // Add the click listener for the "Dzikr Counter" button
        binding.buttonDzikrCounter.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_dzikrCounterFragment)
        }

        // Add the click listener for the "Text Classifier" button
        binding.buttonTextClassifier.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_textClassifierFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
