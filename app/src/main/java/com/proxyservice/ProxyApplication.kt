package com.proxyservice

import android.app.Application

class ProxyApplication : Application() {
    companion object {
        const val SETTINGS_NAME = "proxy_settings"
        const val DEFAULT_API_URL = "https://api.shadowmere.xyz"
    }
    var apiBaseUrl: String = DEFAULT_API_URL
}