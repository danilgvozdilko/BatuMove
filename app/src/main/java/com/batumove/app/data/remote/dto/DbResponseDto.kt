package com.batumove.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DbResponseDto(
    val data: DbDataDto
)