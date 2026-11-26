package com.pwani.majisafi.ui.mvi

import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.domain.model.Estate
import com.pwani.majisafi.domain.model.SalinityCategory

data class BoreholeUiState(
    val isLoading: Boolean = true,
    val boreholes: List<Borehole> = emptyList(),
    val filteredBoreholes: List<Borehole> = emptyList(),
    val selectedEstate: Estate = Estate.ALL,
    val selectedCategory: SalinityCategory? = null,
    val searchQuery: String = "",
    val unsyncedCount: Int = 0,
    val userMessage: String? = null
)

sealed interface BoreholeUiIntent {
    data class SelectEstate(val estate: Estate) : BoreholeUiIntent
    data class FilterByCategory(val category: SalinityCategory?) : BoreholeUiIntent
    data class UpdateSearchQuery(val query: String) : BoreholeUiIntent
    data class SubmitSalinityReading(
        val boreholeId: Long,
        val tdsPpm: Int,
        val reporter: String,
        val notes: String
    ) : BoreholeUiIntent
    data object TriggerSync : BoreholeUiIntent
    data object DismissMessage : BoreholeUiIntent
}

sealed interface BoreholeUiEffect {
    data class ShowToast(val message: String) : BoreholeUiEffect
    data object ReportSubmittedSuccessfully : BoreholeUiEffect
}
