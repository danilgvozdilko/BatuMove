package com.batumove.app.domain.model

data class BusStop(
    val id: String,
    val number: Int?,
    val name: String,
    val position: GeoPoint
)