package com.batumove.app.domain.repository


interface TransportDataRepository {

    suspend fun hasLocalData(): Boolean

    suspend fun getLastSyncTime(): Long?

    suspend fun sync()
}