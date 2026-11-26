package com.pwani.majisafi.domain.repository

import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.domain.model.Estate
import com.pwani.majisafi.domain.model.SalinityReport
import kotlinx.coroutines.flow.Flow

interface BoreholeRepository {
    fun getBoreholes(): Flow<List<Borehole>>
    fun getBoreholesByEstate(estate: Estate): Flow<List<Borehole>>
    fun getBoreholeById(id: Long): Flow<Borehole?>
    fun getUnsyncedReports(): Flow<List<SalinityReport>>
    suspend fun submitSalinityReading(
        boreholeId: Long,
        tdsPpm: Int,
        reporter: String,
        notes: String
    ): Result<Unit>
    suspend fun syncPendingReports(): Result<Int>
}
