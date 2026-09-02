package com.proxyservice.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proxyservice.model.ProxyConfig
import com.proxyservice.model.TestResult
import com.proxyservice.ui.theme.ProxyTheme
import com.proxyservice.ui.viewmodel.ProxyViewModel
import kotlinx.coroutines.flow.collectAsStateWithLifecycle
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

@Composable
fun ProxyScreen(
    countryCode: String?,
    viewModel: ProxyViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val proxies by viewModel.proxies.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val testResults by viewModel.testResults.collectAsStateWithLifecycle()
    val selectedProxy by viewModel.selectedProxy.collectAsStateWithLifecycle()

    val context = androidx.compose.ui.platform.LocalContext.current
    
    androidx.compose.runtime.LaunchedEffect(key1 = Unit) {
        viewModel.loadProxies(countryCode, false)
    }

    ProxyTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = { Text("${countryCode?.uppercase() ?: "All"} Proxies") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(imageVector = androidx.compose.material.icons.defaults.Icons.Default.ArrowBack, contentDescription = "Back") } },
                actions = {
                    IconButton(onClick = { viewModel.refresh(true) }) {
                        Icon(imageVector = androidx.compose.material.icons.defaults.Icons.Default.Refresh, contentDescription = "Refresh & Test")
                    }
                    IconButton(onClick = { viewModel.testSelectedProxies() }) {
                        Icon(imageVector = androidx.compose.material.icons.defaults.Icons.Default.Speed, contentDescription = "Test Selected")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )

            if (isLoading && proxies.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                error?.let { err ->
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(text = "Error: $err", color = Color.Red, fontSize = 16.sp)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(proxies) { proxy ->
                        ProxyItem(
                            proxy = proxy,
                            isSelected = selectedProxy?.id == proxy.id,
                            testResult = testResults[proxy.id],
                            onClick = { viewModel.selectProxy(proxy) },
                            onTestClick = { viewModel.testProxies(listOf(proxy.id)) },
                            context = context
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProxyItem(
    proxy: ProxyConfig,
    isSelected: Boolean,
    testResult: TestResult?,
    onClick: () -> Unit,
    onTestClick: () -> Unit,
    context: Context
) {
    val statusColor = when {
        testResult?.success == true -> Color.Green
        testResult?.success == false -> Color.Red
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val statusText = when {
        testResult?.success == true -> "✅ ${testResult.latency_ms}ms"
        testResult?.success == false -> "❌ Failed"
        else -> "⏳ Untested"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${proxy.flagEmoji} ${proxy.country_name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${proxy.ip}:${proxy.port} • ${proxy.protocol}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
            }
            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onTestClick,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text("Test")
                }
                IconButton(onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Proxy URI", proxy.uri ?: proxy.url)
                    clipboard.setPrimaryClip(clip)
                }) {
                    Icon(imageVector = androidx.compose.material.icons.defaults.Icons.Default.ContentCopy, contentDescription = "Copy URI")
                }
            }
        }
    }
}