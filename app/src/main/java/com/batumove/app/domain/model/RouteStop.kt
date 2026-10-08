package com.batumove.app.domain.model

data class RouteStop(
    val stop: BusStop,
    val direction: Direction,
    val order: Int,
)