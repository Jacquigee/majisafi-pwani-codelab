package com.pwani.majisafi.di

import com.pwani.majisafi.data.local.MajiSafiDatabase
import com.pwani.majisafi.data.repository.OfflineFirstBoreholeRepository
import com.pwani.majisafi.domain.repository.BoreholeRepository
import com.pwani.majisafi.ui.viewmodel.BoreholeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Application CoroutineScope
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    // Room Database & DAO
    single { MajiSafiDatabase.getInstance(androidContext(), get()) }
    single { get<MajiSafiDatabase>().boreholeDao() }

    // Repository
    single<BoreholeRepository> { OfflineFirstBoreholeRepository(get()) }

    // ViewModel
    viewModel { BoreholeViewModel(get()) }
}
