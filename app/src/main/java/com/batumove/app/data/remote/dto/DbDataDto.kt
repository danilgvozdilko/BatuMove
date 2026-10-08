package com.batumove.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DbDataDto(
    val busStops: Map<String, BusStopDto>,
    val routesNames: Map<String, RouteDto>,
    val routeCoordinatesGrouped: Map<String, List<GeoPointDto>>
)