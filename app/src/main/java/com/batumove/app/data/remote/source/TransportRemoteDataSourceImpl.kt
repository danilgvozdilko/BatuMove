package com.batumove.app.data.remote.source

import com.batumove.app.data.remote.api.BatBusRealtimeApi
import com.batumove.app.data.remote.api.BatBusStaticApi
import com.batumove.app.data.remote.dto.BusesResponseDto
import com.batumove.app.data.remote.dto.DbResponseDto
import javax.inject.Inject

class TransportRemoteDataSourceImpl @Inject constructor(
    private val staticApi: BatBusStaticApi,
    private val realtimeApi: BatBusRealtimeApi,
) : TransportRemoteDataSource {

    override suspend fun getDatabase(): DbResponseDto =
        staticApi.getDbData()

    override suspend fun getBuses(): BusesResponseDto =
        realtimeApi.getAllBuses()
}