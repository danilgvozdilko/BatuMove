package com.batumove.app.domain.model

data class NearbyStop(
    val stop: BusStop,
    val distanceMeters: Int,
)