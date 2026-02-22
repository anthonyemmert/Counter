package com.example.composeday3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.composeday3.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val isDarkMode: Boolean = false
)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(repository.loadDarkMode())
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleDarkMode() {
        val newValue = !_uiState.value.isDarkMode
        _uiState.value = SettingsUiState(newValue)
        repository.saveDarkMode(newValue)
    }
}
