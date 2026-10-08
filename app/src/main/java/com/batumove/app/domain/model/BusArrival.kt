package com.batumove.app.domain.model

data class NextBusArrival(
    val bus: Bus,
    val distanceMeters: Int,
    val stopsAway: Int,
    val etaMinutes: Int? = null,
)