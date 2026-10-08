package com.batumove.app.data.local.source

import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity
import com.batumove.app.data.local.model.RouteStopWithStop
import com.batumove.app.data.local.model.TransportDatabaseData

interface TransportLocalDataSource {

    suspend fun getRoute(
        routeId: String,
    ): RouteEntity?

    suspend fun hasTransportData(): Boolean

    suspend fun replaceTransportData(
        data: TransportDatabaseData,
    )

    suspend fun getRoutes(): List<RouteEntity>

    suspend fun getRouteStops(
        routeId: String,
        direction: Int,
    ): List<RouteStopWithStop>

    suspend fun getStops(): List<StopEntity>

    suspend fun getRouteGeometry(
        routeId: String,
    ): List<RouteGeometryPointEntity>

    suspend fun getDirectRouteConnections(
        startStopId: String,
        destinationStopId: String,
    ): List<RouteStopEntity>
}