package com.pwani.majisafi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwani.majisafi.domain.model.SalinityReport

@Entity(
    tableName = "salinity_reports",
    foreignKeys = [
        ForeignKey(
            entity = BoreholeEntity::class,
            parentColumns = ["id"],
            childColumns = ["boreholeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["boreholeId"])]
)
data class SalinityReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val boreholeId: Long,
    val tdsPpm: Int,
    val reporterName: String,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
) {
    fun toDomain(): SalinityReport {
        return SalinityReport(
            id = id,
            boreholeId = boreholeId,
            tdsPpm = tdsPpm,
            reporterName = reporterName,
            notes = notes,
            timestamp = timestamp,
            isSynced = isSynced
        )
    }

    companion object {
        fun fromDomain(domain: SalinityReport): SalinityReportEntity {
            return SalinityReportEntity(
                id = domain.id,
                boreholeId = domain.boreholeId,
                tdsPpm = domain.tdsPpm,
                reporterName = domain.reporterName,
                notes = domain.notes,
                timestamp = domain.timestamp,
                isSynced = domain.isSynced
            )
        }
    }
}
