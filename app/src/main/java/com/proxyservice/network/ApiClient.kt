package com.proxyservice.network

import com.proxyservice.model.CountryInfo
import com.proxyservice.model.ProxyConfig
import com.proxyservice.model.StatsResponse
import com.proxyservice.model.TestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProxyApiService {
    @GET("/api/stats")
    suspend fun getStats(): Response<StatsResponse>

    @GET("/api/countries")
    suspend fun getCountries(): Response<List<CountryInfo>>

    @GET("/api/proxies")
    suspend fun getProxies(
        @Query("country") country: String? = null,
        @Query("protocol") protocol: String? = null,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("test") test: Boolean = false
    ): Response<List<ProxyConfig>>

    @GET("/api/proxies/{id}")
    suspend fun getProxy(
        @Path("id") id: Int,
        @Query("test") test: Boolean = false
    ): Response<ProxyConfig>

    @GET("/api/subscription")
    suspend fun getSubscription(
        @Query("country") country: String? = null,
        @Query("format") format: String = "json"
    ): Response<String>

    @POST("/api/test")
    suspend fun testProxies(
        @kotlinx.serialization.json.Json body: String
    ): Response<List<TestResult>>
}

class ApiClient(private val baseUrl: String, private val dispatcher: CoroutineDispatcher) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            kotlinx.serialization.json.Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType())
        )
        .build()

    private val service: ProxyApiService = retrofit.create(ProxyApiService::class.java)

    suspend fun getStats(): Result<StatsResponse> = withContext(dispatcher) {
        try {
            val response = service.getStats()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCountries(): Result<List<CountryInfo>> = withContext(dispatcher) {
        try {
            val response = service.getCountries()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProxies(
        country: String? = null,
        protocol: String? = null,
        limit: Int = 50,
        test: Boolean = false
    ): Result<List<ProxyConfig>> = withContext(dispatcher) {
        try {
            val response = service.getProxies(country, protocol, limit, 0, test)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProxy(id: Int, test: Boolean = false): Result<ProxyConfig> = withContext(dispatcher) {
        try {
            val response = service.getProxy(id, test)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSubscription(country: String? = null, format: String = "json"): Result<String> = withContext(dispatcher) {
        try {
            val response = service.getSubscription(country, format)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun testProxies(proxyIds: List<Int>): Result<List<TestResult>> = withContext(dispatcher) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val body = json.encodeToString(proxyIds.map { id ->
                jsonObject { put("id", id) }
            })
            val response = service.testProxies(body)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    sealed interface Result<out T> {
        data class Success<T>(val value: T) : Result<T>
        data class Failure(val exception: Exception) : Result<T>
    }
}