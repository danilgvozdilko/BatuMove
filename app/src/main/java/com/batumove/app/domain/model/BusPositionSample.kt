package com.batumove.app.domain.model

data class BusPositionSample(
    val position: GeoPoint,
    val timestampMillis: Long,
)