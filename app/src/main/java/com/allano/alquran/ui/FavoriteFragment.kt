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
import com.allano.alquran.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    // This is the corrected way to initialize the ViewModel using its custom Factory.
    // This was the likely cause of the crash.
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Ensure your layout file is named fragment_favorite.xml
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surahAdapter = SurahAdapter(
            onItemClicked = { surah ->
                // This uses the action defined in your nav_graph.xml
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(surah.number)
                findNavController().navigate(action)
            },
            onFavoriteClicked = { surah ->
                // Un-favorites the item
                favoriteViewModel.toggleFavorite(surah)
            }
        )

        // Ensure the RecyclerView in your layout has the id 'rv_favorite_surah'
        binding.rvFavoriteSurah.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = surahAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.favoriteSurahs.collect { surahs ->
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
