package com.proxyservice

import android.app.Application

class ProxyApplication : Application() {

    companion object {
        private const val SETTINGS_NAME = "proxy_settings"
        const val KEY_API_BASE_URL = "api_base_url"
        const val DEFAULT_API_URL = "http://10.0.2.2:8000" // localhost for Android emulator
    }

    var apiBaseUrl: String = DEFAULT_API_URL
}