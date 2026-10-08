package com.batumove.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusDto(
    @SerialName("Lat")
    val latitude: Double,

    @SerialName("Lon")
    val longitude: Double,

    @SerialName("Status")
    val status: Int,

    @SerialName("Name")
    val name: String,
)