package com.batumove.app.data.repository

import com.batumove.app.data.remote.dto.BusesResponseDto
import com.batumove.app.data.remote.dto.DbResponseDto
import com.batumove.app.data.remote.source.TransportRemoteDataSource

class FakeTransportRemoteDataSource : TransportRemoteDataSource {

    var databaseResponse: DbResponseDto? = null
    var busesResponse: BusesResponseDto? = null

    var databaseException: Throwable? = null
    var busesException: Throwable? = null

    override suspend fun getDatabase(): DbResponseDto {
        databaseException?.let { throw it }

        return requireNotNull(databaseResponse) {
            "databaseResponse was not configured"
        }
    }

    override suspend fun getBuses(): BusesResponseDto {
        busesException?.let { throw it }

        return requireNotNull(busesResponse) {
            "busesResponse was not configured"
        }
    }
}