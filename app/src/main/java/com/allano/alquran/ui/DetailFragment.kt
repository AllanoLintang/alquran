package com.allano.alquran.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.allano.alquran.databinding.FragmentDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModel.Factory(requireActivity().application, args.surahNumber)
    }

    private val surahViewModel: SurahViewModel by activityViewModels()

    private lateinit var ayahAdapter: AyahAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInfoAwal()
        setupRecyclerView()
        observeAyahs()
    }

    private fun setupInfoAwal() {
        val surah = surahViewModel.surahs.value.find { it.number == args.surahNumber }
        surah?.let {
            binding.tvDetailSurahName.text = it.englishName
            binding.tvDetailSurahArabicName.text = it.name
            binding.tvDetailTranslation.text = "\"${it.englishNameTranslation}\""
            binding.tvDetailInfo.text = "Surah No: ${it.number} • ${it.revelationType} • ${it.numberOfAyahs} Verses"

        }
    }

    private fun setupRecyclerView() {
        ayahAdapter = AyahAdapter()
        binding.rvAyahs.apply {
            adapter = ayahAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeAyahs() {
        viewLifecycleOwner.lifecycleScope.launch {
            detailViewModel.ayahs.collectLatest { ayahsList ->
                Log.d("DetailFragmentUI", "Menerima update dengan ${ayahsList.size} ayat.")

                binding.detailProgressBar.isVisible = ayahsList.isEmpty()

                ayahAdapter.submitList(ayahsList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}