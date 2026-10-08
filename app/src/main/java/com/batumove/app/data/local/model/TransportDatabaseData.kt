package com.batumove.app.data.local.model

import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity

data class TransportDatabaseData(
    val routes: List<RouteEntity>,
    val stops: List<StopEntity>,
    val routeStops: List<RouteStopEntity>,
    val geometry: List<RouteGeometryPointEntity>,
)