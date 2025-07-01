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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.allano.alquran.databinding.FragmentSurahListBinding
import kotlinx.coroutines.launch

class SurahListFragment : Fragment() {

    private var _binding: FragmentSurahListBinding? = null
    private val binding get() = _binding!!

    private val surahViewModel: SurahViewModel by viewModels {
        SurahViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surahAdapter = SurahAdapter(
            onItemClicked = { surah ->
                val action =
                    SurahListFragmentDirections.actionSurahListFragmentToDetailFragment(surah.number)
                findNavController().navigate(action)
            },
            onFavoriteClicked = { surah ->
                surahViewModel.toggleFavorite(surah)
            }
        )

        binding.rvSurah.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = surahAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                surahViewModel.surahs.collect { surahs ->
                    surahAdapter.submitList(surahs)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
