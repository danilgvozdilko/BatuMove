package com.batumove.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.model.RouteStopWithStop

@Dao
interface RouteStopDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(
        routeStops: List<RouteStopEntity>,
    )

    @Query(
        """
        SELECT 
            stops.*,
            route_stops.direction,
            route_stops.`order`
        FROM route_stops
        INNER JOIN stops
            ON route_stops.stopId = stops.id
        WHERE route_stops.routeId = :routeId
            AND route_stops.direction = :direction
        ORDER BY route_stops.`order` ASC
        """
    )
    suspend fun getRouteStops(
        routeId: String,
        direction: Int,
    ): List<RouteStopWithStop>

    @Query(
        """
    SELECT *
    FROM route_stops
    WHERE stopId = :stopId
    """
    )
    suspend fun getRouteStopsByStopId(
        stopId: String,
    ): List<RouteStopEntity>

    @Query(
        """
    SELECT start.*
    FROM route_stops AS start
    INNER JOIN route_stops AS destination
        ON start.routeId = destination.routeId
        AND start.direction = destination.direction
    WHERE start.stopId = :startStopId
        AND destination.stopId = :destinationStopId
        AND start.`order` < destination.`order`
    """
    )
    suspend fun getDirectRouteConnections(
        startStopId: String,
        destinationStopId: String,
    ): List<RouteStopEntity>

    @Query("DELETE FROM route_stops")
    suspend fun clear()

}