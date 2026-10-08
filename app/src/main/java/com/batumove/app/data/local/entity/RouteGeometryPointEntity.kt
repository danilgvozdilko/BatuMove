package com.batumove.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "route_geometry",
    primaryKeys = [
        "routeId",
        "pointOrder",
    ],
    indices = [
        Index("routeId")
    ],
)
data class RouteGeometryPointEntity(

    val routeId: String,

    val pointOrder: Int,

    val latitude: Double,

    val longitude: Double,
)