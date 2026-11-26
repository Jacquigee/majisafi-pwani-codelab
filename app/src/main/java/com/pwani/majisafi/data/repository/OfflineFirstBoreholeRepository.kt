package com.pwani.majisafi.data.repository

import com.pwani.majisafi.data.local.dao.BoreholeDao
import com.pwani.majisafi.data.local.entity.SalinityReportEntity
import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.domain.model.Estate
import com.pwani.majisafi.domain.model.SalinityReport
import com.pwani.majisafi.domain.repository.BoreholeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class OfflineFirstBoreholeRepository(
    private val dao: BoreholeDao
) : BoreholeRepository {

    override fun getBoreholes(): Flow<List<Borehole>> {
        return dao.getAllBoreholes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getBoreholesByEstate(estate: Estate): Flow<List<Borehole>> {
        return if (estate == Estate.ALL) {
            getBoreholes()
        } else {
            dao.getBoreholesByEstate(estate.name).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }

    override fun getBoreholeById(id: Long): Flow<Borehole?> {
        return dao.getBoreholeById(id).map { it?.toDomain() }
    }

    override fun getUnsyncedReports(): Flow<List<SalinityReport>> {
        return dao.getUnsyncedReports().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun submitSalinityReading(
        boreholeId: Long,
        tdsPpm: Int,
        reporter: String,
        notes: String
    ): Result<Unit> = runCatching {
        val now = System.currentTimeMillis()
        val report = SalinityReportEntity(
            boreholeId = boreholeId,
            tdsPpm = tdsPpm,
            reporterName = reporter,
            notes = notes,
            timestamp = now,
            isSynced = false
        )
        dao.insertSalinityReport(report)
        dao.updateBoreholeTds(boreholeId, tdsPpm, now)
    }

    override suspend fun syncPendingReports(): Result<Int> = runCatching {
        val unsynced = dao.getUnsyncedReports().firstOrNull() ?: emptyList()
        if (unsynced.isEmpty()) return@runCatching 0

        // In a real cloud backend, reports would be POSTed via Retrofit/Ktor to Mombasa Water API.
        // In this codelab, we mark them as synced to demonstrate offline-first sync lifecycle.
        val ids = unsynced.map { it.id }
        dao.markReportsAsSynced(ids)
        ids.size
    }
}
