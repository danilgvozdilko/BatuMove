package com.batumove.app.data.remote.source

import com.batumove.app.data.remote.dto.BusesResponseDto
import com.batumove.app.data.remote.dto.DbResponseDto

interface TransportRemoteDataSource {

    suspend fun getDatabase(): DbResponseDto

    suspend fun getBuses(): BusesResponseDto
}