package com.proxyservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proxyservice.model.StatsResponse
import com.proxyservice.repository.ProxyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProxyRepository) : ViewModel() {

    val stats: StateFlow<StatsResponse?> = repository.stats
    val isLoading: StateFlow<Boolean> = repository.isLoading
    val error: StateFlow<String?> = repository.error

    fun loadStats() {
        viewModelScope.launch {
            repository.loadStats()
        }
    }

    fun loadCountries() {
        viewModelScope.launch {
            repository.loadCountries()
        }
    }

    fun clearError() {
        repository.clearError()
    }
}