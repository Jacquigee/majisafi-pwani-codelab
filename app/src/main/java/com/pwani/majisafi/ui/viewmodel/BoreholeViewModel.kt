package com.pwani.majisafi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.domain.model.Estate
import com.pwani.majisafi.domain.model.SalinityCategory
import com.pwani.majisafi.domain.repository.BoreholeRepository
import com.pwani.majisafi.ui.mvi.BoreholeUiEffect
import com.pwani.majisafi.ui.mvi.BoreholeUiIntent
import com.pwani.majisafi.ui.mvi.BoreholeUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BoreholeViewModel(
    private val repository: BoreholeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoreholeUiState())
    val uiState: StateFlow<BoreholeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<BoreholeUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeBoreholes()
        observeUnsyncedReports()
    }

    fun onIntent(intent: BoreholeUiIntent) {
        when (intent) {
            is BoreholeUiIntent.SelectEstate -> selectEstate(intent.estate)
            is BoreholeUiIntent.FilterByCategory -> filterByCategory(intent.category)
            is BoreholeUiIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is BoreholeUiIntent.SubmitSalinityReading -> submitReading(
                intent.boreholeId,
                intent.tdsPpm,
                intent.reporter,
                intent.notes
            )
            BoreholeUiIntent.TriggerSync -> triggerSync()
            BoreholeUiIntent.DismissMessage -> dismissMessage()
        }
    }

    private fun observeBoreholes() {
        viewModelScope.launch {
            repository.getBoreholes().collect { list ->
                _uiState.update { current ->
                    val filtered = applyFilters(
                        list = list,
                        estate = current.selectedEstate,
                        category = current.selectedCategory,
                        query = current.searchQuery
                    )
                    current.copy(
                        isLoading = false,
                        boreholes = list,
                        filteredBoreholes = filtered
                    )
                }
            }
        }
    }

    private fun observeUnsyncedReports() {
        viewModelScope.launch {
            repository.getUnsyncedReports().collect { reports ->
                _uiState.update { it.copy(unsyncedCount = reports.size) }
            }
        }
    }

    private fun selectEstate(estate: Estate) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.boreholes,
                estate = estate,
                category = current.selectedCategory,
                query = current.searchQuery
            )
            current.copy(selectedEstate = estate, filteredBoreholes = filtered)
        }
    }

    private fun filterByCategory(category: SalinityCategory?) {
        _uiState.update { current ->
            val newCategory = if (current.selectedCategory == category) null else category
            val filtered = applyFilters(
                list = current.boreholes,
                estate = current.selectedEstate,
                category = newCategory,
                query = current.searchQuery
            )
            current.copy(selectedCategory = newCategory, filteredBoreholes = filtered)
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.boreholes,
                estate = current.selectedEstate,
                category = current.selectedCategory,
                query = query
            )
            current.copy(searchQuery = query, filteredBoreholes = filtered)
        }
    }

    private fun submitReading(boreholeId: Long, tdsPpm: Int, reporter: String, notes: String) {
        viewModelScope.launch {
            val result = repository.submitSalinityReading(boreholeId, tdsPpm, reporter, notes)
            if (result.isSuccess) {
                _effect.send(BoreholeUiEffect.ReportSubmittedSuccessfully)
                _uiState.update { it.copy(userMessage = "Reading recorded offline. Queued for sync.") }
            } else {
                _effect.send(BoreholeUiEffect.ShowToast("Failed to record reading."))
            }
        }
    }

    private fun triggerSync() {
        viewModelScope.launch {
            val result = repository.syncPendingReports()
            val count = result.getOrDefault(0)
            _uiState.update {
                it.copy(userMessage = if (count > 0) "Synced $count reading(s) to cloud!" else "All readings already up to date.")
            }
        }
    }

    private fun dismissMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun applyFilters(
        list: List<Borehole>,
        estate: Estate,
        category: SalinityCategory?,
        query: String
    ): List<Borehole> {
        return list.filter { borehole ->
            val matchesEstate = estate == Estate.ALL || borehole.estate == estate
            val matchesCategory = category == null || borehole.salinityCategory == category
            val matchesQuery = query.isBlank() ||
                borehole.name.contains(query, ignoreCase = true) ||
                borehole.estate.displayName.contains(query, ignoreCase = true)
            matchesEstate && matchesCategory && matchesQuery
        }
    }
}
