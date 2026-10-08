package com.batumove.app.domain.repository

import com.batumove.app.data.local.source.TransportLocalDataSource
import com.batumove.app.data.preferences.AppPreferencesDataSource
import com.batumove.app.data.sync.TransportDataSynchronizer
import com.batumove.app.domain.time.TimeProvider
import javax.inject.Inject

class TransportDataRepositoryImpl @Inject constructor(
    private val localDataSource: TransportLocalDataSource,
    private val synchronizer: TransportDataSynchronizer,
    private val preferences: AppPreferencesDataSource,
    private val timeProvider: TimeProvider,
) : TransportDataRepository {

    override suspend fun hasLocalData(): Boolean =
        localDataSource.hasTransportData()

    override suspend fun getLastSyncTime(): Long? =
        preferences.getLastTransportSync()

    override suspend fun sync() {
        synchronizer.sync()

        preferences.setLastTransportSync(
            timeProvider.currentTimeMillis()
        )
    }
}