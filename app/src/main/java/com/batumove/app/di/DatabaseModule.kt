package com.batumove.app.di

import android.content.Context
import androidx.room.Room
import com.batumove.app.data.local.database.BatuMoveDatabase
import com.batumove.app.domain.repository.TransportDataRepository
import com.batumove.app.domain.repository.TransportDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): BatuMoveDatabase =
        Room.databaseBuilder(
            context,
            BatuMoveDatabase::class.java,
            "batumove.db",
        ).build()

    @Provides
    fun provideRouteDao(
        database: BatuMoveDatabase,
    ) = database.routeDao()

    @Provides
    fun provideStopDao(
        database: BatuMoveDatabase,
    ) = database.stopDao()

    @Provides
    fun provideRouteStopDao(
        database: BatuMoveDatabase,
    ) = database.routeStopDao()

    @Provides
    fun provideRouteGeometryDao(
        database: BatuMoveDatabase,
    ) = database.routeGeometryDao()

    @Provides
    fun provideTransportTransactionDao(
        database: BatuMoveDatabase,
    ) = database.transportTransactionDao()

}