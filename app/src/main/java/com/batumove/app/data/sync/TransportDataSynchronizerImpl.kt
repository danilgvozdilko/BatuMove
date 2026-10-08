package com.batumove.app.data.sync

import com.batumove.app.data.local.mapper.toDatabaseData
import com.batumove.app.data.local.source.TransportLocalDataSource
import com.batumove.app.data.remote.source.TransportRemoteDataSource
import javax.inject.Inject

class TransportDataSynchronizerImpl @Inject constructor(
    private val remoteDataSource: TransportRemoteDataSource,
    private val localDataSource: TransportLocalDataSource,
) : TransportDataSynchronizer {

    override suspend fun sync() {

        val remoteData =
            remoteDataSource.getDatabase()

        val databaseData =
            remoteData.toDatabaseData()

        localDataSource.replaceTransportData(
            databaseData
        )
    }
}