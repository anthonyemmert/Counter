package com.example.composeday3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeday3.CounterUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.composeday3.data.CounterRepository


sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}

class CounterViewModel(
    private val repository: CounterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CounterUiState(
            count = repository.loadCount(),
            step = repository.loadStep()
        )
    )

    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow()

    fun increment() {
        val newValue = _uiState.value.count + _uiState.value.step
        _uiState.value = _uiState.value.copy(count = newValue)
        repository.saveCount(newValue)
    }

    fun decrement() {
        val newValue = _uiState.value.count - _uiState.value.step
        _uiState.value = _uiState.value.copy(count = newValue)
        repository.saveCount(newValue)
    }


    fun reset() {
        _uiState.value = CounterUiState(count = 0)
        repository.saveCount(0)

        viewModelScope.launch {
            _events.send(UiEvent.ShowSnackbar("Counter reset"))
        }
    }
    fun stepUp() {
        val newStep = (_uiState.value.step + 1).coerceAtMost(10)
        _uiState.value = _uiState.value.copy(step = newStep)
        repository.saveStep(newStep)
    }

    fun stepDown() {
        val newStep = (_uiState.value.step - 1).coerceAtLeast(1)
        _uiState.value = _uiState.value.copy(step = newStep)
        repository.saveStep(newStep)
    }

    fun resetStep() {
        _uiState.value = _uiState.value.copy(step = 1)
        repository.saveStep(1)
    }



}
