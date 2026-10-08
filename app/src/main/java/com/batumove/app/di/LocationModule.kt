package com.batumove.app.di

import com.batumove.app.data.location.LocationDataSource
import com.batumove.app.data.location.LocationDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationDataSource(
        impl: LocationDataSourceImpl,
    ): LocationDataSource
}