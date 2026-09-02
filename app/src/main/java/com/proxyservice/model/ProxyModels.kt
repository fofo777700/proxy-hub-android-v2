package com.proxyservice.model

import kotlinx.serialization.Serializable

@Serializable
data class ProxyConfig(
    val id: Int,
    val url: String,
    val protocol: String,
    val country_code: String,
    val country_name: String,
    val flagEmoji: String,
    val ip: String,
    val port: Int,
    val uri: String? = null
)

@Serializable
data class CountryInfo(
    val code: String,
    val name: String,
    val count: Int,
    val flagEmoji: String
)

@Serializable
data class StatsResponse(
    val total_proxies: Int,
    val total_countries: Int,
    val countries: List<CountryInfo>? = null
)

@Serializable
data class TestResult(
    val id: Int,
    val success: Boolean,
    val latency_ms: Int? = null,
    val error: String? = null
)

@Serializable
data class SubscriptionResponse(
    val content: String,
    val format: String
)