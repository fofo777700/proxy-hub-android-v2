package com.proxyservice.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proxyservice.R
import com.proxyservice.ui.theme.ProxyTheme
import com.proxyservice.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectAsStateWithLifecycle

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToCountries: () -> Unit,
    onNavigateToSubscription: () -> Unit
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    ProxyTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onNavigateToCountries) {
                        Icon(imageVector = Public, contentDescription = "Countries")
                    }
                    IconButton(onClick = onNavigateToSubscription) {
                        Icon(imageVector = Download, contentDescription = "Subscription")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )

            if (isLoading && stats == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                error?.let { err ->
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(text = "Error: $err", color = Color.Red, fontSize = 16.sp)
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Stats Cards
                    stats?.let { s ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                title = stringResource(R.string.total_proxies),
                                value = "${s.total_proxies}",
                                icon = Router,
                                color = MaterialTheme.colorScheme.primary
                            )
                            StatCard(
                                title = stringResource(R.string.total_countries),
                                value = "${s.total_countries}",
                                icon = Public,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Divider()

                    // Quick Actions
                    Text(text = stringResource(R.string.quick_actions), style = MaterialTheme.typography.titleLarge)

                    ActionCard(
                        title = stringResource(R.string.browse_countries),
                        description = stringResource(R.string.browse_countries_desc),
                        icon = Public,
                        onClick = onNavigateToCountries
                    )

                    ActionCard(
                        title = stringResource(R.string.download_subscription),
                        description = stringResource(R.string.download_subscription_desc),
                        icon = Download,
                        onClick = onNavigateToSubscription
                    )

                    // Top Countries
                    stats?.countries?.take(5)?.let { topCountries ->
                        if (topCountries.isNotEmpty()) {
                            Divider()
                            Text(text = stringResource(R.string.top_countries), style = MaterialTheme.typography.titleLarge)
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(topCountries) { country ->
                                    CountryRow(country = country)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(100.dp)
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = "", tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, color = color, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = color.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp).padding(end = 16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(imageVector = ChevronRight, contentDescription = "Navigate")
        }
    }
}

@Composable
fun CountryRow(country: com.proxyservice.model.CountryInfo) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${country.flagEmoji}", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = country.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${country.count} proxies", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(imageVector = ChevronRight, contentDescription = "")
    }
}