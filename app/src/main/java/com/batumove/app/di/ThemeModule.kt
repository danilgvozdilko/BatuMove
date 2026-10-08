package com.batumove.app.di

import com.batumove.app.data.locale.AppLocaleManager
import com.batumove.app.data.locale.AppLocaleManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeModule {

    @Binds
    @Singleton
    abstract fun bindAppLocaleManager(
        impl: AppLocaleManagerImpl,
    ): AppLocaleManager
}