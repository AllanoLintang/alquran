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

        binding.buttonQuranList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_surahListFragment)
        }

        binding.buttonFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_favoriteFragment)
        }

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
