package com.batumove.app.data.location

import com.batumove.app.domain.model.GeoPoint

interface LocationDataSource {

    suspend fun getCurrentLocation(): GeoPoint?
}