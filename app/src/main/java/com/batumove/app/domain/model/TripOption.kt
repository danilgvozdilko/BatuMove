package com.batumove.app.domain.model

data class TripOption(
    val route: Route,
    val startStop: BusStop,
    val destinationStop: BusStop,
    val distanceToStartMeters: Int,
)