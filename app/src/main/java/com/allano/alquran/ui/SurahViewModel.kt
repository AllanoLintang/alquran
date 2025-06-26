package com.allano.alquran.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.allano.alquran.data.SurahRepository
import com.allano.alquran.data.local.AppDatabase
import com.allano.alquran.data.local.SurahEntity
import com.allano.alquran.data.model.MergedAyah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SurahViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SurahRepository

    private val _surahs = MutableStateFlow<List<SurahEntity>>(emptyList())
    val surahs: StateFlow<List<SurahEntity>> = _surahs

    private val _navigateToDetail = MutableStateFlow<SurahEntity?>(null)
    val navigateToDetail: StateFlow<SurahEntity?> = _navigateToDetail

    init {
        val database = AppDatabase.getDatabase(application)
        val surahDao = database.surahDao()
        val ayahDao = database.ayahDao()
        repository = SurahRepository(surahDao, ayahDao)
        listenToDatabase()
        refreshDataFromServer()
    }

    private fun listenToDatabase() {
        viewModelScope.launch {
            repository.getSurahsStream()
                .catch { e ->
                    Log.e("SurahViewModel", "Error collecting from DB", e)
                }
                .collect { surahList ->
                    _surahs.value = surahList
                    Log.d("SurahViewModel", "Menerima ${surahList.size} surah dari database.")
                }
        }
    }

    // --- REMOVED ---
    // The old, incorrect getSurahs() function has been removed.

    // --- NEW ---
    // Function to handle the favorite button click
    fun toggleFavorite(surah: SurahEntity) {
        viewModelScope.launch {
            repository.setFavorite(surah, !surah.isFavorite)
        }
    }

    private fun refreshDataFromServer() {
        viewModelScope.launch {
            repository.refreshSurahs()
        }
    }

    suspend fun getSurahDetailFromApi(surahNumber: Int): List<MergedAyah>? {
        return repository.getSurahDetailFromApi(surahNumber)
    }

    fun onSurahClicked(surah: SurahEntity) {
        _navigateToDetail.value = surah
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SurahViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SurahViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
