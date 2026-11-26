package com.pwani.majisafi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pwani.majisafi.domain.SalinityClassifier
import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.domain.model.Estate

@Entity(tableName = "boreholes")
data class BoreholeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val estate: String,
    val latitude: Double,
    val longitude: Double,
    val latestTdsPpm: Int,
    val isOperational: Boolean = true,
    val depthMeters: Double = 35.0,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomain(): Borehole {
        return Borehole(
            id = id,
            name = name,
            estate = Estate.fromString(estate),
            latitude = latitude,
            longitude = longitude,
            latestTdsPpm = latestTdsPpm,
            salinityCategory = SalinityClassifier.classify(latestTdsPpm),
            isOperational = isOperational,
            depthMeters = depthMeters,
            lastUpdatedTimestamp = lastUpdatedTimestamp
        )
    }

    companion object {
        fun fromDomain(domain: Borehole): BoreholeEntity {
            return BoreholeEntity(
                id = domain.id,
                name = domain.name,
                estate = domain.estate.name,
                latitude = domain.latitude,
                longitude = domain.longitude,
                latestTdsPpm = domain.latestTdsPpm,
                isOperational = domain.isOperational,
                depthMeters = domain.depthMeters,
                lastUpdatedTimestamp = domain.lastUpdatedTimestamp
            )
        }
    }
}
