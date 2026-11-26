package com.pwani.majisafi.ui.viewmodel

import app.cash.turbine.test
import com.pwani.majisafi.domain.SalinityClassifier
import com.pwani.majisafi.domain.model.Borehole
import com.pwani.majisafi.domain.model.Estate
import com.pwani.majisafi.domain.model.SalinityCategory
import com.pwani.majisafi.domain.model.SalinityReport
import com.pwani.majisafi.domain.repository.BoreholeRepository
import com.pwani.majisafi.ui.mvi.BoreholeUiEffect
import com.pwani.majisafi.ui.mvi.BoreholeUiIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoreholeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val sampleBoreholes = listOf(
        Borehole(
            id = 1,
            name = "Nyali Water Point",
            estate = Estate.NYALI,
            latitude = -4.0321,
            longitude = 39.7123,
            latestTdsPpm = 420,
            salinityCategory = SalinityCategory.FRESH
        ),
        Borehole(
            id = 2,
            name = "Tudor Well",
            estate = Estate.TUDOR,
            latitude = -4.0435,
            longitude = 39.6642,
            latestTdsPpm = 880,
            salinityCategory = SalinityCategory.MILDLY_BRACKISH
        ),
        Borehole(
            id = 3,
            name = "Likoni Stage",
            estate = Estate.LIKONI,
            latitude = -4.0812,
            longitude = 39.6631,
            latestTdsPpm = 3450,
            salinityCategory = SalinityCategory.SALINE
        )
    )

    private class FakeBoreholeRepository(
        val boreholesFlow: MutableStateFlow<List<Borehole>>,
        val unsyncedFlow: MutableStateFlow<List<SalinityReport>> = MutableStateFlow(emptyList())
    ) : BoreholeRepository {
        var lastSubmittedTds: Int? = null

        override fun getBoreholes(): Flow<List<Borehole>> = boreholesFlow
        override fun getBoreholesByEstate(estate: Estate): Flow<List<Borehole>> = boreholesFlow
        override fun getBoreholeById(id: Long): Flow<Borehole?> = MutableStateFlow(boreholesFlow.value.find { it.id == id })
        override fun getUnsyncedReports(): Flow<List<SalinityReport>> = unsyncedFlow

        override suspend fun submitSalinityReading(
            boreholeId: Long,
            tdsPpm: Int,
            reporter: String,
            notes: String
        ): Result<Unit> {
            lastSubmittedTds = tdsPpm
            return Result.success(Unit)
        }

        override suspend fun syncPendingReports(): Result<Int> {
            val count = unsyncedFlow.value.size
            unsyncedFlow.value = emptyList()
            return Result.success(count)
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initialLoad_emitsLoadedBoreholes() = runTest {
        val repo = FakeBoreholeRepository(MutableStateFlow(sampleBoreholes))
        val viewModel = BoreholeViewModel(repo)

        viewModel.uiState.test {
            // initial default state
            val initial = awaitItem()
            testDispatcher.scheduler.advanceUntilIdle()

            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(3, loaded.boreholes.size)
            assertEquals(3, loaded.filteredBoreholes.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onIntent_selectEstate_filtersListAccurately() = runTest {
        val repo = FakeBoreholeRepository(MutableStateFlow(sampleBoreholes))
        val viewModel = BoreholeViewModel(repo)

        viewModel.uiState.test {
            awaitItem() // initial
            testDispatcher.scheduler.advanceUntilIdle()
            awaitItem() // loaded

            viewModel.onIntent(BoreholeUiIntent.SelectEstate(Estate.NYALI))
            testDispatcher.scheduler.advanceUntilIdle()

            val filteredState = awaitItem()
            assertEquals(Estate.NYALI, filteredState.selectedEstate)
            assertEquals(1, filteredState.filteredBoreholes.size)
            assertEquals("Nyali Water Point", filteredState.filteredBoreholes.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onIntent_filterByCategory_filtersCategory() = runTest {
        val repo = FakeBoreholeRepository(MutableStateFlow(sampleBoreholes))
        val viewModel = BoreholeViewModel(repo)

        viewModel.uiState.test {
            awaitItem()
            testDispatcher.scheduler.advanceUntilIdle()
            awaitItem()

            viewModel.onIntent(BoreholeUiIntent.FilterByCategory(SalinityCategory.SALINE))
            testDispatcher.scheduler.advanceUntilIdle()

            val state = awaitItem()
            assertEquals(SalinityCategory.SALINE, state.selectedCategory)
            assertEquals(1, state.filteredBoreholes.size)
            assertEquals("Likoni Stage", state.filteredBoreholes.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onIntent_submitSalinityReading_updatesStateAndEmitsEffect() = runTest {
        val repo = FakeBoreholeRepository(MutableStateFlow(sampleBoreholes))
        val viewModel = BoreholeViewModel(repo)

        viewModel.effect.test {
            viewModel.onIntent(
                BoreholeUiIntent.SubmitSalinityReading(
                    boreholeId = 1L,
                    tdsPpm = 510,
                    reporter = "Mombasa Water Bowman",
                    notes = "Salinity increased post-tide"
                )
            )
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertEquals(BoreholeUiEffect.ReportSubmittedSuccessfully, effect)
            assertEquals(510, repo.lastSubmittedTds)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
