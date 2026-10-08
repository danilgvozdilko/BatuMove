package com.batumove.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    @SerialName("RouteIdGeoGps")
    val id: String,

    @SerialName("RouteNameGeoGps")
    val name: String,

    @SerialName("RouteNameKA")
    val nameKa: String,

    @SerialName("RouteNameEN")
    val nameEn: String,

    @SerialName("RouteIsCircle")
    val isCircular: Boolean,

    @SerialName("RouteSortOrder")
    val sortOrder: Int,
)