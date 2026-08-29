package com.proxyservice.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemHeadline
import androidx.compose.material3.ListItemSecondaryText
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proxyservice.R
import com.proxyservice.model.CountryInfo
import com.proxyservice.ui.theme.ProxyTheme
import com.proxyservice.ui.viewmodel.CountryViewModel
import kotlinx.coroutines.flow.collectAsStateWithLifecycle

@Composable
fun CountryScreen(
    viewModel: CountryViewModel = viewModel(),
    onCountryClick: (String) -> Unit
) {
    val countries by viewModel.countries.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    ProxyTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.countries_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )

            if (isLoading && countries.isEmpty()) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            } else {
                error?.let { err ->
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Error: $err", color = Color.Red, fontSize = 16.sp)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(countries) { country ->
                        CountryItem(country = country, onClick = { onCountryClick(country.code) })
                    }
                }
            }
        }
    }
}

@Composable
fun CountryItem(
    country: CountryInfo,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        headlineContent = {
            Text(
                text = "${country.flagEmoji} ${country.name}",
                style = MaterialTheme.typography.titleLarge
            )
        },
        supportingContent = {
            Text(
                text = "${country.count} proxies",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = androidx.compose.material.icons.defaults.Icons.Default.ChevronRight,
                    contentDescription = "Navigate"
                )
            }
        }
    )
    Divider()
}