package com.proxyservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proxyservice.model.CountryInfo
import com.proxyservice.model.ProxyConfig
import com.proxyservice.repository.ProxyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: ProxyRepository) : ViewModel() {

    val countries: StateFlow<List<CountryInfo>> = repository.countries
    val isLoading: StateFlow<Boolean> = repository.isLoading
    val error: StateFlow<String?> = repository.error

    init {
        loadCountries()
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