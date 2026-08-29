package com.proxyservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proxyservice.model.ProxyConfig
import com.proxyservice.model.TestResult
import com.proxyservice.repository.ProxyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProxyViewModel(private val repository: ProxyRepository) : ViewModel() {

    val proxies: StateFlow<List<ProxyConfig>> = repository.proxies
    val isLoading: StateFlow<Boolean> = repository.isLoading
    val error: StateFlow<String?> = repository.error

    private val _selectedProxy = MutableStateFlow<ProxyConfig?>(null)
    val selectedProxy: StateFlow<ProxyConfig?> = _selectedProxy

    private val _testResults = MutableStateFlow<Map<Int, TestResult>>(emptyMap())
    val testResults: StateFlow<Map<Int, TestResult>> = _testResults

    fun loadProxies(countryCode: String?, test: Boolean = false) {
        viewModelScope.launch {
            repository.loadProxies(countryCode, test)
        }
    }

    fun refresh(test: Boolean = false) {
        viewModelScope.launch {
            repository.refreshCurrent(test)
        }
    }

    fun selectProxy(proxy: ProxyConfig) {
        _selectedProxy.value = proxy
    }

    fun clearSelection() {
        _selectedProxy.value = null
    }

    fun testSelectedProxies() {
        val selected = _selectedProxy.value
        if (selected != null) {
            testProxies(listOf(selected.id))
        }
    }

    fun testProxies(proxyIds: List<Int>) {
        viewModelScope.launch {
            val results = repository.testProxies(proxyIds)
            if (results != null) {
                val map = results.associateBy({ it.id }, { it })
                _testResults.value = _testResults.value + map
            }
        }
    }

    fun getTestResult(id: Int): TestResult? = _testResults.value[id]

    fun getSubscription(countryCode: String?, format: String): String? {
        return repository.getSubscription(countryCode, format)
    }

    fun clearError() {
        repository.clearError()
    }
}