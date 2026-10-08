package com.batumove.app.data.sync

interface TransportDataSynchronizer {

    suspend fun sync()
}