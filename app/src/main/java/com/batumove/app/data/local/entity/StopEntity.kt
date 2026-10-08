package com.batumove.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "stops"
)
data class StopEntity(

    @PrimaryKey
    val id: String,

    val number: Int?,

    val name: String,

    val latitude: Double,

    val longitude: Double,
)