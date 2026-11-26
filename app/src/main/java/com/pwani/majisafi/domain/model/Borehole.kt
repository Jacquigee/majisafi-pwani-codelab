package com.pwani.majisafi.domain.model

data class Borehole(
    val id: Long = 0,
    val name: String,
    val estate: Estate,
    val latitude: Double,
    val longitude: Double,
    val latestTdsPpm: Int,
    val salinityCategory: SalinityCategory,
    val isOperational: Boolean = true,
    val depthMeters: Double = 35.0,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
