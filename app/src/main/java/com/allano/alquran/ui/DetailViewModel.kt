package com.allano.alquran.ui

import android.app.Application
import androidx.lifecycle.*
import com.allano.alquran.data.SurahRepository
import com.allano.alquran.data.local.AppDatabase
import com.allano.alquran.data.local.AyahEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(application: Application, private val surahNumber: Int) : AndroidViewModel(application) {

    private val repository: SurahRepository

    val ayahs: StateFlow<List<AyahEntity>>

    init {
        val surahDao = AppDatabase.getDatabase(application).surahDao()
        val ayahDao = AppDatabase.getDatabase(application).ayahDao()
        repository = SurahRepository(surahDao, ayahDao)

        ayahs = repository.getAyahStream(surahNumber).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.refreshSurahDetail(surahNumber)
        }
    }

    class Factory(private val app: Application, private val surahNumber: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app, surahNumber) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}