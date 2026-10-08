package com.batumove.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeoPointDto(
    val lat: Double,
    val lon: Double
)