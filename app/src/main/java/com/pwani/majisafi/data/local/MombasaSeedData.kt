package com.pwani.majisafi.data.local

import com.pwani.majisafi.data.local.entity.BoreholeEntity
import com.pwani.majisafi.domain.model.Estate

object MombasaSeedData {
    fun getInitialBoreholes(): List<BoreholeEntity> = listOf(
        BoreholeEntity(
            id = 1,
            name = "Nyali Links Water Point",
            estate = Estate.NYALI.name,
            latitude = -4.0321,
            longitude = 39.7123,
            latestTdsPpm = 420, // Fresh
            isOperational = true,
            depthMeters = 42.0
        ),
        BoreholeEntity(
            id = 2,
            name = "Tudor Kiosk Borehole",
            estate = Estate.TUDOR.name,
            latitude = -4.0435,
            longitude = 39.6642,
            latestTdsPpm = 880, // Mildly Brackish
            isOperational = true,
            depthMeters = 30.5
        ),
        BoreholeEntity(
            id = 3,
            name = "Bamburi Fisheries Well",
            estate = Estate.BAMBURI.name,
            latitude = -3.9982,
            longitude = 39.7301,
            latestTdsPpm = 2150, // Brackish
            isOperational = true,
            depthMeters = 22.0
        ),
        BoreholeEntity(
            id = 4,
            name = "Kisauni Mosque Community Well",
            estate = Estate.KISAUNI.name,
            latitude = -4.0189,
            longitude = 39.6891,
            latestTdsPpm = 1250, // Mildly Brackish
            isOperational = true,
            depthMeters = 36.0
        ),
        BoreholeEntity(
            id = 5,
            name = "Likoni Ferry Stage Water Point",
            estate = Estate.LIKONI.name,
            latitude = -4.0812,
            longitude = 39.6631,
            latestTdsPpm = 3450, // Saline (seawater intrusion)
            isOperational = false,
            depthMeters = 18.0
        ),
        BoreholeEntity(
            id = 6,
            name = "Changamwe Industrial Depot Well",
            estate = Estate.CHANGAMWE.name,
            latitude = -4.0354,
            longitude = 39.6241,
            latestTdsPpm = 1780, // Brackish
            isOperational = true,
            depthMeters = 48.0
        ),
        BoreholeEntity(
            id = 7,
            name = "Old Town Heritage Well",
            estate = Estate.MOMBASA_ISLAND.name,
            latitude = -4.0592,
            longitude = 39.6784,
            latestTdsPpm = 3100, // Saline
            isOperational = true,
            depthMeters = 15.0
        ),
        BoreholeEntity(
            id = 8,
            name = "Ganjoni Central Borehole",
            estate = Estate.MOMBASA_ISLAND.name,
            latitude = -4.0673,
            longitude = 39.6638,
            latestTdsPpm = 490, // Fresh (perched aquifer pocket)
            isOperational = true,
            depthMeters = 55.0
        )
    )
}
