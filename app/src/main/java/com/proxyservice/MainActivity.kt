package com.proxyservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proxyservice.network.ApiClient
import com.proxyservice.repository.ProxyRepository
import com.proxyservice.ui.screen.CountryScreen
import com.proxyservice.ui.screen.MainScreen
import com.proxyservice.ui.screen.ProxyScreen
import com.proxyservice.ui.theme.ProxyTheme
import com.proxyservice.ui.viewmodel.CountryViewModel
import com.proxyservice.ui.viewmodel.MainViewModel
import com.proxyservice.ui.viewmodel.ProxyViewModel
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    private val app by lazy { application as ProxyApplication }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProxyTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val apiClient = remember { ApiClient(app.apiBaseUrl, Dispatchers.IO) }
                    val repository = remember { ProxyRepository(apiClient) }
                    val mainViewModel = viewModelFactory { MainViewModel(repository) }
                    val countryViewModel = viewModelFactory { CountryViewModel(repository) }
                    val proxyViewModel = viewModelFactory { ProxyViewModel(repository) }

                    val navController = rememberNavController()
                    NavHost(navController, "main") {
                        composable("main") {
                            MainScreen(
                                viewModel = mainViewModel,
                                onNavigateToCountries = { navController.navigate("countries") },
                                onNavigateToSubscription = { navController.navigate("subscription") }
                            )
                        }
                        composable("countries") {
                            CountryScreen(
                                viewModel = countryViewModel,
                                onCountryClick = { code -> navController.navigate("proxies/$code") }
                            )
                        }
                        composable(
                            route = "proxies/{countryCode}",
                            arguments = listOf(androidx.navigation.navArgument("countryCode") { type = androidx.navigation.NavType.StringType })
                        ) { backStackEntry ->
                            val countryCode = backStackEntry.getString()?.getString("countryCode")
                            ProxyScreen(
                                countryCode = countryCode,
                                viewModel = proxyViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable("subscription") {
                            SubscriptionScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionScreen(onBackClick: () -> Unit) {
    androidx.compose.material3.Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
    ) {
        androidx.compose.material3.TopAppBar(
            title = { androidx.compose.material3.Text("Subscription") },
            navigationIcon = { androidx.compose.material3.IconButton(onClick = onBackClick) { androidx.compose.material3.Icon(imageVector = androidx.compose.material.icons.defaults.Icons.Default.ArrowBack, contentDescription = "Back") } }
        )
        androidx.compose.material3.Text(text = "Subscription Screen - Coming Soon", modifier = androidx.compose.ui.Modifier.fillMaxSize().padding(16.dp))
    }
}