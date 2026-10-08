package com.batumove.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "routes"
)
data class RouteEntity(

    @PrimaryKey
    val id: String,

    val number: String,

    val isCircular: Boolean,

    val sortOrder: Int,
)