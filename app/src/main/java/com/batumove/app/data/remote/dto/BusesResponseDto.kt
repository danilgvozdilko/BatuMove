package com.batumove.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BusesResponseDto(
    val data: Map<String, List<BusDto>>,
    val updatedAt: Long
)