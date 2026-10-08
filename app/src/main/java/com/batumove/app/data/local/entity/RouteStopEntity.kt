package com.batumove.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "route_stops",

    primaryKeys = [
        "routeId",
        "stopId",
    ],

    foreignKeys = [
        ForeignKey(
            entity = RouteEntity::class,
            parentColumns = ["id"],
            childColumns = ["routeId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = StopEntity::class,
            parentColumns = ["id"],
            childColumns = ["stopId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],

    indices = [
        Index("routeId"),
        Index("stopId"),
    ],
)
data class RouteStopEntity(

    val routeId: String,

    val stopId: String,

    val direction: Int,

    val order: Int,
)