package com.pwani.majisafi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.ui.components.AddReadingBottomSheet
import com.pwani.majisafi.ui.components.BoreholeCard
import com.pwani.majisafi.ui.components.EstateFilterChips
import com.pwani.majisafi.ui.mvi.BoreholeUiEffect
import com.pwani.majisafi.ui.mvi.BoreholeUiIntent
import com.pwani.majisafi.ui.viewmodel.BoreholeViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoreholeScreen(
    viewModel: BoreholeViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedBoreholeForReading by remember { mutableStateOf<Borehole?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is BoreholeUiEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                BoreholeUiEffect.ReportSubmittedSuccessfully -> {
                    selectedBoreholeForReading = null
                    snackbarHostState.showSnackbar("Salinity reading recorded offline!")
                }
            }
        }
    }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onIntent(BoreholeUiIntent.DismissMessage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            text = " MajiSafi Pwani",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { viewModel.onIntent(BoreholeUiIntent.TriggerSync) }) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Sync Readings",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Unsynced banner indicator
            if (state.unsyncedCount > 0) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "📦 ${state.unsyncedCount} report(s) queued locally for sync",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            // Search bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onIntent(BoreholeUiIntent.UpdateSearchQuery(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by borehole name or estate...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            // Estate Filter Chips
            EstateFilterChips(
                selectedEstate = state.selectedEstate,
                onEstateSelected = { viewModel.onIntent(BoreholeUiIntent.SelectEstate(it)) }
            )

            // Borehole list or Empty / Loading state
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.filteredBoreholes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No boreholes found for the selected criteria.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.filteredBoreholes,
                        key = { it.id }
                    ) { borehole ->
                        BoreholeCard(
                            borehole = borehole,
                            onAddReadingClick = { selectedBoreholeForReading = borehole }
                        )
                    }
                }
            }
        }

        // Bottom sheet for reporting
        selectedBoreholeForReading?.let { borehole ->
            AddReadingBottomSheet(
                borehole = borehole,
                onDismiss = { selectedBoreholeForReading = null },
                onSubmit = { tds, reporter, notes ->
                    viewModel.onIntent(
                        BoreholeUiIntent.SubmitSalinityReading(
                            boreholeId = borehole.id,
                            tdsPpm = tds,
                            reporter = reporter,
                            notes = notes
                        )
                    )
                }
            )
        }
    }
}
