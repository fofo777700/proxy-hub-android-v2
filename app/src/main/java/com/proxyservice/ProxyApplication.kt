package com.proxyservice

import android.app.Application
import androidx.datastore.preferences.PreferencesDataStoreFactory
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProxyApplication : Application() {

    companion object {
        private const val SETTINGS_NAME = "proxy_settings"
        const val KEY_API_BASE_URL = "api_base_url"
        const val DEFAULT_API_URL = "http://10.0.2.2:8000" // localhost for Android emulator
    }

    val dataStore by lazy {
        createDataStore(
            name = SETTINGS_NAME,
            factory = PreferencesDataStoreFactory
        )
    }

    val apiBaseUrl: String
        get() = runBlocking {
            dataStore.data
                .map { it[stringPreferencesKey(KEY_API_BASE_URL)] ?: DEFAULT_API_URL }
                .first()
        }
        set(value) = CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { it[stringPreferencesKey(KEY_API_BASE_URL)] = value }
        }
}