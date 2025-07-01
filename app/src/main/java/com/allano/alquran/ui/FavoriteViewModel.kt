package com.allano.alquran.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.allano.alquran.data.SurahRepository
import com.allano.alquran.data.local.AppDatabase
import com.allano.alquran.data.local.SurahEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SurahRepository
    private val _favoriteSurahs = MutableStateFlow<List<SurahEntity>>(emptyList())
    val favoriteSurahs: StateFlow<List<SurahEntity>> = _favoriteSurahs

    init {
        val database = AppDatabase.getDatabase(application)
        val surahDao = database.surahDao()
        val ayahDao = database.ayahDao()
        repository = SurahRepository(surahDao, ayahDao)
        listenToFavoriteSurahs()
    }

    private fun listenToFavoriteSurahs() {
        viewModelScope.launch {
            repository.getFavoriteSurahsStream()
                .catch { e ->
                    Log.e("FavoriteViewModel", "Error collecting from favorite surahs stream", e)
                }
                .collect { surahList ->
                    _favoriteSurahs.value = surahList
                    Log.d("FavoriteViewModel", "Received ${surahList.size} favorite surahs from database.")
                }
        }
    }

    fun toggleFavorite(surah: SurahEntity) {
        viewModelScope.launch {
            repository.setFavorite(surah, false)
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
