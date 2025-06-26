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

    // Use StateFlow to hold the list of favorite SurahEntity objects
    private val _favoriteSurahs = MutableStateFlow<List<SurahEntity>>(emptyList())
    val favoriteSurahs: StateFlow<List<SurahEntity>> = _favoriteSurahs

    init {
        // Correctly initialize the repository
        val database = AppDatabase.getDatabase(application)
        val surahDao = database.surahDao()
        val ayahDao = database.ayahDao()
        repository = SurahRepository(surahDao, ayahDao)
        // Start listening to the stream of favorite surahs from the database
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

    /**
     * Toggles the favorite status of a surah. In this screen, this will effectively
     * remove the item from the list by setting its isFavorite status to false.
     */
    fun toggleFavorite(surah: SurahEntity) {
        viewModelScope.launch {
            // Setting isFavorite to 'false' will remove it from the favorites stream
            repository.setFavorite(surah, false)
        }
    }

    /**
     * A factory for creating instances of the FavoriteViewModel.
     */
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
