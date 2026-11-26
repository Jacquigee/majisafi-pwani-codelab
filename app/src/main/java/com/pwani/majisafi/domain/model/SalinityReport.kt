package com.pwani.majisafi.domain.model

data class SalinityReport(
    val id: Long = 0,
    val boreholeId: Long,
    val tdsPpm: Int,
    val reporterName: String,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
