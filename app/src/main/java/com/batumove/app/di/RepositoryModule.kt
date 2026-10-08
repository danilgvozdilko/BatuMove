package com.batumove.app.di

import com.batumove.app.data.local.source.TransportLocalDataSource
import com.batumove.app.data.local.source.TransportLocalDataSourceImpl
import com.batumove.app.data.remote.source.TransportRemoteDataSource
import com.batumove.app.data.remote.source.TransportRemoteDataSourceImpl
import com.batumove.app.data.repository.TransportRepositoryImpl
import com.batumove.app.data.sync.TransportDataSynchronizer
import com.batumove.app.data.sync.TransportDataSynchronizerImpl
import com.batumove.app.domain.repository.AppPreferencesRepository
import com.batumove.app.domain.repository.AppPreferencesRepositoryImpl
import com.batumove.app.domain.repository.TransportDataRepository
import com.batumove.app.domain.repository.TransportDataRepositoryImpl
import com.batumove.app.domain.repository.TransportRepository
import com.batumove.app.domain.usecase.ObserveRouteBuses
import com.batumove.app.domain.usecase.ObserveRouteBusesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransportRemoteDataSource(
        impl: TransportRemoteDataSourceImpl,
    ): TransportRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTransportRepository(
        impl: TransportRepositoryImpl,
    ): TransportRepository

    @Binds
    @Singleton
    abstract fun bindObserveRouteBuses(
        impl: ObserveRouteBusesUseCase,
    ): ObserveRouteBuses

    @Binds
    @Singleton
    abstract fun bindTransportLocalDataSource(
        impl: TransportLocalDataSourceImpl,
    ): TransportLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTransportDataSynchronizer(
        impl: TransportDataSynchronizerImpl,
    ): TransportDataSynchronizer

    @Binds
    @Singleton
    abstract fun bindTransportDataRepository(
        impl: TransportDataRepositoryImpl,
    ): TransportDataRepository

    @Binds
    @Singleton
    abstract fun bindAppPreferencesRepository(
        impl: AppPreferencesRepositoryImpl,
    ): AppPreferencesRepository
}