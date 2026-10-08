package com.batumove.app.domain.model

data class Bus(
    val name: String,
    val routeId: String,
    val position: GeoPoint,
    val direction: Direction,
)