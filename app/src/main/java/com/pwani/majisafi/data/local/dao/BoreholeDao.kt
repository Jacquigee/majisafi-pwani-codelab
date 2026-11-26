package com.pwani.majisafi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pwani.majisafi.data.local.entity.BoreholeEntity
import com.pwani.majisafi.data.local.entity.SalinityReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoreholeDao {

    @Query("SELECT * FROM boreholes ORDER BY name ASC")
    fun getAllBoreholes(): Flow<List<BoreholeEntity>>

    @Query("SELECT * FROM boreholes WHERE estate = :estate ORDER BY name ASC")
    fun getBoreholesByEstate(estate: String): Flow<List<BoreholeEntity>>

    @Query("SELECT * FROM boreholes WHERE id = :id")
    fun getBoreholeById(id: Long): Flow<BoreholeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoreholes(boreholes: List<BoreholeEntity>)

    @Update
    suspend fun updateBorehole(borehole: BoreholeEntity)

    @Query("UPDATE boreholes SET latestTdsPpm = :tdsPpm, lastUpdatedTimestamp = :timestamp WHERE id = :boreholeId")
    suspend fun updateBoreholeTds(boreholeId: Long, tdsPpm: Int, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalinityReport(report: SalinityReportEntity): Long

    @Query("SELECT * FROM salinity_reports WHERE isSynced = 0 ORDER BY timestamp ASC")
    fun getUnsyncedReports(): Flow<List<SalinityReportEntity>>

    @Query("UPDATE salinity_reports SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markReportsAsSynced(ids: List<Long>)
}
