package com.batumove.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusStopDto(
    @SerialName("BusStopNumber")
    val number: Int? = null,

    @SerialName("BusStopNameGeoGps")
    val name: String? = null,

    @SerialName("BusStopNameKA")
    val nameKa: String? = null,

    @SerialName("BusStopNameEN")
    val nameEn: String? = null,

    @SerialName("BusStopLatitude")
    val latitude: Double,

    @SerialName("BusStopLongitude")
    val longitude: Double,

    val routes: Map<String, RouteRelationDto> = emptyMap(),
)