package com.batumove.app.di

import com.batumove.app.domain.time.SystemTimeProvider
import com.batumove.app.domain.time.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeModule {

    @Binds
    @Singleton
    abstract fun bindTimeProvider(
        impl: SystemTimeProvider,
    ): TimeProvider
}