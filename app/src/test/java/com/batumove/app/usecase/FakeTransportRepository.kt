package com.batumove.app.usecase

import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop
import com.batumove.app.domain.repository.TransportRepository

class FakeTransportRepository : TransportRepository {

    var routes: List<Route> = emptyList()
    var routesException: Throwable? = null

    var routeStops: List<RouteStop> = emptyList()
    var routeGeometry: RouteGeometry? = null

    var busesResults: ArrayDeque<Result<List<Bus>>> =
        ArrayDeque()

    var getBusesCalls = 0
        private set

    override suspend fun getRoutes(): List<Route> {
        routesException?.let { throw it }
        return routes
    }

    override suspend fun getRouteStops(
        routeId: String,
        direction: Direction,
    ): List<RouteStop> {
        return routeStops
    }

    override suspend fun getRouteGeometry(
        routeId: String,
    ): RouteGeometry {
        return requireNotNull(routeGeometry)
    }

    override suspend fun getBuses(
        routeId: String,
    ): List<Bus> {
        getBusesCalls++

        return busesResults
            .removeFirst()
            .getOrThrow()
    }
}