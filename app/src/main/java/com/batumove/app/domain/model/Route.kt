package com.batumove.app.domain.model

data class Route(
    val id: String,
    val number: String,
    val isCircular: Boolean,
    val sortOrder: Int,
)