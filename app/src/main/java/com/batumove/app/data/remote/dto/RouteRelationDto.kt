package com.batumove.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteRelationDto(
    @SerialName("Status")
    val status: Int,

    @SerialName("Order")
    val order: Int,
)