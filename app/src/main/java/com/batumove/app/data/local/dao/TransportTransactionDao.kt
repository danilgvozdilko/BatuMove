package com.batumove.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity

@Dao
abstract class TransportTransactionDao {

    @Query("DELETE FROM route_stops")
    protected abstract suspend fun clearRouteStops()

    @Query("DELETE FROM route_geometry")
    protected abstract suspend fun clearGeometry()

    @Query("DELETE FROM stops")
    protected abstract suspend fun clearStops()

    @Query("DELETE FROM routes")
    protected abstract suspend fun clearRoutes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertRoutes(
        routes: List<RouteEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertStops(
        stops: List<StopEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertRouteStops(
        routeStops: List<RouteStopEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertGeometry(
        geometry: List<RouteGeometryPointEntity>,
    )

    @Transaction
    open suspend fun replaceAll(
        routes: List<RouteEntity>,
        stops: List<StopEntity>,
        routeStops: List<RouteStopEntity>,
        geometry: List<RouteGeometryPointEntity>,
    ) {
        clearRouteStops()
        clearGeometry()
        clearStops()
        clearRoutes()

        insertRoutes(routes)
        insertStops(stops)
        insertRouteStops(routeStops)
        insertGeometry(geometry)
    }
}