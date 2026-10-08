package com.batumove.app.data.remote.api

import com.batumove.app.data.remote.dto.DbResponseDto
import retrofit2.http.GET

interface BatBusStaticApi {

    @GET("api/getDbData")
    suspend fun getDbData(): DbResponseDto
}