package com.batumove.app.data.repository

import com.batumove.app.data.local.mapper.toDatabaseValue
import com.batumove.app.data.local.mapper.toDomain
import com.batumove.app.data.local.source.TransportLocalDataSource
import com.batumove.app.data.mapper.toDomain
import com.batumove.app.data.remote.source.TransportRemoteDataSource
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop
import com.batumove.app.domain.repository.TransportRepository
import javax.inject.Inject

class TransportRepositoryImpl @Inject constructor(
    private val localDataSource: TransportLocalDataSource,
    private val remoteDataSource: TransportRemoteDataSource
) : TransportRepository {

    override suspend fun getRoutes(): List<Route> =
        localDataSource
            .getRoutes()
            .map { entity ->
                entity.toDomain()
            }

    override suspend fun getRoute(
        routeId: String,
    ): Route =
        localDataSource
            .getRoute(routeId)
            ?.toDomain()
            ?: throw IllegalStateException(
                "Route not found: $routeId"
            )

    override suspend fun getRouteStops(
        routeId: String,
        direction: Direction,
    ): List<RouteStop> =
        localDataSource
            .getRouteStops(
                routeId = routeId,
                direction = direction.toDatabaseValue(),
            )
            .map { it.toDomain() }

    override suspend fun getBuses(
        routeId: String,
    ): List<Bus> {
        val response = remoteDataSource.getBuses()

        return response.data[routeId]
            .orEmpty()
            .map { dto ->
                dto.toDomain(routeId)
            }
    }

    override suspend fun getStops(): List<BusStop> =
        localDataSource
            .getStops()
            .map { entity ->
                entity.toDomain()
            }

    override suspend fun getBuses(): List<Bus> {
        val response = remoteDataSource.getBuses()

        return response.data.flatMap { (routeId, buses) ->
            buses.map { dto ->
                dto.toDomain(routeId)
            }
        }
    }

    override suspend fun getRouteGeometry(
        routeId: String,
    ): RouteGeometry {

        val points =
            localDataSource
                .getRouteGeometry(routeId)
                .map { it.toDomain() }

        return RouteGeometry(
            routeId = routeId,
            points = points,
        )
    }

    override suspend fun getDirectRoutes(
        startStopId: String,
        destinationStopId: String,
    ): List<Route> {

        val connections =
            localDataSource.getDirectRouteConnections(
                startStopId = startStopId,
                destinationStopId = destinationStopId,
            )

        val routeIds =
            connections
                .map { it.routeId }
                .distinct()

        val routes =
            localDataSource.getRoutes()

        return routes
            .filter { entity ->
                entity.id in routeIds
            }
            .map { entity ->
                entity.toDomain()
            }
    }
}