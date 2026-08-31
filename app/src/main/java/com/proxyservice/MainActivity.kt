package com.proxyservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProxyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val apiClient = remember { ApiClient(app.apiBaseUrl, Dispatchers.IO) }
                    val repository = remember { ProxyRepository(apiClient) }
                    val mainViewModel = viewModel { MainViewModel(repository) }
                    val countryViewModel = viewModel { CountryViewModel(repository) }
                    val proxyViewModel = viewModel { ProxyViewModel(repository) }

                    MainScreen(
                        viewModel = mainViewModel,
                        onNavigateToCountries = { /* TODO: Navigation */ },
                        onNavigateToSubscription = { /* TODO: Navigation */ }
                    )
                }
            }
        }
    }
}

@Composable
fun SubscriptionScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = { Text("Subscription") },
            navigationIcon = { IconButton(onClick = onBackClick) { Icon(imageVector = androidx.compose.material.icons.defaults.Icons.Default.ArrowBack, contentDescription = "Back") } }
        )
        Text(text = "Subscription Screen - Coming Soon", modifier = Modifier.fillMaxSize().padding(16.dp))
    }
}