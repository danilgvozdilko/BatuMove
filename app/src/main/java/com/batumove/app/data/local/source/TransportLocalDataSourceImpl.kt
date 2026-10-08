package com.batumove.app.data.local.source

import com.batumove.app.data.local.dao.RouteDao
import com.batumove.app.data.local.dao.RouteGeometryDao
import com.batumove.app.data.local.dao.RouteStopDao
import com.batumove.app.data.local.dao.StopDao
import com.batumove.app.data.local.dao.TransportTransactionDao
import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity
import com.batumove.app.data.local.model.RouteStopWithStop
import com.batumove.app.data.local.model.TransportDatabaseData
import javax.inject.Inject

class TransportLocalDataSourceImpl @Inject constructor(
    private val routeDao: RouteDao,
    private val routeStopDao: RouteStopDao,
    private val routeGeometryDao: RouteGeometryDao,
    private val transactionDao: TransportTransactionDao,
    private val stopDao: StopDao
) : TransportLocalDataSource {

    override suspend fun hasTransportData(): Boolean {
        return routeDao.count() > 0
    }

    override suspend fun getRoute(
        routeId: String,
    ): RouteEntity? =
        routeDao.getRoute(routeId)

    override suspend fun replaceTransportData(
        data: TransportDatabaseData,
    ) {
        transactionDao.replaceAll(
            routes = data.routes,
            stops = data.stops,
            routeStops = data.routeStops,
            geometry = data.geometry,
        )
    }

    override suspend fun getRoutes():
            List<RouteEntity> =
        routeDao.getRoutes()

    override suspend fun getRouteStops(
        routeId: String,
        direction: Int,
    ): List<RouteStopWithStop> =
        routeStopDao.getRouteStops(
            routeId = routeId,
            direction = direction,
        )

    override suspend fun getStops(): List<StopEntity> =
        stopDao.getStops()

    override suspend fun getRouteGeometry(
        routeId: String,
    ): List<RouteGeometryPointEntity> =
        routeGeometryDao.getRouteGeometry(
            routeId = routeId,
        )

    override suspend fun getDirectRouteConnections(
        startStopId: String,
        destinationStopId: String,
    ): List<RouteStopEntity> =
        routeStopDao.getDirectRouteConnections(
            startStopId = startStopId,
            destinationStopId = destinationStopId,
        )
}