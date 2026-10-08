package com.batumove.app.di

import com.batumove.app.data.theme.AppThemeManager
import com.batumove.app.data.theme.AppThemeManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleModule {

    @Binds
    @Singleton
    abstract fun bindAppThemeManager(
        impl: AppThemeManagerImpl,
    ): AppThemeManager
}