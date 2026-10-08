package com.batumove.app.domain.repository

import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop

interface TransportRepository {

    suspend fun getRoutes(): List<Route>

    suspend fun getRoute(
        routeId: String,
    ): Route

    suspend fun getRouteStops(
        routeId: String,
        direction: Direction,
    ): List<RouteStop>

    suspend fun getBuses(
        routeId: String,
    ): List<Bus>

    suspend fun getStops(): List<BusStop>

    suspend fun getBuses(): List<Bus>

    suspend fun getRouteGeometry(
        routeId: String,
    ): RouteGeometry

    suspend fun getDirectRoutes(
        startStopId: String,
        destinationStopId: String,
    ): List<Route>
}