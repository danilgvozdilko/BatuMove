package com.batumove.app.data.remote.api

import com.batumove.app.data.remote.dto.BusesResponseDto
import retrofit2.http.GET

interface BatBusRealtimeApi {

    @GET("daadbc5886dd072964db3a93114167e1/getAllBuses")
    suspend fun getAllBuses(): BusesResponseDto
}