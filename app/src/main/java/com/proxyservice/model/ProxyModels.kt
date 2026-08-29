package com.proxyservice.model

import kotlinx.serialization.Serializable

@Serializable
data class ProxyConfig(
    val id: Int,
    val url: String,
    val protocol: String,
    val country_code: String,
    val country_name: String,
    val location: String,
    val ip: String,
    val port: Int,
    val last_checked: String,
    val is_active: Boolean,
    val latency_ms: Int? = null,
    val is_working: Boolean? = null,
    val method: String? = null,
    val password: String? = null,
    val uri: String? = null
) {
    val displayName: String
        get() = "$country_name ($ip:$port)"
    
    val flagEmoji: String
        get() = countryCodeToFlag(country_code)
    
    fun countryCodeToFlag(code: String): String {
        return code.uppercase().map { (it.code + 0x1F1E5).toChar() }.joinToString("")
    }
}

@Serializable
data class CountryInfo(
    val code: String,
    val name: String,
    val count: Int
) {
    val flagEmoji: String
        get() = code.uppercase().map { (it.code + 0x1F1E5).toChar() }.joinToString("")
}

@Serializable
data class StatsResponse(
    val total_proxies: Int,
    val total_countries: Int,
    val protocols: Map<String, Int>,
    val countries: List<CountryInfo>,
    val last_updated: String
)

@Serializable
data class TestResult(
    val id: Int,
    val success: Boolean,
    val latency_ms: Int?,
    val error: String?
)