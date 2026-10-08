package com.batumove.app.data.repository

import com.batumove.app.domain.repository.TransportDataRepository

class FakeTransportDataRepository :
    TransportDataRepository {

    var hasLocalData = false

    var lastSyncTime: Long? = null

    var syncException: Throwable? = null

    var syncCalls = 0
        private set

    override suspend fun hasLocalData(): Boolean =
        hasLocalData

    override suspend fun getLastSyncTime(): Long? =
        lastSyncTime

    override suspend fun sync() {
        syncCalls++

        syncException?.let {
            throw it
        }
    }
}