package com.proxyservice.repository

import com.proxyservice.model.CountryInfo
import com.proxyservice.model.ProxyConfig
import com.proxyservice.model.StatsResponse
import com.proxyservice.model.TestResult
import com.proxyservice.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProxyRepository(private val apiClient: ApiClient) {

    private val _countries = MutableStateFlow<List<CountryInfo>>(emptyList())
    val countries: StateFlow<List<CountryInfo>> = _countries.asStateFlow()

    private val _proxies = MutableStateFlow<List<ProxyConfig>>(emptyList())
    val proxies: StateFlow<List<ProxyConfig>> = _proxies.asStateFlow()

    private val _stats = MutableStateFlow<StatsResponse?>(null)
    val stats: StateFlow<StatsResponse?> = _stats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentCountry: String? = null

    suspend fun loadStats() {
        val result = apiClient.getStats()
        when (result) {
            is ApiClient.Result.Success -> _stats.value = result.value
            is ApiClient.Result.Failure -> _error.value = result.exception.message
        }
    }

    suspend fun loadCountries() {
        val result = apiClient.getCountries()
        when (result) {
            is ApiClient.Result.Success -> _countries.value = result.value
            is ApiClient.Result.Failure -> _error.value = result.exception.message
        }
    }

    suspend fun loadProxies(country: String? = null, test: Boolean = false) {
        _isLoading.value = true
        _error.value = null
        currentCountry = country

        val result = apiClient.getProxies(country = country, test = test)
        when (result) {
            is ApiClient.Result.Success -> {
                _proxies.value = result.value
                _isLoading.value = false
            }
            is ApiClient.Result.Failure -> {
                _error.value = result.exception.message
                _isLoading.value = false
            }
        }
    }

    suspend fun refreshCurrent(test: Boolean = false) {
        loadProxies(currentCountry, test)
    }

    suspend fun testProxies(proxyIds: List<Int>): List<TestResult>? {
        val result = apiClient.testProxies(proxyIds)
        return when (result) {
            is ApiClient.Result.Success -> result.value
            is ApiClient.Result.Failure -> {
                _error.value = result.exception.message
                null
            }
        }
    }

    suspend fun getSubscription(country: String? = null, format: String = "json"): String? {
        val result = apiClient.getSubscription(country, format)
        return when (result) {
            is ApiClient.Result.Success -> result.value
            is ApiClient.Result.Failure -> {
                _error.value = result.exception.message
                null
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}