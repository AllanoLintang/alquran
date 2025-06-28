package com.allano.alquran.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DzikrCounterViewModel : ViewModel() {

    // Private mutable state for the count
    private val _count = MutableStateFlow(0)
    // Public immutable state to observe from the UI
    val count: StateFlow<Int> = _count

    fun increment() {
        _count.value++
        if(_count.value == 33){
            reset()
        }
    }

    fun reset() {
        _count.value = 0
    }
}
